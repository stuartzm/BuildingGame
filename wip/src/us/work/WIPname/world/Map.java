package us.rescyou.meme.world;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import us.rescyou.meme.Util;
import us.rescyou.meme.block.Block;
import us.rescyou.meme.entity.Entity;
import us.rescyou.meme.floor.Floor;
import us.rescyou.meme.resource.ResourceManager;

public class Map {

	/* Private Variables */

	private Block[][] blocks;
	private Floor[][] floors;

	/* Initialization */

	/**
	 * Creates a new Map.
	 * 
	 * @param width
	 *            the Width of the Map in grid units.
	 * @param height
	 *            the Height of the Map in grid units.
	 */
	public Map(int width, int height) {
		blocks = new Block[width][height];
		floors = new Floor[width][height];
	}

	/* Updating */

	/**
	 * Draws everything that should be drawn within the World.
	 * 
	 * @param xOffset
	 *            the x value of the Camera's rendering offset.
	 * @param yOffset
	 *            the y value of the Camera's rendering offset.
	 * @param g
	 *            the Graphics object used in rendering.
	 * @throws SlickException
	 */
	public void render(float xOffset, float yOffset, Graphics g) throws SlickException {
		renderAll(getFloors(), xOffset, yOffset, g);
		renderAll(getBlocks(), xOffset, yOffset, g);
	}

