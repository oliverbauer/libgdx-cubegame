package io.libgdx.cubegame.screens;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.SnapshotArray;

import io.libgdx.cubegame.Config;
import io.libgdx.cubegame.animation.PlayerAnimation;
import io.libgdx.cubegame.background.Background;
import io.libgdx.cubegame.helpers.Grid;
import io.libgdx.cubegame.input.Keyboard;
import io.libgdx.cubegame.levels.Level;
import io.libgdx.cubegame.levels.YAMLLevel1;
import io.libgdx.cubegame.levels.YAMLLevel2;
import io.libgdx.cubegame.levels.YAMLLevel3;
import io.libgdx.cubegame.player.PlayerController;
import io.libgdx.cubegame.stages.StageConfig;
import io.libgdx.cubegame.stages.StageHUD;
import io.libgdx.cubegame.stages.StageLevelDialog;

public class GameScreen implements Screen {
	private Environment environment;
	private ModelBatch modelBatch;
	private PerspectiveCamera perspectiveCamera;
	private CameraInputController camController;
	private ModelInstance gridInstance;

	private Keyboard keyboard;
	private Level level;
	
	private StageConfig stageConfig;
	private StageHUD stageHUD;
	private StageLevelDialog stageLevelDialog;
	
	private Background background;
	
	private PlayerController playerController;
	
	@Override
	public void show() {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.9f, 1f));

		modelBatch = new ModelBatch();
		perspectiveCamera = new PerspectiveCamera(100f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		level = new YAMLLevel1(this, perspectiveCamera);
				
		perspectiveCamera.lookAt(level.xlength()/2, 0 ,level.zlength()/2);
		perspectiveCamera.position.set(0, 5, 0);
		perspectiveCamera.update();

		gridInstance = new ModelInstance(Grid.createGridModel());
		
		camController = new CameraInputController(perspectiveCamera);
		camController.reset();

		keyboard = new Keyboard(level);

		stageConfig = new StageConfig(this);
		
		InputMultiplexer inputMultiplexer = new InputMultiplexer() {
			@Override
			public boolean touchDown (int screenX, int screenY, int pointer, int button) {
				SnapshotArray<InputProcessor> processors = getProcessors();
				processors.begin();
				processors.forEach(p -> p.touchDown(screenX, screenY, pointer, button));
				processors.end();
				return false;
			}
			@Override
			public boolean touchUp (int screenX, int screenY, int pointer, int button) {
				SnapshotArray<InputProcessor> processors = getProcessors();
				processors.begin();
				processors.forEach(p -> p.touchUp(screenX, screenY, pointer, button));
				processors.end();
				return false;
			}
		};
		
		stageHUD = new StageHUD();
		stageHUD.init(level);
		stageLevelDialog = new StageLevelDialog(this);

		inputMultiplexer.addProcessor(keyboard);
		inputMultiplexer.addProcessor(camController);
		inputMultiplexer.addProcessor(stageConfig.getStage());
		inputMultiplexer.addProcessor(stageLevelDialog.getStage());
		Gdx.input.setInputProcessor(inputMultiplexer);
		

		background = new Background(environment, modelBatch, perspectiveCamera);
		
		playerController = new PlayerController(level, this);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.5f, 0.8f, 1f, 0f); // Background lightblue... without: black
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		if (!stageHUD.isFailed() && !stageHUD.isCompleted()) {
			float dR = (Gdx.input.isKeyPressed(Keys.Q) ? -1 : 0) + (Gdx.input.isKeyPressed(Keys.E) ? 1 : 0);
			float rotateSpeed = 1;
			perspectiveCamera.rotate(perspectiveCamera.direction, dR * rotateSpeed);
		}
		perspectiveCamera.update();
		background.render();
		
		modelBatch.begin(perspectiveCamera);
		if (gridInstance != null && Config.showGrid) {
			modelBatch.render(gridInstance, environment);
		}
		
		level.cameraUpdate(perspectiveCamera);
		if (!stageHUD.isFailed() && !stageHUD.isCompleted()) {
			level.updateAnimatedBlocks();
		}
		
		/**
		 * If the player hasn't been moved with W/A/S/D for 5 seconds, jump ;-)
		 */
		if (System.currentTimeMillis() - keyboard.lastWASD > 5000 && !stageHUD.isFailed() && !stageHUD.isCompleted()) {
			keyboard.lastWASD = System.currentTimeMillis();

			int x = level.getPlayer().x;
			int y = level.getPlayer().y;
			int z = level.getPlayer().z;
			// Animation
			level.getPlayer().anim = new PlayerAnimation(
				Arrays.asList(
					new Vector3(x, y, z),
					new Vector3(x, y + 1, z),
					new Vector3(x, y, z)
				)
			);
		}
		
		
		if (level.getPlayer().anim != null) {
			level.getPlayer().anim.update(level.getPlayer());
		}
		
		level.renderLevel(modelBatch, environment);
		if (!stageHUD.isFailed() && !stageHUD.isCompleted()) {
			level.spawnBlocks();
		}
		modelBatch.end();
		
		// must be after modelBatch.end()
		stageConfig.render();

		stageHUD.render(level);
		
		stageLevelDialog.render(stageHUD.isFailed(), stageHUD.isCompleted());

		if (!stageHUD.isFailed() && !stageHUD.isCompleted()) {
			playerController.playerMovement(keyboard.getNextDirection());
		}
	}

	public void setCompleted() {
		stageHUD.setCompleted(true);
	}
	
	public void setFailed(boolean value) {
		stageHUD.setFailed(value);
	}

	public void retry() {
		level.dispose();
		if (level instanceof YAMLLevel1) {
			level = new YAMLLevel1(this, perspectiveCamera);		
		} else if (level instanceof YAMLLevel2) {
			level = new YAMLLevel2(this, perspectiveCamera);
		} else if (level instanceof YAMLLevel3) {
			level = new YAMLLevel3(this, perspectiveCamera);
		}
		updateLevel();
	}
	
	public void nextLevel() {
		level.dispose();
		if (level instanceof YAMLLevel1) {
			level = new YAMLLevel2(this, perspectiveCamera);		
		} else if (level instanceof YAMLLevel2) {
			level = new YAMLLevel3(this, perspectiveCamera);
		} else if (level instanceof YAMLLevel3) {
			level = new YAMLLevel3(this, perspectiveCamera); // TODO Add more levels
		}
		updateLevel();
	}
	
	private void updateLevel() {
		stageHUD.init(level);
		stageLevelDialog.setTouchable(Touchable.disabled);
		
		keyboard.g = level;
		camController.reset();

		perspectiveCamera.lookAt(level.xlength()/2, 0 ,level.zlength()/2);
		perspectiveCamera.position.set(0, 5, 0);
		perspectiveCamera.update();
		
		level.getPlayer().isMoving = false;
		playerController = new PlayerController(level, this);
	}

	@Override
	public void resize(int width, int height) {
		stageConfig.getStage().getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
		// nothing
	}

	@Override
	public void resume() {
		// nothing	
	}

	@Override
	public void hide() {
		// nothing
	}

	@Override
	public void dispose() {
		// TODO dispose...
	}
}
