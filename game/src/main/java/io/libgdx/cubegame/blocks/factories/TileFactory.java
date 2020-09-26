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
import io.libgdx.cubegame.blocks.types.TileBroken;
import io.libgdx.cubegame.blocks.types.TileElevator;
import io.libgdx.cubegame.blocks.types.TileElevator.ElevatorDirection;
import io.libgdx.cubegame.blocks.types.TileJumper;
import io.libgdx.cubegame.blocks.types.TileLifeforce;
import io.libgdx.cubegame.blocks.types.TilePoint;
import io.libgdx.cubegame.blocks.types.TileTrappingDoor;
import io.libgdx.cubegame.blocks.types.TileJumper.JumpDirection;

public class TileFactory {
	public static final Color groundColor = Color.BLACK;
	
	private static final BlendingAttribute blendingAttribute = new BlendingAttribute(.9f);
	
	public static Block createGround(int x, int y, int z) {
		return createGround(groundColor, BlockType.GROUND, x, y, z);
	}

	public static Block createElevator(ElevatorDirection elevatorDirection, int x, int y, int z) {
		TileElevator block =  new TileElevator(elevatorDirection);
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
	public static Block createBrokenTile(Color color, int x, int y, int z) {
		TileBroken block =  new TileBroken();
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
	public static Block createTileTrappingDoor(Color color, int x, int y, int z) {
		TileTrappingDoor block =  new TileTrappingDoor(color);
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
		TileLifeforce block =  new TileLifeforce(color);
		block.x = x;
		block.y = y;
		block.z = z;
		block.setPosition(x, y, z);
		return block;
	}


	public static Block createPoint(Color color, int x, int y, int z) {
		TilePoint block =  new TilePoint(color);
		block.x = x;
		block.y = y;
		block.z = z;
		block.setPosition(x, y, z);
		
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

		ModelBuilder modelBuilder = new ModelBuilder();
		float d = 0.5f;
		int attr = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
				| VertexAttributes.Usage.TextureCoordinates;

		modelBuilder.begin();

		modelBuilder.part("front", GL20.GL_TRIANGLES, attr, material)
			.rect(
					-d, -d, -d, 
					-d, d, -d, 
					d, d, -d,
					d, -d, -d, 
					0, 0, -1);
		modelBuilder.part("back", GL20.GL_TRIANGLES, attr, material).rect(
				-d, d, d, 
				-d, -d, d, 
				d, -d, d, 
				d, d, d, 
				0, 0, 1);
		modelBuilder.part("bottom", GL20.GL_TRIANGLES, attr, material).rect(
				-d, -d, d, 
				-d, -d, -d,
				d, -d, -d,
				d, -d, d, 
				0, -1, 0);
		modelBuilder.part("top", GL20.GL_TRIANGLES, attr, material).rect(
				-d , d, -d, 
				-d , d, d, 
				d  , d, d, 
				d  , d, -d, 
				0,	1, 0);
		modelBuilder.part("left", GL20.GL_TRIANGLES, attr, material).rect(
				-d, -d, d, 
				-d, d, d, 
				-d, d, -d, 
				-d, -d, -d, 
				-1, 	0, 0);
		modelBuilder.part("right", GL20.GL_TRIANGLES, attr, material).rect(
				d, -d, -d, 
				d, d, -d, 
				d, d, d, 
				d, -d, d, 
				1, 0, 0);

		Block block =  new Block();
		block.setModel(modelBuilder.end());
		block.setType(BlockType.ENEMY);
		block.color = color;
		block.x = x;
		block.y = y;
		block.z = z;
		block.setPosition(x, y, z);
		return block;
	}
	
	public static Block createJumper(Color color, JumpDirection direction, int x, int y, int z) {
		TileJumper block =  new TileJumper(color, direction);
		
		block.color = color;
		block.x = x;
		block.y = y;
		block.z = z;
		block.setPosition(x, y, z);
		return block;
	}
}
