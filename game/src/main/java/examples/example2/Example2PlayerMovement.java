package examples.example2;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import examples.example2.CubeCornerResult.MOD;

public class Example2PlayerMovement extends ApplicationAdapter {
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1920;
		config.height = 1080;
		new LwjglApplication(new Example2PlayerMovement(), config);
	}

	private Environment environment;
	private PerspectiveCamera cam;
	private ModelBatch modelBatch;

	// The ground
	private ModelInstance level;
	// Moveable player
	private ModelInstance player;
	
	private ModelInstance mouseModelInstance;
	private Vector3 mousePosition = new Vector3(1.5f, 1.5f, 1.5f);
	
	private PlayerController playerController;
	private Vector3 playerPosition = new Vector3(1.5f, 1.5f, 1.5f);
	private Vector3 playerPositionTarget = new Vector3();
	private PlayerDirection direction = PlayerDirection.NONE;
	
	public enum PlayerDirection {
		FORWARD, BACK, LEFT, RIGHT, NONE, 
		TEST_TOP1, TEST_TOP2, TEST_TOP3,
		TEST_BOTTOM1, TEST_BOTTOM2, TEST_BOTTOM3
	}

	// Initial colors... when player moves, those will be "switched"
	private java.awt.Color front = java.awt.Color.BLACK;
	private java.awt.Color right = java.awt.Color.YELLOW;
	private java.awt.Color bottom = java.awt.Color.BLUE;
	private java.awt.Color left = java.awt.Color.RED;
	private java.awt.Color top = java.awt.Color.GREEN;
	private java.awt.Color back = java.awt.Color.CYAN;
	
	private float alpha = 0;
	private float cubeRotationSpeed = 3f;
	private float fromAngle = 0;
	private float toAngle = 90;
	private boolean moving = false;
	private Vector3 axis;
	
	// Just for demonstration of rotation around a point
	private ModelInstance pyramidGround;
	private ModelInstance pyradmiHeadRotating;
	
	private int x = 4;
	private int y = 0;
	private int z = 6;
	
	
	private List<ModelInstance> createdModels;
	
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

		level = createLevel();
		player = createPlayer(playerPosition);
		
		pyramidGround = createPyramidGround(x,y,z);
		pyradmiHeadRotating = createPyradmidHead(x,y,z);
		
		playerController = new PlayerController();
		
		
		mouseModelInstance = createMousePosition(playerPosition);
		
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(playerController);
		inputMultiplexer.addProcessor(new CameraInputController(cam));
		inputMultiplexer.addProcessor(new MouseController());
		
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		createdModels = new ArrayList<>();
	}
	
	final class MouseController extends InputAdapter {
		Vector3 tmpVector = new Vector3();
		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			Ray ray = cam.getPickRay(screenX, screenY);
			final float distance = -ray.origin.y / ray.direction.y;
			tmpVector.set(ray.direction).scl(distance).add(ray.origin);
			
			mousePosition.x = (int)tmpVector.x;
//			playerPosition.y = (int)tmpVector.y;
			mousePosition.z = (int)tmpVector.z;
			mousePosition.x += 0.5f;
//			playerPosition.y += 0.5f;
			mousePosition.z += 0.5f;
			mouseModelInstance.transform = new Matrix4().translate(mousePosition);
			
			return super.mouseMoved(screenX, screenY);
		} 
	}

	final class PlayerController extends InputAdapter {
		PlayerDirection nextDir = PlayerDirection.NONE;
		
		@Override
		public boolean keyDown(final int keycode) {
			if (keycode == Input.Keys.W) {
				nextDir = PlayerDirection.BACK;
			} else if (keycode == Input.Keys.S) {
				nextDir = PlayerDirection.FORWARD;
			} else if (keycode == Input.Keys.A) {
				nextDir = PlayerDirection.LEFT;
			} else if (keycode == Input.Keys.D) {
				nextDir = PlayerDirection.RIGHT;
			} else if (keycode == Input.Keys.T) {
				nextDir = PlayerDirection.TEST_TOP1;
			} else if (keycode == Input.Keys.Z) {
				nextDir = PlayerDirection.TEST_TOP2;
			} else if (keycode == Input.Keys.U) {
				nextDir = PlayerDirection.TEST_TOP3;
			} else if (keycode == Input.Keys.B) {
				nextDir = PlayerDirection.TEST_BOTTOM1;
			} else if (keycode == Input.Keys.N) {
				nextDir = PlayerDirection.TEST_BOTTOM2;
			} else if (keycode == Input.Keys.M) {
				nextDir = PlayerDirection.TEST_BOTTOM3;
			}
			
			if (keycode == Input.Keys.P) {
				int x = playerPositionTarget.x < 0 ? -1 : 0;
				int z = playerPositionTarget.z < 0 ? -1 : 0;
				
				createdModels.add(
					dynamicCube(
						(int)playerPositionTarget.x + x, 
						(int)playerPositionTarget.y, 
						(int)playerPositionTarget.z + z)
				);
			}
			
			return false;
		}

		@Override
		public boolean keyUp(final int keycode) {
			if (keycode == Input.Keys.W && nextDir == PlayerDirection.BACK) {
				nextDir = PlayerDirection.NONE;
			} else if (keycode == Input.Keys.S && nextDir == PlayerDirection.FORWARD) {
				nextDir = PlayerDirection.NONE;
			} else if (keycode == Input.Keys.A && nextDir == PlayerDirection.LEFT) {
				nextDir = PlayerDirection.NONE;
			} else if (keycode == Input.Keys.D && nextDir == PlayerDirection.RIGHT) {
				nextDir = PlayerDirection.NONE;
			} else if (keycode == Input.Keys.T && nextDir == PlayerDirection.TEST_TOP1) {
				nextDir = PlayerDirection.NONE;
			} else if (keycode == Input.Keys.Z && nextDir == PlayerDirection.TEST_TOP2) {
				nextDir = PlayerDirection.NONE;
			} else if (keycode == Input.Keys.U && nextDir == PlayerDirection.TEST_TOP3) {
				nextDir = PlayerDirection.NONE;
			} else if (keycode == Input.Keys.B && nextDir == PlayerDirection.TEST_BOTTOM1) {
				nextDir = PlayerDirection.NONE;
			} else if (keycode == Input.Keys.N && nextDir == PlayerDirection.TEST_BOTTOM2) {
				nextDir = PlayerDirection.NONE;
			} else if (keycode == Input.Keys.M && nextDir == PlayerDirection.TEST_BOTTOM3) {
				nextDir = PlayerDirection.NONE;
			}
			

			
			return false;
		}
	}
	
	@Override
	public void render() {
		float delta = Gdx.graphics.getDeltaTime();
		
		Gdx.gl.glClearColor(0.5f, 0.8f, 1f, 0f); // Background r,g,b,a... without: black
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		// "A rotation around a point is the same as translating to that point, rotating and then translating back."
		// Cf. https://stackoverflow.com/questions/26656334/libgdx-set-rotation-point-3d
		pyradmiHeadRotating.transform
			.translate(x,y,z) // Positon of the pyramid
			.rotate(0, 0.1f, 0, 45*delta)
			.translate(-x, -y, -z); // Positon of the pyramid 
		
		modelBatch.begin(cam);
		modelBatch.render(level, environment);
		modelBatch.render(pyramidGround, environment);
		modelBatch.render(pyradmiHeadRotating, environment);
		
		for (ModelInstance m : createdModels) {
			modelBatch.render(m, environment);
		}
		
		handleMovementAndRotation();
		modelBatch.render(player, environment);
		
		modelBatch.render(mouseModelInstance, environment);
		
		modelBatch.end();
	}

	public void handleMovementAndRotation() {
		PlayerDirection nextDirection = playerController.nextDir;
		
		if (direction == PlayerDirection.NONE && nextDirection == PlayerDirection.NONE) {
			
		} else if (direction == PlayerDirection.NONE) {
			moving = true;
			direction = nextDirection;
			playerPositionTarget.set(playerPosition.x, playerPosition.y, playerPosition.z);

			if (direction == PlayerDirection.RIGHT) {
				playerPositionTarget.add(0, 0, 1);
				
				fromAngle = 0;
				toAngle = fromAngle + 90 % 360;
				axis = Vector3.X;
			} else if (direction == PlayerDirection.FORWARD) { // TODO Wenn nächste Ebene, nimm TEST_TOP
				playerPositionTarget.add(-1, 0, 0);

				fromAngle = 0;
				toAngle = fromAngle + 90 % 360;
				axis = Vector3.Z;
			} else if (direction == PlayerDirection.LEFT) {
				playerPositionTarget.add(0, 0, -1);

				fromAngle = 0;
				toAngle = fromAngle - 90 % 360;
				axis = Vector3.X;
			} else if (direction == PlayerDirection.BACK) {
				playerPositionTarget.add(1, 0, 0);

				fromAngle = 0;
				toAngle = fromAngle - 90 % 360;
				axis = Vector3.Z;
			} else if (direction == PlayerDirection.TEST_TOP1) {
				playerPositionTarget.add(1, 1, 0);

				fromAngle = 0;
				toAngle = fromAngle - 90 % 360;
				axis = Vector3.Z;
//				axis = null;
			} else if (direction == PlayerDirection.TEST_TOP2) {
				playerPositionTarget.add(0, 1, 0);

				fromAngle = 0;
				toAngle = fromAngle - 90 % 360;
				axis = Vector3.Z;
				axis = null;
			} else if (direction == PlayerDirection.TEST_TOP3) {
				playerPositionTarget.add(0, 1, 0);

				fromAngle = 0;
				toAngle = fromAngle - 90 % 360;
				axis = Vector3.Z;
			} else if (direction == PlayerDirection.TEST_BOTTOM1) {
				playerPositionTarget.add(-1, -1, 0);

				fromAngle = 0;
				toAngle = fromAngle + 90 % 360;
				axis = Vector3.Z;
//				axis = null;
			} else if (direction == PlayerDirection.TEST_BOTTOM2) {
				playerPositionTarget.add(0, -1, 0);

				fromAngle = 0;
				toAngle = fromAngle + 90 % 360;
				axis = Vector3.Z;
				axis = null;
			} else if (direction == PlayerDirection.TEST_BOTTOM3) {
				playerPositionTarget.add(0, -1, 0);

				fromAngle = 0;
				toAngle = fromAngle + 90 % 360;
				axis = Vector3.Z;
			}

			alpha = 0;

		} else {
			if (moving) {
				
				final float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f);
				alpha += delta * cubeRotationSpeed;
				float angle = fromAngle + alpha * (toAngle - fromAngle);
				Vector3 tmpV = new Vector3();

				Vector3 currentPos  = new Vector3(playerPosition.x, playerPosition.y, playerPosition.z);
				Vector3 newPosition = new Vector3(playerPositionTarget.x, playerPositionTarget.y, playerPositionTarget.z);
				
				tmpV.set(currentPos).lerp(newPosition, alpha);

				if (axis != null) {
					player.transform.setToRotation(axis, angle);
				}
				player.transform.setTranslation(tmpV);

				boolean stop = false;
				
				if (angle > toAngle && direction == PlayerDirection.RIGHT) {
					stop = true;
				} else if (angle < toAngle && direction == PlayerDirection.LEFT) {
					stop = true;
				} else if (angle < toAngle && direction == PlayerDirection.BACK) {
					stop = true;
//					playerController.nextDir = PlayerDirection.NONE;
				} else if (angle > toAngle && direction == PlayerDirection.FORWARD) {
					stop = true;
				} else if (angle < toAngle && direction == PlayerDirection.TEST_TOP1) {
					// TODO angle ???
					stop = true;
					
					
//					rotate(direction, playerPosition);
//
//					direction = PlayerDirection.NONE;
//					playerController.nextDir = PlayerDirection.BACK;
//					
//					playerPosition.x = playerPositionTarget.x;
//					playerPosition.y = playerPositionTarget.y;
//					playerPosition.z = playerPositionTarget.z;
//					player.transform = new Matrix4().translate(playerPosition);
//
//					// next part of rotation
//					playerPositionTarget.set(playerPosition.x, playerPosition.y, playerPosition.z);
//					playerPositionTarget.add(1, 0, 0);
//
//					fromAngle = 0;
//					toAngle = fromAngle + 90 % 360;
//					axis = Vector3.Z;
				} else if (angle < toAngle && direction == PlayerDirection.TEST_TOP2) {
					stop = true;
				} else if (angle < toAngle && direction == PlayerDirection.TEST_TOP3) {
					stop = true;
				} else if (angle > toAngle && direction == PlayerDirection.TEST_BOTTOM1) {
					stop = true;
				} else if (angle > toAngle && direction == PlayerDirection.TEST_BOTTOM2) {
					stop = true;
				} else if (angle > toAngle && direction == PlayerDirection.TEST_BOTTOM3) {
					stop = true;
				}
				
				if (stop) {
					moving = false;
					rotate(direction, playerPosition);

					direction = PlayerDirection.NONE;
					
					playerPosition.x = playerPositionTarget.x;
					playerPosition.y = playerPositionTarget.y;
					playerPosition.z = playerPositionTarget.z;
					player.transform = new Matrix4().translate(playerPosition);
					
					
					int fontSize = 20;
					String message = "("+(int)playerPosition.x+","+(int)playerPosition.y+","+(int)playerPosition.z+")";
//					player.materials.get(0).set(TextureAttribute.createDiffuse(createTextureWithText(front,"Front")));
//					player.materials.get(1).set(TextureAttribute.createDiffuse(createTextureWithText(back,"Back")));
//					player.materials.get(2).set(TextureAttribute.createDiffuse(createTextureWithText(bottom,"Bottom")));
					player.materials.get(3).set(TextureAttribute.createDiffuse(createTextureWithText(top,message,fontSize)));
//					player.materials.get(4).set(TextureAttribute.createDiffuse(createTextureWithText(left,"Left")));
//					player.materials.get(5).set(TextureAttribute.createDiffuse(createTextureWithText(right,"Right")));
				}
			}
		}
	}
	
	@Override
	public void dispose() {
		modelBatch.dispose();
	}
	
	private ModelInstance createPyramidGround(int x, int y, int z) {
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = .9f;

		ModelBuilder builder = new ModelBuilder();
		builder.begin();

		Material material = createDiffuseMaterialFromImage(java.awt.Color.CYAN);

		MeshPartBuilder mpb = builder.part("box", GL20.GL_TRIANGLES, 
				VertexAttributes.Usage.Position
				| VertexAttributes.Usage.Normal 
				| VertexAttributes.Usage.ColorPacked
				| VertexAttributes.Usage.TextureCoordinates, material);

		new CubeCornerResult(x,y,z,1,2,1).modify(MOD.ONE).finialize(mpb);
		
		ModelInstance mi = new ModelInstance(builder.end());
		mi.transform.translate(0.5f, 0.5f, 0.5f);
		mi.materials.get(0).set(TextureAttribute.createDiffuse(createTexture(java.awt.Color.LIGHT_GRAY)));
		return mi;
	}

	private ModelInstance createPlayer(Vector3 playerPosition) {
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = .9f;

		ModelBuilder builder = new ModelBuilder();
		builder.begin();

		float d = 0.5f;
		
		int attr = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates;
		
		
		builder.part("front", GL20.GL_TRIANGLES, attr, createDiffuseMaterialFromImage(front))
		.rect(
				-d, -d, -d, 
				-d, d, -d, 
				d, d, -d,
				d, -d, -d, 
				0, 0, -1);
		builder.part("back", GL20.GL_TRIANGLES, attr, createDiffuseMaterialFromImage(back)).rect(
			-d, d, d, 
			-d, -d, d, 
			d, -d, d, 
			d, d, d, 
			0, 0, 1);
		builder.part("bottom", GL20.GL_TRIANGLES, attr, createDiffuseMaterialFromImage(bottom)).rect(
			-d, -d, d, 
			-d, -d, -d,
			d, -d, -d,
			d, -d, d, 
			0, -1, 0);
		builder.part("top", GL20.GL_TRIANGLES, attr, createDiffuseMaterialFromImage(top)).rect(
			-d , d, -d, 
			-d , d, d, 
			d  , d, d, 
			d  , d, -d, 
			0,	1, 0);
		builder.part("left", GL20.GL_TRIANGLES, attr, createDiffuseMaterialFromImage(left)).rect(
			-d, -d, d, 
			-d, d, d, 
			-d, d, -d, 
			-d, -d, -d, 
			-1, 	0, 0);
		builder.part("right", GL20.GL_TRIANGLES, attr, createDiffuseMaterialFromImage(right)).rect(
			d, -d, -d, 
			d, d, -d, 
			d, d, d, 
			d, -d, d, 
			1, 0, 0);
		
		
		ModelInstance mi = new ModelInstance(builder.end());
		mi.materials.get(0).set(TextureAttribute.createDiffuse(createTexture(front)));
		mi.materials.get(1).set(TextureAttribute.createDiffuse(createTexture(back)));
		mi.materials.get(2).set(TextureAttribute.createDiffuse(createTexture(bottom)));
		mi.materials.get(3).set(TextureAttribute.createDiffuse(createTexture(top)));
		mi.materials.get(4).set(TextureAttribute.createDiffuse(createTexture(left)));
		mi.materials.get(5).set(TextureAttribute.createDiffuse(createTexture(right)));
		
		mi.transform = new Matrix4().translate(
				playerPosition.x,
				playerPosition.y,
				playerPosition.z);
		
		return mi;
	}

	private void rotate(PlayerDirection rot, Vector3 playerPosition) {
		if (rot == PlayerDirection.LEFT) {
			java.awt.Color temp = top;
			top = back;
			back = bottom;
			bottom = front;
			front = temp;
		} else if (rot == PlayerDirection.RIGHT) {
			java.awt.Color temp = front;
			front = bottom;
			bottom = back;
			back = top;
			top = temp;
		} else if (rot == PlayerDirection.FORWARD || rot == PlayerDirection.TEST_BOTTOM1 || rot == PlayerDirection.TEST_BOTTOM3) {
			java.awt.Color temp = bottom;
			bottom = left;
			left = top;
			top = right;
			right = temp;
		} else if (rot == PlayerDirection.BACK || rot == PlayerDirection.TEST_TOP1 || rot == PlayerDirection.TEST_TOP3) {
			java.awt.Color temp = right;
			right = top;
			top = left;
			left = bottom;
			bottom = temp;
		}

		player = createPlayer(playerPosition);
	}
	
	private ModelInstance dynamicCube(int x, int y, int z) {
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = .9f;

		ModelBuilder builder = new ModelBuilder();
		builder.begin();

		Material material = createDiffuseMaterialFromImage(java.awt.Color.CYAN);

		MeshPartBuilder mpb = builder.part("box", GL20.GL_TRIANGLES, 
				VertexAttributes.Usage.Position
				| VertexAttributes.Usage.Normal 
				| VertexAttributes.Usage.ColorPacked
				| VertexAttributes.Usage.TextureCoordinates, material);

		float width = 1;
		float height = 1;
		float depth = 1;
		
		
		new CubeCornerResult(x,y,z,width,height,depth).finialize(mpb);
		
		ModelInstance mi = new ModelInstance(builder.end());
		mi.transform.translate(0.5f, 0.5f, 0.5f);
		mi.materials.get(0).set(TextureAttribute.createDiffuse(createTextureWithText(java.awt.Color.LIGHT_GRAY,x+","+y+","+z,20)));
		return mi;
		
	}
	
	private ModelInstance createLevel() {
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = .9f;

		ModelBuilder builder = new ModelBuilder();
		builder.begin();

		Material material = createDiffuseMaterialFromImage(java.awt.Color.CYAN);

		MeshPartBuilder mpb = builder.part("box", GL20.GL_TRIANGLES, 
				VertexAttributes.Usage.Position
				| VertexAttributes.Usage.Normal 
				| VertexAttributes.Usage.ColorPacked
				| VertexAttributes.Usage.TextureCoordinates, material);

		float width = 0.95f;
		float height = 0.1f;
		float depth = 0.95f;

		new CubeCornerResult(1,-1.5f,0,width,height,depth).finialize(mpb);
		new CubeCornerResult(2,-1.5f,0,width,height,depth).finialize(mpb);
		new CubeCornerResult(3,-1.5f,0,width,height,depth).finialize(mpb);
		new CubeCornerResult(4,-1.5f,0,width,height,depth).finialize(mpb);
		
		// two cubes over each other
		new CubeCornerResult(4,-1.5f,1,width,height,depth).finialize(mpb);
		new CubeCornerResult(4,-0.5f,1,width,height,depth).finialize(mpb);
		
		
		new CubeCornerResult(1,-1.5f,1,width,height,depth).finialize(mpb);
		new CubeCornerResult(1,-1.5f,2,width,height,depth).finialize(mpb);
		new CubeCornerResult(1,-1.5f,3,width,height,depth).finialize(mpb);
		new CubeCornerResult(2,-1.5f,1,width,height,depth).finialize(mpb);
		new CubeCornerResult(2,-1.5f,2,width,height,depth).finialize(mpb);
		new CubeCornerResult(2,-1.5f,3,width,height,depth).finialize(mpb);
		new CubeCornerResult(3,-1.5f,1,width,height,depth).finialize(mpb);
		new CubeCornerResult(3,-1.5f,2,width,height,depth).finialize(mpb);
		new CubeCornerResult(3,-1.5f,3,width,height,depth).finialize(mpb);
		new CubeCornerResult(4,-1.5f,2,width,height,depth).finialize(mpb);
		new CubeCornerResult(4,-1.5f,3,width,height,depth).finialize(mpb);
		new CubeCornerResult(5,-1.5f,0,width,height,depth).finialize(mpb);
		new CubeCornerResult(5,-1.5f,1,width,height,depth).finialize(mpb);
		new CubeCornerResult(5,-1.5f,2,width,height,depth).finialize(mpb);
		new CubeCornerResult(5,-1.5f,3,width,height,depth).finialize(mpb);
		
		// cubes of height = 2
		height = 2;
		
		width = 0.1f;
		depth = 0.95f;
		
		new CubeCornerResult(0,-1f,0,width,height,depth).finialize(mpb);
		new CubeCornerResult(0,-1f,1,width,height,depth).finialize(mpb);
		new CubeCornerResult(0,-1f,2,width,height,depth).finialize(mpb);
		new CubeCornerResult(0,-1f,3,width,height,depth).finialize(mpb);
		
		ModelInstance mi = new ModelInstance(builder.end());
		mi.transform.translate(0.5f, 2.45f, 0.5f);
//		mi.materials.get(0).set(TextureAttribute.createDiffuse(createTexture(java.awt.Color.LIGHT_GRAY)));
		return mi;
	}
	
	private ModelInstance createPyradmidHead(int x, int y, int z) {
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = .9f;

		ModelBuilder builder = new ModelBuilder();
		builder.begin();

		Material material = createDiffuseMaterialFromImage(java.awt.Color.CYAN);

		MeshPartBuilder mpb = builder.part("box", GL20.GL_TRIANGLES, 
				VertexAttributes.Usage.Position
				| VertexAttributes.Usage.Normal 
				| VertexAttributes.Usage.ColorPacked
				| VertexAttributes.Usage.TextureCoordinates, material);

		new CubeCornerResult(x,y,z,1,1,1).modify(MOD.TWO).finialize(mpb);
		ModelInstance mi = new ModelInstance(builder.end());
		mi.transform.setTranslation(0.5f, 2, 0.5f);
		
		mi.materials.get(0).set(TextureAttribute.createDiffuse(createTexture(java.awt.Color.LIGHT_GRAY)));
		
		return mi;
	}
	
	private Texture createTexture(java.awt.Color c) {
		BufferedImage bufferedImage = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = bufferedImage.createGraphics();
	    
	    g2d.setColor(java.awt.Color.BLACK);
	    g2d.fillRect(0, 0, 80, 80);
	    
	    g2d.setColor(c);
	    g2d.fillRect(1, 1, 78, 78);
	    
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
			ImageIO.write(bufferedImage, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    byte[] byteArray = baos.toByteArray();
	    Pixmap mask = new Pixmap(byteArray, 0, byteArray.length);
		
	    return new Texture(mask);
	}

	private Texture createTextureWithText(java.awt.Color c, String text, int size) {
		BufferedImage bufferedImage = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = bufferedImage.createGraphics();
	    
	    int scale = 1;
	    int width = scale*80;
	    int height =scale*80;
	    
	    g2d.setColor(java.awt.Color.BLACK);
	    g2d.fillRect(0, 0, width, height);
	    
	    g2d.setColor(c);
	    g2d.fillRect(1, 1, width-2, height-2);
	    
	    
	    
	    String fName = "/cubegame/font/rocketfuel.ttf";
	    InputStream is = Example2PlayerMovement.class.getResourceAsStream(fName);
	    Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, is);
			font = font.deriveFont(Font.BOLD, size);
		} catch (FontFormatException | IOException e) {
			font = new Font("Verdana", Font.PLAIN, 20);
		}
		
        g2d.setFont(font);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fm = g2d.getFontMetrics();

        int x1 = (width - fm.stringWidth(text)) / 2;
        int y1 = ((height - fm.getHeight()) / 2);

        int rule = AlphaComposite.SRC_OVER;
        Composite comp = AlphaComposite.getInstance(rule , 0.7f);
        g2d.setComposite(comp );
        if (c == java.awt.Color.BLACK) {
        	g2d.setColor(java.awt.Color.WHITE);
        } else {
        	g2d.setColor(java.awt.Color.BLACK);
        }
        g2d.drawString(text, x1, y1 + fm.getAscent());
	    
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
			ImageIO.write(bufferedImage, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    
	    byte[] byteArray = baos.toByteArray();
	    Pixmap mask = new Pixmap(byteArray, 0, byteArray.length);
		
	    return new Texture(mask);
	}
	
	private Material createDiffuseMaterialFromImage(java.awt.Color c) {
		return new Material(TextureAttribute.createAmbient(createTexture(c)));
	}
	
	private ModelInstance createMousePosition(Vector3 position) {
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = .9f;

		ModelBuilder builder = new ModelBuilder();
		builder.begin();

		float d = 0.6f;
		
		int attr = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates;
		
		int primitiveType = GL20.GL_LINES;
//		primitiveType = GL_TRIANGLES;
		builder.part("front", primitiveType, attr, createDiffuseMaterialFromImage(front))
		.rect(
				-d, -d, -d, 
				-d, d, -d, 
				d, d, -d,
				d, -d, -d, 
				0, 0, -1);
		builder.part("back", primitiveType, attr, createDiffuseMaterialFromImage(back)).rect(
			-d, d, d, 
			-d, -d, d, 
			d, -d, d, 
			d, d, d, 
			0, 0, 1);
		builder.part("bottom",primitiveType, attr, createDiffuseMaterialFromImage(bottom)).rect(
			-d, -d, d, 
			-d, -d, -d,
			d, -d, -d,
			d, -d, d, 
			0, -1, 0);
		builder.part("top", primitiveType, attr, createDiffuseMaterialFromImage(top)).rect(
			-d , d, -d, 
			-d , d, d, 
			d  , d, d, 
			d  , d, -d, 
			0,	1, 0);
		builder.part("left", primitiveType, attr, createDiffuseMaterialFromImage(left)).rect(
			-d, -d, d, 
			-d, d, d, 
			-d, d, -d, 
			-d, -d, -d, 
			-1, 	0, 0);
		builder.part("right", primitiveType, attr, createDiffuseMaterialFromImage(right)).rect(
			d, -d, -d, 
			d, d, -d, 
			d, d, d, 
			d, -d, d, 
			1, 0, 0);
		
		
		ModelInstance mi = new ModelInstance(builder.end());
		
		mi.transform = new Matrix4().translate(
				position.x,
				position.y,
				position.z);
		
		return mi;
	}

}