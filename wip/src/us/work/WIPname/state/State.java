package us.rescyou.meme.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import us.rescyou.meme.Game;

public abstract class State {

	/* Initialization */
	
	public State() throws SlickException {
		
	}
	
	/**
	 * Steps the Game simulation forward one tick.
	 * 
	 * @param container
	 *            the Container holding the Game.
	 * @param game
	 *            the Game holding this State.
	 * @param delta
	 *            the time since the last tick in seconds.
	 * @throws SlickException
	 */
	public abstract void tick(GameContainer container, Game game, float delta) throws SlickException;

	/**
	 * Draws everything that should be drawn within the State.
	 * 
	 * @param container
	 *            the Container holding the Game.
	 * @param game
	 *            the Game holding this State.
	 * @param g
	 *            the Graphics object used in rendering.
	 * @throws SlickException
	 */
	public abstract void render(GameContainer container, Game game, Graphics g) throws SlickException;

}
