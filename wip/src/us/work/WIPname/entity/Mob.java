package us.rescyou.meme.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.Config;
import us.rescyou.meme.Util;
import us.rescyou.meme.block.Block;
import us.rescyou.meme.task.MoveTask;
import us.rescyou.meme.task.Task;
import us.rescyou.meme.task.TaskQueue;
import us.rescyou.meme.unit.Unit;
import us.rescyou.meme.world.Map;
import us.rescyou.meme.world.World;

public abstract class Mob extends Entity {

	/* Configuration */

	public static final float IMAGE_ALTERNATE_DISTANCE = 2.0f;
	public static final float PATH_DESTINATION_CHANGE_BUFFER = 0.1f;
	public static final float SPEED = 5.0f;
	public static final float BOUNDING_BOX_PADDING = 0.15f;
	public static final float BOB_SCALE = 1.1f;

	/* Private Variables */

	private float speed;
	private boolean flipped;
	private float alternateImageDistance;
	private boolean usingAlternateImage;
	private TaskQueue taskQueue;

	/* Initialization */

	/**
	 * Creates a new Mob.
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
	
	public Mob(float startX, float startY, SpriteSheet spriteSheet, Vector2f[] spriteSheetPositions, float speed) throws SlickException {
		super(startX, startY, spriteSheet, spriteSheetPositions);
		this.speed = speed;
		this.flipped = false;
		this.alternateImageDistance = IMAGE_ALTERNATE_DISTANCE;
		this.usingAlternateImage = false;
		this.taskQueue = new TaskQueue();

		repositionBoundingBox();
	}

	/* Updating */

	public void tick(World world, float delta) throws SlickException {
		if (getTaskQueue().getCurrentTask() != null) {
			getTaskQueue().tick(delta);
		} else {
			idle(world, delta);
		}
	}

	public void render(float xOffset, float yOffset, Graphics g) throws SlickException {
		if (isOnScreen(xOffset, yOffset)) {
			float adjustedX = Util.toPixels(getX() + xOffset);
			float adjustedY = Util.toPixels(getY() + yOffset);

			float alternateImageDistance = getAlternateImageDistance();
			float maxAlternateImageDistance = IMAGE_ALTERNATE_DISTANCE;

			float normalized = (maxAlternateImageDistance - alternateImageDistance) / maxAlternateImageDistance;
			float scale = (float) (1.0f + (Math.sin((normalized * Math.PI)) * (BOB_SCALE - 1.0f)));

			float positionOffset = Util.toPixels(-((getWidth() * scale) - 1.0f)) / 2.0f;

			renderShadow(xOffset, yOffset, positionOffset, scale, g);
			getImage().getScaledCopy(scale).draw(adjustedX + positionOffset, adjustedY + positionOffset);
		}

		if (Config.RENDER_PATHFINDING_NODES) {
			Task current = getTaskQueue().getCurrentTask();
			if (current instanceof MoveTask) {
				MoveTask moveTask = (MoveTask) current;
				Vector2f[] path = moveTask.getPath();

				if (path != null) {
					for (int i = 0; i < path.length; i++) {
						Vector2f currentPosition = path[i];

						if (currentPosition != null) {
							Rectangle draw = new Rectangle(Util.toPixels(xOffset + currentPosition.getX()), Util.toPixels(yOffset + currentPosition.getY()), 3.0f, 3.0f);
							g.setColor(Color.green);
							g.fill(draw);
						}
					}
				}
			}
		}
	}

	private void renderShadow(float xOffset, float yOffset, float positionOffsetX, float scale, Graphics g) throws SlickException {
		SpriteSheet shadowSpriteSheet = Entity.SHADOW_SPRITE_SHEET;
		int spriteSheetX = (int) Entity.SHADOW_SPRITE_SHEET_POSITION.getX();
		int spriteSheetY = (int) Entity.SHADOW_SPRITE_SHEET_POSITION.getY();
		
		Image shadow = shadowSpriteSheet.getSubImage(spriteSheetX, spriteSheetY).getScaledCopy(scale);
		shadow.setAlpha(Config.SHADOW_OPACITY);
		
		float bufferOffset = -((Config.SHADOW_HEIGHT_BUFFER * scale) - Config.SHADOW_HEIGHT_BUFFER) / 2.0f;
		float adjustedX = Util.toPixels(getX() + xOffset) + positionOffsetX;
		float adjustedY = Util.toPixels(getY() + yOffset) + bufferOffset + Config.SHADOW_HEIGHT_BUFFER;

		shadow.draw(adjustedX, adjustedY);
	}

	public abstract void idle(World world, float delta) throws SlickException;

	/* Movement */

	public void moveX(float x) {
		setX(getX() + x);

		// Flip the image if required
		if (x > 0) {
			setFlipped(true);
		} else {
			setFlipped(false);
		}

		// See if we need to animate
		float alternateImageDistance = getAlternateImageDistance();
		setAlternateImageDistance(alternateImageDistance - Math.abs(x));

		if (getAlternateImageDistance() <= 0.0f) {
			setAlternateImageDistance(IMAGE_ALTERNATE_DISTANCE);
			setUsingAlternateImage(!isUsingAlternateImage());

			if (isUsingAlternateImage()) {
				setSpriteSheetY(getSpriteSheetY() + 1);
			} else {
				setSpriteSheetY(getSpriteSheetY() - 1);
			}
		}
	}

