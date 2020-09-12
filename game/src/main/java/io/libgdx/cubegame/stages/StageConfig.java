package io.libgdx.cubegame.stages;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import io.libgdx.cubegame.Config;
import io.libgdx.cubegame.blocks.factories.TextureFactory;
import io.libgdx.cubegame.screens.GameScreen;
import io.libgdx.cubegame.screens.MenuScreen;

public class StageConfig {
	private Stage stage;
	
	public StageConfig(GameScreen cubeApp) {
		stage = new Stage();
		
	    String fName = "/cubegame/font/rocketfuel.ttf";
	    InputStream is = MenuScreen.class.getResourceAsStream(fName);
	    Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, is);
			font = font.deriveFont(Font.BOLD, 10f);
		} catch (FontFormatException | IOException e) {
			font = new Font("Verdana", Font.PLAIN, 20);
		}
		
		
		/*
		 * Actor 1:
		 */
		final Texture textureRenderGrid = TextureFactory.createTexture(100, 20, "Render Grid", font, Color.BLACK, null);
		textureRenderGrid.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureActor actorRenderGrid = new TextureActor(textureRenderGrid, 100, 20);
		actorRenderGrid.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Config.showGrid = !Config.showGrid;
				return true;
			}
		});
		actorRenderGrid.setPosition(
			Gdx.graphics.getWidth() - 200/2, 
			// 0 //Gdx.graphics.getHeight() / 2 - 10
			Gdx.graphics.getHeight()  - 20
		);
		stage.addActor(actorRenderGrid);
		

		/*
		 * Actor 2:
		 */
		final Texture textureFollowCube = TextureFactory.createTexture(100, 20, "Follow Cube", font, Color.BLACK, null);
		textureFollowCube.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureActor actorFollowCube = new TextureActor(textureFollowCube, 100, 20);
		actorFollowCube.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Config.followPlayer = !Config.followPlayer;
				return true;
			}
		});
		actorFollowCube.setPosition(
			Gdx.graphics.getWidth() - 200/2, 
			Gdx.graphics.getHeight() - 50
		);
		stage.addActor(actorFollowCube);
	}

	public void render() {
		stage.act();
		stage.draw();
	}
	
	public Stage getStage() {
		return stage;
	}
	
	/**
	 * TODO Same as in MenuScreen
	 */
	private class TextureActor extends Actor {
		private final Texture mTexture;
		
		public TextureActor(Texture tex, int width, int height) {
			mTexture = tex;
			setSize(width, height);
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
}
