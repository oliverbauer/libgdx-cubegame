package examples.example3;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;

import examples.example3.Example3PlayerMovement.PlayerDirection;

public class Player extends Block {

	// Initial colors... when player moves, those will be "switched"
	private Color front = Color.BLACK;
	private Color right = Color.YELLOW;
	private Color bottom = Color.BLUE;
	private Color left = Color.RED;
	private Color top = Color.GREEN;
	private Color back = Color.CYAN;
	
	private java.awt.Color frontAWT = java.awt.Color.BLACK;
	private java.awt.Color rightAWT = java.awt.Color.YELLOW;
	private java.awt.Color bottomAWT = java.awt.Color.BLUE;
	private java.awt.Color leftAWT = java.awt.Color.RED;
	private java.awt.Color topAWT = java.awt.Color.GREEN;
	private java.awt.Color backAWT = java.awt.Color.CYAN;
	
	public boolean isMoving = false;
	
	public Player(int x, int y, int z) {
		super(x, y, z);
		
		create();
	}

	public void rotate(PlayerDirection rot) {
		if (rot == PlayerDirection.LEFT) {
			Color temp = top;
			top = back;
			back = bottom;
			bottom = front;
			front = temp;
			
			java.awt.Color tempAWT = topAWT;
			topAWT = backAWT;
			backAWT = bottomAWT;
			bottomAWT = frontAWT;
			frontAWT = tempAWT;
		} else if (rot == PlayerDirection.RIGHT) {
			Color temp = front;
			front = bottom;
			bottom = back;
			back = top;
			top = temp;
			
			java.awt.Color tempAWT = frontAWT;
			frontAWT = bottomAWT;
			bottomAWT = backAWT;
			backAWT = topAWT;
			topAWT = tempAWT;

		} else if (rot == PlayerDirection.FORWARD) {
			Color temp = bottom;
			bottom = left;
			left = top;
			top = right;
			right = temp;
			
			java.awt.Color tempAWT = bottomAWT;
			bottomAWT = leftAWT;
			leftAWT = topAWT;
			topAWT = rightAWT;
			rightAWT = tempAWT;
		} else if (rot == PlayerDirection.BACK) {
			Color temp = right;
			right = top;
			top = left;
			left = bottom;
			bottom = temp;
			
			java.awt.Color tempAWT = rightAWT;
			rightAWT = topAWT;
			topAWT = leftAWT;
			leftAWT = bottomAWT;
			bottomAWT = tempAWT;
		}

		create();
	}
	
