package io.libgdx.cubegame.blocks.factories;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import io.libgdx.cubegame.blocks.Block;
import io.libgdx.cubegame.blocks.BlockType;

public class TileFactory {
	private static final Color groundColor = Color.BLACK;
	
	private static final BlendingAttribute blendingAttribute = new BlendingAttribute(.9f);
	
	public static Block createGround(int x, int y, int z) {
		return createGround(groundColor, BlockType.GROUND, x, y, z);
	}

	public static Block createElevator(BlockType type, int x, int y, int z) {
		Texture texture3 = TextureFactory.createElevatorTexture(Color.RED);
		Material topMaterial = new Material(TextureAttribute.createDiffuse(texture3));

		Texture texture32 = TextureFactory.createTexture(groundColor);
		Material otherMaterial = new Material(TextureAttribute.createDiffuse(texture32));

//		return createBlock(topMaterial, otherMaterial, type, Color.BLACK, x, y, z);
		
		
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = .9f;

		ModelBuilder builder = new ModelBuilder();
		builder.begin();

		float width = 0.9f;
		float height = 0.1f;
		float depth = 0.9f;

		Material materials[] = 
			Arrays.asList(otherMaterial,otherMaterial,otherMaterial,topMaterial,otherMaterial,otherMaterial).toArray(new Material[6]);
		
		float transparency = 0.5f;
		new CubeCornerResult(0,0,0,width,height,depth)
			.finialize(builder, materials, transparency);

		Block block =  new Block();
		block.setModel(builder.end());
		block.setType(type);
		block.color = Color.BLACK; // will be ignored
		block.x = x;
		block.y = y;
		block.z = z;
		block.setPosition(x, y, z);
		return block;
	}

	private static Block createGround(Color color, BlockType type, int x, int y, int z) {
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = .9f;

		ModelBuilder builder = new ModelBuilder();
		builder.begin();

		float width = 0.9f;
		float height = 0.1f;
		float depth = 0.9f;
		List<Color> colors = Arrays.asList(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.BLACK, Color.ORANGE);
		List<java.awt.Color> awtcolors = Arrays.asList(java.awt.Color.RED, java.awt.Color.BLUE, java.awt.Color.YELLOW, java.awt.Color.GREEN, java.awt.Color.BLACK, java.awt.Color.ORANGE);

		float transparency = 0.5f;
		new CubeCornerResult(0,0,0,width,height,depth)
			.finialize(builder, colors.toArray(new Color[6]), awtcolors.toArray(new java.awt.Color[6]), transparency);

		Block block =  new Block();
		block.setModel(builder.end());
		block.setType(type);
		block.color = color;
		block.x = x;
		block.y = y;
		block.z = z;
		block.setPosition(x, y, z);
		return block;
	}
	
	/**
	 * Only for lifeforces!
	 */
	public static Block createLifeforce(Color color, int x, int y, int z) {
		Texture texture3 = TextureFactory.createLifeforceTexture(color);
		
		ColorAttribute colorAttribute = ColorAttribute.createEmissive(color);
		TextureAttribute textureAttribute = TextureAttribute.createDiffuse(texture3);
		
		Material topMaterial = new Material(colorAttribute, textureAttribute);

		Texture texture = TextureFactory.createTexture(color);
		Material otherMaterial = new Material(TextureAttribute.createDiffuse(texture));
		
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = .9f;

		ModelBuilder builder = new ModelBuilder();
		builder.begin();

		float width = 0.9f;
		float height = 0.1f;
		float depth = 0.9f;

		Material materials[] = 
			Arrays.asList(otherMaterial,otherMaterial,otherMaterial,topMaterial,otherMaterial,otherMaterial).toArray(new Material[6]);
		
		float transparency = 0.5f;
		new CubeCornerResult(0,0,0,width,height,depth)
			.finialize(builder, materials, transparency);

		Block block =  new Block();
		block.setModel(builder.end());
		block.setType(BlockType.LIFEFORCE);
		block.color = color;
		block.x = x;
		block.y = y;
		block.z = z;
		block.setPosition(x, y, z);
		return block;
	}


