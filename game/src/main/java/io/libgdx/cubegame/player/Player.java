package io.libgdx.cubegame.player;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import io.libgdx.cubegame.animation.Animation;
import io.libgdx.cubegame.animation.PlayerAnimation;
import io.libgdx.cubegame.blocks.Block;
import io.libgdx.cubegame.blocks.BlockType;
import io.libgdx.cubegame.blocks.factories.CubeCornerResult;

public class Player extends Block {
	/**
	 * For jumping over places see {@link PlayerAnimation}.
	 * For jumping on up if player did not press w/a/s/d see {@link CubeApp#render()}
	 */
	public Animation anim;
	
	public boolean isMoving = false;

	// Initial colors... when player moves, those will be "switched"
	public Color front = Color.BLACK;
	public Color right = Color.YELLOW;
	public Color bottom = Color.BLUE;
	public Color left = Color.RED;
	public Color top = Color.GREEN;
	public Color back = Color.CYAN;
	
	private java.awt.Color frontAWT = java.awt.Color.BLACK;
	private java.awt.Color rightAWT = java.awt.Color.YELLOW;
	private java.awt.Color bottomAWT = java.awt.Color.BLUE;
	private java.awt.Color leftAWT = java.awt.Color.RED;
	private java.awt.Color topAWT = java.awt.Color.GREEN;
	private java.awt.Color backAWT = java.awt.Color.CYAN;

	// Idea from warpstrone-libgdx-mashup-master
	private float lightPosition = 0;
	private Vector3 lightCenter = new Vector3(3f, 3f, 3f);
	private float radiusA = 1f;
	private float radiusB = 1f;
	private PointLight pointLight; 
	
	public static float xOffset = 0f;
	public static float yOffset = -0.5f;
	public static float zOffset = 0f;

	public Player(Model model, BlockType type) {
		create();
		setType(type);
		instance.transform = new Matrix4().translate(getPosition());
	}
	
	@Override
	public Vector3 getPosition() {
		return new Vector3(x + xOffset, y + yOffset, z + zOffset);
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
			
			java.awt.Color tempAWT = topAWT;
			topAWT = backAWT;
			backAWT = bottomAWT;
			bottomAWT = frontAWT;
			frontAWT = tempAWT;
		} else if (rot == PlayerDirection.RIGHT) {
			Color temp = front;
			front = bottom;
			bottom = back;
			back = top;
			top = temp;
			
			java.awt.Color tempAWT = frontAWT;
			frontAWT = bottomAWT;
			bottomAWT = backAWT;
			backAWT = topAWT;
			topAWT = tempAWT;

		} else if (rot == PlayerDirection.FORWARD) {
			Color temp = bottom;
			bottom = left;
			left = top;
			top = right;
			right = temp;
			
			java.awt.Color tempAWT = bottomAWT;
			bottomAWT = leftAWT;
			leftAWT = topAWT;
			topAWT = rightAWT;
			rightAWT = tempAWT;
		} else if (rot == PlayerDirection.BACK) {
			Color temp = right;
			right = top;
			top = left;
			left = bottom;
			bottom = temp;
			
			java.awt.Color tempAWT = rightAWT;
			rightAWT = topAWT;
			topAWT = leftAWT;
			leftAWT = bottomAWT;
			bottomAWT = tempAWT;
		}

		create();
		
