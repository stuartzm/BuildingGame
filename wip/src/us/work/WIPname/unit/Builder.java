package us.rescyou.meme.unit;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.Assets;
import us.rescyou.meme.block.Block;
import us.rescyou.meme.block.BlockQueue;
import us.rescyou.meme.pathfinding.BlockPathPair;
import us.rescyou.meme.task.BreakTask;
import us.rescyou.meme.task.BuildTask;
import us.rescyou.meme.task.TaskQueue;
import us.rescyou.meme.world.World;

public class Builder extends BlockTargetUnit {

	/* Configuration */

	public static final SpriteSheet SPRITE_SHEET = Assets.creatures;
	public static final Vector2f[] SPRITE_SHEET_POSITIONS = { new Vector2f(2, 5), new Vector2f(6, 5) };
	public static final float SPEED = Unit.SPEED;

	public static final float BUILDING_TIME_SECONDS = Miner.MINING_TIME_SECONDS;

	/* Initialization */

	/**
	 * Creates a new Builder.
	 * 
	 * @param startX
	 *            the x value of the Builder's initial position.
	 * @param startY
	 *            the y value of the Builder's initial position.
	 * @throws SlickException
	 */
	public Builder(float startX, float startY) throws SlickException {
		super(startX, startY, SPRITE_SHEET, SPRITE_SHEET_POSITIONS, SPEED);
	}

	/* Updating */

	@Override
	public void idle(World world, float delta) throws SlickException {
		setFindTaskTimer(getFindTaskTimer() + delta);
		float findTaskTimer = getFindTaskTimer();

		if (findTaskTimer >= Unit.FIND_TASK_WAIT_TIME_SECONDS) {
			setFindTaskTimer(0.0f);
		} else {
			// Haven't yet passed the timer
			return;
		}

		// Make sure there is something in the Break Queue
		BlockQueue buildQueue = world.getBuildQueue();
		
		if (buildQueue == null || buildQueue.getQueue().size() == 0) {
			return;
		}

		// See if the Break Queue has changed at all
		BlockQueue oldBuildQueue = getOldBuildQueueCopy();

		if (oldBuildQueue != null && oldBuildQueue.equals(buildQueue)) {
			// Update the old break queue copy
			setOldBuildQueueCopy(new BlockQueue(buildQueue));

			return;
		}

		// Update the old break queue copy
		setOldBuildQueueCopy(new BlockQueue(buildQueue));
		BlockPathPair pair = buildQueue.getClosestUnclaimedBlock(this, world, getPosition());
		
		// Nothing available to work on
		if(pair == null) {
			return;
		}
		
		Block target = pair.getBlock();
		Vector2f[] targetPath = pair.getPath();
		
		if (target == null) {
			return;
		}

		TaskQueue taskQueue = getTaskQueue();
		taskQueue.addTask(new BuildTask(world, this, target, targetPath));
		buildQueue.claimBlock(target);
	}

}
