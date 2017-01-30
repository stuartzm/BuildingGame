package us.rescyou.meme.block;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.Assets;

public class BonesBlock extends Block {

	/* Private Variables */

	public static final SpriteSheet SPRITE_SHEET = Assets.terrain;
	public static final Vector2f[] SPRITE_SHEET_POSITIONS = { new Vector2f(32, 1), new Vector2f(33, 1), new Vector2f(34, 1), new Vector2f(35, 1), new Vector2f(36, 1), new Vector2f(37, 1), new Vector2f(38, 1) };

	/* Initialization */

	public BonesBlock() throws SlickException {
		super(0.0f, 0.0f, SPRITE_SHEET, SPRITE_SHEET_POSITIONS);
	}

}
