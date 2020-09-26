package io.libgdx.cubegame.blocks.types;

import java.util.Arrays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import io.libgdx.cubegame.assets.Assets;
import io.libgdx.cubegame.blocks.Block;
import io.libgdx.cubegame.blocks.factories.CubeCornerResult;
import io.libgdx.cubegame.blocks.factories.TextureFactory;
import io.libgdx.cubegame.blocks.factories.TileFactory;
import io.libgdx.cubegame.levels.Level;
import io.libgdx.cubegame.player.Player;
import io.libgdx.cubegame.player.PlayerDirection;
import io.libgdx.cubegame.score.Score;

public class TileLifeforce extends Block {
	
	public TileLifeforce(Color color) {
		this.color = color;
		
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
		
		setModel(builder.end());
	}
	
	@Override
	public void enemyMovedOn(Level level) {
		// cleanup
		dispose(); 
		
		level.field()[x][y][z] = TileFactory.createGround(x, y, z);
	}
	
	@Override
	public void playerMovedOn(Level level) {
		// cleanup
		dispose(); 
		
		level.field()[x][y][z] = TileFactory.createGround(x, y, z);
		
		if (Assets.instance().soundLifeforce != null) {
			Assets.instance().soundLifeforce.play();
		}
			
		Score.lifeforces++;
		if (Score.lifeforces >= level.requiredLifeforces()) {
			level.setCompleted(true);
		}
	}
	
	@Override
	public boolean allowed(Player player, PlayerDirection direction) {
		Color lifeforceColor = color;
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
		
		return lifeforceColor.equals(cubeColor);
	}
}
