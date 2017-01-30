package us.rescyou.meme.state;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.Assets;
import us.rescyou.meme.Config;
import us.rescyou.meme.Game;
import us.rescyou.meme.Util;
import us.rescyou.meme.block.Block;
import us.rescyou.meme.block.BlockQueue;
import us.rescyou.meme.block.RockBlock;
import us.rescyou.meme.entity.Entity;
import us.rescyou.meme.entity.EntityManager;
import us.rescyou.meme.menu.Button;
import us.rescyou.meme.menu.GUI;
import us.rescyou.meme.menu.Menu;
import us.rescyou.meme.pathfinding.Pathfinder;
import us.rescyou.meme.task.MoveTask;
import us.rescyou.meme.task.Task;
import us.rescyou.meme.task.TaskQueue;
import us.rescyou.meme.unit.Unit;
import us.rescyou.meme.world.Map;
import us.rescyou.meme.world.World;

public class RunningState extends State {

	/* Private Variables */

	private World world;
	private Unit selected;
	private InputMode inputMode;
	private Menu currentMenu;
	private GUI gui;

	/* Enumerated Variables */

	private enum InputMode {
		NORMAL, BUILD, BREAK;
	}

	/* Initialization */

	/**
	 * Creates a new RunningState.
	 * 
	 * @throws SlickException
	 */
	public RunningState(GameContainer container) throws SlickException {
		super();
		this.world = new World(Config.MAP_WIDTH, Config.MAP_HEIGHT);
		this.inputMode = InputMode.NORMAL;
		this.currentMenu = null;

		Input input = container.getInput();
		this.gui = new GUI(input);

		KeyListener keyboardListener = new KeyListener() {

			@Override
			public void inputEnded() {
			}

			@Override
			public void inputStarted() {
			}

			@Override
			public boolean isAcceptingInput() {
				return true;
			}

			@Override
			public void setInput(Input input) {
			}

			@Override
			public void keyPressed(int keyCode, char keyCharacter) {
			}

			@Override
			public void keyReleased(int keyCode, char keyCharacter) {
				switch (keyCharacter) {
				case Config.INPUT_NORMAL_MODE:
					closeMenu();
					setInputMode(InputMode.NORMAL);
					break;
				case Config.INPUT_BUILD_MODE:
//					openBuildMenu(container);
					setInputMode(InputMode.BUILD);
					break;
				case Config.INPUT_BREAK_MODE:
					closeMenu();
					setInputMode(InputMode.BREAK);
					break;
				}
			}

		};

		MouseListener mouseListener = new MouseListener() {

			@Override
			public void inputEnded() {
			}

			@Override
			public void inputStarted() {
			}

			@Override
			public boolean isAcceptingInput() {
				Menu currentMenu = getCurrentMenu();
				return currentMenu == null;
			}

			@Override
			public void setInput(Input button) {
			}

			@Override
			public void mouseClicked(int button, int arg1, int arg2, int arg3) {
				World world = getWorld();
				InputMode inputMode = getInputMode();

				float worldX = Util.toWorldUnits(input.getMouseX(), world.getXOffset());
				float worldY = Util.toWorldUnits(input.getMouseY(), world.getYOffset());

				switch (button) {
				case Config.INPUT_MOUSE_ACTION_1:
					switch (inputMode) {
					case NORMAL:
						selectUnit(worldX, worldY);
						break;
					case BUILD:
						enqueueBuildQueue(worldX, worldY);
						break;
					case BREAK:
						enqueueBreakQueue(worldX, worldY);
						break;
					}
					break;
				case Config.INPUT_MOUSE_ACTION_2:
					switch (inputMode) {
					case NORMAL:
						moveSelectedUnit(worldX, worldY);
						break;
					case BUILD:
						break;
					case BREAK:
						break;
					}
					break;
				}
			}

			@Override
			public void mouseDragged(int arg0, int arg1, int arg2, int arg3) {
				World world = getWorld();
				InputMode inputMode = getInputMode();

				float worldX = Util.toWorldUnits(input.getMouseX(), world.getXOffset());
				float worldY = Util.toWorldUnits(input.getMouseY(), world.getYOffset());

				if (input.isMouseButtonDown(Config.INPUT_MOUSE_ACTION_1)) {
					switch (inputMode) {
					case NORMAL:
						break;
					case BUILD:
						enqueueBuildQueue(worldX, worldY);
						break;
					case BREAK:
						enqueueBreakQueue(worldX, worldY);
						break;
					}
				} else if (input.isMouseButtonDown(Config.INPUT_MOUSE_ACTION_2)) {
					switch (inputMode) {
					case NORMAL:
						moveSelectedUnit(worldX, worldY);
						break;
					case BUILD:
						break;
					case BREAK:
						break;
					}
				}
			}

			@Override
			public void mouseMoved(int button, int arg1, int arg2, int arg3) {
			}

			@Override
			public void mousePressed(int button, int arg1, int arg2) {
			}

			@Override
			public void mouseReleased(int button, int arg1, int arg2) {
			}

			@Override
			public void mouseWheelMoved(int button) {
			}

		};

		input.addKeyListener(keyboardListener);
		input.addMouseListener(mouseListener);
	}

	/* Updating */

	@Override
	public void tick(GameContainer container, Game game, float delta) throws SlickException {
		World world = getWorld();
		world.tick(delta);

		Input input = container.getInput();
		tickCameraInput(input, delta);

		if (currentMenu != null && currentMenu.isClosed()) {
			currentMenu = null;
		}
	}

