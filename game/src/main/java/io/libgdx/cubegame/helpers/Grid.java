package io.libgdx.cubegame.helpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class Grid {
	public static Model createGridModel() {
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		final float GRID_MIN = -10f;
		final float GRID_MAX = 10f;
		final float GRID_STEP = 1f;
		MeshPartBuilder builder = modelBuilder.part("grid", GL20.GL_LINES, Usage.Position, new Material());
		builder.setColor(Color.GREEN);
		for (float t = GRID_MIN; t <= GRID_MAX; t += GRID_STEP) {
			builder.line(t, 0, GRID_MIN, t, 0, GRID_MAX);
			builder.line(GRID_MIN, 0, t, GRID_MAX, 0, t);
		}
		builder = modelBuilder.part("axes", GL20.GL_LINES, Usage.Position, new Material());
		builder.setColor(Color.RED);
		builder.line(0, 0, 0, 100, 0, 0);
		builder.setColor(Color.GREEN);
		builder.line(0, 0, 0, 0, 100, 0);
		builder.setColor(Color.BLUE);
		builder.line(0, 0, 0, 0, 0, 100);
		return modelBuilder.end();
	}
	
	// Debugging for https://github.com/oliverbauer/libgdx-cubegame/issues/1
	public static Model createLine(int x1, int y1, int z1, int x2, int y2, int z2) {
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		MeshPartBuilder builder = modelBuilder.part("grid", GL20.GL_LINES, Usage.Position, new Material());
		builder.line(x1, y1, z1, x2, y2, z2);
		return modelBuilder.end();

	}
}
