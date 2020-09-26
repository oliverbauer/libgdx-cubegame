package examples.example3;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import examples.example3.Example3PlayerMovement.PlayerDirection;

public class Keyboard extends InputAdapter {
	PlayerDirection nextDir = PlayerDirection.NONE;
	
	@Override
	public boolean keyDown(final int keycode) {
		if (keycode == Input.Keys.W) {
			nextDir = PlayerDirection.BACK;
		} else if (keycode == Input.Keys.S) {
			nextDir = PlayerDirection.FORWARD;
		} else if (keycode == Input.Keys.A) {
			nextDir = PlayerDirection.LEFT;
		} else if (keycode == Input.Keys.D) {
			nextDir = PlayerDirection.RIGHT;
		}
		
		return false;
	}

	@Override
	public boolean keyUp(final int keycode) {
		if (keycode == Input.Keys.W && nextDir == PlayerDirection.BACK) {
			nextDir = PlayerDirection.NONE;
		} else if (keycode == Input.Keys.S && nextDir == PlayerDirection.FORWARD) {
			nextDir = PlayerDirection.NONE;
		} else if (keycode == Input.Keys.A && nextDir == PlayerDirection.LEFT) {
			nextDir = PlayerDirection.NONE;
		} else if (keycode == Input.Keys.D && nextDir == PlayerDirection.RIGHT) {
			nextDir = PlayerDirection.NONE;
		}			
		return false;
	}
}
