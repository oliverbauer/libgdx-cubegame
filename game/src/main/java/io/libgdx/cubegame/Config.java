package io.libgdx.cubegame;

import io.libgdx.cubegame.levels.Difficulty;

public class Config {
	public static boolean loadSoundsAndMusic = false; // Github: No music/sounds added, so this is set to false... cf. Assets.java
	
	public static Difficulty difficulty = Difficulty.HARD;
	
	public static boolean showGrid = false;
	public static boolean followPlayer = false;
	
	public static boolean useTextureAsBackground = true; // TODO Enum: CubeMap(String path), Static(String imagefile), None(String backgroundColor)
}
