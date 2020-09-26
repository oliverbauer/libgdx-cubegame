package io.libgdx.cubegame;

import io.libgdx.cubegame.levels.Difficulty;

public class Config {
	public static boolean loadSoundsAndMusic = true; // Github: No music/sounds added, so this is set to false... cf. Assets.java
	
	public static Difficulty difficulty = Difficulty.HARD;
	
	public static boolean showGrid = false;
	public static boolean followPlayer = false;
	
	public enum Background_Type {
		TEXTURE,
		CUBEMAP,
		NONE
	}
	public static Background_Type backgroundType = Background_Type.NONE;
}