	@Override
	public void render(GameContainer container, Game game, Graphics g) throws SlickException {
		g.setFont(Assets.kenPixel);
		getWorld().render(g);

		Input input = container.getInput();
		g.setColor(Color.white);
		g.drawString("" + Pathfinder.pathfindingTime, input.getMouseX() / Config.SCREEN_SCALE, input.getMouseY() / Config.SCREEN_SCALE);

		if (Config.RENDER_SELECTED_BOX) {
			Unit selected = getSelected();
			if (selected != null) {
				Rectangle selectedBox = new Rectangle(Util.toPixels(selected.getX() + getWorld().getXOffset()), Util.toPixels(selected.getY() + getWorld().getYOffset()), Util.toPixels(1.0f), Util.toPixels(1.0f));

				Color newColor = Color.blue;
				newColor.a = Config.BLOCK_OVERLAY_OPACITY;
				g.setColor(newColor);

				g.fill(selectedBox);
			}
		}

		renderBuildMode(g);

		GUI gui = getGUI();
		gui.render(g);

		if (currentMenu != null) {
			currentMenu.render(g);
		}
	}

	private void renderBuildMode(Graphics g) {
		g.setColor(Color.white);
		g.drawString(getInputMode().name(), 10, 10);
	}

	private void tickCameraInput(Input input, float delta) {
		World world = getWorld();
		float movementDistance = Config.CAMERA_SPEED * delta;

		float xOffset = world.getXOffset();
		float yOffset = world.getYOffset();

		// Handle Camera movement
		if (input.isKeyDown(Input.KEY_W)) {
			yOffset += movementDistance;
		}

		if (input.isKeyDown(Input.KEY_S)) {
			yOffset -= movementDistance;
		}

		if (input.isKeyDown(Input.KEY_D)) {
			xOffset -= movementDistance;
		}

		if (input.isKeyDown(Input.KEY_A)) {
			xOffset += movementDistance;
		}

		world.setXOffset(xOffset);
		world.setYOffset(yOffset);
	}

	/* Input Helpers */

	private void selectUnit(float worldX, float worldY) {
		World world = getWorld();
		EntityManager unitManager = world.getUnitManager();
		ArrayList<Entity> entities = unitManager.getEntities();

		Rectangle clickBox = new Rectangle(worldX, worldY, Config.CLICK_BOX_WIDTH, Config.CLICK_BOX_HEIGHT);
		Unit selected = null;

		for (Entity ent : entities) {
			Rectangle boundingBox = ent.getBoundingBox();
			if (boundingBox.intersects(clickBox) || boundingBox.contains(clickBox)) {
				selected = (Unit) ent;
				break;
			}
		}

		setSelected(selected);
	}

	private void enqueueBreakQueue(float worldX, float worldY) {
		World world = getWorld();
		BlockQueue breakQueue = world.getBreakQueue();

		Map map = world.getMap();
		Block target = map.getBlock((int) worldX, (int) worldY);

		breakQueue.enqueueBlock(target);
	}

	private void enqueueBuildQueue(float worldX, float worldY) {
		World world = getWorld();
		Map map = world.getMap();
		BlockQueue buildQueue = world.getBuildQueue();

		// Make sure there isn't already a block here
		Block existing = map.getBlock((int) worldX, (int) worldY);
		
		if(existing != null) {
			return;
		}
		
		try {
			Block newBlock = new RockBlock(worldX, worldY);
			buildQueue.enqueueBlock(newBlock);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	private void moveSelectedUnit(float worldX, float worldY) {
		Unit selected = getSelected();

		if (selected != null) {
			World world = getWorld();
			TaskQueue taskQueue = selected.getTaskQueue();
			Task currentTask = taskQueue.getCurrentTask();

			// Remove the current task if it is a move task, allowing us to replace it safely.
			if (currentTask instanceof MoveTask) {
				selected.getTaskQueue().removeCurrentTask();
			}

			float selectedWidth = selected.getWidth();
			float selectedHeight = selected.getHeight();

			Vector2f destination = new Vector2f(worldX - (selectedWidth / 2.0f), worldY - (selectedHeight / 2.0f));
			MoveTask newMoveTask = new MoveTask(world, selected, destination);

			taskQueue.appendTask(newMoveTask);

		}
	}

	/* Menu Creation */

	private void closeMenu() {
		Menu current = getCurrentMenu();

		if (current == null) {
			return;
		}

		current.close();
		current = null;
	}

	/* Getters */

	/**
	 * @return the World currently being updated.
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * @return the Unit currently selected by the Player.
	 */
	public Unit getSelected() {
		return selected;
	}

	public InputMode getInputMode() {
		return inputMode;
	}

	public Menu getCurrentMenu() {
		return currentMenu;
	}

	public GUI getGUI() {
		return gui;
	}

	/* Setters */

	/**
	 * Sets the Unit currently selected by the Player.
	 * 
	 * @param selected
	 *            the Unit currently selected by the Player.
	 */
	public void setSelected(Unit selected) {
		this.selected = selected;
	}

	public void setInputMode(InputMode inputMode) {
		this.inputMode = inputMode;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public void setCurrentMenu(Menu currentMenu) {
		closeMenu();
		this.currentMenu = currentMenu;
	}

	public void setGUI(GUI gui) {
		this.gui = gui;
	}

}
