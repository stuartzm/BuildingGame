package us.rescyou.meme.world;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.Config;
import us.rescyou.meme.Util;
import us.rescyou.meme.block.Block;
import us.rescyou.meme.block.BlockQueue;
import us.rescyou.meme.block.RockBlock;
import us.rescyou.meme.entity.EntityManager;
import us.rescyou.meme.entity.Mob;
import us.rescyou.meme.floor.RockFloor;
import us.rescyou.meme.pathfinding.Pathfinder;
import us.rescyou.meme.resource.ResourceManager;
import us.rescyou.meme.resource.ResourceManager.Resource;
import us.rescyou.meme.unit.Builder;
import us.rescyou.meme.unit.Miner;

public class World {

	/* Private Variables */

	private Map map;
	private EntityManager unitManager;
	private EntityManager enemyManager;
	private BlockQueue breakQueue;
	private BlockQueue buildQueue;
	private float xOffset, yOffset;
	private Pathfinder pathfinder;
	private ResourceManager resourceManager;

	/* Initialization */

	/**
	 * Creates a new World.
	 * 
	 * @param mapWidth
	 *            the width of the World in grid units.
	 * @param mapHeight
	 *            the height of the World in grid units.
	 * @throws SlickException
	 */
	public World(int mapWidth, int mapHeight) throws SlickException {
		map = new Map(mapWidth, mapHeight);
		unitManager = new EntityManager();
		enemyManager = new EntityManager();
		breakQueue = new BlockQueue(map);
		buildQueue = new BlockQueue(map);
		pathfinder = new Pathfinder(this);
		resourceManager = new ResourceManager();

		setupMap();
		setupUnits();
	}

	/**
	 * Sets up the Map with default values.
	 * 
	 * @throws SlickException
	 */
	public void setupMap() throws SlickException {
		Map map = getMap();

		map.fillBlocks(new RockBlock(), true);
		map.fillFloors(new RockFloor(), true);

		for (int x = 0; x < Config.START_AREA_WIDTH; x++) {
			for (int y = 0; y < Config.START_AREA_HEIGHT; y++) {
				int adjustedX = Config.START_AREA_BUFFER + x;
				int adjustedY = Config.START_AREA_BUFFER + y;

				map.removeBlock(adjustedX, adjustedY);
			}
		}
	}

	/**
	 * Sets up the World with a default set of Units.
	 * 
	 * @throws SlickException
	 */
	public void setupUnits() throws SlickException {
		EntityManager unitManager = getUnitManager();

		// Create some miners
		for (int i = 0; i < Config.START_MINER_COUNT; i++) {
			int xRandom = Util.getRandomBetween(Config.START_AREA_BUFFER, Config.START_AREA_WIDTH + Config.START_AREA_BUFFER);
			int yRandom = Util.getRandomBetween(Config.START_AREA_BUFFER, Config.START_AREA_HEIGHT + Config.START_AREA_BUFFER);

			unitManager.addEntity(new Miner(xRandom, yRandom));
		}

		// Create some builders
		for (int i = 0; i < Config.START_BUILDER_COUNT; i++) {
			int xRandom = Util.getRandomBetween(Config.START_AREA_BUFFER, Config.START_AREA_WIDTH + Config.START_AREA_BUFFER);
			int yRandom = Util.getRandomBetween(Config.START_AREA_BUFFER, Config.START_AREA_HEIGHT + Config.START_AREA_BUFFER);

			unitManager.addEntity(new Builder(xRandom, yRandom));
		}
	}

	/* Updating */

	/**
	 * Draws everything that should be drawn within the World.
	 * 
	 * @param g
	 *            the Graphics object used in rendering.
	 * @throws SlickException
	 */
	public void render(Graphics g) throws SlickException {
		float xOffset = getXOffset();
		float yOffset = getYOffset();

		getMap().render(xOffset, yOffset, g);
		getUnitManager().render(xOffset, yOffset, g);
		getEnemyManager().render(xOffset, yOffset, g);

		if (Config.RENDER_BREAK_BOXES) {
			renderBreakBoxes(g);
		}

		renderResources(g);
	}

	/**
	 * Steps the World simulation forward one tick.
	 * 
	 * @param delta
	 *            the time since the last tick in seconds.
	 * @throws SlickException
	 */
	public void tick(float delta) throws SlickException {
		getMap().tick(this, delta);
		getUnitManager().tick(this, delta);
		getEnemyManager().tick(this, delta);
		getBreakQueue().tick();
	}

	/**
	 * Renders indicators over Blocks queued to be broken.
	 * 
	 * @param g
	 * @throws SlickException
	 */
	private void renderBreakBoxes(Graphics g) throws SlickException {
		float xOffset = getXOffset();
		float yOffset = getYOffset();

		for (Block block : getBreakQueue().getQueue()) {
			if (block != null && block.isOnScreen(xOffset, yOffset)) {
				Rectangle boundingBox = block.getBoundingBox();

				// Move and scale the box to its screen position / size
				boundingBox.setLocation(Util.toPixels(boundingBox.getX() + xOffset), Util.toPixels(boundingBox.getY() + yOffset));
				boundingBox.setWidth(block.getWidthPixels());
				boundingBox.setHeight(block.getHeightPixels());

				// Actually draw the box
				Color newColor = null;
				if (getBreakQueue().getClaimed().contains(block)) {
					newColor = Color.yellow;
				} else {
					newColor = Color.red;
				}

				newColor.a = Config.BLOCK_OVERLAY_OPACITY;
				g.setColor(newColor);
				g.fill(boundingBox);

				// Reset the bounding box's position
				boundingBox.setLocation(block.getX(), block.getY());
				boundingBox.setWidth(block.getWidth());
				boundingBox.setHeight(block.getHeight());
			}
		}
	}

