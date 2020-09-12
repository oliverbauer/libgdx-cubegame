package io.libgdx.cubegame.background.image;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ImageBackground {
	private Texture bg;
	private SpriteBatch sb;
	
	public ImageBackground(String file) {
		bg = new Texture(file);
		sb = new SpriteBatch();
	}
	
	public void render() {
		sb.begin();
		sb.draw(bg, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); 
        sb.end();
	}
}
