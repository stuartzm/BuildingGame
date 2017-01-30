package us.rescyou.meme.menu;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import us.rescyou.meme.Assets;
import us.rescyou.meme.Config;

public class Button {

	/* Private Variables */

	private int x, y;
	private int width, height;
	private Runnable callback;
	private String text;

	/* Initialization */

	public Button(int x, int y, int width, int height, String text, Runnable callback) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.callback = callback;
		this.text = text;
	}

	/* Updating */

	public void doClick() {
		callback.run();
	}

	public void render(int menuX, int menuY, Graphics g) {
		int x = getX() + menuX;
		int y = getY() + menuY;

		int width = getWidth();
		int height = getHeight();

		// Draw the background
		Rectangle background = new Rectangle(x, y, width, height);

		g.setColor(Config.MENU_BACKGROUND_ALTERNATE_COLOR);
		g.fill(background);

		// Draw the trim

		/*
		 * Sets the trim to be 1 pixel within the background on all sides. We have to add another 1 because Slick2D is literally retarded and renders
		 * the box 1 unit left of where you tell it to.
		 */
		int trimXOffset = 1 + 1;
		int trimYOffset = 1; // Renders correctly on the y axis though, go figure

		int trimX = x + trimXOffset;
		int trimY = y + trimYOffset;

		/* Again slick is retarded so we have to bring the width/height in 3 units instead of because Slick2D renders the box 1 unit to wide/tall. */
		int trimWidth = width - 2 - 1;
		int trimHeight = height - 2 - 1;

		Rectangle trim = new Rectangle(trimX, trimY, trimWidth, trimHeight);

		g.setColor(Config.MENU_FOREGROUND_COLOR);
		g.setLineWidth(1.0f);
		g.draw(trim);

		// Draw the text
		String text = getText();

		g.setColor(Config.MENU_FOREGROUND_COLOR);

		int textX = x + (width / 2) - (Assets.kenPixel.getWidth(text) / 2);
		int textY = y + (height / 2) - (Assets.kenPixel.getLineHeight() / 2);

		// We have to add another pixel to the text's y positions because of fucking slick fucking render 1 pixel too high
		textY += 1;
		
		g.drawString(text, textX, textY);
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

	public Runnable getCallback() {
		return callback;
	}

	public String getText() {
		return text;
	}

	public Rectangle getBoundingBox(int menuX, int menuY) {
		return new Rectangle(menuX + getX(), menuY + getY(), getWidth(), getHeight());
	}

	/* Setters */

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setCallback(Runnable callback) {
		this.callback = callback;
	}

	public void setText(String text) {
		this.text = text;
	}

}
