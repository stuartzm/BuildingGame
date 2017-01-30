package us.rescyou.meme.block;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.Assets;

public class WallBlock extends Block {
	
	/* Configuration */
	
	public static final SpriteSheet SPRITE_SHEET = Assets.terrain;
	public static final Vector2f[] SPRITE_SHEET_POSITIONS = { new Vector2f(1, 6), new Vector2f(2, 6) };
	
	/* Initialization */

	public WallBlock() throws SlickException {
		super(0.0f, 0.0f, SPRITE_SHEET, SPRITE_SHEET_POSITIONS);
	}

}
