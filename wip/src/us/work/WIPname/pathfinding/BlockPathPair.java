package us.rescyou.meme.pathfinding;

import org.newdawn.slick.geom.Vector2f;

import us.rescyou.meme.block.Block;

public class BlockPathPair {

	/* Private Variables */

	private Block block;
	private Vector2f[] path;

	/* Initialization */

	public BlockPathPair(Block block, Vector2f[] path) {
		this.block = block;
		this.path = path;
	}

	/* Getters */

	public Block getBlock() {
		return block;
	}

	public Vector2f[] getPath() {
		return path;
	}

	/* Setters */

	public void setBlock(Block block) {
		this.block = block;
	}

	public void setPath(Vector2f[] path) {
		this.path = path;
	}

}
