package us.rescyou.meme.menu;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class ImageButton extends Button {
	
	/* Private Variables */
	
	private Image image;

	/* Initialization */
	
	public ImageButton(int x, int y, Image image, Runnable callback) {
		super(x, y, image.getWidth(), image.getHeight(), "", callback);
		this.image = image;
	}
	
	/* Updating */
	
	@Override
	public void render(int menuX, int menuY, Graphics g) {
		int buttonX = getX() + menuX;
		int buttonY = getY() + menuY;
		
		getImage().draw(buttonX, buttonY);
	}
	
	/* Getters */

	public Image getImage() {
		return image;
	}
	
	/* Setters */

	public void setImage(Image image) {
		this.image = image;
	}

}
