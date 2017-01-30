package us.rescyou.meme;

import org.newdawn.slick.Color;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class Config {

	/* Window Properties */

	public static final String   SCREEN_TITLE                    = "GETSUCC";
	public static final boolean  SCREEN_FULLSCREEN               = false;
	public static final int      SCREEN_WIDTH_PIXELS             = 1280;
	public static final int      SCREEN_HEIGHT_PIXELS            = 720;
	public static final float    SCREEN_WIDTH_GRID               = Util.toGrid(SCREEN_WIDTH_PIXELS) / Config.SCREEN_SCALE;
	public static final float    SCREEN_HEIGHT_GRID              = Util.toGrid(SCREEN_HEIGHT_PIXELS) / Config.SCREEN_SCALE;
	public static final float    SCREEN_SCALE                    = 3.0f;
	public static final float    SCREEN_VOLUME                   = 0.25f;
	
	public static final int      COLLISION_SEARCH_RADIUS         = 1;
	public static final float    PATHFINDING_ACCURACY            = 0.25f;
	public static final float    PATHFINDING_HEURISTIC_SCALE     = 1.0f;
	public static final Vector2f PATHFINDING_OFFSET              = new Vector2f(PATHFINDING_ACCURACY / 2, PATHFINDING_ACCURACY / 2);
	
	public static final int      MAP_WIDTH                       = 50;
	public static final int      MAP_HEIGHT                      = 50;
	public static final float    MAP_GRID_SIZE                   = Assets.TERRAIN_TILE_SIZE;
	
	public static final int      START_MINER_COUNT               = 5;
	public static final int      START_BUILDER_COUNT             = 2;
	public static final int      START_AREA_WIDTH                = 10;
	public static final int      START_AREA_HEIGHT               = 5;
	public static final int      START_AREA_BUFFER               = 1;
	
	public static final boolean  RENDER_BREAK_BOXES              = true;
	public static final boolean  RENDER_SELECTED_BOX             = true;
	public static final boolean  RENDER_PATHFINDING_NODES        = false;
	public static final boolean  RENDER_SHADOWS                  = true;
	public static final float    SHADOW_HEIGHT_BUFFER            = 3.0f;
	public static final float    SHADOW_OPACITY                  = 0.8f;
	public static final float    BLOCK_OVERLAY_OPACITY           = 0.2f;
	
	public static final float    CAMERA_SPEED                    = 10.0f;
	public static final float    BREAK_BLOCK_BONES_CHANCE        = 0.05f;
	public static final float    CLICK_BOX_WIDTH                 = 1.0f;
	public static final float    CLICK_BOX_HEIGHT                = 1.0f;
	
	public static final int      MENU_CLOSE_BUTTON_WIDTH         = Assets.CLASSES_TILE_WIDTH;
	public static final int      MENU_CLOSE_BUTTON_HEIGHT        = Assets.CLASSES_TILE_HEIGHT;
	public static final int      MENU_HEADER_HEIGHT              = Assets.CLASSES_TILE_HEIGHT;
	public static final int      MENU_PADDING                    = 30;
	public static final String   MENU_BUILD_LABEL                = "Build";
	public static final int      MENU_BUILD_BUTTON_HEIGHT        = 50;
	public static final Color    MENU_BACKGROUND_COLOR           = new Color(0.11f, 0.10f, 0.09f);
	public static final Color    MENU_BACKGROUND_ALTERNATE_COLOR = new Color(0.0f, 0.0f, 0.0f);
	public static final Color    MENU_FOREGROUND_COLOR           = new Color(0.77f, 0.77f, 0.77f);
	
	public static final int      GUI_BOTTOM_BAR_HEIGHT           = 50;
	public static final int      GUI_BUTTON_WIDTH                = 200;
	public static final int      GUI_BUTTON_HEIGHT               = 25;
	public static final int      GUI_MENU_WIDTH                  = 300;
	public static final int      GUI_MENU_HEIGHT                 = 500;
	
	/* Input Keys */
	
	public static final char     INPUT_NORMAL_MODE               = ' ';
	public static final char     INPUT_BUILD_MODE                = 'b';
	public static final char     INPUT_BREAK_MODE                = 'm';
	public static final char     INPUT_CAMERA_MOVE_UP            = 'w';
	public static final char     INPUT_CAMERA_MOVE_DOWN          = 's';
	public static final char     INPUT_CAMERA_MOVE_LEFT          = 'a';
	public static final char     INPUT_CAMERA_MOVE_RIGHT         = 'd';
	public static final int      INPUT_MOUSE_ACTION_1            = Input.MOUSE_LEFT_BUTTON;
	public static final int      INPUT_MOUSE_ACTION_2            = Input.MOUSE_RIGHT_BUTTON;
	
	/* State Properties */
	
	public enum StateID {
		LOADING, RUNNING
	}
	
	public static StateID FIRST_STATE  = StateID.LOADING;
	public static StateID SECOND_STATE = StateID.RUNNING;

}