	private void create() {
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = .5f;

		ModelBuilder builder = new ModelBuilder();
		builder.begin();


		float width = 0.8f;
		float height = 0.8f;
		float depth = 0.8f;
		float size = 0.9f;
		
		float min = 0.1f;
		float max = 0.7f;
		
		boolean innerCubeBlack = false;
		
		List<java.awt.Color>  awtBlack = Arrays.asList(java.awt.Color.BLACK, java.awt.Color.BLACK, java.awt.Color.BLACK, java.awt.Color.BLACK, java.awt.Color.BLACK, java.awt.Color.BLACK);
		List<java.awt.Color>  awtCube = Arrays.asList(frontAWT, backAWT, bottomAWT, topAWT, leftAWT, rightAWT);
		List<Color> colorListCube = Arrays.asList(front, back, bottom, top, left, right);
		List<Color> colorListCubeBlack = Arrays.asList(Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK,Color.BLACK);

		
		java.awt.Color[] colorsAWTBlack = awtBlack.toArray(new java.awt.Color[6]);
		java.awt.Color[] colorsAWTCube = awtCube.toArray(new java.awt.Color[6]);
		Color[] colorsCube = colorListCube.toArray(new Color[6]);
		Color[] blackCube = colorListCubeBlack.toArray(new Color[6]);

		java.awt.Color[] awtColors;
		Color[] gdxColors;
		
		if (innerCubeBlack) {
			awtColors = colorsAWTBlack;
			gdxColors = blackCube;
		} else {
			awtColors = colorsAWTCube;
			gdxColors = colorsCube;
		}
		
		float transparency = 1f;
		
		new CubeCornerResult(0,0,0,size,size,size).finialize(builder, gdxColors, awtColors, transparency);
		
		width = min;
		height = max;
		depth = max;

		if (innerCubeBlack) {
			awtColors = colorsAWTCube;
			gdxColors = colorsCube;
		} else {
			awtColors = colorsAWTBlack;
			gdxColors = blackCube;
		}
		
		new CubeCornerResult(0+size/2,0,0,width,height,depth).finialize(builder, gdxColors, awtColors, transparency);
		new CubeCornerResult(0-size/2,0,0,width,height,depth).finialize(builder, gdxColors, awtColors, transparency);
		
		width = max;
		height = min;
		depth = max;
		new CubeCornerResult(0,0+size/2,0,width,height,depth).finialize(builder, gdxColors, awtColors, transparency);
		new CubeCornerResult(0,0-size/2,0,width,height,depth).finialize(builder, gdxColors, awtColors, transparency);

		width = max;
		height = max;
		depth = min;
		new CubeCornerResult(0,0,0+size/2,width,height,depth).finialize(builder, gdxColors, awtColors, transparency);
		new CubeCornerResult(0,0,0-size/2,width,height,depth).finialize(builder, gdxColors, awtColors, transparency);

		instance = new ModelInstance(builder.end());
		
		if (innerCubeBlack) {
			instance.materials.get(0).set(TextureAttribute.createDiffuse(createTextureWithText(java.awt.Color.BLACK, "", 20)));
			instance.materials.get(1).set(TextureAttribute.createDiffuse(createTextureWithText(java.awt.Color.BLACK, "", 20)));
			instance.materials.get(2).set(TextureAttribute.createDiffuse(createTextureWithText(java.awt.Color.BLACK, "", 20)));
			instance.materials.get(3).set(TextureAttribute.createDiffuse(createTextureWithText(java.awt.Color.BLACK, "", 20)));
			instance.materials.get(4).set(TextureAttribute.createDiffuse(createTextureWithText(java.awt.Color.BLACK, "", 20)));
			instance.materials.get(5).set(TextureAttribute.createDiffuse(createTextureWithText(java.awt.Color.BLACK, "", 20)));
		} else {
			instance.materials.get(0).set(TextureAttribute.createDiffuse(createTextureWithText(frontAWT, "", 20)));
			instance.materials.get(1).set(TextureAttribute.createDiffuse(createTextureWithText(backAWT, "", 20)));
			instance.materials.get(2).set(TextureAttribute.createDiffuse(createTextureWithText(bottomAWT, "", 20)));
			instance.materials.get(3).set(TextureAttribute.createDiffuse(createTextureWithText(topAWT, "", 20)));
			instance.materials.get(4).set(TextureAttribute.createDiffuse(createTextureWithText(leftAWT, "", 20)));
			instance.materials.get(5).set(TextureAttribute.createDiffuse(createTextureWithText(rightAWT, "", 20)));
		}
		
		instance.transform = new Matrix4().translate(
			x + Example3PlayerMovement.xOffsetPlayer, 
			y + Example3PlayerMovement.yOffsetPlayer, 
			z + Example3PlayerMovement.zOffsetPlayer);
	}
	
	private Texture createTextureWithText(java.awt.Color c, String text, int size) {
		BufferedImage bufferedImage = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = bufferedImage.createGraphics();
	    
	    int scale = 1;
	    int width = scale*80;
	    int height =scale*80;
	    
	    g2d.setColor(java.awt.Color.BLACK);
	    g2d.fillRect(0, 0, width, height);
	    
	    g2d.setColor(c);
	    g2d.fillRect(1, 1, width-2, height-2);
	    
	    
	    
	    String fName = "/cubegame/font/rocketfuel.ttf";
	    InputStream is = Player.class.getResourceAsStream(fName);
	    Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, is);
			font = font.deriveFont(Font.BOLD, size);
		} catch (FontFormatException | IOException e) {
			font = new Font("Verdana", Font.PLAIN, 20);
		}
		
        g2d.setFont(font);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fm = g2d.getFontMetrics();

        int x1 = (width - fm.stringWidth(text)) / 2;
        int y1 = ((height - fm.getHeight()) / 2);

        int rule = AlphaComposite.SRC_OVER;
        Composite comp = AlphaComposite.getInstance(rule , 0.7f);
        g2d.setComposite(comp );
        if (c == java.awt.Color.BLACK) {
        	g2d.setColor(java.awt.Color.WHITE);
        } else {
        	g2d.setColor(java.awt.Color.BLACK);
        }
        g2d.drawString(text, x1, y1 + fm.getAscent());
	    
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

}
