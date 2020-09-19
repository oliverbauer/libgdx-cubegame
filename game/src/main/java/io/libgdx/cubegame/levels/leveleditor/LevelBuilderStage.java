package io.libgdx.cubegame.levels.leveleditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

import io.libgdx.cubegame.assets.Assets;

public class LevelBuilderStage {
	public Stage stage;
	
	public LevelBuilderStage(final LevelBuilder levelBuilder) {
		stage = new Stage();
		
		TextButtonStyle normalSizeStyle = new TextButtonStyle();
		normalSizeStyle.font = Assets.instance().font48;
		TextButton retry = new TextButton("Grid", normalSizeStyle);
		retry.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				LevelBuilder.showGrid = !LevelBuilder.showGrid;
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		stage.addActor(retry);

		TextButton retry2 = new TextButton("Level-Bleeding", normalSizeStyle);
		retry2.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				levelBuilder.switchLevelBleeding();
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		stage.addActor(retry2);
		
		Table table = new Table();
		table.setBounds(
			0, //Gdx.graphics.getWidth()/4, 
			0, // Gdx.graphics.getHeight()/4, 
			Gdx.graphics.getWidth(), 
			60);
		table.setFillParent(false);
		table.row();
		table.add(retry).pad(50);
		table.add(retry2);

		table.setTouchable(Touchable.enabled);
		retry.setTouchable(Touchable.enabled);
		retry2.setTouchable(Touchable.enabled);
		
		stage.addActor(table);
	}
	
	public void render() {
		stage.act();
		stage.draw();
	}
}
