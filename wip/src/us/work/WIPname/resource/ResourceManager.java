package us.rescyou.meme.resource;

import java.util.HashMap;

public class ResourceManager {

	/* Private Variables */

	private HashMap<Resource, Integer> resources;

	/* Enumerated Variables */

	public enum Resource {
		STONE, DIRT
	}

	/* Initialization */

	public ResourceManager() {
		resources = new HashMap<Resource, Integer>();

		initializeResources();
	}

	private void initializeResources() {
		Resource[] resources = Resource.values();

		for (int i = 0; i < resources.length; i++) {
			setResource(resources[i], 0);
		}
	}

	/* Getters */

	public HashMap<Resource, Integer> getResources() {
		return resources;
	}
	
	public int getResource(Resource resource) {
		HashMap<Resource, Integer> resources = getResources();
		return resources.get(resource);
	}

	/* Setters */

	public void setResource(Resource resource, int amount) {
		HashMap<Resource, Integer> resources = getResources();
		resources.put(resource, amount);
	}
	
	public void addResource(Resource resource, int amount) {
		HashMap<Resource, Integer> resources = getResources();
		
		int currentAmount = getResource(resource);
		setResource(resource, currentAmount + amount);
	}
	
	private void setResources(HashMap<Resource, Integer> resources) {
		this.resources = resources;
	}

}
