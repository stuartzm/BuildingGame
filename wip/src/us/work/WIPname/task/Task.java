package us.rescyou.meme.task;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.block.Block;
import us.rescyou.meme.entity.Mob;
import us.rescyou.meme.pathfinding.Pathfinder;
import us.rescyou.meme.world.World;

public abstract class Task {

	/* Private Variables */

	private World world;
	private Mob mob;
	private boolean completed;

	/* Initialization */

	/**
	 * Creates a new Task.
	 * 
	 * @param world
	 *            the World within which the Task is to be completed.
	 * @param mob
	 *            the Mob set to carry out the Task.
	 */
	public Task(World world, Mob mob) {
		this.world = world;
		this.mob = mob;
		this.completed = false;
	}

	/* Updating */

	/**
	 * Forces the Mob to complete one tick of the Task.
	 * 
	 * @param delta
	 *            the amount of time, in seconds, since the last tick.
	 * @throws SlickException
	 */
	public abstract void tick(float delta) throws SlickException;

	/* Miscellaneous */

	/**
	 * Appends a MoveTask to the Mob's TaskQueue.
	 * 
	 * @param target
	 *            the position to move to.
	 */
	public void appendMoveToTargetTask(Vector2f target, Vector2f[] path) {
		World world = getWorld();
		Mob mob = getMob();
		TaskQueue taskQueue = mob.getTaskQueue();

		// See if it's even possible to get to this block
		if (!Pathfinder.isViablePath(path, target)) {
			// Just kill this Task
			setCompleted(true);
			return;
		}

		// Make the Mob move towards the block before breaking
		MoveTask moveTask = new MoveTask(world, mob, path);
		taskQueue.addTask(moveTask);
	}

//	public void appendMoveToBlockTask(Block target, Vector2f[] path) {
//		World world = getWorld();
//		Mob mob = getMob();
//		TaskQueue taskQueue = mob.getTaskQueue();
//	}

	/* Getters */

	/**
	 * @return whether or not the Task has been fully carried out.
	 */
	public boolean isCompleted() {
		return completed;
	}

	/**
	 * @return the Mob set to carry out the Task.
	 */
	public Mob getMob() {
		return mob;
	}

	/**
	 * @return the World within which the Task is to be carried out.
	 */
	public World getWorld() {
		return world;
	}

	/* Setters */

	/**
	 * Sets whether or not the Task has been fully carried out.
	 * 
	 * @param completed
	 *            whether or not the Task has been fully carried out.
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	/**
	 * Sets the Mob set to carry out the Task.
	 * 
	 * @param mob
	 *            the Mob set to carry out the Task.
	 */
	public void setMob(Mob mob) {
		this.mob = mob;
	}

	/**
	 * Sets the World within which the Task is to be carried out.
	 * 
	 * @param world
	 *            the World within which the Task is to be carried out.
	 */
	public void setWorld(World world) {
		this.world = world;
	}

}
