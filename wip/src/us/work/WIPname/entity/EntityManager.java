package us.rescyou.meme.entity;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import us.rescyou.meme.Assets;
import us.rescyou.meme.Config;
import us.rescyou.meme.Util;
import us.rescyou.meme.world.World;

public class EntityManager {

	/* Private Variables */

	private ArrayList<Entity> entities;

	/* Initialization */

	/**
	 * Creates a new EntityManager.
	 * 
	 * @throws SlickException
	 */
	public EntityManager() throws SlickException {
		entities = new ArrayList<Entity>();
	}

	/* Updating */

	/**
	 * Draws everything that should be drawn for each Entity.
	 * 
	 * @param xOffset
	 *            the x value of the Camera's rendering offset.
	 * @param yOffset
	 *            the y value of the Camera's rendering offset.
	 * @param g
	 *            the Graphics object used in rendering.
	 * @throws SlickException
	 */
	public void render(float xOffset, float yOffset, Graphics g) throws SlickException {
		for (Entity entity : getEntities()) {
			if (entity != null) {
				entity.render(xOffset, yOffset, g);
			}
		}
	}

	/**
	 * Steps each Entity's simulation forward one tick.
	 * 
	 * @param delta
	 *            the time since the last tick in seconds.
	 * @throws SlickException
	 */
	public void tick(World world, float delta) throws SlickException {
		for (Entity entity : getEntities()) {
			if (entity != null) {
				entity.tick(world, delta);
			}
		}
	}

	/* Miscellaneous */

	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	public void removeEntity(Entity entity) {
		ArrayList<Entity> entities = getEntities();

		// Iterating backwards to avoid Concurrent Modificication Exception.
		for (int i = entities.size() - 1; i >= 0; i--) {
			Entity current = entities.get(i);
			if (current == entity) {
				entities.remove(i);
			}
		}
	}

	/* Getters */

	/**
	 * @return all of the Entities held within this Manager.
	 */
	public ArrayList<Entity> getEntities() {
		return entities;
	}

}