	/**
	 * Steps the Map simulation forward one tick.
	 * 
	 * @param delta
	 *            the time since the last tick in seconds.
	 * @throws SlickException
	 */
	public void tick(World world, float delta) throws SlickException {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				Block current = getBlock(x, y);

				if (current != null) {
					current.tick(world, delta);
				}
			}
		}
	}

	/**
	 * Renders every Entity within a 2D array.
	 * 
	 * @param entities
	 *            the 2D array of Entities.
	 * @param xOffset
	 *            the Camera's x rendering offset.
	 * @param yOffset
	 *            the Camera's y rendering offset.
	 * @param g
	 *            the Graphics object used in rendering
	 * @throws SlickException
	 */
	private void renderAll(Entity[][] entities, float xOffset, float yOffset, Graphics g) throws SlickException {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				Entity entity = entities[x][y];

				if (entity != null) {
					entity.render(xOffset, yOffset, g);
				}
			}
		}
	}

	/* Blocks */

	/**
	 * Fills the entire map with duplicates of the template Block.
	 * 
	 * @param template
	 *            the Block to make copies from.
	 * @param randomizeImages
	 *            whether or the copy should have its image randomized.
	 * @throws SlickException
	 */
	public void fillBlocks(Block template, boolean randomizeImages) throws SlickException {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				placeCopyBlock(template, x, y, randomizeImages);
			}
		}
	}

	/**
	 * Places a copy of the template Block into the Map.
	 * 
	 * @param template
	 *            the Block to make copies from.
	 * @param x
	 *            the x value of the Block's coordinates within the Map.
	 * @param y
	 *            the y value of the Block's coordinates within the Map.
	 * @param randomizeImage
	 *            whether or the copy should have its image randomized.
	 * @throws SlickException
	 */
	public void placeCopyBlock(Block template, int x, int y, boolean randomizeImage) throws SlickException {
		Block copy = new Block(template);
		placeBlock(copy, x, y);

		if (randomizeImage) {
			copy.randomizeImage();
		}
	}

	/**
	 * Places a Block into the Map.
	 * 
	 * @param block
	 *            the Block to be placed.
	 * @param x
	 *            the x value of the Block's coordinates within the Map.
	 * @param y
	 *            the y value of the Block's coordinates within the Map.
	 */
	public void placeBlock(Block block, int x, int y) {
		if(block == null) {
			return;
		}
		
		float tileSizeGrid = Util.toGrid(Util.getTileWidth(block.getSpriteSheet()));
		block.setX(x * tileSizeGrid);
		block.setY(y * tileSizeGrid);
		getBlocks()[x][y] = block;
	}

	/**
	 * Removes a Block from the Map.
	 * 
	 * @param x
	 *            the x value of the Block's position within the Map.
	 * @param y
	 *            the y value of the Block's position within the Map.
	 */
	public void removeBlock(int x, int y) {
		getBlocks()[x][y] = null;
	}

	/**
	 * Searches for and then removes a Block from the Map.
	 * 
	 * @param block
	 *            the Block to remove.
	 */
	public void removeBlock(Block block) {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				Block current = getBlock(x, y);

				if (current == block) {
					removeBlock(x, y);
				}
			}
		}
	}

	/* Floors */

	/**
	 * Fills the entire Map with duplicates of the template Floor.
	 * 
	 * @param template
	 *            the Floor to make copies from.
	 * @param randomizeImages
	 *            whether or the copies should have their images randomized.
	 * @throws SlickException
	 */
	public void fillFloors(Floor template, boolean randomizeImages) throws SlickException {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				placeCopyFloor(template, x, y, randomizeImages);
			}
		}
	}

	/**
	 * Places a copy of the template Floor into the Map.
	 * 
	 * @param template
	 *            the Floor to make copies from.
	 * @param x
	 *            the x value of the Floor's coordinates within the Map.
	 * @param y
	 *            the y value of the Floor's coordinates within the Map.
	 * @param randomizeImage
	 *            whether or the copy should have its image randomized.
	 * @throws SlickException
	 */
	public void placeCopyFloor(Floor template, int x, int y, boolean randomizeImage) throws SlickException {
		Floor copy = new Floor(template);
		placeFloor(copy, x, y);

		if (randomizeImage) {
			copy.randomizeImage();
		}
	}

	/**
	 * Places a Floor into the Map.
	 * 
	 * @param floor
	 *            the Floor to be placed.
	 * @param x
	 *            the x value of the Floor's coordinates within the Map.
	 * @param y
	 *            the y value of the Floor's coordinates within the Map.
	 */
	public void placeFloor(Floor floor, int x, int y) {
		float tileSizeGrid = Util.toGrid(Util.getTileWidth(floor.getSpriteSheet()));
		floor.setX(x * tileSizeGrid);
		floor.setY(y * tileSizeGrid);
		getFloors()[x][y] = floor;
	}

	/**
	 * Removes a Floor from the Map.
	 * 
	 * @param x
	 *            the x value of the Floor's position within the Map.
	 * @param y
	 *            the y value of the Floor's position within the Map.
	 */
	public void removeFloor(int x, int y) {
		getFloors()[x][y] = null;
	}

	/**
	 * Searches for and then removes a Floor from the Map.
	 * 
	 * @param block
	 *            the Floor to remove.
	 */
	public void removeFloor(Floor floor) {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				Floor current = getFloor(x, y);

				if (current == floor) {
					removeFloor(x, y);
				}
			}
		}
	}

	/* Miscellaneous */

	/**
	 * Returns whether or not the provided coordinates are within the bounds of this Map.
	 * 
	 * @param x
	 *            the x value of the coordinates to be tested.
	 * @param y
	 *            the y value of the coordinates to be tested.
	 * @return whether or not the provided coordinates are within the bounds of this Map.
	 */
	public boolean isInBounds(int x, int y) {
		boolean inBoundsX = x >= 0 && x < getWidth();
		boolean inBoundsY = y >= 0 && y < getHeight();
		
		return inBoundsX && inBoundsY;
	}

	/* Getters */

	/**
	 * @return all of the Blocks within the Map.
	 */
	public Block[][] getBlocks() {
		return blocks;
	}

	/**
	 * @param x
	 *            the x value of the returned Block's location.
	 * @param y
	 *            the y value of the returned Block's location.
	 * @return the Block located at the provided coordinates.
	 */
	public Block getBlock(int x, int y) {
		return getBlocks()[x][y];
	}

	/**
	 * @return the Width of the Map in grid units.
	 */
	public int getWidth() {
		return getBlocks().length;
	}

	/**
	 * @return the Height of the Map in grid units.
	 */
	public int getHeight() {
		return getBlocks()[0].length;
	}

	/**
	 * @return all of the Floors within the Map.
	 */
	public Floor[][] getFloors() {
		return floors;
	}

	/**
	 * @param x
	 *            the x value of the returned Floor's location.
	 * @param y
	 *            the y value of the returned Floor's location.
	 * @return the Floor located at the provided coordinates.
	 */
	public Floor getFloor(int x, int y) {
		return getFloors()[x][y];
	}

}
