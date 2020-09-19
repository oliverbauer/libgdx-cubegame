package io.libgdx.cubegame.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

import io.libgdx.cubegame.Config;
import io.libgdx.cubegame.background.image.ImageBackground;
import io.libgdx.cubegame.background.skybox.MySceneManager;
import io.libgdx.cubegame.background.skybox.MySceneSkybox;
import net.mgsx.gltf.scene3d.utils.EnvironmentUtil;

public class Background {
	private ImageBackground imageBackground;
	
	private Cubemap environmentCubemap;
	private MySceneManager mySceneManager;
	private MySceneSkybox mySkybox;
	
	public Background(Environment environment, ModelBatch modelBatch, PerspectiveCamera cam) {
		imageBackground = new ImageBackground("cubegame/environment/backgrounds/1.jpg");
		
		environmentCubemap = EnvironmentUtil.createCubemap(new InternalFileHandleResolver(), 
				"cubegame/environment/backgrounds/level01/", ".jpg", EnvironmentUtil.FACE_NAMES_FULL);
		
		// setup skybox
		mySkybox = new MySceneSkybox(environmentCubemap, environment, modelBatch);
		mySceneManager = new MySceneManager();
		mySceneManager.setSkyBox(mySkybox);
		mySceneManager.camera = cam;
	}
	
	public void render() {
		switch (Config.backgroundType) {
			case TEXTURE:
				imageBackground.render();
				break;
			case CUBEMAP:
				mySceneManager.update(Gdx.graphics.getDeltaTime());
				mySceneManager.render();
				break;
			case NONE:
		}
	}
}
