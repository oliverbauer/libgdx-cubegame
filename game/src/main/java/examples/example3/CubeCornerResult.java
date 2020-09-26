package examples.example3;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class CubeCornerResult {
	
	public CubeCornerResult(float x, float y, float z, float width, float height, float depth) {
		// manual of the last
		final float hw = width * 0.5f;
		final float hh = height * 0.5f;
		final float hd = depth * 0.5f;
		final float x0 = x - hw, y0 = y - hh, z0 = z - hd, x1 = x + hw, y1 = y + hh, z1 = z + hd;
		
		corner000 = new Vector3().set(x0, y0, z0);
		corner010 = new Vector3().set(x0, y1, z0);
		corner100 = new Vector3().set(x1, y0, z0);
		corner110 = new Vector3().set(x1, y1, z0);
		corner001 = new Vector3().set(x0, y0, z1);
		corner011 = new Vector3().set(x0, y1, z1);
		corner101 = new Vector3().set(x1, y0, z1);
		corner111 = new Vector3().set(x1, y1, z1);
	}
	
	Vector3 corner000, corner010, corner100, corner110, corner001, corner011, corner101, corner111;
	
	public CubeCornerResult finialize(ModelBuilder builder, Color[] colors, java.awt.Color[] awtColors, float transparency) {
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = transparency;
		
		Vector3 tmpV1 = new Vector3();
		Vector3 tmpV2 = new Vector3();

		float normalm = 0.5f;

		Vector3 nor = tmpV1.set(corner000).lerp(corner110, normalm).sub(tmpV2.set(corner001).lerp(corner111, normalm)).nor();
		int attr = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates;
		int i=0;
		Material materials = new Material(
			TextureAttribute.createDiffuse(createTexture(awtColors[i])),
			ColorAttribute.createAmbient(colors[0]),
			blendingAttribute
		);
		MeshPartBuilder mpb = builder.part("part"+0, GL20.GL_TRIANGLES,	attr, materials);
		
		mpb.rect(corner000, corner010, corner110, corner100, nor);
		
		i++;
		materials = new Material(
				TextureAttribute.createDiffuse(createTexture(awtColors[i])),
				ColorAttribute.createAmbient(colors[i]),
				blendingAttribute
			);
		mpb = builder.part("part"+i, GL20.GL_TRIANGLES,	attr, materials);
		
		mpb.rect(corner011, corner001, corner101, corner111, nor.scl(-1));
		nor = tmpV1.set(corner000).lerp(corner101, normalm).sub(tmpV2.set(corner010).lerp(corner111, normalm)).nor();
		
		i++;
		materials = new Material(
				TextureAttribute.createDiffuse(createTexture(awtColors[i])),
				ColorAttribute.createAmbient(colors[i]),
				blendingAttribute
			);
		mpb = builder.part("part"+i, GL20.GL_TRIANGLES,	attr, materials);
		mpb.rect(corner001, corner000, corner100, corner101, nor);

		i++;
		materials = new Material(
				TextureAttribute.createDiffuse(createTexture(awtColors[i])),
				ColorAttribute.createAmbient(colors[i]),
				blendingAttribute
			);
		mpb = builder.part("part"+i, GL20.GL_TRIANGLES,	attr, materials);
		mpb.rect(corner010, corner011, corner111, corner110, nor.scl(-1));
		nor = tmpV1.set(corner000).lerp(corner011, normalm).sub(tmpV2.set(corner100).lerp(corner111, normalm)).nor();

		i++;
		materials = new Material(
				TextureAttribute.createDiffuse(createTexture(awtColors[i])),
				ColorAttribute.createAmbient(colors[i]),
				blendingAttribute
			);
		mpb = builder.part("part"+i, GL20.GL_TRIANGLES,	attr, materials);
		mpb.rect(corner001, corner011, corner010, corner000, nor);

		i++;
		materials = new Material(
				TextureAttribute.createDiffuse(createTexture(awtColors[i])),
				ColorAttribute.createAmbient(colors[i]),
				blendingAttribute
			);
		mpb = builder.part("part"+i, GL20.GL_TRIANGLES,	attr, materials);
		mpb.rect(corner100, corner110, corner111, corner101, nor.scl(-1));

		return this;
	}

	public Texture createTexture(java.awt.Color c) {
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
	
	public Material createDiffuseMaterialFromImage(java.awt.Color c) {
		return new Material(TextureAttribute.createAmbient(createTexture(c)));
	}
}
