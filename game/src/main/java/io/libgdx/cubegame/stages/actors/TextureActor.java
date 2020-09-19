package io.libgdx.cubegame.stages.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TextureActor extends Actor {
	private final Texture mTexture;
	
	public TextureActor(Texture tex, int width, int height) {
		mTexture = tex;
		setSize(width, height);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(getColor());
		batch.draw(mTexture, getX(), getY(), getOriginX(), getOriginY(),
				getWidth(), getHeight(), getScaleX(), getScaleY(),
				getRotation(), 0, 0, mTexture.getWidth(),
				mTexture.getHeight(), false, false);
	}
}
