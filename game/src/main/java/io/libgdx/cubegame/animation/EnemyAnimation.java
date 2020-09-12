package io.libgdx.cubegame.animation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import io.libgdx.cubegame.blocks.Block;
import io.libgdx.cubegame.blocks.BlockType;
import io.libgdx.cubegame.blocks.factories.BlockFactory;
import io.libgdx.cubegame.screens.GameScreen;

public class EnemyAnimation implements Animation {
	private static final Logger logger = LoggerFactory.getLogger(EnemyAnimation.class);
	private GameScreen cubeApp;
	
	public EnemyAnimation(GameScreen cubeApp) {
		this.cubeApp = cubeApp;
	}
	
	private float alpha = 0;
	private float speed = 5f;
	public Vector3 start;
	public Vector3 end;
	public Block[][][] field;
	
	private int index = 0;
	private List<Vector3> vectors = new ArrayList<>();
	
	public void add(Vector3 vector) {
		vectors.add(vector);
	}

	@Override
	public void update(Block block) {
		final float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f);
		alpha += delta * speed;
		
		if (alpha > 1) {
			alpha = 0; // repeat...
			
			index = index % vectors.size();
			
			start = end;
			end = vectors.get(index);
			
			Block block2 = field[(int)start.x][(int)start.y][(int)start.z];
			if (block2 != null && block2.getType() == BlockType.PLAYER) {
				logger.info("Same layer: AI wins;-)!"); 
				// Enemy 1 Player 0;-)
				cubeApp.setFailed(true);
			} 
			Block block3 = field[(int)start.x][(int)start.y - 1][(int)start.z];
			if (block3 != null) {
				if (block3.getType() == BlockType.POINT || block3.getType() == BlockType.LIFEFORCE) {
					int x = (int)start.x;
					int y = (int)start.y;
					int z = (int)start.z;
					field[x][y-1][z].dispose(); // cleanup last block
					
					field[x][y-1][z] = BlockFactory.createGround(x, y-1, z);
				}
			}
			
			index++;
			index = index % vectors.size();
			return;
		}
		
		float fromAngle = 0;
		float toAngle = 90;
		
		Vector3 tmpV = new Vector3();
		Vector3 axis;
		if (start.x == end.x) {
			axis = Vector3.X;
			if (start.z > end.z) {
				toAngle = -90;
			}
		} else {
			axis = Vector3.Z;
			if (start.x < end.x) {
				toAngle = -90;
			}
		}
			
		float angle = fromAngle + alpha * (toAngle - fromAngle);
		
		tmpV.set(start).lerp(end, alpha);
		
		block.getInstance().transform.setToRotation(axis, angle);
		block.getInstance().transform.setTranslation(tmpV);
	}
}
