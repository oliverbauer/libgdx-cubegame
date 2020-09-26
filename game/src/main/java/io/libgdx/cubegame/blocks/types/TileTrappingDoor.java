package io.libgdx.cubegame.blocks.types;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import io.libgdx.cubegame.blocks.Block;
import io.libgdx.cubegame.blocks.factories.CubeCornerResult;
import io.libgdx.cubegame.levels.Level;
import io.libgdx.cubegame.player.Player;
import io.libgdx.cubegame.player.PlayerDirection;

/**
 * Opens/closes a trap door on a different position
 */

public class TileTrappingDoor extends Block {
	List<java.awt.Color> awtcolors;
	public TileTrappingDoor(Color c) {
		this.color = c;
		
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = .9f;

		ModelBuilder builder = new ModelBuilder();
		builder.begin();

		float width = 0.9f;
		float height = 0.1f;
		float depth = 0.9f;
		List<Color> colors = Arrays.asList(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.BLACK, Color.ORANGE);
		awtcolors = Arrays.asList(java.awt.Color.RED, java.awt.Color.BLUE, java.awt.Color.YELLOW, java.awt.Color.GREEN, java.awt.Color.BLACK, java.awt.Color.ORANGE);

		float transparency = 0.5f;
		new CubeCornerResult(0,0,0,width,height,depth)
			.finialize(builder, colors.toArray(new Color[6]), awtcolors.toArray(new java.awt.Color[6]), transparency);
		
		setModel(builder.end());
		updateMaterial();
	}
	
	private void updateMaterial() {
		for (int i=0; i<=5; i++) {
			getInstance().materials.get(i).set(TextureAttribute.createDiffuse(createTextureWithText(awtcolors.get(i), "TT", 40)));
		}
	}
	
	@Override
	public void playerMovedOn(Level level) {
		// TODO Use Block.anim
		// TODO Make configurable
		// TODO Allow multiple positions
		// TODO Use +90 (open) saand -90 (close again)
		level.field()[x][y][z+3].getInstance().transform.rotate(Vector3.X, 90);
		super.playerMovedOn(level);
	}

	@Override
	public boolean allowed(Player player, PlayerDirection direction) {
		return true;
	}
}
