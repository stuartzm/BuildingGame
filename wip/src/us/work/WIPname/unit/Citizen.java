package us.rescyou.meme.unit;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.Assets;
import us.rescyou.meme.world.World;

public class Citizen extends Unit {

	/* Configuration */

	public static final SpriteSheet SPRITE_SHEET = Assets.creatures;
	public static final Vector2f[] SPRITE_SHEET_POSITIONS = {  new Vector2f(5, 24), new Vector2f(6, 24) };
	public static final float SPEED = Unit.SPEED;

	/* Initialization */

	/**
	 * Creates a new Citizen.
	 * 
	 * @param startX
	 *            the x value of the Citizen's initial position.
	 * @param startY
	 *            the y value of the Citizen's initial position.
	 * @throws SlickException
	 */
	public Citizen(float startX, float startY) throws SlickException {
		super(startX, startY, SPRITE_SHEET, SPRITE_SHEET_POSITIONS, SPEED);
	}

	/* Updating */
	
	@Override
	public void idle(World world, float delta) throws SlickException {
		// Do nothing
	}

}