	public void moveY(float y) {
		setY(getY() + y);

		// See if we need to animate
		float alternateImageDistance = getAlternateImageDistance();
		setAlternateImageDistance(alternateImageDistance - Math.abs(y));
		if (getAlternateImageDistance() <= 0.0f) {
			setAlternateImageDistance(IMAGE_ALTERNATE_DISTANCE);
			setUsingAlternateImage(!isUsingAlternateImage());

			if (isUsingAlternateImage()) {
				setSpriteSheetY(getSpriteSheetY() + 1);
			} else {
				setSpriteSheetY(getSpriteSheetY() - 1);
			}
		}
	}

	public void move(Vector2f delta) {
		moveX(delta.x);
		moveY(delta.y);
	}

	public Vector2f[] getPathTo(World world, float x, float y) {
		return world.getPath(this, getPosition(), new Vector2f(x, y));
	}

	/* Miscellaneous */

	public boolean wouldCollide(Map map, float x, float y) {
		/*
		 * Temporarily move the Mob to the test position. We're moving the whole mob because this will move the boundingBox as well, but will respect
		 * it's padding. This wouldn't happen if we just did boundingBox.setLocation() or whatever.
		 */
		float oldX = getX();
		float oldY = getY();

		setX(x);
		setY(y);

		Rectangle boundingBox = getBoundingBox();
		Vector2f boundingBoxPosition = boundingBox.getLocation();

		int startXIndex = (int) boundingBoxPosition.getX();
		int startYIndex = (int) boundingBoxPosition.getY();

		for (int dx = -Config.COLLISION_SEARCH_RADIUS; dx <= Config.COLLISION_SEARCH_RADIUS; dx++) {
			for (int dy = -Config.COLLISION_SEARCH_RADIUS; dy <= Config.COLLISION_SEARCH_RADIUS; dy++) {
				int currentXIndex = startXIndex + dx;
				int currentYIndex = startYIndex + dy;

				// Make sure that the location we're checking is in bounds
				if (!map.isInBounds(currentXIndex, currentYIndex)) {
					continue;
				}

				Block currentBlock = map.getBlock(currentXIndex, currentYIndex);

				// Don't bother checking if the Block doesn't exist
				if (currentBlock == null) {
					continue;
				}

				Rectangle currentBoundingBox = currentBlock.getBoundingBox();

				if (currentBoundingBox.intersects(boundingBox) || currentBoundingBox.contains(boundingBox)) {
					// Move the mob back
					setX(oldX);
					setY(oldY);

					return true;
				}
			}
		}

		// Move the mob back
		setX(oldX);
		setY(oldY);

		return false;
	}

	public boolean isWithinInteractionDistance(Vector2f testPosition) {
		Vector2f position = getPosition();
		float distance = new Vector2f(position).sub(testPosition).length();

		return distance < Unit.INTERACTION_DISTANCE_MINIMUM;
	}

	private void repositionBoundingBox() {
		Rectangle boundingBox = getBoundingBox();

		float boxX = getX() + BOUNDING_BOX_PADDING;
		float boxY = getY() + BOUNDING_BOX_PADDING;

		float boxWidth = getWidth() - (BOUNDING_BOX_PADDING * 2);
		float boxHeight = getHeight() - (BOUNDING_BOX_PADDING * 2);

		boundingBox.setX(boxX);
		boundingBox.setY(boxY);

		boundingBox.setWidth(boxWidth);
		boundingBox.setHeight(boxHeight);
	}

	/* Getters */

	public float getSpeed() {
		return speed;
	}

	public float getMovementDistance(float delta) {
		return getSpeed() * delta;
	}

	@Override
	public Image getImage() {
		return getSpriteSheet().getSubImage(getSpriteSheetX(), getSpriteSheetY()).getFlippedCopy(isFlipped(), false);
	}

	private boolean isFlipped() {
		return flipped;
	}

	private float getAlternateImageDistance() {
		return alternateImageDistance;
	}

	public boolean isUsingAlternateImage() {
		return usingAlternateImage;
	}

	public TaskQueue getTaskQueue() {
		return taskQueue;
	}

	/* Setters */

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setX(float x) {
		super.setX(x);
		repositionBoundingBox();
	}

	public void setY(float y) {
		super.setY(y);
		repositionBoundingBox();
	}

	private void setFlipped(boolean flipped) {
		this.flipped = flipped;
	}

	private void setAlternateImageDistance(float alternateImageDistance) {
		this.alternateImageDistance = alternateImageDistance;
	}

	private void setUsingAlternateImage(boolean usingAlternateImage) {
		this.usingAlternateImage = usingAlternateImage;
	}

	public void setTaskQueue(TaskQueue taskQueue) {
		this.taskQueue = taskQueue;
	}

}
