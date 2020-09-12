package io.libgdx.cubegame.blocks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import io.libgdx.cubegame.animation.Animation;
import io.libgdx.cubegame.animation.PlayerAnimation;

public class Block implements Disposable {
	private static ModelBuilder modelBuilder = new ModelBuilder();
	private Model model;
	protected ModelInstance instance;
	private BlockType type;

	public int x = -1;
	public int y = -1;
	public int z = -1;
	
	public Color color;
	
	public Animation anim;
	
	public Vector3 getPosition() {
		return new Vector3(x, y, z);
	}
	
	public Block(Model model, BlockType type) {
		this.type = type;
		this.model = model;
		this.instance = new ModelInstance(model);
	}
	
	public Block(Texture texture, BlockType type) {
		this.type = type;

		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = .7f;
		Material material = new Material(blendingAttribute, TextureAttribute.createDiffuse(texture));
		
		modelBuilder.begin();
		modelBuilder.node();
		
		
		MeshPartBuilder mpb = modelBuilder.part(
			"box", 
			GL20.GL_TRIANGLES, 
			VertexAttributes.Usage.Position	| VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates, 
			material);
		BoxShapeBuilder.build(mpb, 1, 1, 1); // width height depth
		

		
		model = modelBuilder.end();
		
		instance = new ModelInstance(model);
	}
	
	public void setPosition(float x, float y, float z){
		instance.transform = new Matrix4().translate(x, y, z);
	}

	public void setModel(Model m) {
		this.model = m;
		instance = new ModelInstance(m);
	}
	
	public ModelInstance getInstance() {
		return instance;
	}

	public void render(ModelBatch modelBatch, Environment environment) {
		modelBatch.render(instance, environment);
	}
	
	public void dispose() {
		try {
			model.dispose();
		} catch (Exception e) {
			
		}
	}
	
	public BlockType getType() {
		return type;
	}

	@Override
	public String toString() {
		return super.toString()+" Block [x="	+ x + ", y=" + y + ", z=" + z + "] type "+type;
	}
}
