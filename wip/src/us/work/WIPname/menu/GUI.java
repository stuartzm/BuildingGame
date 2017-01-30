package us.rescyou.meme.menu;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import us.rescyou.meme.Config;

public class GUI {

	/* Menus */

	private Menu bottomBar;
	private Menu buildMenu;
	private Menu craftMenu;
	private Menu resourcesMenu;

	/* Button Position */

	private int buildButtonX;
	private int craftButtonX;
	private int resourcesButtonX;

	/* Initialization */

	public GUI(Input input) {
		initializeMenus(input);
	}

	private void initializeMenus(Input input) {
		bottomBar = createBottomBar(input);

		buildMenu = createBuildMenu(input);
		buildMenu.setClosed(true);

		craftMenu = createCraftMenu(input);
		craftMenu.setClosed(true);

		resourcesMenu = createtResourcesMenu(input);
		resourcesMenu.setClosed(true);
	}

	private Menu createBuildMenu(Input input) {
		/*
		 * Creating the actual Menu
		 */

		Menu bottomBar = getBottomBar();
		int bottomBarY = bottomBar.getY();

		int buildButtonX = getBuildButtonX();
		int buildButtonWidth = Config.GUI_BUTTON_WIDTH;

		int buildMenuWidth = Config.GUI_MENU_WIDTH;
		int buildMenuHeight = Config.GUI_MENU_HEIGHT;

		int buildMenuX = buildButtonX - ((buildMenuWidth - buildButtonWidth) / 2);
		int buildMenuY = bottomBarY - buildMenuHeight;

		Menu buildMenu = new Menu(input, buildMenuX, buildMenuY, buildMenuWidth, buildMenuHeight, Config.MENU_BUILD_LABEL, false);

		/*
		 * Creating the Buttons
		 */

		ArrayList<Button> buttons = buildMenu.getButtons();

		int buttonWidth = buildMenuWidth - (2 * Config.MENU_PADDING);
		int buttonHeight = Config.MENU_BUILD_BUTTON_HEIGHT;

		Button stoneWall = new Button(Config.MENU_PADDING, Config.MENU_HEADER_HEIGHT + Config.MENU_PADDING, buttonWidth, buttonHeight, "Stone Wall", new Runnable() {
			@Override
			public void run() {
				System.out.println("Now building: STONE WALL");
			}
		});

		Button stoneFloor = new Button(Config.MENU_PADDING, Config.MENU_HEADER_HEIGHT + buttonHeight + (Config.MENU_PADDING * 2), buttonWidth, buttonHeight, "Stone Floor", new Runnable() {
			@Override
			public void run() {
				System.out.println("Now building: STONE FLOOR");
			}
		});

		buttons.add(stoneWall);
		buttons.add(stoneFloor);

		return buildMenu;
	}

	private Menu createCraftMenu(Input input) {
		/*
		 * Creating the actual Menu
		 */

		Menu bottomBar = getBottomBar();
		int bottomBarY = bottomBar.getY();

		int craftButtonX = getCraftButtonX();
		int craftButtonWidth = Config.GUI_BUTTON_WIDTH;

		int craftMenuWidth = Config.GUI_MENU_WIDTH;
		int craftMenuHeight = Config.GUI_MENU_HEIGHT;

		int craftMenuX = craftButtonX - ((craftMenuWidth - craftButtonWidth) / 2);
		int craftMenuY = bottomBarY - craftMenuHeight;

		Menu craftMenu = new Menu(input, craftMenuX, craftMenuY, craftMenuWidth, craftMenuHeight, "", false);

		/*
		 * Creating the Buttons
		 */

		ArrayList<Button> buttons = craftMenu.getButtons();

		int buttonWidth = craftMenuWidth - (2 * Config.MENU_PADDING);
		int buttonHeight = Config.MENU_BUILD_BUTTON_HEIGHT;

		// Add some buttons

		return craftMenu;
	}

	private Menu createtResourcesMenu(Input input) {
		/*
		 * Creating the actual Menu
		 */

		Menu bottomBar = getBottomBar();
		int bottomBarY = bottomBar.getY();

		int resourcesButtonX = getResourcesButtonX();
		int resourcesButtonWidth = Config.GUI_BUTTON_WIDTH;

		int resourcesMenuWidth = Config.GUI_MENU_WIDTH;
		int resourcesMenuHeight = Config.GUI_MENU_HEIGHT;

		int resourcesMenuX = resourcesButtonX - ((resourcesMenuWidth - resourcesButtonWidth) / 2);
		int resourcesMenuY = bottomBarY - resourcesMenuHeight;

		Menu resourcesMenu = new Menu(input, resourcesMenuX, resourcesMenuY, resourcesMenuWidth, resourcesMenuHeight, "", false);

		/*
		 * Creating the Buttons
		 */

		ArrayList<Button> buttons = resourcesMenu.getButtons();

		int buttonWidth = resourcesMenuWidth - (2 * Config.MENU_PADDING);
		int buttonHeight = Config.MENU_BUILD_BUTTON_HEIGHT;

		// Add some buttons

		return resourcesMenu;
	}

