package us.rescyou.meme.block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.entity.Mob;
import us.rescyou.meme.pathfinding.BlockPathPair;
import us.rescyou.meme.pathfinding.Pathfinder;
import us.rescyou.meme.world.Map;
import us.rescyou.meme.world.World;

public class BlockQueue {

	/* Private Variables */

	private Map map;
	private Set<Block> queue;
	private Set<Block> claimed;

	/* Initialization */

	/**
	 * Creates a new BlockQueue.
	 * 
	 * @param map
	 *            the Map holding the queued Blocks.
	 */
	public BlockQueue(Map map) {
		this.map = map;
		this.queue = new HashSet<Block>();
		this.claimed = new HashSet<Block>();
	}

	public BlockQueue(BlockQueue toCopy) {
		this(toCopy.getMap());

		Set<Block> queueCopy = new HashSet<Block>(toCopy.getQueue());
		Set<Block> claimedCopy = new HashSet<Block>(toCopy.getClaimed());

		setQueue(queueCopy);
		setClaimed(claimedCopy);
	}

	/* Updating */

	/**
	 * Removes Blocks that shouldn't be in this BlockQueue.
	 */
	public void tick() {
		dequeueRemovedBlocks();
	}

	/* Adding */

	/**
	 * Adds a Block to the BlockQueue.
	 * 
	 * @param block
	 *            the Block to be added to the BlockQueue.
	 */
	public void enqueueBlock(Block block) {
		// Make sure the block isn't null
		if (block == null) {
			return;
		}

		Set<Block> queue = getQueue();

		queue.add(block);
	}

	/**
	 * Marks a Block as claimed by a Unit.
	 * 
	 * @param block
	 *            the Block to be claimed.
	 */
	public void claimBlock(Block block) {
		// Make sure the block isn't null
		if (block == null) {
			return;
		}

		Set<Block> queue = getQueue();
		Set<Block> claimed = getClaimed();

		// Make sure the Block is in the queue
		if (queue.contains(block)) {
			claimed.add(block);
		}
	}

	/* Removing */

	/**
	 * Removes a Block from the BlockQueue.
	 * 
	 * @param block
	 */
	public void dequeueBlock(Block block) {
		Set<Block> queue = getQueue();
		Set<Block> claimed = getClaimed();

		queue.remove(block);

		// Removing from claimed too; a non-queued block cannot be claimed
		claimed.remove(block);
	}

	/**
	 * Removes a Block's claimed by a Unit mark.
	 * 
	 * @param block
	 */
	public void unclaimBlock(Block block) {
		Set<Block> claimed = getClaimed();
		claimed.remove(block);
	}

	/* Miscellaneous */

	/**
	 * Dequeues all Blocks in the Queue that have been removed from the Map.
	 */
	private void dequeueRemovedBlocks() {
		Set<Block> queue = getQueue();
		Set<Block> claimed = getClaimed();
		ArrayList<Block> queueList = new ArrayList<Block>(queue);
		ArrayList<Block> claimedList = new ArrayList<Block>(queue);
		Map map = getMap();

		// Remove null Blocks, in-case they're there
		queue.remove(null);
		claimed.remove(null);

		// Remove removed queued Blocks
		for (Block block : queueList) {
			if (map.getBlock((int) block.getX(), (int) block.getY()) == null) {
				queue.remove(block);
			}
		}

		// Remove removed claimed Blocks
		for (Block block : claimedList) {
			if (map.getBlock((int) block.getX(), (int) block.getY()) == null || !queue.contains(block)) {
				claimed.remove(block);
			}
		}
	}

	/**
	 * Returns whether or not another BlockQueue contains the same information
	 * as this one.
	 * 
	 * @param other
	 *            the other BlockQueue to test against.
	 * @return whether or not another BlockQueue contains the same information
	 *         as this one.
	 */
	public boolean equals(BlockQueue other) {
		return (other.getQueue().equals(getQueue()) && other.getClaimed().equals(getClaimed()));
	}

	/* Getters */

	/**
	 * @param position
	 *            the position to compare distance to.
	 * @return the Block closest to the provided position.
	 */
	public BlockPathPair getClosestUnclaimedBlock(Mob mob, World world, Vector2f position) {
		Set<Block> unclaimed = getUnclaimed();
		ArrayList<Block> unclaimedList = new ArrayList<Block>(unclaimed);

		Pathfinder.sortByHeuristicDistance(unclaimedList, new Vector2f(position));
		Pathfinder pathfinder = world.getPathfinder();

		Map map = world.getMap();

		for (Block current : unclaimedList) {

			if (isSurrounded(current, map)) {
				continue;
			}

			Vector2f currentPosition = current.getPosition();
			Vector2f[] path = pathfinder.getPath(mob, new Vector2f(position), new Vector2f(currentPosition));

			if (Pathfinder.isViablePath(path, new Vector2f(currentPosition))) {
				return new BlockPathPair(current, path);
			}
		}

		return null;
	}

	/**
	 * @return a Set of all unclaimed Blocks in the BlockQueue.
	 */
	public Set<Block> getUnclaimed() {
		Set<Block> queue = getQueue();
		Set<Block> claimed = getClaimed();
		ArrayList<Block> queueList = new ArrayList<Block>(queue);
		ArrayList<Block> claimedList = new ArrayList<Block>(claimed);
		Set<Block> unclaimed = new HashSet<Block>();

		for (Block block : queueList) {
			if (!claimedList.contains(block)) {
				unclaimed.add(block);
			}
		}

		return unclaimed;
	}

	/**
	 * @return the Map containing this BlockQueue's Blocks.
	 */
	public Map getMap() {
		return map;
	}

	/**
	 * @return a Set of all queued Blocks in the BlockQueue.
	 */
	public Set<Block> getQueue() {
		return queue;
	}

	/**
	 * @return a Set of all claimed Blocks in the BlockQueue.
	 */
	public Set<Block> getClaimed() {
		return claimed;
	}

	/**
	 * Determines whether or not a Block is surrounded on all sides by other
	 * Blocks.
	 * 
	 * @param block
	 *            the block to be checked.
	 * @param map
	 *            the map within which the Block resides.
	 * @return whether or not the supplied Block is surrounded on all sides by
	 *         other blocks.
	 */
	private boolean isSurrounded(Block block, Map map) {
		int gridX = (int) (block.getX() / block.getWidth());
		int gridY = (int) (block.getY() / block.getHeight());

		// Check one unit in ever direction
		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				// Make sure we aren't checking the supplied Block itself.
				// Adding two 0s would just gives us the same (the block's)
				// coordinates.
				if (dx == 0 && dy == 0) {
					continue;
				}

				int neighborX = gridX + dx;
				int neighborY = gridY + dy;
				
				if(!map.isInBounds(neighborX, neighborY)) {
					continue;
				}

				Block neighbor = map.getBlock(neighborX, neighborY);

				if (neighbor == null) {
					return false;
				}
			}
		}

		return true;
	}

	/* Setters */

	/**
	 * Sets the Map containing the BlockQueue's Blocks.
	 * 
	 * @param map
	 *            the Map containing the BlockQueue's Blocks.
	 */
	private void setMap(Map map) {
		this.map = map;
	}

	/**
	 * Sets the Set containing all of the BlockQueue's queued Blocks.
	 * 
	 * @param queue
	 */
	private void setQueue(Set<Block> queue) {
		this.queue = queue;
	}

	/**
	 * Sets the Set containing all of the BlockQueue's claimed Blocks.
	 * 
	 * @param claimed
	 */
	private void setClaimed(Set<Block> claimed) {
		this.claimed = claimed;
	}

}