	private void renderResources(Graphics g) {
		ResourceManager resourceManager = getResourceManager();
		Resource[] resources = Resource.values();

		g.setColor(Color.white);

		for (int i = 0; i < resources.length; i++) {
			Resource current = resources[i];
			g.drawString(current.name() + " : " + resourceManager.getResource(current), 10, 30 + (i * 15));
		}
	}

	/* Path Finding */

	public Vector2f[] getPath(Mob mob, Vector2f start, Vector2f destination) {
		Pathfinder pathfinder = getPathfinder();
		return pathfinder.getPath(mob, start, destination);
	}

	/* Getters */

	/**
	 * @return the Map containing the World's Blocks.
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * @return the EntityManager managing the World's Units.
	 */
	public EntityManager getUnitManager() {
		return unitManager;
	}

	/**
	 * @return the EntityManager managing the World's Enemies.
	 */
	public EntityManager getEnemyManager() {
		return enemyManager;
	}

	/**
	 * @return the World's Camera's x rendering offset.
	 */
	public float getXOffset() {
		return xOffset;
	}

	/**
	 * @return the World's Camera's y rendering offset.
	 */
	public float getYOffset() {
		return yOffset;
	}

	/**
	 * @return the BlockQueue containing Blocks queued to be broken.
	 */
	public BlockQueue getBreakQueue() {
		return breakQueue;
	}

	/**
	 * @return the BlockQueue containing Blocks queued to be built.
	 */
	public BlockQueue getBuildQueue() {
		return buildQueue;
	}

	/**
	 * @return the A* Pathfinder used by the World.
	 */
	public Pathfinder getPathfinder() {
		return pathfinder;
	}

	/**
	 * @return the ResourceManager managing this World's resources.
	 */
	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	/* Setters */

	/**
	 * Sets the Map containing the World's Blocks.
	 * 
	 * @param map
	 *            the Map containing the World's Blocks.
	 */
	public void setMap(Map map) {
		this.map = map;
	}

	/**
	 * Sets the EntityManager managing the World's Units.
	 * 
	 * @param unitManager
	 */
	private void setUnitManager(EntityManager unitManager) {
		this.unitManager = unitManager;
	}

	/**
	 * Sets the EntityManager managing the World's Enemies.
	 * 
	 * @param enemyManager
	 *            the EntityManager managing the World's Enemies.
	 */
	private void setEnemyManager(EntityManager enemyManager) {
		this.enemyManager = enemyManager;
	}

	/**
	 * Sets the World's Camera's x rendering offset.
	 * 
	 * @param xOffset
	 *            the World's Camera's x rendering offset.
	 */
	public void setXOffset(float xOffset) {
		float xMin = -(Config.MAP_WIDTH - Config.SCREEN_WIDTH_GRID);

		if (xOffset > 0.0f) {
			this.xOffset = 0.0f;
		} else if (xOffset < xMin) {
			this.xOffset = xMin;
		} else {
			this.xOffset = xOffset;
		}
	}

	/**
	 * Sets the World's Camera's y rendering offset.
	 * 
	 * @param yOffset
	 *            the World's Camera's y rendering offset.
	 */
	public void setYOffset(float yOffset) {
		float yMin = -(Config.MAP_HEIGHT - Config.SCREEN_HEIGHT_GRID);

		if (yOffset > 0.0f) {
			this.yOffset = 0.0f;
		} else if (yOffset < yMin) {
			this.yOffset = yMin;
		} else {
			this.yOffset = yOffset;
		}
	}

	/**
	 * Sets the BlockQueue holding Blocks queued to be broken.
	 * 
	 * @param breakQueue
	 *            the BlockQueue holding Blocks queued to be broken.
	 */
	private void setBreakQueue(BlockQueue breakQueue) {
		this.breakQueue = breakQueue;
	}

	/**
	 * Sets the BlockQueue holding Blocks queued to be built.
	 * 
	 * @param buildQueue
	 */
	private void setBuildQueue(BlockQueue buildQueue) {
		this.buildQueue = buildQueue;
	}

	/**
	 * Sets the A* Pathfinder used by the World.
	 * 
	 * @param pathfinder
	 *            the A* Pathfinder used by the World.
	 */
	private void setPathfinder(Pathfinder pathfinder) {
		this.pathfinder = pathfinder;
	}

	/**
	 * Sets the ResourceManager managing this World's resources.
	 * 
	 * @param resourceManager
	 *            the ResourceManager managing this World's resources.
	 */
	private void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

}
