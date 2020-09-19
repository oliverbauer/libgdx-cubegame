package io.libgdx.cubegame.screens;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import io.libgdx.cubegame.Config;
import io.libgdx.cubegame.CubeGame;
import io.libgdx.cubegame.assets.Assets;
import io.libgdx.cubegame.blocks.factories.TextureFactory;
import io.libgdx.cubegame.levels.Difficulty;
import io.libgdx.cubegame.stages.actors.TextureActor;

public class MenuScreen implements Screen {
	private Stage stage;
	private final CubeGame game;
	
	public MenuScreen(CubeGame game) {
		this.game = game;
		this.stage = new Stage();
		
		int width = 200;
		int height = 100;
		
	    String fName = "/cubegame/font/rocketfuel.ttf";
	    InputStream is = MenuScreen.class.getResourceAsStream(fName);
	    Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, is);
			font = font.deriveFont(Font.BOLD, 60f);
		} catch (FontFormatException | IOException e) {
			font = new Font("Verdana", Font.PLAIN, 20);
		}
		
		final Texture img = TextureFactory.createTexture(width, height, "Play", font, Color.BLACK, Color.GREEN);
		img.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		final StartGameButtonActor startGameActor = new StartGameButtonActor(img, width, height);
		startGameActor.setPosition(
			Gdx.graphics.getWidth() / 2 - width/2, 
			Gdx.graphics.getHeight() / 2 - height/2
		);
		stage.addActor(startGameActor);
		
		
		
		
		
		
		

		
		
		final TextureActor difficulties[] = new TextureActor[Difficulty.values().length];
		font = font.deriveFont(Font.BOLD, 20f);
		for (int i=0; i<=Difficulty.values().length-1; i++) {
			final Texture img2 = TextureFactory.createTexture(width, height, Difficulty.values()[i].name(), font, Color.BLACK, Color.RED);
			img2.setFilter(TextureFilter.Linear, TextureFilter.Linear);

			difficulties[i] = new TextureActor(img2, 150, 50);

			difficulties[i].setPosition(
				Gdx.graphics.getWidth() / 2 - width/2 - 330 + i*180, 
				Gdx.graphics.getHeight() / 2 - height/2 - 200
			);
			
			final int j=i;
			difficulties[i].addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					
					for (int k=0; k<=Difficulty.values().length-1; k++) {
						difficulties[k].setColor(Color.WHITE);
					}
					difficulties[j].setColor(Color.GRAY);
					
					Config.difficulty = Difficulty.values()[j];
					return true;
				}
			});
			
			stage.addActor(difficulties[i]);
		}
		
		Config.difficulty = Difficulty.VERY_EASY; // preselect...
		difficulties[0].setColor(Color.GRAY);     // preselection
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		
		Music backgroundMusic = Assets.instance().music;
		if (backgroundMusic != null) {
			backgroundMusic.setLooping(true);
			backgroundMusic.play();
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.getViewport().apply();
		stage.act(delta);
		stage.draw();
	}
	
	private class StartGameButtonActor extends Actor {
		private final Texture mTexture;
		
		public StartGameButtonActor(Texture tex, int width, int height) {
			mTexture = tex;

			setSize(width, height);

			addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					game.setScreen(new GameScreen());
					return true;
				}

				@Override
				public void enter(InputEvent event, float x, float y,
						int pointer, Actor fromActor) {
					setColor(Color.WHITE);
					super.enter(event, x, y, pointer, fromActor);
				}

				@Override
				public void exit(InputEvent event, float x, float y,
						int pointer, Actor toActor) {
					setColor(Color.LIGHT_GRAY);
					super.exit(event, x, y, pointer, toActor);
				}
			});
			setColor(Color.LIGHT_GRAY);
		}
		
		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.setColor(getColor());
			batch.draw(mTexture, getX(), getY(), getOriginX(), getOriginY(),
					getWidth(), getHeight(), getScaleX(), getScaleY(),
					getRotation(), 0, 0, mTexture.getWidth(),
					mTexture.getHeight(), false, false);
		}
	}
	
	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		
	}
}
