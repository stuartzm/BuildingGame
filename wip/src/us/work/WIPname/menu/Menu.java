package us.rescyou.meme.menu;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.geom.Rectangle;

import us.rescyou.meme.Assets;
import us.rescyou.meme.Config;
import us.rescyou.meme.Util;

public class Menu {

	/* Private Variables */

	private int x, y;
	private int width, height;
	private boolean closed;
	private boolean hasHeader;
	private String label;
	private Rectangle background;
	private ArrayList<Button> buttons;
	private ArrayList<CoordinateString> strings;
	private MouseListener mouseListener;
	private Input input;
	private Button closeButton;

	/* Initialization */

	public Menu(Input input, int x, int y, int width, int height, String label, boolean hasHeader) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.background = new Rectangle(x, y, width, height);
		this.buttons = new ArrayList<Button>();
		this.strings = new ArrayList<CoordinateString>();
		this.closed = false;
		this.input = input;
		this.closeButton = hasHeader ? createCloseButton(input) : null;
		this.label = label;
		this.hasHeader = hasHeader;

		this.mouseListener = new MouseListener() {
			@Override
			public void inputEnded() {

			}

			@Override
			public void inputStarted() {

			}

			@Override
			public boolean isAcceptingInput() {
				return !isClosed();
			}

			@Override
			public void setInput(Input arg0) {
			}

			@Override
			public void mouseClicked(int buttonCode, int x, int y, int clickCount) {
				Rectangle mouseClickBox = new Rectangle(x, y, Config.CLICK_BOX_WIDTH, Config.CLICK_BOX_HEIGHT);
				ArrayList<Button> buttons = getButtons();

				int menuX = getX();
				int menuY = getY();

				for (Button button : buttons) {
					Rectangle buttonBoundingBox = button.getBoundingBox(menuX, menuY);

					if (mouseClickBox.intersects(buttonBoundingBox) || mouseClickBox.contains(buttonBoundingBox)) {
						button.doClick();
					}
				}

				// Check the close button specifically
				if (hasHeader()) {
					Button closeButton = getCloseButton();
					Rectangle closeButtonBoundingBox = closeButton.getBoundingBox(menuX, menuY);

					if (mouseClickBox.intersects(closeButtonBoundingBox) || mouseClickBox.contains(closeButtonBoundingBox)) {
						closeButton.doClick();
					}
				}
			}

			@Override
			public void mouseDragged(int arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void mouseMoved(int arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void mousePressed(int arg0, int arg1, int arg2) {
			}

			@Override
			public void mouseReleased(int arg0, int arg1, int arg2) {
			}

			@Override
			public void mouseWheelMoved(int arg0) {
			}

		};

		input.addMouseListener(mouseListener);

		// Make sure that the menu wasn't placed off screen
		moveOnScreen();
	}

	/* Miscellaneous */

	public void close() {
		Input input = getInput();
		MouseListener mouseListener = getMouseListener();
		input.removeMouseListener(mouseListener);
		setClosed(true);

		Assets.menuClose.play(1.0f, Config.SCREEN_VOLUME);
	}

	private Button createCloseButton(Input input) {
		int x = getWidth() - Config.MENU_CLOSE_BUTTON_WIDTH;
		int y = 0;

		Button close = new ImageButton(x, y, Assets.classes.getSubImage(5, 5), new Runnable() {
			@Override
			public void run() {
				close();
			}
		});

		return close;
	}

	private void moveOnScreen() {
		int width = getWidth();
		int height = getHeight();

		int maxX = Config.SCREEN_WIDTH_PIXELS - width;
		int maxY = Config.SCREEN_HEIGHT_PIXELS - height;

		int x = getX();
		int y = getY();

		int newX = (int) Util.clamp(x, 0, maxX);
		int newY = (int) Util.clamp(y, 0, maxY);

		setX(newX);
		setY(newY);
	}

	/* Updating */

	public void doClick(int button, int mouseX, int mouseY) {

	}

	public void render(Graphics g) {
		boolean closed = isClosed();

		if (closed) {
			return;
		}

		g.scale(1.0f / Config.SCREEN_SCALE, 1.0f / Config.SCREEN_SCALE);
		renderBackground(g);

		if (hasHeader()) {
			renderHeader(g);
		}

		renderButtons(g);
		renderStrings(g);

		g.scale(Config.SCREEN_SCALE, Config.SCREEN_SCALE);
	}

	public void renderBackground(Graphics g) {
		float startX = getX();
		float startY = hasHeader ? getY() + Config.MENU_HEADER_HEIGHT : getY();

		Rectangle background = new Rectangle(startX, startY, width, height);

		g.setColor(Config.MENU_BACKGROUND_COLOR);
		g.fill(background);

		Rectangle trim = new Rectangle(getX() + 2, startY + 1, getWidth() - 3, getHeight() - 3);

		g.setColor(Config.MENU_FOREGROUND_COLOR);
		g.setLineWidth(1.0f);
		g.draw(trim);
	}

	public void renderHeader(Graphics g) {
		int x = getX();
		int y = getY();
		int width = getWidth();
		int height = Config.MENU_HEADER_HEIGHT;

		// Fill the header background
		Rectangle background = new Rectangle(x, y, width, height);

		g.setColor(Config.MENU_BACKGROUND_ALTERNATE_COLOR);
		g.fill(background);

		// Draw the close button
		Button close = getCloseButton();
		close.render(getX(), getY(), g);

		// Draw the trim

		/*
		 * Sets the trim to be 1 pixel within the background on all sides. We have to add another 1 because Slick2D is literally retarded and renders
		 * the box 1 unit left of where you tell it to.
		 */
		int trimXOffset = 1 + 1;
		int trimYOffset = 1; // Renders correctly on the y axis though, go
								// figure

		int trimX = x + trimXOffset;
		int trimY = y + trimYOffset;

		/*
		 * Again slick is retarded so we have to bring the width/height in 3 units instead of because Slick2D renders the box 1 unit to wide/tall.
		 */
		int trimWidth = width - 2 - 1;
		int trimHeight = height - 2 - 1;

		Rectangle trim = new Rectangle(trimX, trimY, trimWidth, trimHeight);

		g.setColor(Config.MENU_FOREGROUND_COLOR);
		g.setLineWidth(1.0f);
		g.draw(trim);

		renderLabel(g);
	}

	private void renderLabel(Graphics g) {
		String label = getLabel();

		int x = getX();
		int y = getY();

		int width = getWidth();
		int height = Config.MENU_HEADER_HEIGHT;

		int textX = x + ((width - Config.MENU_CLOSE_BUTTON_WIDTH) / 2) - (Assets.kenPixel.getWidth(label) / 2);
		int textY = y + (height / 2) - (Assets.kenPixel.getLineHeight() / 2);

		g.setColor(Config.MENU_FOREGROUND_COLOR);
		g.drawString(label, textX, textY);
	}

	public void renderButtons(Graphics g) {
		ArrayList<Button> buttons = getButtons();
		int x = getX();
		int y = getY();

		for (Button button : buttons) {
			button.render(x, y, g);
		}
	}

	public void renderStrings(Graphics g) {

	}

	/* Getters */

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Rectangle getBackground() {
		return background;
	}

	public ArrayList<Button> getButtons() {
		return buttons;
	}

	public ArrayList<CoordinateString> getStrings() {
		return strings;
	}

	public MouseListener getMouseListener() {
		return mouseListener;
	}

	public boolean isClosed() {
		return closed;
	}

	public Input getInput() {
		return input;
	}

	public Button getCloseButton() {
		return closeButton;
	}

	public String getLabel() {
		return label;
	}

	public boolean hasHeader() {
		return hasHeader;
	}

	/* Setters */

	public void setX(int x) {
		this.x = x;

		Rectangle background = getBackground();
		background.setX(x);
	}

	public void setY(int y) {
		this.y = y;

		Rectangle background = getBackground();
		background.setY(y);
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setBackground(Rectangle background) {
		this.background = background;
	}

	public void setButtons(ArrayList<Button> buttons) {
		this.buttons = buttons;
	}

	public void setStrings(ArrayList<CoordinateString> strings) {
		this.strings = strings;
	}

	public void setMouseListener(MouseListener mouseListener) {
		this.mouseListener = mouseListener;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public void setInput(Input input) {
		this.input = input;
	}

	public void setCloseButton(Button closeButton) {
		this.closeButton = closeButton;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
	}

}