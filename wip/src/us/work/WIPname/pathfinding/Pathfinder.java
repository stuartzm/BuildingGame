package us.rescyou.meme.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.Config;
import us.rescyou.meme.Util;
import us.rescyou.meme.block.Block;
import us.rescyou.meme.entity.Mob;
import us.rescyou.meme.unit.Unit;
import us.rescyou.meme.world.Map;
import us.rescyou.meme.world.World;

public class Pathfinder {

	/* Public Variables */

	public static final int CLOSEST_POSITION_ITERATIONS = 5;

	/* Private Variables */

	private World world;

	/* Debug Variables */

	public static float pathfindingTime = 0;

	/* Initialization */

	public Pathfinder(World world) {
		this.world = world;
	}

	/* Path Finding */

	public Vector2f[] getPath(Mob mob, Vector2f start, Vector2f destination) {
		d = 0;
		
		// Updating the awful debug variables
		pathfindingTime = 0;
		long pathfindingStartTime = System.nanoTime();

		// Gathering context
		World world = getWorld();
		Map map = world.getMap();

		int nodeGraphWidth = toNodeGraphUnits(Config.MAP_WIDTH);
		int nodeGraphHeight = toNodeGraphUnits(Config.MAP_HEIGHT);

		// The closed set contains all of the Nodes that have already been checked
		Node[][] closedSet = new Node[nodeGraphWidth][nodeGraphHeight];

		// The open set contains all the Nodes that have been discovered, but not yet checked
		Node[][] openSet = new Node[nodeGraphWidth][nodeGraphHeight];

		// If the destination is blocked by some obstacle, move the destination somewhere close by (if possible)
		if (mob.wouldCollide(map, destination.getX(), destination.getY())) {
			Vector2f closestOpenPosition = getClosestOpenPosition(mob, map, new Vector2f(destination));

			// There were no open spots within interaction distance
			if (closestOpenPosition != null) {
				destination = closestOpenPosition;
			}
		}

		Node startNode = new Node(new Vector2f(start), getHeuristicDistance(start, destination), 0.0f, null);
		Node destinationNode = new Node(new Vector2f(destination), 0.0f, Float.MAX_VALUE, null);

		// Find the start and destination Node's corresponding indexes within the Node graphs
		int startXIndex = toNodeGraphUnits(start.getX());
		int startYIndex = toNodeGraphUnits(start.getY());
		int destinationXIndex = toNodeGraphUnits(destination.getX());
		int destinationYIndex = toNodeGraphUnits(destination.getY());

		// Add the start Node to the open set, setting it up to be searched first
		openSet[startXIndex][startYIndex] = startNode;

		// Creating a bunch of variables in advance to that they aren't re-allocated every iteration
		Node currentNode;
		Node neighborNode;

		Vector2f currentPosition;
		Vector2f neighborPosition;

		int currentXIndex;
		int currentYIndex;
		int neighborXIndex;
		int neighborYIndex;

		float currentGScore;
		float neighborGScore;
		float distance;
		float tentativeGScore;

		// Start the actual path finding, continue while there are still Nodes to be checked
		while (!isEmpty(openSet)) {
			// Get the "best looking" (lowest f-score) Node in our open set, and mark it's position
			currentNode = getLowestFScore(openSet);
			currentPosition = currentNode.getPosition();

			// Determine the current Node's indexes within either set (based on location)
			currentXIndex = toNodeGraphUnits(currentPosition.getX());
			currentYIndex = toNodeGraphUnits(currentPosition.getY());

			// If this newly found Node happens to be the goal, stop here, construct the path, and return it
			if (currentXIndex == destinationXIndex && currentYIndex == destinationYIndex) {
				// Update more awful debug variables
				pathfindingTime = toMilliseconds(System.nanoTime() - pathfindingStartTime);

				System.out.println(">>> ye path >>> " + d);
				return constructPath(currentNode);
			}

			// Remove the current block from the openSet, and add it to the closedSet
			openSet[currentXIndex][currentYIndex] = null;
			closedSet[currentXIndex][currentYIndex] = currentNode;

			// Discover the the neighbors of the current Node, so that they can be checked
			Node[] neighbors = getNeighbors(openSet, mob, currentNode, destinationNode);

			// Check each of the current Node's neighbors
			for (int i = 0; i < neighbors.length; i++) {
				neighborNode = neighbors[i];
				neighborPosition = neighborNode.getPosition();

				neighborXIndex = toNodeGraphUnits(neighborPosition.getX());
				neighborYIndex = toNodeGraphUnits(neighborPosition.getY());

				/*
				 * TODO: This used to be == neighbor, but for some reason that didn't work. That makes me think that the getNeighbors function is
				 * creating new Nodes under circumstances where it shouldn't.
				 */
				if (closedSet[neighborXIndex][neighborYIndex] != null) {
					// Has already been evaluated; skip
					continue;
				}

				openSet[neighborXIndex][neighborYIndex] = neighborNode;

				// Figure out how good this path is
				currentGScore = currentNode.getGScore();
				neighborGScore = neighborNode.getGScore();

				distance = getHeuristicDistance(currentPosition, neighborPosition);
				tentativeGScore = currentGScore + distance;

				// Check if this new path is better than the old one
				if (tentativeGScore < neighborGScore) {
					// Update the neighbor Node to the new g-score, and link
					// to the path
					neighborNode.setParent(currentNode);
					neighborNode.setGScore(tentativeGScore);
				}
			}
		}

		// There was no path
		pathfindingTime = toMilliseconds(System.nanoTime() - pathfindingStartTime);
		System.out.println(">>> no path >>> " + d);
		return constructPath(getClosest(closedSet, destination));
	}

