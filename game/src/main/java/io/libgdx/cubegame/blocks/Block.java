package io.libgdx.cubegame.blocks;

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

import javax.imageio.ImageIO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import io.libgdx.cubegame.animation.Animation;
import io.libgdx.cubegame.levels.Level;
import io.libgdx.cubegame.player.Player;
import io.libgdx.cubegame.player.PlayerDirection;

public class Block implements Disposable {
	private Model model;
	protected ModelInstance instance;
	private BlockType type;

	public int x = -1;
	public int y = -1;
	public int z = -1;
	
	public Color color;
	
	public Animation anim;
	
	public void playerMovedOn(Level level) {
		
	}
	
	public void enemyMovedOn(Level level) {
		
	}
	
	/**
	 * Default is true. Tiles need to override if needed (e.g. not allowed to move with red color on blue lifeforce)
	 * 
	 * @param player
	 * @param direction
	 * @return
	 */
	public boolean allowed(Player player, PlayerDirection direction) {
		return true;
	}
	
	public Vector3 getPosition() {
		return new Vector3(x, y, z);
	}
	
	public void setPosition(float x, float y, float z){
		instance.transform = new Matrix4().translate(x, y, z);
	}

	public void setModel(Model m) {
		this.model = m;
		this.instance = new ModelInstance(m);
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
	
	public void setType(BlockType type) {
		this.type = type;
	}

	public BlockType getType() {
		return type;
	}

	@Override
	public String toString() {
		return super.toString()+" Block [x="	+ x + ", y=" + y + ", z=" + z + "] type "+type;
	}
	
	public Texture createTextureWithText(java.awt.Color c, String text, int size) {
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