	public static Block createPoint(Color color, int x, int y, int z) {
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = .9f;

		ModelBuilder builder = new ModelBuilder();
		builder.begin();

		float width = 0.9f;
		float height = 0.1f;
		float depth = 0.9f;
		List<Color> colors = Arrays.asList(color, color, color, color, color, color);
		
		java.awt.Color awtColor = TextureFactory.toAWTColor(color);
		
		List<java.awt.Color> awtcolors = Arrays.asList(awtColor,awtColor,awtColor,awtColor,awtColor,awtColor);

//		float transparency = 0.5f;
		float transparency = 1f;
		new CubeCornerResult(0,0,0,width,height,depth)
			.finialize(builder, colors.toArray(new Color[6]), awtcolors.toArray(new java.awt.Color[6]), transparency);

		Block block =  new Block();
		block.setModel(builder.end());
		block.setType(BlockType.POINT);
		block.color = color;
		block.x = x;
		block.y = y;
		block.z = z;
		block.setPosition(x, y, z);
		
		block.getInstance().materials.get(0).set(TextureAttribute.createDiffuse(block.createTextureWithText(awtColor, "", 20)));
		block.getInstance().materials.get(1).set(TextureAttribute.createDiffuse(block.createTextureWithText(awtColor, "", 20)));
		block.getInstance().materials.get(2).set(TextureAttribute.createDiffuse(block.createTextureWithText(awtColor, "", 20)));
		block.getInstance().materials.get(3).set(TextureAttribute.createDiffuse(block.createTextureWithText(awtColor, "", 20)));
		block.getInstance().materials.get(4).set(TextureAttribute.createDiffuse(block.createTextureWithText(awtColor, "", 20)));
		block.getInstance().materials.get(5).set(TextureAttribute.createDiffuse(block.createTextureWithText(awtColor, "", 20)));
		
		
		return block;
	}

	// TODO wie Player
	public static Block createEnemy(Color color, int x, int y, int z) {
		Texture texture = TextureFactory.createTexture(color);
		
		Material material = new Material(
			blendingAttribute, 
			ColorAttribute.createDiffuse(color),
			TextureAttribute.createDiffuse(texture)
		);

		return createBlock(material, material, BlockType.ENEMY, color, x,y,z);
	}
	
	public static Block createJumper(Color color, BlockType type, int x, int y, int z) {
		Texture texture3 = TextureFactory.createJumperTexture(color);
		
		Material topMaterial = new Material(ColorAttribute.createEmissive(color),
				TextureAttribute.createDiffuse(texture3));

		Texture texture32 = TextureFactory.createTexture(color);
		Material otherMaterial = new Material(TextureAttribute.createDiffuse(texture32));

		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = .9f;

		ModelBuilder builder = new ModelBuilder();
		builder.begin();

		float width = 0.9f;
		float height = 0.1f;
		float depth = 0.9f;

		Material materials[] = 
			Arrays.asList(otherMaterial,otherMaterial,otherMaterial,topMaterial,otherMaterial,otherMaterial).toArray(new Material[6]);
		
		float transparency = 0.5f;
		new CubeCornerResult(0,0,0,width,height,depth)
			.finialize(builder, materials, transparency);

		Block block =  new Block();
		block.setModel(builder.end());
		block.setType(type);
		block.color = color;
		block.x = x;
		block.y = y;
		block.z = z;
		block.setPosition(x, y, z);
		return block;
	}

	private static Block createBlock(Material materialTop, Material other, BlockType type, Color color, int x, int y, int z) {
		ModelBuilder modelBuilder = new ModelBuilder();
		float d = 0.5f;
		int attr = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
				| VertexAttributes.Usage.TextureCoordinates;

		modelBuilder.begin();

		modelBuilder.part("front", GL20.GL_TRIANGLES, attr, other)
			.rect(
					-d, -d, -d, 
					-d, d, -d, 
					d, d, -d,
					d, -d, -d, 
					0, 0, -1);
		modelBuilder.part("back", GL20.GL_TRIANGLES, attr, other).rect(
				-d, d, d, 
				-d, -d, d, 
				d, -d, d, 
				d, d, d, 
				0, 0, 1);
		modelBuilder.part("bottom", GL20.GL_TRIANGLES, attr, other).rect(
				-d, -d, d, 
				-d, -d, -d,
				d, -d, -d,
				d, -d, d, 
				0, -1, 0);
		modelBuilder.part("top", GL20.GL_TRIANGLES, attr, materialTop).rect(
				-d , d, -d, 
				-d , d, d, 
				d  , d, d, 
				d  , d, -d, 
				0,	1, 0);
		modelBuilder.part("left", GL20.GL_TRIANGLES, attr, other).rect(
				-d, -d, d, 
				-d, d, d, 
				-d, d, -d, 
				-d, -d, -d, 
				-1, 	0, 0);
		modelBuilder.part("right", GL20.GL_TRIANGLES, attr, other).rect(
				d, -d, -d, 
				d, d, -d, 
				d, d, d, 
				d, -d, d, 
				1, 0, 0);

		Block block =  new Block();
		block.setModel(modelBuilder.end());
		block.setType(type);
		block.color = color;
		block.x = x;
		block.y = y;
		block.z = z;
		block.setPosition(x, y, z);
		return block;
	}
}
