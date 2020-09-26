package io.libgdx.cubegame.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import io.libgdx.cubegame.levels.Level;

public class PlayerController {
	private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
	private Level level;
	
	private int xNext;
	private int yNext;
	private int zNext;
	
	private float alpha = 0;
	private float cubeRotationSpeed = 5f;
	private float fromAngle = 0;
	private float toAngle = 90;

	private PlayerDirection direction = PlayerDirection.NONE;
	
	public PlayerController(Level level) {
		this.level = level;
	}
	
	public void playerMovement(PlayerDirection nextDirection) {
		if (direction == PlayerDirection.NONE && nextDirection == PlayerDirection.NONE) {
			return;
		} else if (direction == PlayerDirection.NONE) {
			direction = nextDirection;
			if (logger.isDebugEnabled()) {
				logger.debug("Current dir = {}", direction);
			}

			level.getPlayer().isMoving = true;
			xNext= level.getPlayer().x;
			yNext = level.getPlayer().y;
			zNext = level.getPlayer().z;

			if (direction == PlayerDirection.RIGHT) {
				zNext++;
				
				fromAngle = 0;
				toAngle = fromAngle + 90 % 360;
			} else if (direction == PlayerDirection.FORWARD) {
				xNext--;

				fromAngle = 0;
				toAngle = fromAngle + 90 % 360;
			} else if (direction == PlayerDirection.LEFT) {
				zNext--;

				fromAngle = 0;
				toAngle = fromAngle - 90 % 360;
			} else if (direction == PlayerDirection.BACK) {
				xNext++;

				fromAngle = 0;
				toAngle = fromAngle - 90 % 360;
			}

			alpha = 0;

		} else {
			if (level.getPlayer().isMoving) {
				boolean temp = level.allowed(direction);

				if (!temp) {
					level.getPlayer().isMoving = false;
					direction = PlayerDirection.NONE;
					return;
				}
				
				final float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f);
				alpha += delta * cubeRotationSpeed;
				float angle = fromAngle + alpha * (toAngle - fromAngle);
				Vector3 tmpV = new Vector3();

				Vector3 axis;
				if (xNext == level.getPlayer().x) {
					axis = Vector3.X;
				} else {
					axis = Vector3.Z;
				}
				
				Vector3 newPosition = new Vector3(xNext + Player.xOffset, yNext + Player.yOffset, zNext + Player.zOffset);
				tmpV.set(level.getPlayer().getPosition()).lerp(newPosition, alpha);
				
				
				/*
				 * TODO Use correct rotation point (solution from stackoverlfow already found): 
				 * https://stackoverflow.com/questions/21939393/rotate-modelinstance-at-specific-point
				 * 
				 * Rotating around a point is the same as translating to that point, rotating and then translating back. 
				 * E.g.: 
				 * 
				 * inst.transform.translate(-x, -y, -z).rotate(axis, angle).translate(x, y, z); 
				 */
				
				
//				level.getPlayer().getInstance()
//					.transform
//					.translate(tmpV)
//					.rotate(axis, angle)
//					.translate(tmpV);
				level.getPlayer().getInstance().transform.setToRotation(axis, angle);
				level.getPlayer().getInstance().transform.setTranslation(tmpV);

				if (angle > toAngle && direction == PlayerDirection.RIGHT) {
					level.getPlayer().isMoving = false;
					direction = PlayerDirection.NONE;
					
					level.field()[level.getPlayer().x][level.getPlayer().y][level.getPlayer().z] = null;
					
					level.getPlayer().x = xNext;
					level.getPlayer().y = yNext;
					level.getPlayer().z = zNext;
					level.getPlayer().rotate(PlayerDirection.RIGHT);

					// Exception if not on board.... need to fix keyDown in keybaord class which uses isAllowed in Grid class...
					updatePosition();
				}
				if (angle < toAngle && direction == PlayerDirection.LEFT) {
					level.getPlayer().isMoving = false;
					direction = PlayerDirection.NONE;
					
					level.field()[level.getPlayer().x][level.getPlayer().y][level.getPlayer().z] = null;
					
					level.getPlayer().x = xNext;
					level.getPlayer().y = yNext;
					level.getPlayer().z = zNext;
					level.getPlayer().rotate(PlayerDirection.LEFT);
					
					// Exception if not on board.... need to fix keyDown in keybaord class which uses isAllowed in Grid class...
					updatePosition();
				}

				if (angle < toAngle && direction == PlayerDirection.BACK) {
					level.getPlayer().isMoving = false;
					direction = PlayerDirection.NONE;
					
					level.field()[level.getPlayer().x][level.getPlayer().y][level.getPlayer().z] = null;
					
					level.getPlayer().x = xNext;
					level.getPlayer().y = yNext;
					level.getPlayer().z = zNext;
					level.getPlayer().rotate(PlayerDirection.BACK);
					
					// Exception if not on board.... need to fix keyDown in keybaord class which uses isAllowed in Grid class...
					updatePosition();

				}
				if (angle > toAngle && direction == PlayerDirection.FORWARD) {
					level.getPlayer().isMoving = false;
					direction = PlayerDirection.NONE;
					
					level.field()[level.getPlayer().x][level.getPlayer().y][level.getPlayer().z] = null;
					
					level.getPlayer().x = xNext;
					level.getPlayer().y = yNext;
					level.getPlayer().z = zNext;
					level.getPlayer().rotate(PlayerDirection.FORWARD);
					
					// Exception if not on board.... need to fix keyDown in keybaord class which uses isAllowed in Grid class...
					updatePosition();
				}
			}
		}
	}
	
	private void updatePosition() {
		int x = level.getPlayer().x;
		int y = level.getPlayer().y;
		int z = level.getPlayer().z;
		if (logger.isDebugEnabled()) {
			logger.debug("Update array to {} {} {}", x, y, z);
		}
		
		level.field()[x][y][z] = level.getPlayer(); // FIXME überschreibt!
		level.getPlayer().x = x;
		level.getPlayer().y = y;
		level.getPlayer().z = z;
		
		// collect lifeforce, die, do nothing...
		level.playerMovedOn(x,y,z);
	}
}
