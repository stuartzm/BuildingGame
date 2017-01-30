package us.rescyou.meme.unit;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.entity.Mob;

public abstract class Unit extends Mob {

	/* Configuration */

	public static final float TASK_UPDATE_INTERVAL_SECONDS = 5.0f;
	public static final float INTERACTION_DISTANCE_MINIMUM = 2.0f;
	public static final float FIND_TASK_WAIT_TIME_SECONDS = 0.25f;

	/* Initialization */

	/**
	 * Creates a new friendly, controllable Unit.
	 * 
	 * @param startX
	 *            the x value of the Mob's initial position.
	 * @param startY
	 *            the y value of the Mob's initial position.
	 * @param spriteSheet
	 *            the SpriteSheet containing the Mob's image.
	 * @param spriteSheetPositions
	 *            the array of possible positions from which the Mob's image may be drawn.
	 * @param speed
	 *            the speed of the Unit in grid units per second.
	 * @throws SlickException
	 */
	public Unit(float startX, float startY, SpriteSheet spriteSheet, Vector2f[] spriteSheetPositions, float speed) throws SlickException {
		super(startX, startY, spriteSheet, spriteSheetPositions, speed);
	}

}