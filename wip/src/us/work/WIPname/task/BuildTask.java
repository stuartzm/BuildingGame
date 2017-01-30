package us.rescyou.meme.task;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.block.Block;
import us.rescyou.meme.entity.Mob;
import us.rescyou.meme.pathfinding.Pathfinder;
import us.rescyou.meme.unit.Builder;
import us.rescyou.meme.world.Map;
import us.rescyou.meme.world.World;

public class BuildTask extends Task {

	/* Private Variables */

	private Block toBuild;
	private float buildTimer;

	/* Initialization */

	/**
	 * Creates a new BuildTask.
	 * 
	 * @param world
	 *            the World within which the Task is to be completed.
	 * @param mob
	 *            the Mob set to carry out the Task.
	 * @param toBuild
	 *            the Block to be built.
	 * @param buildPosition
	 *            the position at which the Block should be built.
	 */
	public BuildTask(World world, Mob mob, Block toBuild, Vector2f[] path) {
		super(world, mob);
		this.toBuild = toBuild;
		this.buildTimer = 0.0f;

		Vector2f targetPosition = toBuild.getPosition();
		Map map = world.getMap();

		int toBuildX = (int) (targetPosition.getX() / toBuild.getWidth());
		int toBuildY = (int) (targetPosition.getY() / toBuild.getHeight());

		/*
		 * We have to fuck with the path a little bit just to make sure the Builder doesn't build a Block over himself. We're godo this by replacing
		 * the last waypoint of the path with the closest non-colliding location relative to toBuild.
		 */

		// Temporarily add the toBuild Block so that we don't path ourselves inside of it
		Block old = map.getBlock(toBuildX, toBuildY);
		map.placeBlock(toBuild, toBuildX, toBuildY);

		System.out.println(path[path.length - 1]);
		Vector2f closestLocation = Pathfinder.getClosestOpenPosition(mob, map, path[path.length - 1]);
		path[path.length - 1] = closestLocation;
		System.out.println(path[path.length - 1]);

		appendMoveToTargetTask(targetPosition, path);

		// Remove the Block again
		// Accessing by index because it's faster than removing via Block
		map.removeBlock(toBuildX, toBuildY);
		map.placeBlock(old, toBuildX, toBuildY);
	}

	/* Updating */

	@Override
	public void tick(float delta) throws SlickException {
		Block toBuild = getToBuild();
		Vector2f buildPosition = toBuild.getPosition();

		// See if something is already in the build position
		if (getWorld().getMap().getBlock((int) buildPosition.getX(), (int) buildPosition.getY()) != null) {
			setCompleted(true);
			getMob().getTaskQueue().removeCurrentTask();
			return;
		}

		setBuildTimer(getBuildTimer() + delta);

		if (getBuildTimer() > Builder.BUILDING_TIME_SECONDS) {
			// Put the block here
			World world = getWorld();
			world.getMap().placeBlock(getToBuild(), (int) buildPosition.getX(), (int) buildPosition.getY());

			setCompleted(true);
			return;
		}
	}

	/* Getters */

	/**
	 * @return the Block designated to be built.
	 */
	public Block getToBuild() {
		return toBuild;
	}

	/**
	 * @return the amount of time, in seconds, since the Mob started building.
	 */
	private float getBuildTimer() {
		return buildTimer;
	}

	/* Setters */

	/**
	 * @param toBuild
	 *            the new Block to be built.
	 */
	public void setToBuild(Block toBuild) {
		this.toBuild = toBuild;
	}

	/**
	 * Sets the amount of time, in seconds, since the Mob started building.
	 * 
	 * @param buildTimer
	 *            the amount of time the Mob has been building the Block.
	 */
	private void setBuildTimer(float buildTimer) {
		this.buildTimer = buildTimer;
	}

}
