package us.rescyou.meme.floor;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.Assets;

public class RockFloor extends Floor {

	/* Configuration */

	public static final SpriteSheet SPRITE_SHEET = Assets.terrain;
	public static final Vector2f[] SPRITE_SHEET_POSITIONS = { new Vector2f(29, 24), new Vector2f(30, 24) };

	/* Initialization */

	/**
	 * Creates a new RockFloor template.
	 * 
	 * @throws SlickException
	 */
	public RockFloor() throws SlickException {
		super(0.0f, 0.0f, SPRITE_SHEET, SPRITE_SHEET_POSITIONS);
	}

}
