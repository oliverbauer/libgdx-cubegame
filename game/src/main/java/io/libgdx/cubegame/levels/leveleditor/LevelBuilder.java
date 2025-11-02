package io.libgdx.cubegame.levels.leveleditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
//import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
//import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.SnapshotArray;

import io.libgdx.cubegame.helpers.Grid;

/**
 * TODO Set Player-Position
 * TODO Set enemy-Position
 * TODO Create YAML-File
 * 
 * TODO left click on node: selection mode
 * TODO right click: if selected: unselect, if not selected remove
 */
public class LevelBuilder extends ApplicationAdapter {
	public static void main(String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		//config.width = 1920;
		//config.height = 1080;
		new Lwjgl3Application(new LevelBuilder(), config);
	}
	
	public static boolean showGrid = true;
	private boolean useLevelBeeding = true;
	
	public void switchLevelBleeding() {
		this.useLevelBeeding = !useLevelBeeding;
		
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		BlendingAttribute blendingAttribute2 = new BlendingAttribute();
		
		blendingAttribute.opacity = .9f;
		blendingAttribute2.opacity = .2f;
		
		for (Entry<Vector3, Block> m : blockPositions.entrySet()) {
			float y = m.getKey().y;
			
			m.getValue().instance.transform = new Matrix4().translate(
				0.5f,
				y + 0.5f,
				0.5f
			);
			
			if (useLevelBeeding) {
				if (y == 0) {
					m.getValue().instance.materials.get(0).set(blendingAttribute);
				} else {
					m.getValue().instance.materials.get(0).set(blendingAttribute2);
				}
			} else {
				m.getValue().instance.materials.get(0).set(blendingAttribute);
			}
		}		
	}
	
	private Environment environment;
	private PerspectiveCamera cam;
	private ModelBatch modelBatch;

	private ModelInstance grid;
	private Block player;
	private PlayerController playerController;
	private Vector3 playerPosition = new Vector3(0f, 0f, 0f);
	
	public enum PlayerDirection {
		UP, DOWN, NONE 
	}

	private List<Block> blocks;
	private Map<Vector3, Block> blockPositions;
	private Block selectedBlock;
	
	private LevelBuilderStage levelBuilderStage;
	
	@Override
	public void create() {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		modelBatch = new ModelBatch();

		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(-5f, 10f, -5f);
		cam.lookAt(5, 0, 5);
		cam.update();

		grid = new ModelInstance(Grid.createGridModel());
		
		player = new Block(GL20.GL_LINES, 0, 0, 0, 1, 1, 1);
		
		playerController = new PlayerController();
		levelBuilderStage = new LevelBuilderStage(this);
		
		
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
		
//		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(playerController);
		inputMultiplexer.addProcessor(new CameraInputController(cam));
		inputMultiplexer.addProcessor(levelBuilderStage.stage);
		
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		blocks = new ArrayList<>();
		blockPositions = new HashMap<>();
	}

	final class PlayerController extends InputAdapter {
		PlayerDirection nextDir = PlayerDirection.NONE;
		
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		BlendingAttribute blendingAttribute2 = new BlendingAttribute();

		public PlayerController() {
			blendingAttribute.opacity = .9f;
			blendingAttribute2.opacity = .2f;
		}
		
		@Override
		public boolean keyDown(final int keycode) {
			if (keycode == Input.Keys.W) {
				nextDir = PlayerDirection.DOWN;
			} else if (keycode == Input.Keys.S) {
				nextDir = PlayerDirection.UP;
			}			
			
			return false;
		}
		
