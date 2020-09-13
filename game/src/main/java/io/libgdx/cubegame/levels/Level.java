package io.libgdx.cubegame.levels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import io.libgdx.cubegame.Config;
import io.libgdx.cubegame.animation.PlayerAnimation;
import io.libgdx.cubegame.assets.Assets;
import io.libgdx.cubegame.blocks.Block;
import io.libgdx.cubegame.blocks.BlockType;
import io.libgdx.cubegame.blocks.factories.BlockFactory;
import io.libgdx.cubegame.player.Player;
import io.libgdx.cubegame.player.PlayerDirection;
import io.libgdx.cubegame.score.Score;
import io.libgdx.cubegame.screens.GameScreen;

public abstract class Level {
	public Logger logger = LoggerFactory.getLogger(Level.class);
	Date lastNotAllowed = new Date();
	Date lastSpawnLifeforce = new Date();
	Date lastSpawnPoint = new Date();
	long seed = 4711;
	Random r = new Random(seed);
	public LifeforcePos lifeforcePos = new LifeforcePos();
	
	boolean completed = false;
	boolean failed = false;
	
	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isFailed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}


	/**
	 * Used for spawning lifeforces...
	 */
	final List<Vector3> stones = new ArrayList<>();
	
	public float getTimeLimit() {
		return 100;
	}
	
	public int requiredLifeforces() {
		return 3;
	}
	
	public abstract Block[][][] field();
	
	public abstract Player getPlayer();

	public int xlength() {
		return 10;
	}
	public int ylength() {
		return 10;
	}
	public int zlength() {
		return 10;
	}
	
	public void cameraUpdate(Camera cam) {
		if (Config.followPlayer) {
			cameraFollowsCube(cam);
			cam.lookAt(xlength(), getPlayer().y	-5,	ylength()/2);
		}
	}
	
	public void updateAnimatedBlocks() {
		for (int i = 0; i < xlength(); i++) {
			for (int j = 0; j < ylength(); j++) {
				for (int k = 0; k < zlength(); k++) {
					if (field()[i][j][k] != null && field()[i][j][k].anim != null) {
						Block block = field()[i][j][k];
						block.anim.update(block);
					}
				}
			}
		}
	}
	
	// https://stackoverflow.com/questions/24047172/libgdx-camera-smooth-translation
	public void cameraFollowsCube(Camera cam, int x, int y, int z) {
		Vector3 p = new Vector3();
		p.x = getPlayer().x + x;
		p.y = getPlayer().y + y;
		p.z = getPlayer().z + z;
		final float s = 0.01f;
		final float ispeed = 1.0f - s;
		// The result is roughly: old_position*0.9 + target * 0.1
		Vector3 cameraPosition = cam.position;
		cameraPosition.scl(ispeed);
		p.scl(s);
		cameraPosition.add(p);
		cam.position.set(cameraPosition);
		cam.update();
	}
	
	public void cameraFollowsCube(Camera cam) {
		cameraFollowsCube(cam, -7, 2, -3);
	}
	
	public void renderLevel(ModelBatch modelBatch, Environment environment) {
		for (int i = 0; i < xlength(); i++) {
			for (int j = 0; j < ylength(); j++) {
				for (int k = 0; k < zlength(); k++) {
					if (field()[i][j][k] != null) {
						field()[i][j][k].render(modelBatch, environment);
					}
				}
			}
		}
	}

	public boolean allowed(PlayerDirection direction) {
		int x = getPlayer().x;
		int y = getPlayer().y;
		int z = getPlayer().z;
		
		switch (direction) {
		case FORWARD:
			if (x >= 1) {
				Block onPos = field()[x-1][y-1][z]; // one layer below
				
				if (onPos == null) {
					logger.info(" null(hole) on {} {} {}", x-1, y-1, z);
					
					playNotAllowedEffect();
					
					return false;
				}
				
				if (onPos.getType() == BlockType.LIFEFORCE || onPos.getType() == BlockType.POINT) {
					Color lifeforceColor = onPos.color;
					Color cubeColor = getPlayer().left;
					// oldscrool
					cubeColor = getPlayer().right;
					
					return lifeforceColor.equals(cubeColor);
				}
				if (onPos.getType() == BlockType.FORWARD_SHIFT) {
					Color lifeforceColor = onPos.color;
					Color cubeColor = getPlayer().right;
					
					return lifeforceColor.equals(cubeColor);
				}
				// Only forward on forward-shift allowed 
				if (onPos.getType() == BlockType.BACKWARD_SHIFT) {
					return false;
				}
				if (onPos.getType() == BlockType.LEFT_SHIFT) {
					return false;
				}
				if (onPos.getType() == BlockType.RIGHT_SHIFT) {
					return false;
				}				
				
				if (onPos.getType() == BlockType.ELEVATOR_UP || onPos.getType() == BlockType.ELEVATOR_DOWN) {
					return true;
				}
				
				onPos = field()[x-1][y][z]; // same layer
				// Check for wall
				if (onPos != null) {
					logger.info(" wall: {}", onPos);
					
					playNotAllowedEffect();
					
					return false;
				}
				
			} else {
				playNotAllowedEffect();
			}
			return x >= 1;
		case BACK:
			if (x < xlength() - 1) {
				Block onPos = field()[x+1][y-1][z]; // one layer below
				
				if (onPos == null) {
					logger.info(" null(hole) on {} {} {}", x+1, y-1, z);
					playNotAllowedEffect();
					
					return false;
				}
				
				if (onPos.getType() == BlockType.LIFEFORCE || onPos.getType() == BlockType.POINT) {
					Color lifeforceColor = onPos.color;
					Color cubeColor = getPlayer().right;
					// oldscrool
					cubeColor = getPlayer().left;
					
					return lifeforceColor.equals(cubeColor);
				}
				
				if (onPos.getType() == BlockType.BACKWARD_SHIFT) {
					Color lifeforceColor = onPos.color;
					Color cubeColor = getPlayer().left;
					
					return lifeforceColor.equals(cubeColor);
				}
				// Only backward on backward-shift allowed 
				if (onPos.getType() == BlockType.FORWARD_SHIFT) {
					return false;
				}
				if (onPos.getType() == BlockType.LEFT_SHIFT) {
					return false;
				}
				if (onPos.getType() == BlockType.RIGHT_SHIFT) {
					return false;
				}
				if (onPos.getType() == BlockType.ELEVATOR_UP || onPos.getType() == BlockType.ELEVATOR_DOWN) {
					return true;
				}
				
				onPos = field()[x+1][y][z]; // same layer
				// Check for wall
				if (onPos != null) {
					logger.info(" wall: {}", onPos);
					
					playNotAllowedEffect();
					
					return false;
				}
			} else {
				playNotAllowedEffect();
			}
			
			return x < xlength() - 1;
		case LEFT:
			
			if (z >= 1) {
				Block onPos = field()[x][y-1][z-1]; // one layer below
				
				if (onPos == null) {
					logger.info(" null(hole) on {} {} {}", x, y-1, z-1);
					
					playNotAllowedEffect();
					
					return false;
				}
				
				if (onPos.getType() == BlockType.LIFEFORCE || onPos.getType() == BlockType.POINT) {
					Color lifeforceColor = onPos.color;
					Color cubeColor = getPlayer().front;
					// oldscrool
					cubeColor = getPlayer().back;
					
					return lifeforceColor.equals(cubeColor);
				}
				
				if (onPos.getType() == BlockType.LEFT_SHIFT) {
					Color lifeforceColor = onPos.color;
					Color cubeColor = getPlayer().back;
					
					return lifeforceColor.equals(cubeColor);
				}
				// Only left on left-shift allowed 
				if (onPos.getType() == BlockType.FORWARD_SHIFT) {
					return false;
				}
				if (onPos.getType() == BlockType.RIGHT_SHIFT) {
					return false;
				}
				if (onPos.getType() == BlockType.RIGHT_SHIFT) {
					return false;
				}
				if (onPos.getType() == BlockType.ELEVATOR_UP || onPos.getType() == BlockType.ELEVATOR_DOWN) {
					return true;
				}

				onPos = field()[x][y][z-1]; // same layer
				// Check for wall
				if (onPos != null) {
					logger.info(" wall: {}", onPos);
					
					playNotAllowedEffect();
					
					return false;
				}
				
			} else {
				playNotAllowedEffect();
			}
			return z >= 1;
		case RIGHT:
			
			if (z < zlength() - 1) {
				Block onPos = field()[x][y-1][z+1]; // one layer below
				
				if (onPos == null) {
					logger.info(" null(hole) on {} {} {}", x, y-1, z+1);
					playNotAllowedEffect();
					
					return false;
				}
				
				if (onPos.getType() == BlockType.LIFEFORCE || onPos.getType() == BlockType.POINT) {
					Color lifeforceColor = onPos.color;
					Color cubeColor = getPlayer().back;
					// oldscool
					cubeColor = getPlayer().front;
					
					return lifeforceColor.equals(cubeColor);
				}
				if (onPos.getType() == BlockType.RIGHT_SHIFT) {
					Color lifeforceColor = onPos.color;
					Color cubeColor = getPlayer().back;
					// oldscool
					cubeColor = getPlayer().front;
					
					boolean equals = lifeforceColor.equals(cubeColor);
					
					return equals;
				}
				// Only right on right-shift allowed 
				if (onPos.getType() == BlockType.FORWARD_SHIFT) {
					return false;
				}
				if (onPos.getType() == BlockType.LEFT_SHIFT) {
					return false;
				}
				if (onPos.getType() == BlockType.BACKWARD_SHIFT) {
					return false;
				}
				if (onPos.getType() == BlockType.ELEVATOR_UP || onPos.getType() == BlockType.ELEVATOR_DOWN) {
					return true;
				}
				
				onPos = field()[x][y][z+1]; // same layer
				
				// Check for wall
				if (onPos != null) {
					
					logger.info(" wall: {}", onPos);
					
					playNotAllowedEffect();
					
					return false;
				}
			} else {
				playNotAllowedEffect();
			}
			
			return z < zlength() - 1;
		default:
			return true;
		}
	}
	
	/**
	 * Should only be invoked when allowed was true...
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void playerMovedOn(int x, int y, int z) {
		if (getPlayer().isMoving) {
			return;
		}
		
		Block onPos = field()[x][y-1][z]; // one layer below
		
		if (onPos == null) {
			logger.error("player moved on {} {} {} but block is null", x, y, z);
			return;
		}
		
		int nextXShift = -1;
		int nextZShift = -1;
		boolean shift = false;
		switch (onPos.getType()) {
			case LIFEFORCE:
			case POINT:
				field()[x][y-1][z].dispose(); // cleanup last block
				field()[x][y-1][z] = BlockFactory.createGround(x, y-1, z);
				
				if (onPos.getType() == BlockType.POINT) {
					Score.score += 100;
				} else {
					if (Assets.instance().soundLifeforce != null) {
						Assets.instance().soundLifeforce.play();
					}
					
					Score.lifeforces++;
					if (Score.lifeforces >= requiredLifeforces()) {
						setCompleted(true);
					}
				}
				
				break;
			
			case RIGHT_SHIFT:
				nextXShift = 0;
				nextZShift = 2; // TODO XX_Shift: Configurable how many stones will be jumped over...
				shift = true;
				break;
			case LEFT_SHIFT:
				nextXShift = 0;
				nextZShift = -2;
				shift = true;
				break;
			case BACKWARD_SHIFT:
				nextXShift = 2;
				nextZShift = 0;
				shift = true;
				break;
			case FORWARD_SHIFT:
				nextXShift = -2;
				nextZShift = 0;
				shift = true;
				break;
				
			case WALL:
				throw new RuntimeException("Not allowed to move on a wall");
			case ELEVATOR_UP:
				
				field()[x][y][z] = null;
				
				field()[x][y][z] = getPlayer();
				
				int nextY = y+1;
				while (true) {
					if (field()[x][nextY][z] != null && field()[x][nextY][z].getType().equals(BlockType.GROUND)) {
						nextY++;
						break;
					}
					nextY++;
				}
				
				field()[x][y][z].setPosition(x, nextY, z);
				field()[x][y][z].x = x;
				field()[x][y][z].y = nextY; 
				field()[x][y][z].z = z;
				
				getPlayer().x = x;
				getPlayer().y = nextY;
				getPlayer().z = z;
				
				// Animation
				getPlayer().anim = new PlayerAnimation(
					Arrays.asList(
						new Vector3(x, y, z),
						new Vector3(x, y+(nextY-y)/2, z),
						new Vector3(x, nextY, z)
					)
				);

				break;
			case ELEVATOR_DOWN:
				
				field()[x][y][z] = null;
				
				field()[x][y][z] = getPlayer();
				
				int nextY2 = y-1;
				while (true) {
					if (field()[x][nextY2][z] != null && field()[x][nextY2][z].getType().equals(BlockType.GROUND)) {
						nextY2++;
						break;
					}
					nextY2--;
				}
				
				
				field()[x][y][z].setPosition(x, nextY2, z);
				field()[x][y][z].x = x;
				field()[x][y][z].y = nextY2;
				field()[x][y][z].z = z;
				
				getPlayer().x = x;
				getPlayer().y = nextY2;
				getPlayer().z = z;
				
				// Animation
				getPlayer().anim = new PlayerAnimation(
					Arrays.asList(
						new Vector3(x, y, z),
						new Vector3(x, y-(y-nextY2)/2, z),
						new Vector3(x, nextY2, z)
					)
				);

				break;				
		}
		
		if (shift) {
			field()[x][y][z] = null;
			
			field()[x+nextXShift][y][z+nextZShift] = getPlayer();
			field()[x+nextXShift][y][z+nextZShift].setPosition(x+nextXShift, y, z+nextZShift);
			field()[x+nextXShift][y][z+nextZShift].x = x+nextXShift;
			field()[x+nextXShift][y][z+nextZShift].y = y;
			field()[x+nextXShift][y][z+nextZShift].z = z+nextZShift;
			
			getPlayer().x = x+nextXShift;
			getPlayer().y = y;
			getPlayer().z = z+nextZShift;
			
			// Animation "jump"
			getPlayer().anim = new PlayerAnimation(
				Arrays.asList(
					new Vector3(x, y, z),
					new Vector3(x+nextXShift/2, y+1, z+nextZShift/2),
					new Vector3(x+nextXShift, y, z+nextZShift)
				)
			);
		}
		
		if (Assets.instance().soundPlayerMoved != null) {
			Assets.instance().soundPlayerMoved.play();
		}
	}
	
	public void dispose() {
		for (int i = 0; i < xlength(); i++) {
			for (int j = 0; j < ylength(); j++) {
				for (int k = 0; k < zlength(); k++) {
					if (field()[i][j][k] != null) {
						field()[i][j][k].dispose();
					}
				}
			}
		}
	}
	
	public void translateAllBlocks() {
		stones.clear();
		lifeforcePos.x = 0;
		lifeforcePos.y = 0;
		lifeforcePos.z = 0;
		for (int i = 0; i < xlength(); i++) {
			for (int j = 0; j < ylength(); j++) {
				for (int k = 0; k < zlength(); k++) {
					if (field()[i][j][k] != null) {
						field()[i][j][k].x = i;
						field()[i][j][k].y = j;
						field()[i][j][k].z = k;
						field()[i][j][k].getInstance().transform = new Matrix4().translate(i,j,k);
					}
					if (field()[i][j][k] != null && field()[i][j][k].getType() == BlockType.GROUND) {
						stones.add(new Vector3(i, j, k));
					}
				}
			}
		}
	}
	
	public void playNotAllowedEffect() {
		if (System.currentTimeMillis() - lastNotAllowed.getTime() > 1000) {
			if (Assets.instance().soundMovementNotAllowed != null) {
				Assets.instance().soundMovementNotAllowed.play();
			}
			lastNotAllowed.setTime(System.currentTimeMillis());
		}
	}
	
	static class LifeforcePos {
		public int x = 0;
		public int y = 0;
		public int z = 0;
		
		boolean avail(Block[][][] l) {
			return l[x][y][z] != null && l[x][y][z].getType() == BlockType.LIFEFORCE;
		}
	}
	
	
	public void spawnBlocks() {
		if (System.currentTimeMillis() - lastSpawnLifeforce.getTime() > 1000 && !lifeforcePos.avail(field())) {
			lastSpawnLifeforce.setTime(System.currentTimeMillis());
			Vector3 pos = stones.get(r.nextInt(stones.size()));
			
			int x2 = (int)pos.x;
			int y2 = (int)pos.y;
			int z2 = (int)pos.z;
			
			Block block2                  = field()[x2][y2][z2];
			Block possiblePlayerPosition2 = field()[x2][y2+1][z2];
			if (block2 != null && block2.getType() == BlockType.GROUND && possiblePlayerPosition2 == null) {
				lifeforcePos.x = x2;
				lifeforcePos.y = y2;
				lifeforcePos.z = z2;
				
				Color pointsColor = Color.WHITE;
				int count = r.nextInt(6);
				if (count == 0) {
					pointsColor = getPlayer().back;	
				} else if (count == 1) {
					pointsColor = getPlayer().top;
				} else if (count == 2) {
					pointsColor = getPlayer().bottom;
				} else if (count == 3) {
					pointsColor = getPlayer().front;
				} else if (count == 4) {
					pointsColor = getPlayer().left;
				} else if (count == 5) {
					pointsColor = getPlayer().right;
				}

				field()[x2][y2][z2] = BlockFactory.createLifeforce(pointsColor, x2, y2, z2);
				
				if (Assets.instance().soundLifeforce != null) {
					Assets.instance().soundLifeforce.play();
				}

				
				logger.debug("spawed lifeforce on {} {}", x2, z2);
			}
		}
		if (System.currentTimeMillis() - lastSpawnPoint.getTime() > 3000) {
			lastSpawnPoint.setTime(System.currentTimeMillis());
			
			Vector3 pos = stones.get(r.nextInt(stones.size()));
			
			int x = (int)pos.x;
			int y = (int)pos.y;
			int z = (int)pos.z;
			
			Block block                  = field()[x][y][z];
			Block possiblePlayerPosition = field()[x][y+1][z];
			if (block != null && block.getType() == BlockType.GROUND && possiblePlayerPosition == null) {

				Color pointsColor = Color.WHITE;
				int count = r.nextInt(6);
				if (count == 0) {
					pointsColor = getPlayer().back;	
				} else if (count == 1) {
					pointsColor = getPlayer().top;
				} else if (count == 2) {
					pointsColor = getPlayer().bottom;
				} else if (count == 3) {
					pointsColor = getPlayer().front;
				} else if (count == 4) {
					pointsColor = getPlayer().left;
				} else if (count == 5) {
					pointsColor = getPlayer().right;
				}

				field()[x][y][z] = BlockFactory.createPoint(pointsColor, x, y, z);
				
				logger.debug("spawed point on {} {}", x, z);
			}
		}
	}
}
