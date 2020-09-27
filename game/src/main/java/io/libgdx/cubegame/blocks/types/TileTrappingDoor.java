package io.libgdx.cubegame.blocks.types;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import io.libgdx.cubegame.animation.Animation;
import io.libgdx.cubegame.animation.AnimationCompletedListener;
import io.libgdx.cubegame.animation.EnemyAnimation;
import io.libgdx.cubegame.animation.PlayerAnimation;
import io.libgdx.cubegame.blocks.Block;
import io.libgdx.cubegame.blocks.BlockListener;
import io.libgdx.cubegame.blocks.factories.CubeCornerResult;
import io.libgdx.cubegame.levels.Level;
import io.libgdx.cubegame.player.Player;
import io.libgdx.cubegame.player.PlayerDirection;

public class TileTrappingDoor extends Block implements BlockListener {
	private static final Logger logger = LoggerFactory.getLogger(TileTrappingDoor.class);
	private List<java.awt.Color> awtcolors;
	private Level level;
	
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
	public void enemyMovedOn(Level level) {
		playerMovedOn(level); // identical
	}

	private boolean isClosed = true;
	final int xOffset = -1;
	
	@Override
	public void playerMovedOn(Level level) {
		this.level = level;
		// TODO Allow multiple positions and make positions configurable
		// TODO How to show which position is the door?
		// TODO Play sound
		
		level.field()[x + xOffset][y][z+3].getListeningBlocks().add(this);
		
		if (isClosed) {
			// open
			level.field()[x + xOffset][y][z+3].anim = new Animation() {
				private float alpha = 0;
				private float speed = 1f;
				private float fromAngle = 0;
				private float toAngle = 90;
				
				@Override
				public void update(Block block) {
					final float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f);
					alpha += delta * speed;
					
					if (alpha > 1) {
						block.anim = null;
						return;
					}
					
					float angle = fromAngle + alpha * (toAngle - fromAngle);
					
					Vector3 tmpV = new Vector3();
					tmpV.set(
						new Vector3(x + xOffset, y, z+3)
					).lerp(
						new Vector3(x + xOffset, y - 0.5f, z+3 - 0.5f),
						alpha);
				
					block.getInstance().transform.setToRotation(Vector3.X, angle);
					block.getInstance().transform.setTranslation(tmpV);					
				}
			};
		} else {
			// close animation
			level.field()[x + xOffset][y][z+3].anim = new Animation() {
				private float alpha = 0;
				private float speed = 1f;
				private float fromAngle = 90;
				private float toAngle = 0;
				
				@Override
				public void update(Block block) {
					final float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f);
					alpha += delta * speed;
					
					if (alpha > 1) {
						block.anim = null;
						return;
					}
					
					float angle = fromAngle + alpha * (toAngle - fromAngle);
					
					Vector3 tmpV = new Vector3();
					tmpV.set(
						new Vector3(x + xOffset, y - 0.5f, z+3 -0.5f)
					).lerp(
						new Vector3(x + xOffset, y, z+3),
						alpha);
				
					block.getInstance().transform.setToRotation(Vector3.X, angle);
					block.getInstance().transform.setTranslation(tmpV);					
				}
			};
		}
		isClosed = !isClosed;
		
		super.playerMovedOn(level);
	}

	@Override
	public void playerMovedOnBlock(Block block) {
		logger.info(" player moved on block that is references by me "+block);
		if (!isClosed) {
			level.getPlayer().anim = new PlayerAnimation(
				Arrays.asList(
					level.getPlayer().getPosition(),
					new Vector3(x + xOffset, y - 4, z+3),
					new Vector3(x + xOffset, y - 8, z+3)
				)
			);
			
			AnimationCompletedListener completed = new AnimationCompletedListener() {
				@Override
				public void animationCompleted() {
					level.setFailed(true);
				}
			};
			level.getPlayer().anim.addAnimationCompletedListener(completed);
		}
	}
	
	@Override
	public void enemyMovedOnBlock(Block block, EnemyAnimation enemy) {
		logger.info(" enemy moved on block that is references by me "+block);
		if (!isClosed) {
			block.anim = null;
			
			enemy.destroyEnemy();
			
			// Convention: Player-Block-Pos should always be one above its start position
			level.field()[(int)enemy.start.x][(int)enemy.start.y + 1][(int)enemy.start.z] = null;
		}
	}
	
	@Override
	public boolean allowed(Player player, PlayerDirection direction) {
		return true;
	}
}
