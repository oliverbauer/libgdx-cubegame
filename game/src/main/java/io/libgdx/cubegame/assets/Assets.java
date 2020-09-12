package io.libgdx.cubegame.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;

import io.libgdx.cubegame.Config;

public class Assets {
	private AssetManager assets;
	private static Assets instance;

	public static Assets instance() {
		if (instance == null) {
			instance = new Assets();
		}
		return instance;
	}
	
	private Assets() {
		assets = new AssetManager();
		
		FileHandleResolver resolver = new InternalFileHandleResolver();
		assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assets.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		
		loadFonts();
		if (Config.loadSoundsAndMusic) {
			loadMusic();
			loadSounds();
		}
		assets.finishLoading();
		if (Config.loadSoundsAndMusic) {
			music = assets.get("cubegame/music/endorfun.mp3", Music.class);
			soundMovementNotAllowed = assets.get("cubegame/sounds/effect1.mp3", Sound.class);
			soundLifeforce = assets.get("cubegame/sounds/heal.mp3", Sound.class);
			soundPlayerMoved = assets.get("cubegame/sounds/flip.mp3", Sound.class);
		}

		font48 = assets.get("size48.ttf", BitmapFont.class);
		font96 = assets.get("size96.ttf", BitmapFont.class);
	}
	
	public BitmapFont font48 = null;
	public BitmapFont font96 = null;
	public Music music;
	
	public Sound soundMovementNotAllowed;
	public Sound soundLifeforce;
	public Sound soundPlayerMoved;
	
	private void loadFonts() {
		String fontFile = "rocketfuel.ttf";
		
		FreeTypeFontLoaderParameter size48 = new FreeTypeFontLoaderParameter();
		size48.fontFileName = "cubegame/font/"+fontFile;
		int size = 48;
		size48.fontParameters.size = Gdx.graphics.getWidth() * size / 1920;
		size48.fontParameters.borderWidth = 3f;
		size48.fontParameters.borderColor = Color.BLACK;
		assets.load("size48.ttf", BitmapFont.class, size48);
		
		
		FreeTypeFontLoaderParameter size96 = new FreeTypeFontLoaderParameter();
		size96.fontFileName = "cubegame/font/"+fontFile;
		int size2 = 96;
		size96.fontParameters.size = Gdx.graphics.getWidth() * size2 / 1920;
		size96.fontParameters.borderWidth = 3f;
		size96.fontParameters.borderColor = Color.YELLOW;
		size96.fontParameters.color = Color.BLUE;
		assets.load("size96.ttf", BitmapFont.class, size96);
	}
	
	private void loadMusic() {
		assets.load("cubegame/music/endorfun.mp3", Music.class);
	}
	
	private void loadSounds() {
		assets.load("cubegame/sounds/effect1.mp3", Sound.class);
		assets.load("cubegame/sounds/heal.mp3", Sound.class);
		assets.load("cubegame/sounds/flip.mp3", Sound.class);
	}
}
