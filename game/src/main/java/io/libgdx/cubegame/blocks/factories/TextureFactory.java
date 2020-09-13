package io.libgdx.cubegame.blocks.factories;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

// TODO Cleanup this mess...
public class TextureFactory {
	public static Texture createTexture(Color color) {
		BufferedImage bufferedImage = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.fillRect(0, 0, 80, 80);
        g2d.setColor(toAWTColor(color));
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
			ImageIO.write(bufferedImage, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
        byte[] byteArray = baos.toByteArray();
        Pixmap mask = new Pixmap(byteArray, 0, byteArray.length);
		return new Texture( mask );
	}
	
	public static Texture createTextureWithBorder(Color color, java.awt.Color borderColor) {
		BufferedImage bufferedImage = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        
        g2d.setColor(borderColor);
        g2d.fillRect(0, 0, 80, 980);
        
        g2d.setColor(toAWTColor(color));
        g2d.fillRect(2, 2, 76, 76);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
			ImageIO.write(bufferedImage, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
        byte[] byteArray = baos.toByteArray();
        Pixmap mask = new Pixmap(byteArray, 0, byteArray.length);
		return new Texture( mask );
	}
	
	public static Texture createLifeforceTexture(Color color) {
		final int width = 80;
		final int height = 80;
		
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(toAWTColor(color));

        Font font = new Font("Verdana", Font.PLAIN, 20);
        g2d.setFont(font);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fm = g2d.getFontMetrics();

        String option = "$";

        int x1 = (width - fm.stringWidth(option)) / 2;
        int y1 = ((height - fm.getHeight()) / 2);

        int rule = AlphaComposite.SRC_OVER;
        Composite comp = AlphaComposite.getInstance(rule , 0.7f);
        g2d.setComposite(comp );
        
        g2d.drawString(option, x1, y1 + fm.getAscent());
        g2d.fillRect(x1 - 20, y1 - 10, fm.stringWidth(option) + 40,fm.getHeight() + 20);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
			ImageIO.write(bufferedImage, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
        byte[] byteArray = baos.toByteArray();
		return new Texture(new Pixmap(byteArray, 0, byteArray.length));
	}

	
	public static Texture createJumperTexture(Color color) {
		final int width = 80;
		final int height = 80;
		
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(toAWTColor(color));

        Font font = new Font("Verdana", Font.PLAIN, 20);
        g2d.setFont(font);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fm = g2d.getFontMetrics();

        String option = "v";

        int x1 = (width - fm.stringWidth(option)) / 2;
        int y1 = ((height - fm.getHeight()) / 2);

        int rule = AlphaComposite.SRC_OVER;
        Composite comp = AlphaComposite.getInstance(rule , 0.7f);
        g2d.setComposite(comp );
        
        g2d.drawString(option, x1, y1 + fm.getAscent());
        g2d.fillRect(x1 - 20, y1 - 10, fm.stringWidth(option) + 40,fm.getHeight() + 20);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
			ImageIO.write(bufferedImage, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
        byte[] byteArray = baos.toByteArray();
		return new Texture(new Pixmap(byteArray, 0, byteArray.length));
	}

	public static Texture createElevatorTexture(Color color) {
		final int width = 80;
		final int height = 80;
		
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(toAWTColor(color));

        Font font = new Font("Verdana", Font.PLAIN, 60);
        g2d.setFont(font);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fm = g2d.getFontMetrics();

        String option = "E";

        int x1 = (width - fm.stringWidth(option)) / 2;
        int y1 = ((height - fm.getHeight()) / 2);

        int rule = AlphaComposite.SRC_OVER;
        Composite comp = AlphaComposite.getInstance(rule , 0.7f);
        g2d.setComposite(comp );
        
        g2d.drawString(option, x1, y1 + fm.getAscent());
//        g2d.fillRect(x1 - 20, y1 - 10, fm.stringWidth(option) + 40,fm.getHeight() + 20);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
			ImageIO.write(bufferedImage, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
        byte[] byteArray = baos.toByteArray();
		return new Texture(new Pixmap(byteArray, 0, byteArray.length));
	}
	
	public static Texture createTexture(int width, int height, String option, Font font, Color fontColor, Color backgroundColor) {
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        if (backgroundColor != null) {
        	g2d.setColor(toAWTColor(backgroundColor));
        } else {
        	g2d.setColor(new java.awt.Color(1, 1, 1, 0));
        }
        g2d.fillRect(0, 0, width, height);
        if (fontColor != null) {
        	g2d.setColor(toAWTColor(fontColor));
        }

        g2d.setFont(font);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fm = g2d.getFontMetrics();

        int x1 = (width - fm.stringWidth(option)) / 2;
        int y1 = ((height - fm.getHeight()) / 2);

        int rule = AlphaComposite.SRC_OVER;
        Composite comp = AlphaComposite.getInstance(rule , 0.7f);
        g2d.setComposite(comp );
        
        g2d.drawString(option, x1, y1 + fm.getAscent());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
			ImageIO.write(bufferedImage, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
        byte[] byteArray = baos.toByteArray();
		return new Texture(new Pixmap(byteArray, 0, byteArray.length));
	}
	
	private static java.awt.Color toAWTColor(Color color) {
        if (color == Color.BLACK) {
        	return java.awt.Color.BLACK;
        } else if (color == Color.YELLOW) {
        	return java.awt.Color.YELLOW;
        } else if (color == Color.BLUE) {
        	return java.awt.Color.BLUE;
        } else if (color == Color.RED) {
        	return java.awt.Color.RED;
        } else if (color == Color.GREEN) {
        	return java.awt.Color.GREEN;
        } else if (color == Color.CYAN) {
        	return java.awt.Color.CYAN;
        } else {
        	throw new RuntimeException("Unknown Color! "+color);
        }
	}
}
