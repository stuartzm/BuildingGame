package us.rescyou.meme;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import us.rescyou.meme.Config.StateID;
import us.rescyou.meme.state.LoadingState;
import us.rescyou.meme.state.RunningState;
import us.rescyou.meme.state.StateManager;

public class Game extends BasicGame {

	/* Private Variables */

	private StateManager stateManager;

	/* Initialization */

	/**
	 * Creates a Game and a Container to hold it.
	 * 
	 * @param args
	 *            unused.
	 * @throws SlickException
	 */
	public static void main(String[] args) throws SlickException {
		AppGameContainer container = new AppGameContainer(new Game(Config.SCREEN_TITLE));
		container.setDisplayMode(Config.SCREEN_WIDTH_PIXELS, Config.SCREEN_HEIGHT_PIXELS, Config.SCREEN_FULLSCREEN);
		container.setAlwaysRender(true);
		container.start();
	}

	/**
	 * Creates a new Game.
	 * 
	 * @param title
	 *            the title displayed at the top of the Game window.
	 */
	public Game(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		stateManager = new StateManager();

		initializeStates(container);
		stateManager.enterState(Config.FIRST_STATE);
	}

	/**
	 * Initializes all the States to be used in the Game.
	 * 
	 * @throws SlickException
	 */
	private void initializeStates(GameContainer container) throws SlickException {
		StateManager stateManager = getStateManager();

		stateManager.addState(StateID.LOADING, new LoadingState());
		stateManager.addState(StateID.RUNNING, new RunningState(container));
	}

	/* Updating */

	@Override
	public void render(GameContainer container, Graphics g) throws SlickException {
		g.scale(Config.SCREEN_SCALE, Config.SCREEN_SCALE);
		getStateManager().getCurrentState().render(container, this, g);
	}

	/**
	 * Steps the Game simulation forward one tick.
	 * 
	 * @param container
	 *            the Container holding the Game.
	 * @param delta
	 *            the amount of time since the last update in seconds.
	 * @throws SlickException
	 */
	public void tick(GameContainer container, float delta) throws SlickException {
		getStateManager().getCurrentState().tick(container, this, delta);
	}

	@Override
	public void update(GameContainer container, int deltaMilliseconds) throws SlickException {
		final int millisecondsPerSecond = 1000;
		float deltaSeconds = (float) deltaMilliseconds / (float) millisecondsPerSecond;

		tick(container, deltaSeconds);
	}

	/* Getters */

	/**
	 * @return the StateManager holding the Game's States.
	 */
	public StateManager getStateManager() {
		return stateManager;
	}

}