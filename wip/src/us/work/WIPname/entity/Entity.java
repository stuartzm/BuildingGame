package us.rescyou.meme.entity;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.Assets;
import us.rescyou.meme.Config;
import us.rescyou.meme.Util;
import us.rescyou.meme.unit.Miner;
import us.rescyou.meme.world.World;

public abstract class Entity {

	/* Public Variables */

	public static final SpriteSheet SHADOW_SPRITE_SHEET = Assets.creatures;
	public static final Vector2f SHADOW_SPRITE_SHEET_POSITION = new Vector2f(2, 23);

	/* Private Variables */

	private float x, y;
	private int spriteSheetX, spriteSheetY;
	private SpriteSheet spriteSheet;
	private Vector2f[] spriteSheetPositions;
	private Rectangle boundingBox;

	/* Initialization */

	/**
	 * Creates a new Entity.
	 * 
	 * @param startX
	 *            the x value of the Entity's initial position.
	 * @param startY
	 *            the y value of the Entity's initial position.
	 * @param spriteSheet
	 *            the SpriteSheet containing the Entity's image.
	 * @param spriteSheetPositions
	 *            the array of possible positions from which the Entity's image
	 *            may be drawn.
	 * @throws SlickException
	 */
	public Entity(float startX, float startY, SpriteSheet spriteSheet, Vector2f[] spriteSheetPositions) throws SlickException {
		this.x = startX;
		this.y = startY;
		this.spriteSheet = spriteSheet;
		this.spriteSheetPositions = Util.copyVector2fs(spriteSheetPositions);
		this.boundingBox = new Rectangle(x, y, getWidth(), getHeight());
		randomizeImage();
	}

	/* Updating */

	public void render(float xOffset, float yOffset, Graphics g) throws SlickException {
		if (isOnScreen(xOffset, yOffset)) {
			float adjustedX = Util.toPixels(getX() + xOffset);
			float adjustedY = Util.toPixels(getY() + yOffset);

			getImage().draw(adjustedX, adjustedY);
		}
	}

	public abstract void tick(World world, float delta) throws SlickException;

	/* Miscellaneous */

	public void randomizeImage() {
		Vector2f newPosition = (Vector2f) Util.getRandomElement(getSpriteSheetPositions());
		setSpriteSheetPosition(newPosition);
	}

	public boolean isOnScreen(float xOffset, float yOffset) {
		float adjustedX = getX() + xOffset;
		float adjustedY = getY() + yOffset;

		boolean onScreenX = adjustedX >= -getWidth() && adjustedX < Config.SCREEN_WIDTH_GRID;
		boolean onScreenY = adjustedY >= -getHeight() && adjustedY < Config.SCREEN_HEIGHT_GRID;
		
		return onScreenX && onScreenY;
	}

	/* Getters */

	public Vector2f getPosition() {
		return new Vector2f(getX(), getY());
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public SpriteSheet getSpriteSheet() {
		return spriteSheet;
	}

	public int getSpriteSheetX() {
		return spriteSheetX;
	}

	public int getSpriteSheetY() {
		return spriteSheetY;
	}

	public Vector2f[] getSpriteSheetPositions() {
		return spriteSheetPositions;
	}

	public Image getImage() {
		return getSpriteSheet().getSubImage(getSpriteSheetX(), getSpriteSheetY());
	}

	public int getWidthPixels() {
		return getImage().getWidth();
	}

	public int getHeightPixels() {
		return getImage().getHeight();
	}

	public Rectangle getBoundingBox() {
		return boundingBox;
	}

	public float getWidth() {
		return Util.toGrid(getImage().getWidth());
	}

	public float getHeight() {
		return Util.toGrid(getImage().getHeight());
	}

	/* Setters */

	public void setPosition(Vector2f position) {
		setX(position.getX());
		setY(position.getY());
	}

	public void setX(float x) {
		this.x = x;
		getBoundingBox().setX(x);
	}

	public void setY(float y) {
		this.y = y;
		getBoundingBox().setY(y);
	}

	public void setSpriteSheetX(int spriteSheetX) {
		this.spriteSheetX = spriteSheetX;
	}

	public void setSpriteSheetY(int spriteSheetY) {
		this.spriteSheetY = spriteSheetY;
	}

	public void setSpriteSheetPosition(Vector2f spriteSheetPosition) {
		setSpriteSheetX((int) spriteSheetPosition.x);
		setSpriteSheetY((int) spriteSheetPosition.y);
	}

	
	public void setSpriteSheet(SpriteSheet spriteSheet) {
		this.spriteSheet = spriteSheet;
	}
	

	public void setSpriteSheetPositions(Vector2f[] spriteSheetPositions) {
		this.spriteSheetPositions = spriteSheetPositions;
	}
	

	public void setBoundingBox(Rectangle boundingBox) {
		this.boundingBox = boundingBox;
	}

}
