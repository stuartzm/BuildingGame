package us.rescyou.meme;

import java.util.ArrayList;

import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Vector2f;

public class Util {

	public static Vector2f[] copyVector2fs(Vector2f[] vector2fs) {
		Vector2f[] out = new Vector2f[vector2fs.length];

		for (int i = 0; i < out.length; i++) {
			out[i] = new Vector2f(vector2fs[i]);
		}

		return out;
	}

	public static Object getRandomElement(Object[] array) {
		if (array == null || array.length == 0) {
			return null;
		} else {
			return array[(int) ((Math.random() * array.length))];
		}
	}

	public static int toPixels(float grid) {
		return (int) (grid * Config.MAP_GRID_SIZE);
	}

	public static float toGrid(int pixels) {
		return (float) pixels / Config.MAP_GRID_SIZE;
	}

	public static int getTileWidth(SpriteSheet spriteSheet) {
		return spriteSheet.getWidth() / spriteSheet.getHorizontalCount();
	}

	public static void removeByValues(ArrayList<Vector2f> vectors, Vector2f other) {
		for (Vector2f vector : vectors) {
			if (vector.x == other.x && vector.y == other.y) {
				vectors.remove(vector);
				return;
			}
		}
	}

	public static float toWorldUnits(float mouseUnit, float offset) {
		return toGrid((int) ((mouseUnit / Config.SCREEN_SCALE) - toPixels(offset)));
	}

	public static float getDistanceBetween(Vector2f start, Vector2f destination) {
		return new Vector2f(destination).sub(new Vector2f(start)).length();
	}

	public static int getRandomBetween(int min, int max) {
		return (int) (min + (Math.random() * (max - min)));
	}
	
	public static float clamp(float value, float minimum, float maximum) {
		if (value < minimum) {
			return minimum;
		} else if (value > maximum) {
			return maximum;
		} else {
			return value;
		}
	}
	
}
