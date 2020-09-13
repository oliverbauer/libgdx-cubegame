package io.libgdx.cubegame.stages;

import java.text.DecimalFormat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

import io.libgdx.cubegame.assets.Assets;
import io.libgdx.cubegame.levels.Level;
import io.libgdx.cubegame.score.Score;

public class StageHUD {
	public Stage stage;
	
	public TextButton labelPoints;
	public TextButton labelLifeforces;
	public TextButton labelTime;
	
	public StageHUD() {
		stage = new Stage();
		
		// Use
		TextButtonStyle style = new TextButtonStyle();
		style.font = Assets.instance().font48;
		
		labelLifeforces = new TextButton("", style);
		labelPoints = new TextButton("", style);
		labelTime = new TextButton("", style);
		
		float width = Gdx.graphics.getWidth();
		
		Table tableLeft = new Table();
		tableLeft.setBounds(
			0, 
			Gdx.graphics.getHeight()-50, 
			Gdx.graphics.getWidth(), 
			50);
		tableLeft.setFillParent(false);
		tableLeft.top();
		tableLeft.row();
		
		tableLeft.add(labelLifeforces).colspan((int)(width/3)).expandX();
		tableLeft.add(labelTime).colspan((int)(width/3)).expandX();
		tableLeft.add(labelPoints).colspan((int)(width/3)).expandX();
		
		stage.addActor(tableLeft);
	}
	
	private float remainingTime;
	
	public void init(Level level) {
		this.remainingTime = level.getTimeLimit();
	}
	
	public void render(Level level) {
		// Points/Liveforces/Remaining time
		labelLifeforces.setText("Lifeforces: "+Score.lifeforces+" / "+level.requiredLifeforces());
		labelPoints.setText("Points: "+Score.score);

		if (remainingTime > 0.1f) {
			if (!level.isCompleted() && !level.isFailed()) {
				remainingTime -= Gdx.graphics.getDeltaTime();
	
				DecimalFormat sb = new DecimalFormat("#.0");
				labelTime.setText(sb.format(remainingTime));
			}
		} else {
			labelTime.setText("0.0");
			level.setFailed(true);
			level.setCompleted(false);
			// TODO Stop Background rotation and lerp of color
			// TODO Disable camera-rotation
		}
		
		stage.act();
		stage.draw();
	}
}