		@Override
		public boolean keyUp(final int keycode) {
			if (keycode == Input.Keys.P && selectedBlock != null) {
				if (selectedBlock.getHeight() <= 0.9f) {
					selectedBlock.updateSize(
						selectedBlock.getWidth(), 
						selectedBlock.getHeight() + 0.1f, 
						selectedBlock.getDepth()
					);
				}
			}
			if (keycode == Input.Keys.O && selectedBlock != null) {
				if (selectedBlock.getHeight() >= 0.2f) {
					selectedBlock.updateSize(
						selectedBlock.getWidth(), 
						selectedBlock.getHeight() - 0.1f, 
						selectedBlock.getDepth()
					);
				}
			}

			if (keycode == Input.Keys.K && selectedBlock != null) {
				// Testing...
				
				selectedBlock.instance.transform.translate(					
					new Vector3(
						selectedBlock.getX(),
						selectedBlock.getY(),
						selectedBlock.getZ()
					)
				)
				.rotate(Vector3.X, 5f)
				.translate(					
					new Vector3(
						-selectedBlock.getX(),
						-selectedBlock.getY(),
						-selectedBlock.getZ()
					)
				);
				
//				selectedBlock.instance.transform.setToRotation(Vector3.Y, 0.1f);
//				selectedBlock.instance.transform.setTranslation(
//					new Vector3(
//							selectedBlock.getX(),
//							selectedBlock.getY(),
//							selectedBlock.getZ()
//					)
//				);
//				selectedBlock.instance.transform.rotate(Vector3.X, 0.1f);
			}
			
			
			if (keycode == Input.Keys.W && nextDir == PlayerDirection.DOWN) {
				nextDir = PlayerDirection.NONE;
				
				for (Entry<Vector3, Block> m : blockPositions.entrySet()) {
					m.getKey().y--;
					
					m.getValue().update(m.getKey());
					
					if (useLevelBeeding) {
						if (m.getKey().y == 0) {
							m.getValue().instance.materials.get(0).set(blendingAttribute);
						} else {
							m.getValue().instance.materials.get(0).set(blendingAttribute2);
						}
					} else {
						m.getValue().instance.materials.get(0).set(blendingAttribute);
					}
				}
			} else if (keycode == Input.Keys.S && nextDir == PlayerDirection.UP) {
				nextDir = PlayerDirection.NONE;
				
				
				for (Entry<Vector3, Block> m : blockPositions.entrySet()) {
					m.getKey().y++;
					
					m.getValue().update(m.getKey());
					
					if (useLevelBeeding) {
						if (m.getKey().y == 0) {
							m.getValue().instance.materials.get(0).set(blendingAttribute);
						} else {
							m.getValue().instance.materials.get(0).set(blendingAttribute2);
						}
					} else {
						m.getValue().instance.materials.get(0).set(blendingAttribute);
					}
				}
			}
			return false;
		}
		
		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			Ray ray = cam.getPickRay(screenX, screenY);
			final float distance = -ray.origin.y / ray.direction.y;
			tmpVector.set(ray.direction).scl(distance).add(ray.origin);
			
			playerPosition.x = (int)tmpVector.x;
			playerPosition.z = (int)tmpVector.z;
			playerPosition.x += 0.5f;
			playerPosition.z += 0.5f;
			player.updateWithXZ(playerPosition);
			
			return super.mouseMoved(screenX, screenY);
		} 
 
		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			dragged = true;
			return super.touchDragged(screenX, screenY, pointer);
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			// if camera movement is due to a dragging event we dont spawn/remove a new position
			if (dragged) {
				dragged = false;
				return super.touchUp(screenX, screenY, pointer, button);
			}
			
			int xPos = (int)playerPosition.x;
			int yPos = (int)playerPosition.y;
			int zPos = (int)playerPosition.z;
			
			if (button == 0 && xPos >= 0 && zPos >=0) {
				// left click
				if (blockPositions.get(new Vector3(xPos, yPos, zPos)) == null) {
					Block m = dynamicCube(xPos,yPos,zPos);
					blocks.add(m);
					blockPositions.put(new Vector3(xPos, yPos, zPos), m);
				}
				selectedBlock = blockPositions.get(new Vector3(xPos, yPos, zPos)); // null or the newly created block
			} else if (button == 1) {
				// right click
				selectedBlock = null;
				Block m = blockPositions.get(new Vector3(xPos, yPos, zPos));
				if (m != null) {
					blocks.remove(m);
					blockPositions.remove(new Vector3(xPos, yPos, zPos));
				}
			}

			return super.touchUp(screenX, screenY, pointer, button);
		}

		boolean dragged = false;
		Vector3 tmpVector = new Vector3();
	}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(0.5f, 0.8f, 1f, 0f); // Background r,g,b,a... without: black
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		if (showGrid) {
			modelBatch.render(grid, environment);
		}
		
		for (Block m : blocks) {
			modelBatch.render(m.instance, environment);
		}
		
		if (playerPosition.x >= 0 && playerPosition.z >= 0) {
			modelBatch.render(player.instance, environment);
		}
		
		modelBatch.end();
		
		levelBuilderStage.render();
	}
	
	@Override
	public void dispose() {
		modelBatch.dispose();
	}

	private Block dynamicCube(int x, int y, int z) {
		float width = 0.95f;
		float height = 0.95f;
		float depth = 0.95f;
		return new Block(x, y, z, width, height, depth);
	}
}