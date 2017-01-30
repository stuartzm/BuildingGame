package us.rescyou.meme;

import org.newdawn.slick.Music;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.UnicodeFont;

public class Assets {

	/* Paths */
	
	public static final String PATH_ASSETS = "assets\\";
	public static final String PATH_SPRITE_SHEETS = PATH_ASSETS + "spritesheets\\";
	public static final String PATH_SOUNDS = PATH_ASSETS + "sounds\\";
	public static final String PATH_FONTS = PATH_ASSETS + "fonts\\";
	
	/* Terrain */
	
	public static SpriteSheet terrain;
	public static final String TERRAIN_PATH = PATH_SPRITE_SHEETS + "terrain.png";
	public static final int TERRAIN_TILE_SIZE = 24;
	
	/* Creatures */
	
	public static SpriteSheet creatures;
	public static final String CREATURES_PATH = PATH_SPRITE_SHEETS + "creatures.png";
	public static final int CREATURES_TILE_SIZE = 24;
	
	/* Classes */
	
	public static SpriteSheet classes;
	public static final String CLASSES_PATH = PATH_SPRITE_SHEETS + "classes.png";
	public static final int CLASSES_TILE_WIDTH = 26;
	public static final int CLASSES_TILE_HEIGHT = 28;
	
	/* Title Theme */
	
	public static Music titleTheme;
	public static String TITLE_THEME_PATH = PATH_SOUNDS + "title_theme.ogg";
	
	/* Plowshares into Swords */
	
	public static Music plowSharesIntoSwords;
	public static String PLOW_SHARES_PATH = PATH_SOUNDS + "plowshares_into_swords.ogg";
	
	/* Menu Sounds */
	
	public static Sound menuOpen;
	public static String MENU_OPEN_PATH = PATH_SOUNDS + "bookOpen.ogg";
	
	public static Sound menuClose;
	public static String MENU_CLOSE_PATH = PATH_SOUNDS + "bookFlip3.ogg";
	
	/* Fonts */
	
	public static UnicodeFont kenPixel;
	public static final String KEN_PIXEL_PATH = Assets.PATH_FONTS + "kenpixel.ttf";
	
}
