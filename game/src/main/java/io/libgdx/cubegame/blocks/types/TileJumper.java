package io.libgdx.cubegame.blocks.types;

import java.util.Arrays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import io.libgdx.cubegame.animation.PlayerAnimation;
import io.libgdx.cubegame.blocks.Block;
import io.libgdx.cubegame.blocks.factories.CubeCornerResult;
import io.libgdx.cubegame.blocks.factories.TextureFactory;
import io.libgdx.cubegame.levels.Level;
import io.libgdx.cubegame.player.Player;
import io.libgdx.cubegame.player.PlayerDirection;

/**
 * Jump over a null field. Currently only <b>one</b> null field is supported
 */
public class TileJumper extends Block {
	public enum JumpDirection {
		RIGHT,
		LEFT,
		FORWARD,
		BACKWARD
	}
	
	private JumpDirection jumpDirection;
	
	public TileJumper(Color color, JumpDirection jumpDirection) {
		super.color = color;
		this.jumpDirection = jumpDirection;
		
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
		
		setModel(builder.end());
	}
	
	@Override
	public void playerMovedOn(Level level) {
		int nextXShift = 0;
		int nextZShift = 0;
		
		level.field()[x][y+1][z] = null; // remove player from current position
		
		switch (jumpDirection) {
			case RIGHT:
				nextXShift = 0;
				nextZShift = 2; // TODO XX_Shift: Configurable how many stones will be jumped over...
				break;
			case LEFT:
				nextXShift = 0;
				nextZShift = -2;
				break;
			case BACKWARD:
				nextXShift = 2;
				nextZShift = 0;
				break;
			case FORWARD:
				nextXShift = -2;
				nextZShift = 0;
		}

		level.field()[x+nextXShift][y+1][z+nextZShift] = level.getPlayer();
		level.field()[x+nextXShift][y+1][z+nextZShift].setPosition(x+nextXShift, y+1, z+nextZShift);
		level.field()[x+nextXShift][y+1][z+nextZShift].x = x+nextXShift;
		level.field()[x+nextXShift][y+1][z+nextZShift].y = y+1;
		level.field()[x+nextXShift][y+1][z+nextZShift].z = z+nextZShift;
		
		level.getPlayer().x = x+nextXShift;
		level.getPlayer().y = y+1;
		level.getPlayer().z = z+nextZShift;
		
		// Animation "jump"
		level.getPlayer().anim = new PlayerAnimation(
			Arrays.asList(
				new Vector3(x, y+1 + Player.yOffset, z),
				new Vector3(x+nextXShift/2, y+2 + Player.yOffset, z+nextZShift/2),
				new Vector3(x+nextXShift, y+1 + Player.yOffset, z+nextZShift)
			)
		);
	}

	@Override
	public boolean allowed(Player player, PlayerDirection direction) {
		if (direction == PlayerDirection.FORWARD && jumpDirection == JumpDirection.FORWARD) {
			return color.equals(player.right);
		} else if (direction == PlayerDirection.RIGHT && jumpDirection == JumpDirection.RIGHT) {
			return color.equals(player.front);
		} else if (direction == PlayerDirection.BACK && jumpDirection == JumpDirection.BACKWARD) {
			return color.equals(player.left);
		} else if (direction == PlayerDirection.LEFT && jumpDirection == JumpDirection.LEFT) {
			return color.equals(player.back);
		}
		
		return false;
	}
}
