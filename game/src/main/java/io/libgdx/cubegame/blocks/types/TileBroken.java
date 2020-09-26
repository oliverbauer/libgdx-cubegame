package io.libgdx.cubegame.blocks.types;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import io.libgdx.cubegame.blocks.Block;
import io.libgdx.cubegame.blocks.factories.CubeCornerResult;
import io.libgdx.cubegame.levels.Level;
import io.libgdx.cubegame.player.Player;
import io.libgdx.cubegame.player.PlayerDirection;

/**
 * A block which destroys itself after n times someone moves over it
 * (only Player?)
 */
public class TileBroken extends Block {
	private int num = 3;

	List<java.awt.Color> awtcolors;
	
	public TileBroken() {
		color = Color.BLACK;
		
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
			getInstance().materials.get(i).set(TextureAttribute.createDiffuse(createTextureWithText(awtcolors.get(i), String.valueOf(num), 40)));
		}
	}
	
	@Override
	public void playerMovedOn(Level level) {
		num--;
		
		updateMaterial();
		
		if (num == 0) {
			// TODO Should occur on next allowed movement of player, not directly
			level.field()[x][y][z] = null; 
		}
	}

	@Override
	public boolean allowed(Player player, PlayerDirection direction) {
		return true;
	}
}
