package us.rescyou.meme.block;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.Assets;
import us.rescyou.meme.entity.Entity;
import us.rescyou.meme.world.World;

public class Block extends Entity {

	public Block(float startX, float startY, SpriteSheet spriteSheet, Vector2f[] spriteSheetPositions)
			throws SlickException {
		super(startX, startY, spriteSheet, spriteSheetPositions);
	}

	public Block(Block copy) throws SlickException {
		this(copy.getX(), copy.getY(), copy.getSpriteSheet(), copy.getSpriteSheetPositions());
		setSpriteSheetX(copy.getSpriteSheetX());
		setSpriteSheetY(copy.getSpriteSheetY());
	}

	@Override
	public void tick(World world, float delta) throws SlickException {
		// Do nothing
	}

}
