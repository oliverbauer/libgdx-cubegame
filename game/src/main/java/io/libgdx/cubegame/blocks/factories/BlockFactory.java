package io.libgdx.cubegame.blocks.factories;

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

public class BlockFactory {
	private static final Color groundColor = Color.BLUE;
	
	private static final BlendingAttribute blendingAttribute = new BlendingAttribute(.3f);
	
	public static Block createGround(int x, int y, int z) {
		return createGround(groundColor, BlockType.GROUND, x, y, z);
	}

	public static Block createElevator(BlockType type, int x, int y, int z) {
		Texture texture3 = TextureFactory.createElevatorTexture(Color.RED);
		Material topMaterial = new Material(TextureAttribute.createDiffuse(texture3));

		Texture texture32 = TextureFactory.createTexture(groundColor);
		Material otherMaterial = new Material(TextureAttribute.createDiffuse(texture32));

		return createBlock(topMaterial, otherMaterial, type, Color.BLACK, x, y, z);
	}

	/**
	 * Only for Ground!
	 */
	private static Block createGround(Color color, BlockType type, int x, int y, int z) {
		Texture texture = TextureFactory.createTextureWithBorder(color, java.awt.Color.YELLOW);
		
		ColorAttribute colorAttribute = ColorAttribute.createEmissive(color);
		TextureAttribute textureAttribute = TextureAttribute.createDiffuse(texture);
		
		Material material = new Material(blendingAttribute, colorAttribute, textureAttribute);

		return createBlock(material, material, type, color, x, y, z);
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

		return createBlock(topMaterial, otherMaterial, BlockType.LIFEFORCE, color, x, y, z);
	}


	public static Block createPoint(Color color, int x, int y, int z) {
		ColorAttribute colorAttribute = ColorAttribute.createDiffuse(color);
		
		Material topMaterial = new Material(colorAttribute);

		Texture texture32 = TextureFactory.createTexture(color);
		Material otherMaterial = new Material(TextureAttribute.createDiffuse(texture32));

		return createBlock(topMaterial, otherMaterial, BlockType.POINT, color, x,y,z);
	}

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

		return createBlock(topMaterial, otherMaterial, type, color, x,y,z);
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

		Block block =  new Block(modelBuilder.end(), type);
		block.color = color;
		block.x = x;
		block.y = y;
		block.z = z;
		block.setPosition(x, y, z);
		return block;
	}
}
