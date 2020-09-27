package io.libgdx.cubegame.animation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import io.libgdx.cubegame.blocks.Block;
import io.libgdx.cubegame.blocks.BlockListener;
import io.libgdx.cubegame.blocks.BlockType;
import io.libgdx.cubegame.screens.GameScreen;

public class EnemyAnimation implements Animation {
	private static final Logger logger = LoggerFactory.getLogger(EnemyAnimation.class);
	private GameScreen cubeApp;
	
	private int xPos;
	private int yPos;
	private int zPos;
	
	public EnemyAnimation(GameScreen cubeApp, int x, int y, int z) {
		this.cubeApp = cubeApp;
		this.xPos = x;
		this.yPos = y;
		this.zPos = z;
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

	public void destroyEnemy() {
		// TODO Change animation to falling enemy... when finished: set position null
		cubeApp.getLevel().field()[xPos][yPos][zPos] = null;
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
				block3.enemyMovedOn(cubeApp.getLevel());
				
				for (BlockListener listener : block3.getListeningBlocks()) {
					listener.enemyMovedOnBlock(block3, this);
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
		
		Vector3 s = new Vector3();
		s.x = start.x;
		s.y = start.y - 0.45f;
		s.z = start.z;
		Vector3 e = new Vector3();
		e.x = end.x;
		e.y = end.y - 0.45f;
		e.z = end.z;
		tmpV.set(s).lerp(e, alpha);
		
		block.getInstance().transform.setToRotation(axis, angle);
		block.getInstance().transform.setTranslation(tmpV);
	}
}
