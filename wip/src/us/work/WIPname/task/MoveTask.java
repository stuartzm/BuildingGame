package us.rescyou.meme.task;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.entity.Mob;
import us.rescyou.meme.world.World;

public class MoveTask extends Task {

	/* Private Variables */

	private Vector2f destination;
	private Vector2f[] path;
	private int targetPositionIndex;

	/* Initialization */

	/**
	 * Creates a new MoveTask.
	 * 
	 * @param world
	 *            the World within which the Task is to be completed.
	 * @param mob
	 *            the Mob set to carry out the Task.
	 * @param path
	 *            the Way-Points defining where to travel.
	 */
	public MoveTask(World world, Mob mob, Vector2f[] path) {
		super(world, mob);
		this.destination = path[path.length - 1];
		this.path = path;
		this.targetPositionIndex = 0;
	}

	/**
	 * Creates a new MoveTask.
	 * 
	 * @param world
	 *            the World within which the Task is to be completed.
	 * @param mob
	 *            the Mob set to carry out the Task.
	 * @param destination
	 *            the position to which the Mob should travel.
	 */
	public MoveTask(World world, Mob mob, Vector2f destination) {
		this(world, mob, mob.getPathTo(world, destination.getX(), destination.getY()));
		this.destination = destination;
	}

	/* Updating */

	@Override
	public void tick(float delta) throws SlickException {
		if (getPath() == null) {
			setCompleted(true);
			return;
		}

		// See if we need to move on to the next node in the path
		if (isAtTargetPosition()) {
			// We've finished the path
			if (getTargetPositionIndex() == getPath().length - 1) {
				setCompleted(true);
				return;
			}

			setTargetPositionIndex(getTargetPositionIndex() + 1);
		}

		// Move towards the new node
		moveTowardsTargetPosition(delta);
	}

	/* Miscellaneous */

	/**
	 * @return whether or not the Mob is at the position its moving towards.
	 */
	private boolean isAtTargetPosition() {
		Vector2f[] path = getPath();
		Mob mob = getMob();
		int targetPositionIndex = getTargetPositionIndex();

		Vector2f nextTarget = path[targetPositionIndex];
		Vector2f mobPosition = mob.getPosition();

		float distance = new Vector2f(nextTarget).sub(mobPosition).length();
		return distance <= Mob.PATH_DESTINATION_CHANGE_BUFFER;
	}

	/**
	 * Moves the Mob towards the target position.
	 * 
	 * @param delta
	 *            the amount of time, in seconds, since the last tick.
	 */
	private void moveTowardsTargetPosition(float delta) {
		Vector2f[] path = getPath();
		Mob mob = getMob();
		int targetPositionIndex = getTargetPositionIndex();

		Vector2f target = path[targetPositionIndex];
		Vector2f mobPosition = mob.getPosition();

		Vector2f deltaMovement = new Vector2f(target).sub(mobPosition);
		deltaMovement.normalise().scale(getMob().getMovementDistance(delta));

		getMob().move(deltaMovement);
	}

	/**
	 * Moves the Mob towards some provided destination.
	 * 
	 * @param destination
	 *            the position towards which the Mob will move.
	 * @param delta
	 *            the amount of time, in seconds, since the last tick.
	 */
	public void moveTowards(Vector2f destination, float delta) {
		Mob mob = getMob();
		Vector2f mobPosition = mob.getPosition();

		Vector2f start = mobPosition;
		Vector2f difference = new Vector2f(destination).sub(new Vector2f(start));

		float distance = difference.length();
		float movementDistance = delta * mob.getSpeed();

		if (movementDistance > distance) {
			mob.setPosition(destination);
		} else {
			difference.normalise().scale(movementDistance);
			mob.moveX(difference.getX());
			mob.moveY(difference.getY());
		}
	}

	/* Getters */

	/**
	 * @return the Mob's final destination.
	 */
	public Vector2f getDestination() {
		return new Vector2f(destination);
	}

	/**
	 * @return the Mob's path through to the destination.
	 */
	public Vector2f[] getPath() {
		return path;
	}

	/**
	 * @return the distance, in grid units, between the Mob and the destination.
	 */
	public float getDistanceToDestination() {
		Mob mob = getMob();
		Vector2f destination = getDestination();
		return destination.sub(mob.getPosition()).length();
	}

	/**
	 * @return the index of the current target position.
	 */
	public int getTargetPositionIndex() {
		return targetPositionIndex;
	}

	/* Setters */

	/**
	 * Sets the destination of the MoveTask.
	 * 
	 * @param destination
	 *            the destination of the MoveTask.
	 */
	private void setDestination(Vector2f destination) {
		this.destination = new Vector2f(destination);
	}

	/**
	 * Sets the path through which the Mob will move.
	 * 
	 * @param path
	 *            the path through which the Mob will move.
	 */
	private void setPath(Vector2f[] path) {
		this.path = path;

		if (path != null && path.length > 0) {
			setDestination(path[0]);
		}
	}

	/**
	 * Sets the index of the target position.
	 * 
	 * @param targetPositionIndex
	 *            the index of the target position.
	 */
	private void setTargetPositionIndex(int targetPositionIndex) {
		this.targetPositionIndex = targetPositionIndex;
	}

}
