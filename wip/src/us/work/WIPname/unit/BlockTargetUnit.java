package us.rescyou.meme.unit;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.block.BlockQueue;

public abstract class BlockTargetUnit extends Unit {

	/* Private Variables */

	private float findTaskTimer;
	private BlockQueue oldBlockQueueCopy;

	/* Initialization */

	public BlockTargetUnit(float startX, float startY, SpriteSheet spriteSheet, Vector2f[] spriteSheetPositions, float speed) throws SlickException {
		super(startX, startY, spriteSheet, spriteSheetPositions, speed);
		this.oldBlockQueueCopy = null;
		this.findTaskTimer = 0.0f;
	}

	/* Getters */

	/**
	 * @return the amount of time, in seconds, the Miner has been waiting to find a new task.
	 */
	public float getFindTaskTimer() {
		return findTaskTimer;
	}

	/**
	 * @return a copy of the World's BlockQueue as it was last tick.
	 */
	public BlockQueue getOldBuildQueueCopy() {
		return oldBlockQueueCopy;
	}

	/* Setters */

	/**
	 * Sets the amount of time the Miner has been waiting to find a new task.
	 * 
	 * @param findTaskTimer
	 *            the amount of time, in seconds, the Miner has been waiting to find a new task.
	 */
	public void setFindTaskTimer(float findTaskTimer) {
		this.findTaskTimer = findTaskTimer;
	}

	/**
	 * Sets the copy of the World's BlockQueue as it was last tick.
	 * 
	 * @param oldBuildQueueCopy
	 *            the copy of the World's BlockQueue as it was last tick.
	 */
	public void setOldBuildQueueCopy(BlockQueue oldBuildQueueCopy) {
		this.oldBlockQueueCopy = oldBuildQueueCopy;
	}

}