	public static Vector2f getClosestOpenPosition(Mob mob, Map map, Vector2f position) {
		float searchRadius = 0;
		float searchRadiusDelta = Unit.INTERACTION_DISTANCE_MINIMUM / CLOSEST_POSITION_ITERATIONS;

		for (int i = 0; i < CLOSEST_POSITION_ITERATIONS; i++) {
			Vector2f found = null;
			float closestDistance = 0;

			float x = position.getX();
			float y = position.getY();

			Vector2f mobPosition = mob.getPosition();

			for (int dx = -1; dx <= 1; dx++) {
				for (int dy = -1; dy <= 1; dy++) {
					if (dx == 0 && dy == 0) {
						continue;
					}

					float searchX = x + (dx * searchRadius);
					float searchY = y + (dy * searchRadius);
					Vector2f searchPosition = new Vector2f(searchX, searchY);

					if (!map.isInBounds((int) searchX, (int) searchY)) {
						continue;
					}

					if (mob.wouldCollide(map, searchX, searchY)) {
						continue;
					}

					float distance = getHeuristicDistance(mobPosition, new Vector2f(searchPosition));

					if (found == null || distance < closestDistance) {
						found = searchPosition;
						closestDistance = distance;
					}
				}
			}

			// If we found anything on this level, return it
			if (found != null) {
				return found;
			} else {
				searchRadius += searchRadiusDelta;
			}
		}

		// fucked
		return null;
	}

	private boolean isEmpty(Node[][] nodeSet) {
		for (int x = 0; x < nodeSet.length; x++) {
			for (int y = 0; y < nodeSet[0].length; y++) {
				if (nodeSet[x][y] != null) {
					return false;
				}
			}
		}

		return true;
	}

	private Node getLowestFScore(Node[][] nodeSet) {
		Node lowestNode = null;
		float lowestFScore = 0;

		for (int x = 0; x < nodeSet.length; x++) {
			for (int y = 0; y < nodeSet[0].length; y++) {
				Node current = nodeSet[x][y];

				// See if there is actually a node here
				if (current == null) {
					continue;
				}

				float currentFScore = current.getFScore();

				if (lowestNode == null || currentFScore < lowestFScore) {
					lowestFScore = currentFScore;
					lowestNode = current;
				}
			}
		}

		return lowestNode;
	}

	private Vector2f[] constructPath(Node destination) {
		ArrayList<Vector2f> pathList = new ArrayList<Vector2f>();
		Node current = destination;

		while (current != null) {
			Node currentParent = current.getParent();
			Vector2f currentPosition = current.getPosition();

			current = currentParent;
			pathList.add(currentPosition);
		}

		Collections.reverse(pathList);
		Vector2f[] pathArray = pathList.toArray(new Vector2f[pathList.size()]);
		return pathArray;
	}

	public static void sortByHeuristicDistance(ArrayList<Block> list, Vector2f destination) {
		Collections.sort(list, new Comparator<Block>() {
			public int compare(Block b1, Block b2) {
				Vector2f b1Position = b1.getPosition();
				Vector2f b2Position = b2.getPosition();

				int b1Heuristic = (int) getHeuristicDistance(b1Position, destination);
				int b2Heuristic = (int) getHeuristicDistance(b2Position, destination);

				return b1Heuristic - b2Heuristic;
			}
		});
	}

