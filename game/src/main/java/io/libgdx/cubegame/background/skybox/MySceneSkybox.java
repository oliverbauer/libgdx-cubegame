package io.libgdx.cubegame.background.skybox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.CubemapAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.DepthTestAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader.Config;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;

import net.mgsx.gltf.scene3d.scene.SceneRenderableSorter;
import net.mgsx.gltf.scene3d.scene.Updatable;

/**
 * Extension to lerp color and rotate skybox a little bit...
 */
public class MySceneSkybox implements RenderableProvider, Updatable, Disposable {

	private DefaultShaderProvider shaderProvider;
	private Model boxModel;
	public Renderable box;
	Environment environment;
	ModelBatch modelBatch;
	
	public MySceneSkybox(Cubemap cubemap, Environment environment, ModelBatch modelBatch){
		super();
		
		this.environment = environment;
		this.modelBatch = modelBatch;
		
		// create shader provider
		Config shaderConfig = new Config();
		String basePathName = "net/mgsx/gltf/shaders/skybox";
		shaderConfig.vertexShader = Gdx.files.classpath(basePathName + ".vs.glsl").readString();
		shaderConfig.fragmentShader = Gdx.files.classpath(basePathName + ".fs.glsl").readString();
		shaderProvider =  new DefaultShaderProvider(shaderConfig);
		
		// create box
		float boxScale = (float)(1.0 / Math.sqrt(2.0));
		boxModel = new ModelBuilder().createBox(boxScale, boxScale, boxScale, new Material(), VertexAttributes.Usage.Position);
		box = boxModel.nodes.first().parts.first().setRenderable(new Renderable());
		
		// assign environment
		Environment env = new Environment();
		env.set(new CubemapAttribute(CubemapAttribute.EnvironmentMap, cubemap));
		env.set(new ColorAttribute(ColorAttribute.AmbientLight, Color.WHITE));
		box.environment = env;
		
		// set hint to render last but before transparent ones
		box.userData = SceneRenderableSorter.Hints.OPAQUE_LAST;
		
		// does nothing...
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = 0.7f;
		
		// set material options : preserve background depth
		box.material = new Material(
			attr
//			,
//			blendingAttribute
		);
		box.material.set(new DepthTestAttribute(false));
		
		
		
		// assign shader
		box.shader = shaderProvider.getShader(box);
		
	
//		PointLight pointLight = new PointLight();
//		pointLight.color.set(Color.RED);
//		box.environment.add(pointLight);
//		box.material.set(ColorAttribute.createEmissive(Color.RED));
	}
	
	public MySceneSkybox set(Cubemap cubemap){
		box.environment.set(new CubemapAttribute(CubemapAttribute.EnvironmentMap, cubemap));
		return this;
	}
	
	/**
	 * @return skybox material color to be modified (default is white)
	 */
	public Color getColor(){
		return box.material.get(ColorAttribute.class, ColorAttribute.Diffuse).color;
	}
	
	public float radiusX = 3f; // max 30
	public float radiusY = 3f; // max 30
	public float radiusZ = 3f; // max 30
	private float lightPosition = 0;
	
	Color lerp = Color.WHITE;
	Color goal = Color.GREEN;
	ColorAttribute attr = ColorAttribute.createDiffuse(lerp);
	
	float alpha = 0;
	@Override
	public void update(Camera camera, float delta){
		alpha += 0.005;
		
//		// TODO Disable when level finished (success/failed)
//		
//		BlendingAttribute blendingAttribute = new BlendingAttribute();
//		blendingAttribute.opacity = 0.7f;
//		
//		Color t = lerp.cpy().lerp(goal, 0.005f);
//		attr = ColorAttribute.createDiffuse(t);
//		box.material = new Material(
//				attr,
//				blendingAttribute
//			);
//			box.material.set(new DepthTestAttribute(false));
//			box.shader = shaderProvider.getShader(box);
//		lerp = t;
////		System.out.println(alpha+": "+lerp+" -> "+goal);
//		if (alpha > 1) {
//			alpha = 0;
//			if (goal == Color.RED) {
//				goal = Color.GREEN;
//			} else {
//				goal = Color.RED;
//			}
//		}
		
		
		// scale skybox to camera range.
		float s = camera.far * (float)Math.sqrt(2.0);

		box.worldTransform.setToScaling(s, s, s);
		
		Vector3 clone = new Vector3();
		clone.x = camera.position.x;
		clone.y = camera.position.y;
		clone.z = camera.position.z;
		
		lightPosition += delta * 1.0f;
        float lx = (float) (radiusX * Math.cos(lightPosition));
        float ly = (float) (radiusY * Math.sin(lightPosition));
        float lz = (float) (radiusZ * Math.sin(lightPosition));
        Vector3 lightVector = new Vector3(lx, ly, lz).add(clone);
		
		box.worldTransform.setTranslation(lightVector);
	}
	
	@Override
	public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
		renderables.add(box);
	}

	@Override
	public void dispose() {
		shaderProvider.dispose();
		boxModel.dispose();
	}
}
