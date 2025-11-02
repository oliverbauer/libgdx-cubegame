package io.libgdx.cubegame;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.libgdx.cubegame.screens.MenuScreen;

public class CubeGame extends Game {
	public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		//config.width = 2*1920;
		//config.height = 2*1080;
		//config.title = "Run Cubi run...";
		//config.fullscreen = true;
		new Lwjgl3Application(new CubeGame(), config);
	}
	
	@Override
	public void create() {
		setScreen(new MenuScreen(this));
	}
}
