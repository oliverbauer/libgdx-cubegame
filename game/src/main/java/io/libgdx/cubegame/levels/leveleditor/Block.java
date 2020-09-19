package io.libgdx.cubegame.levels.leveleditor;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import io.libgdx.cubegame.levels.leveleditor.util.CubeCornerResult;

public class Block {
	public ModelInstance instance;
	
	private int x;
	private int y;
	private int z;
	
	private float width;
	private float height;
	private float depth;
	
	private Material material;
	private BlendingAttribute blendingAttribute;
	
	private int primitveType = GL20.GL_TRIANGLES;
	
	public Block(int primitiveType, int x, int y, int z, float w, float h, float d) {
		this.primitveType = primitiveType;

		this.x = x;
		this.y = y;
		this.z = z;
		this.width = w;
		this.height = h;
		this.depth = d;
		
		material = createDiffuseMaterialFromImage(java.awt.Color.CYAN);
		blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = 1f;
		
		ModelBuilder builder = new ModelBuilder();
		builder.begin();
		
		MeshPartBuilder mpb = builder.part("box", primitveType, 
				VertexAttributes.Usage.Position
				| VertexAttributes.Usage.Normal 
				| VertexAttributes.Usage.ColorPacked
				| VertexAttributes.Usage.TextureCoordinates, material);
		
		new CubeCornerResult(x,y,z,w,h,d).finialize(mpb);
		
		instance = new ModelInstance(builder.end());
		instance.transform.translate(0.5f, height/2, 0.5f);
	}
	
	public Block(int x, int y, int z, float w, float h, float d) {
		this(GL20.GL_TRIANGLES, x,y,z,w,h,d);
	}
	
	public void update(Vector3 v) {
		this.x = (int)v.x;
		this.y = (int)v.y;
		this.z = (int)v.z;
		instance.transform = new Matrix4().translate(
			0.5f,
			y + height/2,
			0.5f
		);
	}
	
	public void updateWithXZ(Vector3 v) {
		this.x = (int)v.x;
		this.y = (int)v.y;
		this.z = (int)v.z;
		instance.transform = new Matrix4().translate(
			x + 0.5f,
			y + height/2,
			z + 0.5f
		);
	}

	
	public void updateSize(float w, float h, float d) {
		this.width = w;
		this.height = h;
		this.depth = d;
		
		ModelBuilder builder = new ModelBuilder();
		builder.begin();
		
		MeshPartBuilder mpb = builder.part("box", primitveType, 
				VertexAttributes.Usage.Position
				| VertexAttributes.Usage.Normal 
				| VertexAttributes.Usage.ColorPacked
				| VertexAttributes.Usage.TextureCoordinates, material);

		new CubeCornerResult(x,y,z,w,h,d).finialize(mpb);
		
		instance = new ModelInstance(builder.end());
		
		instance.transform = new Matrix4().translate(
			0.5f,
			y + height/2,
			0.5f
		);
	}
	
	private Texture createTexture(java.awt.Color c) {
		BufferedImage bufferedImage = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = bufferedImage.createGraphics();
	    
	    g2d.setColor(java.awt.Color.BLACK);
	    g2d.fillRect(0, 0, 80, 80);
	    
	    g2d.setColor(c);
	    g2d.fillRect(1, 1, 78, 78);
	    
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
			ImageIO.write(bufferedImage, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    byte[] byteArray = baos.toByteArray();
	    Pixmap mask = new Pixmap(byteArray, 0, byteArray.length);
		
	    return new Texture(mask);
	}
	private Material createDiffuseMaterialFromImage(java.awt.Color c) {
		return new Material(TextureAttribute.createAmbient(createTexture(c)));
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getDepth() {
		return depth;
	}

	public void setDepth(float depth) {
		this.depth = depth;
	}
}
