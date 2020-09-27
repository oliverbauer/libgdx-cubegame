package io.libgdx.cubegame.blocks.types;

import java.awt.AlphaComposite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import io.libgdx.cubegame.assets.Assets;
import io.libgdx.cubegame.blocks.Block;
import io.libgdx.cubegame.blocks.factories.CubeCornerResult;
import io.libgdx.cubegame.blocks.factories.TextureFactory;
import io.libgdx.cubegame.blocks.factories.TileFactory;
import io.libgdx.cubegame.levels.Level;
import io.libgdx.cubegame.player.Player;
import io.libgdx.cubegame.player.PlayerDirection;
import io.libgdx.cubegame.score.Score;

public class TileLifeforce extends Block {
	
	public TileLifeforce(Color color) {
		this.color = color;
		
		BlendingAttribute blendingAttribute = new BlendingAttribute();
		blendingAttribute.opacity = .5f;

		ModelBuilder builder = new ModelBuilder();
		builder.begin();

		float width = 0.9f;
		float height = 0.1f;
		float depth = 0.9f;

		List<Color> colors = Arrays.asList(Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.BLACK, Color.ORANGE);
		colors = Arrays.asList(color, color, color, color, color, color);

		java.awt.Color awtColor = TextureFactory.toAWTColor(color);
		
		List<java.awt.Color> awtcolors = Arrays.asList(awtColor,awtColor,awtColor,awtColor,awtColor,awtColor);
		float transparency = 1f;
		new CubeCornerResult(0,0,0,width,height,depth)
			.finialize(builder, colors.toArray(new Color[6]), awtcolors.toArray(new java.awt.Color[6]), transparency);
		
		setModel(builder.end());
		
		getInstance().materials.get(0).set(TextureAttribute.createDiffuse(createTextureWithText(awtColor, "", 40)));
		getInstance().materials.get(1).set(TextureAttribute.createDiffuse(createTextureWithText(awtColor, "", 40)));
		getInstance().materials.get(2).set(TextureAttribute.createDiffuse(createTextureWithText(awtColor, "", 40)));
		// Top side:
		getInstance().materials.get(3).set(
			TextureAttribute.createDiffuse(createTextureWithText(awtColor, "$", 40))
		);
		getInstance().materials.get(4).set(TextureAttribute.createDiffuse(createTextureWithText(awtColor, "", 40)));
		getInstance().materials.get(5).set(TextureAttribute.createDiffuse(createTextureWithText(awtColor, "", 40)));
		
	}
	
	@Override
	public void enemyMovedOn(Level level) {
		// cleanup
		dispose(); 
		
		level.field()[x][y][z] = TileFactory.createGround(x, y, z);
	}
	
	@Override
	public void playerMovedOn(Level level) {
		// cleanup
		dispose(); 
		
		level.field()[x][y][z] = TileFactory.createGround(x, y, z);
		
		if (Assets.instance().soundLifeforce != null) {
			Assets.instance().soundLifeforce.play();
		}
			
		Score.lifeforces++;
		if (Score.lifeforces >= level.requiredLifeforces()) {
			level.setCompleted(true);
		}
	}
	
	@Override
	public boolean allowed(Player player, PlayerDirection direction) {
		Color lifeforceColor = color;
		Color cubeColor = null;
		switch (direction) {
			case FORWARD:
				cubeColor = player.left;
				// old-school
				cubeColor = player.right;
				break;
			case BACK:
				cubeColor = player.right;
				// old-school
				cubeColor = player.left;
				break;
			case LEFT:
				cubeColor = player.front;
				// old-school
				cubeColor = player.back;
				break;
			case RIGHT:
				cubeColor = player.back;
				// old-school
				cubeColor = player.front;
				break;
			default:
				break;
		}
		
		return lifeforceColor.equals(cubeColor);
	}
	
	/**
	 * Improve see e.g. http://www.computing.northampton.ac.uk/~gary/csy3019/CSY3019SectionC.html
	 * 
	 * @param color
	 * @return
	 */
	private Texture createLifeforceTexture(Color color) {
		final int width = 80;
		final int height = 80;
		
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(toAWTColor(color));

        Font font = new Font("Verdana", Font.PLAIN, 40);
        g2d.setFont(font);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fm = g2d.getFontMetrics();

        String option = "$";

        int x1 = (width - fm.stringWidth(option)) / 2;
        int y1 = ((height - fm.getHeight()) / 2);
        int rule = AlphaComposite.SRC_OVER;
        

        g2d.setComposite(AlphaComposite.getInstance(rule , 1f));
        g2d.drawString(option, x1, y1 + fm.getAscent());

        
        g2d.setComposite(AlphaComposite.getInstance(rule , 0.3f));
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
}
