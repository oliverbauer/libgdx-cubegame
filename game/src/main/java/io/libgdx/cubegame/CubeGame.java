package io.libgdx.cubegame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import io.libgdx.cubegame.screens.MenuScreen;

public class CubeGame extends Game {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 2*1920;
		config.height = 2*1080;
		config.title = "Run Cubi run...";
		config.fullscreen = true;
		new LwjglApplication(new CubeGame(), config);
	}
	
	@Override
	public void create() {
		setScreen(new MenuScreen(this));
	}
}
