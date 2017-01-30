package us.rescyou.meme.block;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.Assets;

public class RockBlock extends Block {

	/* Configuration */

	public static final SpriteSheet SPRITE_SHEET = Assets.terrain;
	public static final Vector2f[] SPRITE_SHEET_POSITIONS = { new Vector2f(1, 1) };

	/* Initialization */

	public RockBlock(float startX, float startY) throws SlickException {
		super(startX, startY, SPRITE_SHEET, SPRITE_SHEET_POSITIONS);
	}

	public RockBlock() throws SlickException {
		this(0.0f, 0.0f);
	}

}