		instance.transform = new Matrix4().translate(getPosition());
	}
	
	private void create() {
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = .5f;

		ModelBuilder builder = new ModelBuilder();
		builder.begin();


		float width = 0.8f;
		float height = 0.8f;
		float depth = 0.8f;
		float size = 0.9f;
		
		float min = 0.1f;
		float max = 0.7f;
		
		boolean innerCubeBlack = false;
		
		List<java.awt.Color>  awtBlack = Arrays.asList(java.awt.Color.BLACK, java.awt.Color.BLACK, java.awt.Color.BLACK, java.awt.Color.BLACK, java.awt.Color.BLACK, java.awt.Color.BLACK);
		List<java.awt.Color>  awtCube = Arrays.asList(frontAWT, backAWT, bottomAWT, topAWT, leftAWT, rightAWT);
		List<Color> colorListCube = Arrays.asList(front, back, bottom, top, left, right);
		List<Color> colorListCubeBlack = Arrays.asList(Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK);

		
		java.awt.Color[] colorsAWTBlack = awtBlack.toArray(new java.awt.Color[6]);
		java.awt.Color[] colorsAWTCube = awtCube.toArray(new java.awt.Color[6]);
		Color[] colorsCube = colorListCube.toArray(new Color[6]);
		Color[] blackCube = colorListCubeBlack.toArray(new Color[6]);

		java.awt.Color[] awtColors;
		Color[] gdxColors;
		
		if (innerCubeBlack) {
			awtColors = colorsAWTBlack;
			gdxColors = blackCube;
		} else {
			awtColors = colorsAWTCube;
			gdxColors = colorsCube;
		}
		
		float transparency = 1f;
		
		new CubeCornerResult(0,0,0,size,size,size).finialize(builder, gdxColors, awtColors, transparency);
		
		width = min;
		height = max;
		depth = max;

		if (innerCubeBlack) {
			awtColors = colorsAWTCube;
			gdxColors = colorsCube;
		} else {
			awtColors = colorsAWTBlack;
			gdxColors = blackCube;
		}
		
		new CubeCornerResult(0+size/2,0,0,width,height,depth).finialize(builder, gdxColors, awtColors, transparency);
		new CubeCornerResult(0-size/2,0,0,width,height,depth).finialize(builder, gdxColors, awtColors, transparency);
		
		width = max;
		height = min;
		depth = max;
		new CubeCornerResult(0,0+size/2,0,width,height,depth).finialize(builder, gdxColors, awtColors, transparency);
		new CubeCornerResult(0,0-size/2,0,width,height,depth).finialize(builder, gdxColors, awtColors, transparency);

		width = max;
		height = max;
		depth = min;
		new CubeCornerResult(0,0,0+size/2,width,height,depth).finialize(builder, gdxColors, awtColors, transparency);
		new CubeCornerResult(0,0,0-size/2,width,height,depth).finialize(builder, gdxColors, awtColors, transparency);

		Model cube = builder.end();
		
		
		
		setModel(cube);
		setPosition(x, y, z);
		
		if (innerCubeBlack) {
			instance.materials.get(0).set(TextureAttribute.createDiffuse(createTextureWithText(java.awt.Color.BLACK, "", 20)));
			instance.materials.get(1).set(TextureAttribute.createDiffuse(createTextureWithText(java.awt.Color.BLACK, "", 20)));
			instance.materials.get(2).set(TextureAttribute.createDiffuse(createTextureWithText(java.awt.Color.BLACK, "", 20)));
			instance.materials.get(3).set(TextureAttribute.createDiffuse(createTextureWithText(java.awt.Color.BLACK, "", 20)));
			instance.materials.get(4).set(TextureAttribute.createDiffuse(createTextureWithText(java.awt.Color.BLACK, "", 20)));
			instance.materials.get(5).set(TextureAttribute.createDiffuse(createTextureWithText(java.awt.Color.BLACK, "", 20)));
		} else {
			instance.materials.get(0).set(TextureAttribute.createDiffuse(createTextureWithText(frontAWT, "", 20)));
			instance.materials.get(1).set(TextureAttribute.createDiffuse(createTextureWithText(backAWT, "", 20)));
			instance.materials.get(2).set(TextureAttribute.createDiffuse(createTextureWithText(bottomAWT, "", 20)));
			instance.materials.get(3).set(TextureAttribute.createDiffuse(createTextureWithText(topAWT, "", 20)));
			instance.materials.get(4).set(TextureAttribute.createDiffuse(createTextureWithText(leftAWT, "", 20)));
			instance.materials.get(5).set(TextureAttribute.createDiffuse(createTextureWithText(rightAWT, "", 20)));
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
