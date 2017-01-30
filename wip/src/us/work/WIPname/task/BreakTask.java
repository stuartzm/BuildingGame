package us.rescyou.meme.task;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.Config;
import us.rescyou.meme.block.Block;
import us.rescyou.meme.block.BonesBlock;
import us.rescyou.meme.entity.Mob;
import us.rescyou.meme.resource.ResourceManager.Resource;
import us.rescyou.meme.unit.Miner;
import us.rescyou.meme.world.World;

public class BreakTask extends Task {

	/* Private Variables */

	private Block target;
	private float miningTimer;

	/* Initialization */

	/**
	 * Creates a new BreakTask.
	 * 
	 * @param world
	 *            the World within which the Task is to be completed.
	 * @param mob
	 *            the Mob set to carry out the Task.
	 * @param target
	 *            the Block designated to be broken.
	 */
	public BreakTask(World world, Mob mob, Block target, Vector2f[] path) {
		super(world, mob);
		
		this.target = target;
		this.miningTimer = 0.0f;

		// Make sure the mob is actually close enough to the block to mine it
		if (!mob.isWithinInteractionDistance(target.getPosition())) {
			Vector2f targetPosition = target.getPosition();
			appendMoveToTargetTask(targetPosition, path);
		}
	}
	
	/* Updating */

	@Override
	public void tick(float delta) throws SlickException {
		if (getTarget() == null) {
			setCompleted(true);
			getMob().getTaskQueue().removeCurrentTask();
			return;
		}

		setMiningTimer(getMiningTimer() + delta);
		World world = getWorld();

		if (getMiningTimer() > Miner.MINING_TIME_SECONDS) {
			// See if we should spawn some pretty bones here
			if (Math.random() < Config.BREAK_BLOCK_BONES_CHANCE) {
				Block target = getTarget();
				Block bones = new BonesBlock();

				// Remove the old block from the world break queue if it's there
				// Adds the bones, so they'll get mined right away
				world.getBreakQueue().enqueueBlock(bones);
				world.getBreakQueue().dequeueBlock(target);

				// Place the bones
				world.getMap().placeBlock(bones, (int) target.getX(), (int) target.getY());
			} else { // Just remove the block
				world.getMap().removeBlock(getTarget());
			}

			world.getResourceManager().addResource(Resource.STONE, 1);
			setCompleted(true);
			return;
		}
	}

	/* Getters */

	/**
	 * @return the Block designated to be broken.
	 */
	public Block getTarget() {
		return target;
	}

	/**
	 * @return the amount of time, in seconds, since the Mob started breaking.
	 */
	private float getMiningTimer() {
		return miningTimer;
	}

	/* Setters */

	/**
	 * Sets the Block designated to be broken.
	 * 
	 * @param target
	 *            the Block designated to be broken.
	 */
	private void setTarget(Block target) {
		this.target = target;
	}

	/**
	 * Sets the amount of time, in seconds, since the Mob started breaking.
	 * 
	 * @param miningTimer
	 *            the amount of time, in seconds, since the Mob started
	 *            breaking.
	 */
	private void setMiningTimer(float miningTimer) {
		this.miningTimer = miningTimer;
	}

}