	private Menu createBottomBar(Input input) {
		/*
		 * Creating the actual Menu
		 */

		// Figure out where to put this Menu, and how big it is
		int bottomBarX = 0;
		int bottomBarY = Config.SCREEN_HEIGHT_PIXELS - Config.GUI_BOTTOM_BAR_HEIGHT;

		int bottomBarWidth = Config.SCREEN_WIDTH_PIXELS;
		int bottomBarHeight = Config.GUI_BOTTOM_BAR_HEIGHT;

		Menu bottomBar = new Menu(input, bottomBarX, bottomBarY, bottomBarWidth, bottomBarHeight, "", false);

		/*
		 * Creating the Buttons
		 */

		// Just throw buttons in without x coordinates, they'll get adjusted later
		ArrayList<Button> bottomBarButtons = new ArrayList<Button>();

		int buttonWidth = Config.GUI_BUTTON_WIDTH;
		int buttonHeight = Config.GUI_BUTTON_HEIGHT;

		int buttonY = (bottomBarHeight / 2) - (buttonHeight / 2);

		// Build button
		Button build = new Button(-1, buttonY, buttonWidth, buttonHeight, "BUILD", new Runnable() {
			@Override
			public void run() {
				Menu buildMenu = getBuildMenu();
				toggleMenu(buildMenu);
			}
		});

		// Craft button
		Button craft = new Button(-1, buttonY, buttonWidth, buttonHeight, "CRAFT", new Runnable() {
			@Override
			public void run() {
				Menu craftMenu = getCraftMenu();
				toggleMenu(craftMenu);
			}
		});

		// Resources button
		Button resources = new Button(-1, buttonY, buttonWidth, buttonHeight, "RESOURCES", new Runnable() {
			@Override
			public void run() {
				Menu resourcesMenu = getResourcesMenu();
				toggleMenu(resourcesMenu);
			}
		});

		// Register each button
		bottomBarButtons.add(build);
		bottomBarButtons.add(craft);
		bottomBarButtons.add(resources);

		// Put each button where it should be, depending on the number of buttons
		for (int i = 0; i < bottomBarButtons.size(); i++) {
			Button current = bottomBarButtons.get(i);

			int buttonX = (i + 1) * (bottomBarWidth / (bottomBarButtons.size() + 1)) - (buttonWidth / 2);
			current.setX(buttonX);
		}

		bottomBar.setButtons(bottomBarButtons);

		// Mark the button locations
		setBuildButtonX(build.getX());
		setCraftButtonX(craft.getX());
		setResourcesButtonX(resources.getX());

		return bottomBar;
	}

	/* Updating */

	public void render(Graphics g) {
		Menu bottomBar = getBottomBar();
		Menu buildMenu = getBuildMenu();
		Menu craftMenu = getCraftMenu();
		Menu resourcesMenu = getResourcesMenu();

		bottomBar.render(g);
		buildMenu.render(g);
		craftMenu.render(g);
		resourcesMenu.render(g);
	}

	/* Miscellaneous */

	private void toggleMenu(Menu menu) {
		boolean isClosed = menu.isClosed();
		menu.setClosed(!isClosed);
	}

	/* Getters */

	public Menu getBottomBar() {
		return bottomBar;
	}

	public Menu getBuildMenu() {
		return buildMenu;
	}

	public int getBuildButtonX() {
		return buildButtonX;
	}

	public int getCraftButtonX() {
		return craftButtonX;
	}

	public int getResourcesButtonX() {
		return resourcesButtonX;
	}

	public Menu getCraftMenu() {
		return craftMenu;
	}

	public Menu getResourcesMenu() {
		return resourcesMenu;
	}

	/* Setters */

	public void setBottomBar(Menu bottomBar) {
		this.bottomBar = bottomBar;
	}

	public void setBuildMenu(Menu buildMenu) {
		this.buildMenu = buildMenu;
	}

	public void setBuildButtonX(int buildButtonX) {
		this.buildButtonX = buildButtonX;
	}

	public void setCraftButtonX(int craftButtonX) {
		this.craftButtonX = craftButtonX;
	}

	public void setResourcesButtonX(int resourcesButtonX) {
		this.resourcesButtonX = resourcesButtonX;
	}

	public void setCraftMenu(Menu craftMenu) {
		this.craftMenu = craftMenu;
	}

	public void setResourcesMenu(Menu resourcesMenu) {
		this.resourcesMenu = resourcesMenu;
	}

}
