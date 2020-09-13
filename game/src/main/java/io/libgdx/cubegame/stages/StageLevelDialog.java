package io.libgdx.cubegame.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

import io.libgdx.cubegame.CubeGame;
import io.libgdx.cubegame.assets.Assets;
import io.libgdx.cubegame.levels.Level;
import io.libgdx.cubegame.screens.GameScreen;
import io.libgdx.cubegame.screens.MenuScreen;

/**
 * Not displayed.. only when:
 * 
 * <ul> 
 * <li> all required lifeforces of a level are collected or</li> 
 * <li> time was running out or</li> 
 * <li> enemy hit player</li>
 * </ul>
 *
 */
public class StageLevelDialog {
	private Stage stage;
	
	private TextButton status; // success / failed
	
	private TextButton backToMenu;
	private TextButton retry;
	private TextButton nextLevel;
	
	private Table table;
	
	public StageLevelDialog(GameScreen cubeApp) {
		stage = new Stage();
		
		TextButtonStyle normalSizeStyle = new TextButtonStyle();
		normalSizeStyle.font = Assets.instance().font48;
		
		TextButtonStyle largeSizeStyle = new TextButtonStyle();
		largeSizeStyle.font = Assets.instance().font96;
		
		status = new TextButton("Failed", largeSizeStyle);
		
		backToMenu = new TextButton("Back to menu", normalSizeStyle);
		backToMenu.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				CubeGame game = (CubeGame)Gdx.app.getApplicationListener();
				game.setScreen(new MenuScreen(game));
				
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		
		retry = new TextButton("Retry", normalSizeStyle);
		retry.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				cubeApp.retry();
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		nextLevel = new TextButton("Next level", normalSizeStyle);
		nextLevel.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				cubeApp.nextLevel();
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		
		float width = Gdx.graphics.getWidth() / 2;
		
		table = new Table();
		table.setBounds(
			Gdx.graphics.getWidth()/4, 
			Gdx.graphics.getHeight()/4, 
			Gdx.graphics.getWidth()/2, 
			Gdx.graphics.getHeight()/2);
		table.setFillParent(false);
		table.center();
		table.row();
		
		table.add(status).colspan((int)(width)).expandX().expand();
		table.row();
		
		table.add(backToMenu).colspan((int)(width/3)).expandX();
		table.add(retry).colspan((int)(width/3)).expandX();
		table.add(nextLevel).colspan((int)(width/3)).expandX();
		
		table.setTouchable(Touchable.disabled);
		
		stage.addActor(table);
	}
	
	public void setTouchable(Touchable touchable) {
		table.setTouchable(touchable);
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public void render(Level level) {
		
		if (level.isFailed() && !level.isCompleted()) {
			status.setText("FAILED");
			table.setTouchable(Touchable.enabled);
			
			nextLevel.setTouchable(Touchable.disabled);
			retry.setTouchable(Touchable.enabled);
			
			stage.act();
			stage.draw();
		} else if (!level.isFailed() && level.isCompleted()) {
			status.setText("SUCCESS");
			table.setTouchable(Touchable.enabled);
			
			nextLevel.setTouchable(Touchable.enabled);
			retry.setTouchable(Touchable.disabled);
			
			stage.act();
			stage.draw();
		}
	}
}
