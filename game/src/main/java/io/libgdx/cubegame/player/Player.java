package io.libgdx.cubegame.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import io.libgdx.cubegame.animation.Animation;
import io.libgdx.cubegame.animation.PlayerAnimation;
import io.libgdx.cubegame.blocks.Block;
import io.libgdx.cubegame.blocks.BlockType;
import io.libgdx.cubegame.blocks.factories.TextureFactory;

public class Player extends Block {
	private static final Logger logger = LoggerFactory.getLogger(Player.class);

	/**
	 * For jumping over places see {@link PlayerAnimation}.
	 * For jumping on up if player did not press w/a/s/d see {@link CubeApp#render()}
	 */
	public Animation anim;
	
	public boolean isMoving = false;

	public Color front = Color.BLACK;
	public Color right = Color.YELLOW;
	public Color bottom = Color.BLUE;
	public Color left = Color.RED;
	public Color top = Color.GREEN;
	public Color back = Color.CYAN;

	// From warpstrone-libgdx-mashup-master
	private float lightPosition = 0;
	public Vector3 lightCenter = new Vector3(3f, 3f, 3f);
	public float radiusA = 1f;
	public float radiusB = 1f;
	PointLight pointLight; 

	public Player() {
		// TODO Player refactor to remove texture... override from createCube...
		super(TextureFactory.createTexture(Color.BLACK), BlockType.PLAYER);

		createCube();
	}
	
	@Override
	public void render(ModelBatch modelBatch, Environment environment) {
		if (pointLight == null) {
			pointLight = new PointLight();
			pointLight.set(Color.RED, 
					3f, 
					2f,
					3f,
					10f);
			environment.add(pointLight);
		}
		
		lightCenter.set(x, y+1, z);
		pointLight.position.set(x, y+1, z);
		float delta = Gdx.graphics.getDeltaTime();
		
		lightPosition -= delta * 1.0f; // + clockwise, - counter-clockwise

        float lx = (float) (radiusA * Math.cos(lightPosition));
        float ly = (float) (radiusB * Math.sin(lightPosition));
        Vector3 lightVector = new Vector3(lx, 0, ly).add(lightCenter);
        pointLight.position.set(lightVector);
        
        super.render(modelBatch, environment);
	}

	/*
	 * If a cube want to move to the right it is allowed in the followingn situations
	 * * right is a empty ground/floor
	 * * right is a point on ground which has the same color as "rightColor) (e.g. move with red on red, not with red on green)
	 * * right is a elevator (move on y-axis up or down)
	 * 
	 * It is not allowed when
	 * * right is a null-state (e.h. hole that kills the user)
	 * * right is a 
	 */
	/**
	 * Inspired by https://stackoverflow.com/questions/56764195/rotate-cube-and-track-its-sides
	 * @param rot
	 */
	public void rotate(PlayerDirection rot) {
		if (rot == PlayerDirection.LEFT) {
			Color temp = top;
			top = back;
			back = bottom;
			bottom = front;
			front = temp;
		} else if (rot == PlayerDirection.RIGHT) {
			Color temp = front;
			front = bottom;
			bottom = back;
			back = top;
			top = temp;
		} else if (rot == PlayerDirection.FORWARD) {
			Color temp = bottom;
			bottom = left;
			left = top;
			top = right;
			right = temp;
		} else if (rot == PlayerDirection.BACK) {
			Color temp = right;
			right = top;
			top = left;
			left = bottom;
			bottom = temp;
		}

		createCube();
	}

	private void createCube() {
		ModelBuilder modelBuilder = new ModelBuilder();
		float d = 0.5f;
		int attr = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
				| VertexAttributes.Usage.TextureCoordinates;

		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = 1.0f;
		modelBuilder.begin();
		modelBuilder
				.part("front", GL20.GL_TRIANGLES, attr,
						new Material(blendingAttribute, ColorAttribute.createDiffuse(front)))
				.rect(-d, -d, -d, -d, d, -d, d, d, -d, d, -d, -d, 0, 0, -1);
		modelBuilder
				.part("back", GL20.GL_TRIANGLES, attr,
						new Material(blendingAttribute, ColorAttribute.createDiffuse(back)))
				.rect(-d, d, d, -d, -d, d, d, -d, d, d, d, d, 0, 0, 1);
		modelBuilder
				.part("bottom", GL20.GL_TRIANGLES, attr,
						new Material(blendingAttribute, ColorAttribute.createDiffuse(bottom)))
				.rect(-d, -d, d, -d, -d, -d, d, -d, -d, d, -d, d, 0, -1, 0);
		modelBuilder
				.part("top", GL20.GL_TRIANGLES, attr,
						new Material(blendingAttribute, ColorAttribute.createDiffuse(top)))
				.rect(-d, d, -d, -d, d, d, d, d, d, d, d, -d, 0, 1, 0);
		modelBuilder
				.part("left", GL20.GL_TRIANGLES, attr,
						new Material(blendingAttribute, ColorAttribute.createDiffuse(left)))
				.rect(-d, -d, d, -d, d, d, -d, d, -d, -d, -d, -d, -1, 0, 0);
		modelBuilder
				.part("right", GL20.GL_TRIANGLES, attr,
						new Material(blendingAttribute, ColorAttribute.createDiffuse(right)))
				.rect(d, -d, -d, d, d, -d, d, d, d, d, -d, d, 1, 0, 0);
		
		Model cube = modelBuilder.end();

		setModel(cube);
		setPosition(x, y, z);

		if (logger.isDebugEnabled()) {
			logger.debug("Cube with top: {}", logColor(top));
		}
	}
	
	public String logColor(Color c) {
		if (c == Color.BLACK) {
			return "BLACK";
		} else if (c == Color.CYAN) {
			return "CYAN";
		} else if (c == Color.BLUE) {
			return "BLUE";
		} else if (c == Color.GREEN) {
			return "GREEN";
		} else if (c == Color.RED) {
			return "RED";
		} else if (c == Color.YELLOW) {
			return "YELLOW";
		} else {
			throw new RuntimeException("Colors not synchronize...");
		}
	}
}
