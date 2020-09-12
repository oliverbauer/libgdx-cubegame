package io.libgdx.cubegame.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import io.libgdx.cubegame.CubeGame;
import io.libgdx.cubegame.levels.Level;
import io.libgdx.cubegame.player.PlayerDirection;
import io.libgdx.cubegame.screens.MenuScreen;

public class Keyboard extends InputAdapter {
	private static final Logger logger = LoggerFactory.getLogger(Keyboard.class);
	
	private PlayerDirection _nextDirection = PlayerDirection.NONE;
	public Level g;
	
	private boolean lastKeyAllowed = false;
	
	/**
	 * Used for a jumping cube... player should know: I am still alive...
	 */
	public long lastWASD = System.currentTimeMillis();
	
	public Keyboard(Level g) {
		this.g = g;
	}
	
	@Override
	public boolean keyDown(final int keycode) {
		if (keycode == Input.Keys.ESCAPE) {
			CubeGame game = (CubeGame)Gdx.app.getApplicationListener();
			game.setScreen(new MenuScreen(game));
		}
		
		if (keycode == Input.Keys.W) {
			lastWASD = System.currentTimeMillis();
			lastKeyAllowed = g.allowed(PlayerDirection.BACK);
		
			if (logger.isDebugEnabled()) {
				logger.debug("back allowed? {}", lastKeyAllowed);
			}
			
			
			if (lastKeyAllowed) {
				_nextDirection = PlayerDirection.BACK;
			} else {
				_nextDirection = PlayerDirection.NONE;
			}
		} else if (keycode == Input.Keys.S) {
			lastWASD = System.currentTimeMillis();
			lastKeyAllowed = g.allowed(PlayerDirection.FORWARD);
			if (logger.isDebugEnabled()) {
				logger.debug("forward allowed? {}", lastKeyAllowed);
			}
			
			if (lastKeyAllowed) {
				_nextDirection = PlayerDirection.FORWARD;
			} else {
				_nextDirection = PlayerDirection.NONE;
			}
		}
		if (keycode == Input.Keys.A) {
			lastWASD = System.currentTimeMillis();
			lastKeyAllowed = g.allowed(PlayerDirection.LEFT);
			if (logger.isDebugEnabled()) {
				logger.debug("left allowed? {}", lastKeyAllowed);
			}
			
			if (lastKeyAllowed) {
				_nextDirection = PlayerDirection.LEFT;
			} else {
				_nextDirection = PlayerDirection.NONE;
			}
		}
		if (keycode == Input.Keys.D) {
			lastWASD = System.currentTimeMillis();
			lastKeyAllowed = g.allowed(PlayerDirection.RIGHT);
			if (logger.isDebugEnabled()) {
				logger.debug("right allowed? {}", lastKeyAllowed);
			}
			
			if (lastKeyAllowed) {
				_nextDirection = PlayerDirection.RIGHT;
			} else {
				_nextDirection = PlayerDirection.NONE;
			}
		}
		return false;
	}

	@Override
	public boolean keyUp(final int keycode) {
		if (!lastKeyAllowed) {
			return false;
		}
		
		if (keycode == Input.Keys.W && _nextDirection == PlayerDirection.BACK) {
			_nextDirection = PlayerDirection.NONE;
		} else if (keycode == Input.Keys.S && _nextDirection == PlayerDirection.FORWARD) {
			_nextDirection = PlayerDirection.NONE;
		}
		if (keycode == Input.Keys.A && _nextDirection == PlayerDirection.LEFT) {
			_nextDirection = PlayerDirection.NONE;
		}
		if (keycode == Input.Keys.D && _nextDirection == PlayerDirection.RIGHT) {
			_nextDirection = PlayerDirection.NONE;
		}
		return false;
	}

	public boolean isLastKeyAllowed() {
		return lastKeyAllowed;
	}

	public void setLastKeyAllowed(boolean lastKeyAllowed) {
		this.lastKeyAllowed = lastKeyAllowed;
	}

	public PlayerDirection getNextDirection() {
		return _nextDirection;
	}
}