package io.libgdx.cubegame.particles;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;
import com.badlogic.gdx.graphics.g3d.particles.emitters.RegularEmitter;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.RegionInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ScaleInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.SpawnInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier.BrownianAcceleration;
import com.badlogic.gdx.graphics.g3d.particles.renderers.BillboardRenderer;
import com.badlogic.gdx.graphics.g3d.particles.values.PointSpawnShapeValue;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Particles {
	private Array<ParticleController> emitters;
	private BillboardParticleBatch billboardParticleBatch;
	
	public Particles(PerspectiveCamera cam, int x, int y, int z) {
		AssetManager assets = new AssetManager();
		String DEFAULT_PARTICLE = "cubegame/particles/pre_particle.png";
		assets.load(DEFAULT_PARTICLE, Texture.class);
		assets.finishLoading();

		emitters = new Array<>();
		
		billboardParticleBatch = new BillboardParticleBatch();
		billboardParticleBatch.setCamera(cam);

		Texture particleTexture = assets.get(DEFAULT_PARTICLE);
		billboardParticleBatch.setTexture(assets.get(DEFAULT_PARTICLE, Texture.class));

		Vector3 tmpVector = new Vector3();
		ParticleController p1 = addEmitter(new float[] { 1, 1.12156863f, 0.047058824f }, particleTexture, tmpVector.set(x, y, z));
	}
	
	public void render(ModelBatch modelBatch, Environment environment) {
		billboardParticleBatch.begin();
		for (ParticleController controller : emitters) {
			controller.update();
			controller.draw();
		}
		billboardParticleBatch.end();
		modelBatch.render(billboardParticleBatch, environment);
	}
	
	private ParticleController addEmitter(float[] colors, Texture particleTexture, Vector3 translation) {
		ParticleController controller = createBillboardController(colors, particleTexture);
		controller.init();
		controller.start();
		emitters.add(controller);
		controller.translate(translation);
		
		return controller;
	}
	
	private ParticleController createBillboardController(float[] colors, Texture particleTexture) {
		// Emission
		RegularEmitter emitter = new RegularEmitter();
		emitter.getDuration().setLow(3000);
		emitter.getEmission().setHigh(290); // 2900 löst voll auf, 290 cool, 29 noch cooler
		emitter.getLife().setHigh(10000);
		emitter.setMaxParticleCount(3000);

		// Spawn
		PointSpawnShapeValue pointSpawnShapeValue = new PointSpawnShapeValue();
		pointSpawnShapeValue.xOffsetValue.setLow(0, 1f);
		pointSpawnShapeValue.xOffsetValue.setActive(true);
		pointSpawnShapeValue.yOffsetValue.setLow(0, 1f);
		pointSpawnShapeValue.yOffsetValue.setActive(true);
		pointSpawnShapeValue.zOffsetValue.setLow(0, 1f);
		pointSpawnShapeValue.zOffsetValue.setActive(true);
		SpawnInfluencer spawnSource = new SpawnInfluencer(pointSpawnShapeValue);

		// Scale
		ScaleInfluencer scaleInfluencer = new ScaleInfluencer();
		scaleInfluencer.value.setTimeline(new float[] { 0, 1 });
		scaleInfluencer.value.setScaling(new float[] { 1, 0 });
		scaleInfluencer.value.setLow(0);
		scaleInfluencer.value.setHigh(0.5f);

		// Color
		ColorInfluencer.Single colorInfluencer = new ColorInfluencer.Single();
		colorInfluencer.colorValue.setColors(new float[] { colors[0], colors[1], colors[2], 0, 0, 0 });
		colorInfluencer.colorValue.setTimeline(new float[] { 0, 1 });
		colorInfluencer.alphaValue.setHigh(1);
		colorInfluencer.alphaValue.setTimeline(new float[] { 0, 0.5f, 0.8f, 1 });
		colorInfluencer.alphaValue.setScaling(new float[] { 0, 0.15f, 0.5f, 0 });

		// Dynamics
		DynamicsInfluencer dynamicsInfluencer = new DynamicsInfluencer();
		BrownianAcceleration modifier = new BrownianAcceleration();
		modifier.strengthValue.setTimeline(new float[] { 0, 1 });
		modifier.strengthValue.setScaling(new float[] { 0, 1 });
		modifier.strengthValue.setHigh(8);
		modifier.strengthValue.setLow(1, 5);
		dynamicsInfluencer.velocities.add(modifier);

		return new ParticleController("Billboard Controller", emitter, new BillboardRenderer(billboardParticleBatch),
				new RegionInfluencer.Single(particleTexture), spawnSource, scaleInfluencer, colorInfluencer,
				dynamicsInfluencer);
	}

}
