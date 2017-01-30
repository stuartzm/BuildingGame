package us.rescyou.meme.projectile;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.Util;
import us.rescyou.meme.entity.Entity;
import us.rescyou.meme.world.World;


public abstract class Projectile extends Entity
{
	/* Configuration */
	
	public static final float BASE_SPEED 				= 10.0f;
	public static final float BOUNDING_BOX_PADDING 		= 0.15f;

	
	/* Private */
	
	private float speed = BASE_SPEED;
	
	
	/* Constructor */
	
	public Projectile(float startX, float startY, float speed, SpriteSheet spriteSheet, Vector2f[] spriteSheetPositions) throws SlickException
	{
		super(startX, startY, spriteSheet, spriteSheetPositions);
		this.speed = speed;
		
		repositionBoundingBox();
	}
	
	
	/* Updating */

	public void tick(World world, float delta) throws SlickException 
	{
		idle(world, delta);
	}
	
	public void render(float xOffset, float yOffset, Graphics g) throws SlickException 
	{
		if (isOnScreen(xOffset, yOffset)) 
		{
			float adjustedX = Util.toPixels(getX() + xOffset);
			float adjustedY = Util.toPixels(getY() + yOffset);

			getImage().draw(adjustedX, adjustedY);
		}
	}

	public abstract void idle(World world, float delta) throws SlickException;

	
	/* Movement */
	
	public void moveX(float x) 
	{
		setX(getX() + x);
	}
	
	public void moveY(float y) 
	{
		setY(getY() + y);
	}
	
	public void move(Vector2f delta) 
	{
		moveX(delta.x);
		moveY(delta.y);
	}
	
	
	/* Checkers */
	
	private void repositionBoundingBox() 
	{
		Rectangle boundingBox 		= getBoundingBox();

		float boxX 					= getX() + BOUNDING_BOX_PADDING;
		float boxY 					= getY() + BOUNDING_BOX_PADDING;

		float boxWidth 				= getWidth() - (BOUNDING_BOX_PADDING * 2);
		float boxHeight 			= getHeight() - (BOUNDING_BOX_PADDING * 2);

		boundingBox.setX(boxX);
		boundingBox.setY(boxY);

		boundingBox.setWidth(boxWidth);
		boundingBox.setHeight(boxHeight);
	}
	
	
	/* Getters */

	public float getSpeed() 
	{
		return speed;
	}


	/* Setters */

	public void setSpeed(float speed) 
	{
		this.speed = speed;
	}

	public void setX(float x) 
	{
		super.setX(x);
		repositionBoundingBox();
	}

	public void setY(float y) 
	{
		super.setY(y);
		repositionBoundingBox();
	}
}
