package io.libgdx.cubegame.blocks.types;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import io.libgdx.cubegame.blocks.Block;
import io.libgdx.cubegame.blocks.factories.CubeCornerResult;
import io.libgdx.cubegame.blocks.factories.TextureFactory;
import io.libgdx.cubegame.blocks.factories.TileFactory;
import io.libgdx.cubegame.levels.Level;
import io.libgdx.cubegame.player.Player;
import io.libgdx.cubegame.player.PlayerDirection;
import io.libgdx.cubegame.score.Score;

public class TilePoint extends Block {
	
	public TilePoint(Color color) {
		this.color = color;
		
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
		
		setModel(builder.end());
		
		getInstance().materials.get(0).set(TextureAttribute.createDiffuse(createTextureWithText(awtColor, "", 20)));
		getInstance().materials.get(1).set(TextureAttribute.createDiffuse(createTextureWithText(awtColor, "", 20)));
		getInstance().materials.get(2).set(TextureAttribute.createDiffuse(createTextureWithText(awtColor, "", 20)));
		getInstance().materials.get(3).set(TextureAttribute.createDiffuse(createTextureWithText(awtColor, "", 20)));
		getInstance().materials.get(4).set(TextureAttribute.createDiffuse(createTextureWithText(awtColor, "", 20)));
		getInstance().materials.get(5).set(TextureAttribute.createDiffuse(createTextureWithText(awtColor, "", 20)));

	}
	
	@Override
	public void playerMovedOn(Level level) {
		// cleanup
		dispose(); 
		
		level.field()[x][y][z] = TileFactory.createGround(x, y, z);
		
		Score.score += 100;
	}
	
	@Override
	public void enemyMovedOn(Level level) {
		// cleanup
		dispose(); 
		
		level.field()[x][y][z] = TileFactory.createGround(x, y, z);
	}

	@Override
	public boolean allowed(Player player, PlayerDirection direction) {
		Color pointColor = color;
		Color cubeColor = null;
		switch (direction) {
			case FORWARD:
				cubeColor = player.left;
				// old-school
				cubeColor = player.right;
				break;
			case BACK:
				cubeColor = player.right;
				// old-school
				cubeColor = player.left;
				break;
			case LEFT:
				cubeColor = player.front;
				// old-school
				cubeColor = player.back;
				break;
			case RIGHT:
				cubeColor = player.back;
				// old-school
				cubeColor = player.front;
				break;
			default:
				break;
		}
		
		return pointColor.equals(cubeColor);
	}
}