	public static float getHeuristicDistance(Vector2f start, Vector2f destination) {
		Vector2f difference = new Vector2f(destination).sub(start);
		float heuristic = Math.abs(difference.getX()) + Math.abs(difference.getY());

		return heuristic;
	}

	public static boolean isViablePath(Vector2f[] path, Vector2f destination) {
		return path != null && (Util.getDistanceBetween(new Vector2f(destination), path[path.length - 1]) < Unit.INTERACTION_DISTANCE_MINIMUM);
	}

	private int d = 0;
	private Node[] getNeighbors(Node[][] nodeSet, Mob mob, Node node, Node destination) {
		World world = getWorld();
		Map map = world.getMap();

		ArrayList<Node> neighborList = new ArrayList<Node>();
		Vector2f nodePosition = node.getPosition();

		int nodeX = (int) (nodePosition.getX() / Config.PATHFINDING_ACCURACY);
		int nodeY = (int) (nodePosition.getY() / Config.PATHFINDING_ACCURACY);
		
		// Initializing variables so that they aren't reallocated every iteration
		Node neighbor;
		
		int currentX;
		int currentY;
		
		Vector2f neighborPosition;
		Vector2f destinationPosition;

		// Check in a 3 x 3 square around the Node, offset one to the left.
		// This will give us delta x and y values [-1 -> 1] which we can
		// then add to the Node's position to get neighbor positions.
		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				// Skip 0, 0, as that would give us no movement, and thus
				// we would just get the node we were provided with.
				if (dx == 0 && dy == 0) {
					continue;
				}

				currentX = nodeX + dx;
				currentY = nodeY + dy;

				if (isInBounds(nodeSet, currentX, currentY)) {
					neighbor = nodeSet[currentX][currentY];
					neighborPosition = new Vector2f(currentX * Config.PATHFINDING_ACCURACY, currentY * Config.PATHFINDING_ACCURACY);

					/*
					 * We add an offset so that the nodes don't line up exactly on the grid. This makes it easier for mobs to go around stuff, because
					 * they'll now path just to the right / down of EXACTLY bordering Blocks.
					 */
					neighborPosition.add(Config.PATHFINDING_OFFSET);

					// Create a new Node if this one is null
					if (neighbor == null) {
						destinationPosition = destination.getPosition();
						neighbor = new Node(neighborPosition, getHeuristicDistance(neighborPosition, destinationPosition), Float.MAX_VALUE, null);
						d += 1;
					}

					// Make sure the Mob wouldn't collide at this Node
					if (!mob.wouldCollide(map, neighborPosition.getX(), neighborPosition.getY())) {
						neighborList.add(neighbor);
					}
				}
			}
		}

		Node[] neighborArray = neighborList.toArray(new Node[neighborList.size()]);
		return neighborArray;
	}

	private boolean isInBounds(Node[][] nodeSet, int x, int y) {
		boolean inBoundsX = x >= 0 && x < nodeSet.length;
		boolean inBoundsY = y >= 0 && y < nodeSet[0].length;

		return inBoundsX && inBoundsY;
	}

	private Node getClosest(Node[][] nodeSet, Vector2f position) {
		Node closestNode = null;
		float smallestDistance = 0.0f;

		for (int x = 0; x < nodeSet.length; x++) {
			for (int y = 0; y < nodeSet[0].length; y++) {
				Node currentNode = nodeSet[x][y];

				// See if there is actually a node here
				if (currentNode == null) {
					continue;
				}

				Vector2f currentPosition = currentNode.getPosition();

				float currentDistance = Util.getDistanceBetween(currentPosition, position);

				if (closestNode == null || currentDistance < smallestDistance) {
					smallestDistance = currentDistance;
					closestNode = currentNode;
				}
			}
		}

		return closestNode;
	}

	/* Miscellaneous */

	private float toMilliseconds(long nanoSeconds) {
		return nanoSeconds / 1000000.0f;
	}
	
	private int toNodeGraphUnits(float worldUnit) {
		/*
		 * The PATHFINDING_ACCURACY is the amount of units distance between each Node. For instance, the P_A right now is 0.25, meaning the space
		 * between each Node on the grid would be 0.25 in game units. 1 unit is 1 tile. This math should convert a in-game position to that Node's
		 * corresponding index within a node set. IE: 1, 1 -> 4, 4
		 */
		return (int) (worldUnit / Config.PATHFINDING_ACCURACY);
	}

	/* Getters */

	public World getWorld() {
		return world;
	}

	/* Setters */

	public void setWorld(World world) {
		this.world = world;
	}

}
