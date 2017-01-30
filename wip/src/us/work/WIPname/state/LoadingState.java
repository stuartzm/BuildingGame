package us.rescyou.meme.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import us.rescyou.meme.Assets;
import us.rescyou.meme.Config;
import us.rescyou.meme.Game;

public class LoadingState extends State {

	/* Initialization */

	public LoadingState() throws SlickException {
		super();

		// SpriteSheet loading
		Assets.terrain = loadSpriteSheet(Assets.TERRAIN_PATH, Assets.TERRAIN_TILE_SIZE, Assets.TERRAIN_TILE_SIZE);
		Assets.creatures = loadSpriteSheet(Assets.CREATURES_PATH, Assets.CREATURES_TILE_SIZE, Assets.TERRAIN_TILE_SIZE);
		Assets.classes = loadSpriteSheet(Assets.CLASSES_PATH, Assets.CLASSES_TILE_WIDTH, Assets.CLASSES_TILE_HEIGHT);

		// Music loading
		// Assets.titleTheme = new Music(Assets.TITLE_THEME_PATH);
		// Assets.plowSharesIntoSwords = new Music(Assets.PLOW_SHARES_PATH);

		// Sound loading
		Assets.menuOpen = new Sound(Assets.MENU_OPEN_PATH);
		Assets.menuClose = new Sound(Assets.MENU_CLOSE_PATH);

		// Font loading
		Assets.kenPixel = new UnicodeFont(Assets.KEN_PIXEL_PATH, 16, false, false);
		Assets.kenPixel.addAsciiGlyphs();
		Assets.kenPixel.getEffects().add(new ColorEffect());
		Assets.kenPixel.loadGlyphs();
	}

	/* Asset Loading */

	/**
	 * Loads a new SpriteSheet from the disk, and sets it up for use. Removes
	 * anti-aliasing.
	 * 
	 * @param ref
	 *            the path to the image file used by the SpriteSheet.
	 * @param tileSize
	 *            the width and height (in pixels) of each individual sprite on
	 *            the sheet
	 * @return the loaded SpriteSheet.
	 * @throws SlickException
	 */
	private SpriteSheet loadSpriteSheet(String ref, int tileWidth, int tileHeight) throws SlickException {
		SpriteSheet out = new SpriteSheet(ref, tileWidth, tileHeight);
		out.setFilter(Image.FILTER_NEAREST);
		return out;
	}

	/* Updating */

	@Override
	public void tick(GameContainer container, Game game, float delta) throws SlickException {
		// Immediately move on to the next state
		game.getStateManager().enterState(Config.SECOND_STATE);
	}

	@Override
	public void render(GameContainer container, Game game, Graphics g) throws SlickException {
		// Intentionally left blank
		g.drawString("Loading...", 10, 10);
	}

}
