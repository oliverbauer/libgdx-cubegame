package io.libgdx.cubegame.blocks;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import io.libgdx.cubegame.animation.PlayerAnimation;
import io.libgdx.cubegame.blocks.factories.TextureFactory;

public class Arrow extends Block {
	private float alpha = 0;
	private float speed = 0.5f;
	
	public Vector3 start;
	public Vector3 end;
	
	PlayerAnimation animation = new PlayerAnimation(Arrays.asList(new Vector3(1, 1, 1))) {
		@Override
		public void update(Block block) {
			final float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f);
			
			alpha += delta * speed;
			
			if (alpha > 1) {
				alpha = 0; // repeat...
				return;
			}
			
			float angle = 0;
			Vector3 tmpV = new Vector3();
			
			Vector3 axis;
			if (start.x == end.x) {
				axis = Vector3.X;
			} else {
				axis = Vector3.Z;
			}
			
			tmpV.set(start).lerp(end, alpha);
			getInstance().transform.setToRotation(axis, angle);
			getInstance().transform.setTranslation(tmpV);
		}
	};

	
	@SuppressWarnings("deprecation")
	public Arrow(BlockType type) {
		super(TextureFactory.createTexture(Color.BLACK), type);
		
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = 0.7f;
		
		// todo uSE: ArrowShapeBuilder
		modelBuilder.part("arrow", 
			GL20.GL_TRIANGLES, 
			Usage.Position | Usage.Normal | Usage.TextureCoordinates,
//			new Material("diffuse-map", TextureAttribute.createDiffuse(texture)))
		new Material(blendingAttribute, TextureAttribute.createDiffuse(TextureFactory.createTexture(Color.BLACK))))
			.arrow(
				0, // X1 
				-0.5f, // Y1
				0,  // Z1
				0,  // X2
				0.5f,  // Y2
				0,  // Z2
				0.5f, //CAPLENGTH
				0.5f, //STEMtHICKNESS
				10); //DIVISION
		
		setModel(modelBuilder.end());
		anim = animation;
	}
}