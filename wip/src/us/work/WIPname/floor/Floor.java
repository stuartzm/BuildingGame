package us.rescyou.meme.floor;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.entity.Entity;
import us.rescyou.meme.world.World;

public class Floor extends Entity {

	public Floor(float startX, float startY, SpriteSheet spriteSheet, Vector2f[] spriteSheetPositions)
			throws SlickException {
		super(startX, startY, spriteSheet, spriteSheetPositions);
	}
	
	public Floor(Floor copy) throws SlickException {
		this(copy.getX(), copy.getY(), copy.getSpriteSheet(), copy.getSpriteSheetPositions());
		setSpriteSheetX(copy.getSpriteSheetX());
		setSpriteSheetY(copy.getSpriteSheetY());
	}

	@Override
	public void tick(World world, float delta) throws SlickException {
		
	}

}
