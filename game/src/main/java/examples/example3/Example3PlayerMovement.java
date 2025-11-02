package examples.example3;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.SnapshotArray;

import io.libgdx.cubegame.helpers.Grid;
import io.libgdx.cubegame.levels.leveleditor.LevelBuilder;

public class Example3PlayerMovement extends ApplicationAdapter {
	public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        //config.width = 1920;
        //config.height = 1080;
        new Lwjgl3Application(new LevelBuilder(), config);
	}

	private ModelInstance grid;
	private Environment environment;
	private PerspectiveCamera cam;
	private ModelBatch modelBatch;

	// The ground
	private Block[][][] level;
	// Moveable player
	private Player player;
	
	private Keyboard playerController;
	private PlayerDirection direction = PlayerDirection.NONE;
	
	public enum PlayerDirection {
		FORWARD, BACK, LEFT, RIGHT, NONE, 
	}

	private float alpha = 0;
	private float cubeRotationSpeed = 3f;
	private float fromAngle = 0;
	private float toAngle = 90;
	
	public static float xOffsetPlayer = 0.5f;
	public static float yOffsetPlayer = -0.4f;
	public static float zOffsetPlayer = 0.5f;
	
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

		createLevel();
		createPlayer(0,1,0);
		
		
		playerController = new Keyboard();
		
		
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
		
		inputMultiplexer.addProcessor(playerController);
		inputMultiplexer.addProcessor(new CameraInputController(cam));
		
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		for (int i=0; i<=level.length-1; i++) {
			for (int j=0; j<=level[i].length-1; j++) {
				for (int k=0; k<=level[i][j].length-1; k++) {
					if (level[i][j][k] != null) {
						level[i][j][k].instance.transform = new Matrix4().translate(i,j,k);
						
						
						if (level[i][j][k] instanceof Player)
							level[i][j][k].instance.transform = new Matrix4().translate(i + xOffsetPlayer,j + yOffsetPlayer,k + zOffsetPlayer);
						else
							level[i][j][k].instance.transform = new Matrix4().translate(i + 0.5f,j,k + 0.5f);

						
					}
				}
			}
		}
	}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(0.5f, 0.8f, 1f, 0f); // Background r,g,b,a... without: black
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		modelBatch.render(grid, environment);
		
		for (int i=0; i<=level.length-1; i++) {
			for (int j=0; j<=level[i].length-1; j++) {
				for (int k=0; k<=level[i][j].length-1; k++) {
					if (level[i][j][k] != null) {
						level[i][j][k].x = i;
						level[i][j][k].y = j;
						level[i][j][k].z = k;
						
						modelBatch.render(level[i][j][k].instance, environment);
					}
				}
			}
		}
		handleMovementAndRotation(playerController.nextDir);
		
		modelBatch.end();
	}
	
	public int xNext = 0;
	public int yNext = 1;
	public int zNext = 0;
	
	
	public void handleMovementAndRotation(PlayerDirection nextDir) {
		if (direction == PlayerDirection.NONE && nextDir == PlayerDirection.NONE) {

			return;
		} else if (direction == PlayerDirection.NONE) {
			direction = nextDir;

			player.isMoving = true;
			xNext = player.x;
			yNext = player.y;
			zNext = player.z;

			if (direction == PlayerDirection.RIGHT) {
				zNext++;
				
				fromAngle = 0;
				toAngle = fromAngle + 90 % 360;
			} else if (direction == PlayerDirection.FORWARD) {
				xNext--;

				fromAngle = 0;
				toAngle = fromAngle + 90 % 360;
			} else if (direction == PlayerDirection.LEFT) {
				zNext--;

				fromAngle = 0;
				toAngle = fromAngle - 90 % 360;
			} else if (direction == PlayerDirection.BACK) {
				xNext++;

				fromAngle = 0;
				toAngle = fromAngle - 90 % 360;
			}

			alpha = 0;
			
			System.out.println("Next dir "+direction+" from "+fromAngle+" to "+toAngle+ " ("+player.x+","+player.y+","+player.z+") -> ("+xNext+","+yNext+","+zNext+")");

		} else {
			if (player.isMoving) {
				final float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f);
				alpha += delta * cubeRotationSpeed;
				float angle = fromAngle + alpha * (toAngle - fromAngle);
				Vector3 tmpV = new Vector3();

				Vector3 axis;
				if (xNext == player.x) {
					axis = Vector3.X;
				} else {
					axis = Vector3.Z;
				}
				
				Vector3 oldPosition = new Vector3(player.x, player.y, player.z);
				Vector3 newPosition = new Vector3(xNext, yNext, zNext);
				
				oldPosition.x += xOffsetPlayer;
				oldPosition.y += yOffsetPlayer;
				oldPosition.z += zOffsetPlayer;
				
				newPosition.x += xOffsetPlayer;
				newPosition.y += yOffsetPlayer;
				newPosition.z += zOffsetPlayer;
				
				tmpV.set(oldPosition).lerp(newPosition, alpha);
				
				player.instance.transform.setToRotation(axis, angle);
				player.instance.transform.setTranslation(tmpV);

				System.out.println(" Rotate+Lerp dir "+direction+" from "+fromAngle+" to "+toAngle+ " ("+player.x+","+player.y+","+player.z+") -> ("+xNext+","+yNext+","+zNext+"): "+tmpV);
				
				if (angle > toAngle && direction == PlayerDirection.RIGHT) {
					System.out.println("End right");
					player.isMoving = false;
					direction = PlayerDirection.NONE;
					
					level[player.x][player.y][player.z] = null;
					
					player.x = xNext;
					player.y = yNext;
					player.z = zNext;

					level[player.x][player.y][player.z] = player;
				
					player.rotate(PlayerDirection.RIGHT);
//					player.translate();
					
					System.out.println("End right: "+player.x+","+player.y+","+player.z);
					
				}
				if (angle < toAngle && direction == PlayerDirection.LEFT) {
					System.out.println("End left");
					player.isMoving = false;
					direction = PlayerDirection.NONE;
					
					level[player.x][player.y][player.z] = null;
					
					player.x = xNext;
					player.y = yNext;
					player.z = zNext;
					
					level[player.x][player.y][player.z] = player;

					player.rotate(PlayerDirection.LEFT);
				}

				if (angle < toAngle && direction == PlayerDirection.BACK) {
					System.out.println("End back");
					player.isMoving = false;
					direction = PlayerDirection.NONE;
					
					level[player.x][player.y][player.z] = null;
					
					player.x = xNext;
					player.y = yNext;
					player.z = zNext;
					
					level[player.x][player.y][player.z] = player;
					player.rotate(PlayerDirection.BACK);
				}
				if (angle > toAngle && direction == PlayerDirection.FORWARD) {
					System.out.println("End forward");
					player.isMoving = false;
					direction = PlayerDirection.NONE;
					
					level[player.x][player.y][player.z] = null;
					
					player.x = xNext;
					player.y = yNext;
					player.z = zNext;
					
					level[player.x][player.y][player.z] = player;
					player.rotate(PlayerDirection.FORWARD);
				}
			}
		}
	}
	
	@Override
	public void dispose() {
		modelBatch.dispose();
	}
	
	private void createPlayer(int x, int y, int z) {
		player = new Player(x, y, z);
		level[x][y][z] = player;
	}
	
	private void createLevel() {
		level = new Block[10][10][10];
		
		for (int x=0; x<=9; x++) {
			for (int z=0; z<=9; z++) {
				float transparency = 1 - Math.max(0.1f*z, 0.1f*x);
				createBlock(x, 0, z, transparency);
			}
		}
		
//		new CubeCornerResult(1,-1.5f,0,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(2,-1.5f,0,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(3,-1.5f,0,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(4,-1.5f,0,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		
//		// two cubes over each other
//		new CubeCornerResult(4,-1.5f,1,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(4,-0.5f,1,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		
//		
//		new CubeCornerResult(1,-1.5f,1,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(1,-1.5f,2,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(1,-1.5f,3,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(2,-1.5f,1,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(2,-1.5f,2,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(2,-1.5f,3,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(3,-1.5f,1,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(3,-1.5f,2,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(3,-1.5f,3,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(4,-1.5f,2,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(4,-1.5f,3,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(5,-1.5f,0,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(5,-1.5f,1,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(5,-1.5f,2,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(5,-1.5f,3,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		
//		// cubes of height = 2
//		height = 2;
//		
//		width = 0.1f;
//		depth = 0.95f;
//		
//		new CubeCornerResult(0,-1f,0,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(0,-1f,1,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(0,-1f,2,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		new CubeCornerResult(0,-1f,3,width,height,depth).finialize(mpb, colors.toArray(new Color[6]));
//		
//		ModelInstance mi = new ModelInstance(builder.end());
	}
	
	private void createBlock(int x, int y, int z, float transparency) {
		Block block = new Block(x, y, z);
		
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = .9f;

		ModelBuilder builder = new ModelBuilder();
		builder.begin();

		float width = 0.9f;
		float height = 0.1f;
		float depth = 0.9f;
		List<Color> colors = Arrays.asList(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.BLACK, Color.ORANGE);
		List<java.awt.Color> awtcolors = Arrays.asList(java.awt.Color.RED, java.awt.Color.BLUE, java.awt.Color.YELLOW, java.awt.Color.GREEN, java.awt.Color.BLACK, java.awt.Color.ORANGE);

		new CubeCornerResult(0,0,0,width,height,depth)
			.finialize(builder, colors.toArray(new Color[6]), awtcolors.toArray(new java.awt.Color[6]), transparency);

		ModelInstance mi = new ModelInstance(builder.end());
		block.instance = mi;
		
		level[x][y][z] = block; 
	}
}
