package us.rescyou.meme.pathfinding;

import org.newdawn.slick.geom.Vector2f;

public class Node {

	/* Private Variables */

	private Vector2f position;
	private float hScore;
	private float gScore;
	private Node parent;

	public Node(Vector2f position, float hScore, float gScore, Node parent) {
		this.position = new Vector2f(position);
		this.hScore = hScore;
		this.gScore = gScore;
		this.parent = parent;
	}

	/* Miscellaneous */

	public String toString() {
		return "(" + getPosition().x + ", " + getPosition().y + ") - H: " + getHScore() + " G: " + getGScore() + " F: " + getFScore();
	}

	/* Getters */

	public Vector2f getPosition() {
		return new Vector2f(position);
	}

	public Node getParent() {
		return parent;
	}

	public float getHScore() {
		return hScore;
	}

	public float getGScore() {
		return gScore;
	}

	public float getFScore() {
		return getHScore() + getGScore();
	}

	/* Setters */

	public void setPosition(Vector2f position) {
		this.position = new Vector2f(position);
	}

	public void setHScore(float heuristic) {
		this.hScore = heuristic;
	}

	public void setGScore(float movementCost) {
		this.gScore = movementCost;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

}
