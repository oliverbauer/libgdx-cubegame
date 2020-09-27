package io.libgdx.cubegame.blocks.types;

import java.util.Arrays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import io.libgdx.cubegame.animation.PlayerAnimation;
import io.libgdx.cubegame.blocks.Block;
import io.libgdx.cubegame.blocks.BlockType;
import io.libgdx.cubegame.blocks.factories.CubeCornerResult;
import io.libgdx.cubegame.blocks.factories.TextureFactory;
import io.libgdx.cubegame.blocks.factories.TileFactory;
import io.libgdx.cubegame.levels.Level;
import io.libgdx.cubegame.player.Player;
import io.libgdx.cubegame.player.PlayerDirection;

/**
 * Elevator between layers.
 * 
 * TODO Improve: move this block including player... old position of this field will be null... elevator should be working in both directions
 */
public class TileElevator extends Block {
	public enum ElevatorDirection {
		UP,
		DOWN
	}
	
	private ElevatorDirection elevatorDirection;
	
	public TileElevator(ElevatorDirection elevatorDirection) {
		this.elevatorDirection = elevatorDirection;
		
		Texture texture3 = TextureFactory.createElevatorTexture(Color.RED);
		Material topMaterial = new Material(TextureAttribute.createDiffuse(texture3));

		Texture texture32 = TextureFactory.createTexture(TileFactory.groundColor);
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
		
		setModel(builder.end());
	}
	
	@Override
	public void playerMovedOn(Level level) {
		Player player = level.getPlayer();
		
		level.field()[player.x][player.y][player.z] = null;
				
		if (elevatorDirection == ElevatorDirection.UP) {
			
			int nextY = y+1;
			while (true) {
				if (level.field()[x][nextY][z] != null && level.field()[x][nextY][z].getType() != null && level.field()[x][nextY][z].getType().equals(BlockType.GROUND)) {
					nextY++;
					break;
				}
				nextY++;
			}
			
			player.setPosition(x, nextY, z);
			player.x = x;
			player.y = nextY; 
			player.z = z;
			level.field()[player.x][player.y][player.z] = player;
			
			// Animation
			level.getPlayer().anim = new PlayerAnimation(
				Arrays.asList(
					new Vector3(x, y + Player.yOffset, z),
					new Vector3(x, y+(nextY-y)/2 + Player.yOffset, z),
					new Vector3(x, nextY + Player.yOffset, z)
				)
			);
		} else {
			int nextY2 = y-1;
			while (true) {
				if (level.field()[x][nextY2][z] != null && level.field()[x][nextY2][z].getType().equals(BlockType.GROUND)) {
					nextY2++;
					break;
				}
				nextY2--;
			}
			
			player.setPosition(x, nextY2, z);
			player.x = x;
			player.y = nextY2; 
			player.z = z;
			
			// Animation
			level.getPlayer().anim = new PlayerAnimation(
				Arrays.asList(
					new Vector3(x, y + Player.yOffset, z),
					new Vector3(x, y-(y-nextY2)/2, z),
					new Vector3(x, nextY2 + Player.yOffset, z)
				)
			);
			
			level.field()[player.x][player.y][player.z] = player;
		}
	}

	/**
	 * Always allowed...
	 */
	@Override
	public boolean allowed(Player player, PlayerDirection direction) {
		return true;
	}
}
