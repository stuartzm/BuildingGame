package us.rescyou.meme.unit;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.Assets;
import us.rescyou.meme.Config;
import us.rescyou.meme.Util;
import us.rescyou.meme.block.Block;
import us.rescyou.meme.block.BlockQueue;
import us.rescyou.meme.pathfinding.BlockPathPair;
import us.rescyou.meme.task.BreakTask;
import us.rescyou.meme.task.TaskQueue;
import us.rescyou.meme.world.World;

public class Miner extends BlockTargetUnit {

	/* Configuration */

	public static final SpriteSheet SPRITE_SHEET = Assets.creatures;
	public static final Vector2f[] SPRITE_SHEET_POSITIONS = { new Vector2f(13, 24), new Vector2f(14, 24), new Vector2f(15, 24), new Vector2f(16, 24), };
	public static final float SPEED = Unit.SPEED;

	public static final float FIND_TASK_WAIT_TIME_SECONDS = 0.25f;
	public static final float MINING_TIME_SECONDS = 1.0f;

	/* Initialization */

	/**
	 * Creates a new Miner.
	 * 
	 * @param startX
	 *            the x value of the Miner's initial position.
	 * @param startY
	 *            the y value of the Miner's initial position.
	 * @throws SlickException
	 */
	public Miner(float startX, float startY) throws SlickException {
		super(startX, startY, SPRITE_SHEET, SPRITE_SHEET_POSITIONS, SPEED);
	}

	/* Updating */

	@Override
	public void idle(World world, float delta) throws SlickException {
		setFindTaskTimer(getFindTaskTimer() + delta);
		float findTaskTimer = getFindTaskTimer();

		if (findTaskTimer >= FIND_TASK_WAIT_TIME_SECONDS) {
			setFindTaskTimer(0.0f);
		} else {
			// Haven't yet passed the timer
			return;
		}

		// Make sure there is something in the Break Queue
		BlockQueue breakQueue = world.getBreakQueue();

		if (breakQueue == null || breakQueue.getQueue().size() == 0) {
			return;
		}

		// See if the Break Queue has changed at all
		BlockQueue oldBreakQueue = getOldBuildQueueCopy();

		if (oldBreakQueue != null && oldBreakQueue.equals(breakQueue)) {
			// Update the old break queue copy
			setOldBuildQueueCopy(new BlockQueue(breakQueue));

			return;
		}

		// Update the old break queue copy
		setOldBuildQueueCopy(new BlockQueue(breakQueue));
		BlockPathPair pair = breakQueue.getClosestUnclaimedBlock(this, world, getPosition());

		// Nothing available to work on
		if (pair == null) {
			return;
		}

		Block target = pair.getBlock();
		Vector2f[] targetPath = pair.getPath();

		if (target == null) {
			return;
		}

		TaskQueue taskQueue = getTaskQueue();
		taskQueue.addTask(new BreakTask(world, this, target, targetPath));
		breakQueue.claimBlock(target);
	}

	@Override
	public void render(float xOffset, float yOffset, Graphics g) throws SlickException {
		super.render(xOffset, yOffset, g);
		
		if(isOnScreen(xOffset, yOffset) && getTaskQueue().getCurrentTask() instanceof BreakTask) {
			BreakTask task = (BreakTask) getTaskQueue().getCurrentTask();
			Block target = task.getTarget();
			
			Rectangle breakIndicator = new Rectangle(Util.toPixels(target.getX() + xOffset), Util.toPixels(target.getY() + yOffset), Util.toPixels(1.0f), Util.toPixels(1.0f));
			
			g.setColor(new Color(0.0f, 0.5f, 1.0f, Config.BLOCK_OVERLAY_OPACITY));
			g.fill(breakIndicator);
		}
	}

}
