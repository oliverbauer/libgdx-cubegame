package io.libgdx.cubegame.levels;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import io.libgdx.cubegame.Config;
import io.libgdx.cubegame.animation.EnemyAnimation;
import io.libgdx.cubegame.blocks.Block;
import io.libgdx.cubegame.blocks.BlockType;
import io.libgdx.cubegame.blocks.factories.BlockFactory;
import io.libgdx.cubegame.player.Player;
import io.libgdx.cubegame.score.Score;
import io.libgdx.cubegame.screens.GameScreen;
import io.libgdx.cubegame.blocks.Arrow;

/** Generated from level1.yaml with io.libgdx.cubegame.levels.yaml.YAMLLevelFromDSL - do not modify! */
public class YAMLLevel1 extends Level {
	private Player player;
	private Block[][][] field;
	private GameScreen cubeApp;

	public YAMLLevel1(GameScreen cubeApp, PerspectiveCamera cam) {
		Score.lifeforces = 0;
		this.cubeApp = cubeApp;
		field = new Block[xlength()][ylength()][zlength()];

		field()[5][0][2] = BlockFactory.createGround(5, 0, 2);
		field()[5][0][3] = BlockFactory.createGround(5, 0, 3);
		field()[5][0][4] = BlockFactory.createGround(5, 0, 4);
		field()[5][0][5] = BlockFactory.createGround(5, 0, 5);
		field()[5][0][6] = BlockFactory.createGround(5, 0, 6);
		field()[5][0][7] = BlockFactory.createGround(5, 0, 7);
		field()[5][0][8] = BlockFactory.createGround(5, 0, 8);
		field()[4][0][1] = BlockFactory.createGround(4, 0, 1);
		field()[4][0][2] = BlockFactory.createGround(4, 0, 2);
		field()[4][0][3] = BlockFactory.createGround(4, 0, 3);
		field()[4][0][4] = BlockFactory.createGround(4, 0, 4);
		field()[4][0][5] = BlockFactory.createGround(4, 0, 5);
		field()[4][0][6] = BlockFactory.createGround(4, 0, 6);
		field()[4][0][7] = BlockFactory.createGround(4, 0, 7);
		field()[4][0][8] = BlockFactory.createGround(4, 0, 8);
		field()[4][0][9] = BlockFactory.createGround(4, 0, 9);
		field()[3][0][0] = BlockFactory.createGround(3, 0, 0);
		field()[3][0][1] = BlockFactory.createGround(3, 0, 1);
		field()[3][0][2] = BlockFactory.createGround(3, 0, 2);
		field()[3][0][3] = BlockFactory.createGround(3, 0, 3);
		field()[3][0][4] = BlockFactory.createGround(3, 0, 4);
		field()[3][0][5] = BlockFactory.createGround(3, 0, 5);
		field()[3][0][6] = BlockFactory.createGround(3, 0, 6);
		field()[3][0][7] = BlockFactory.createGround(3, 0, 7);
		field()[3][0][8] = BlockFactory.createGround(3, 0, 8);
		field()[3][0][9] = BlockFactory.createGround(3, 0, 9);
		field()[3][0][10] = BlockFactory.createGround(3, 0, 10);
		field()[2][0][0] = BlockFactory.createGround(2, 0, 0);
		field()[2][0][1] = BlockFactory.createGround(2, 0, 1);
		field()[2][0][2] = BlockFactory.createGround(2, 0, 2);
		field()[2][0][3] = BlockFactory.createGround(2, 0, 3);
		field()[2][0][4] = BlockFactory.createGround(2, 0, 4);
		field()[2][0][5] = BlockFactory.createGround(2, 0, 5);
		field()[2][0][6] = BlockFactory.createGround(2, 0, 6);
		field()[2][0][7] = BlockFactory.createGround(2, 0, 7);
		field()[2][0][8] = BlockFactory.createGround(2, 0, 8);
		field()[2][0][9] = BlockFactory.createGround(2, 0, 9);
		field()[2][0][10] = BlockFactory.createGround(2, 0, 10);
		field()[1][0][1] = BlockFactory.createGround(1, 0, 1);
		field()[1][0][2] = BlockFactory.createGround(1, 0, 2);
		field()[1][0][3] = BlockFactory.createGround(1, 0, 3);
		field()[1][0][4] = BlockFactory.createGround(1, 0, 4);
		field()[1][0][5] = BlockFactory.createGround(1, 0, 5);
		field()[1][0][6] = BlockFactory.createGround(1, 0, 6);
		field()[1][0][7] = BlockFactory.createGround(1, 0, 7);
		field()[1][0][8] = BlockFactory.createGround(1, 0, 8);
		field()[1][0][9] = BlockFactory.createGround(1, 0, 9);
		field()[0][0][2] = BlockFactory.createGround(0, 0, 2);
		field()[0][0][3] = BlockFactory.createGround(0, 0, 3);
		field()[0][0][4] = BlockFactory.createGround(0, 0, 4);
		field()[0][0][5] = BlockFactory.createGround(0, 0, 5);
		field()[0][0][6] = BlockFactory.createGround(0, 0, 6);
		field()[0][0][7] = BlockFactory.createGround(0, 0, 7);
		field()[0][0][8] = BlockFactory.createGround(0, 0, 8);

		switch (Config.difficulty) {
			case VERY_EASY:
				break;
			case EASY:
				enemyPath0(Color.RED);
				break;
			case NORMAL:
				enemyPath1(Color.BLUE);
				break;
			case HARD:
				enemyPath1(Color.BLUE);
				enemyPath2(Color.GREEN);
				break;
			case VERY_HARD:
				enemyPath1(Color.BLUE);
				enemyPath2(Color.RED);
		}

		player = new Player();
		player.x = 1;
		player.y = 1;
		player.z = 3;
		field()[player.x][player.y][player.z] = player;
		translateAllBlocks();
	}

	public void enemyPath0(Color color) {
		field()[0][2][8] = BlockFactory.createEnemy(color, 0,2,8);
		EnemyAnimation r = new EnemyAnimation(cubeApp);
		r.start = new Vector3(0,1,8);
		r.end = new Vector3(0, 1, 7);
		r.field = field;
		List<Vector3> vecs = new ArrayList<>();
		vecs.add(new Vector3(0, 1, 6));
		vecs.add(new Vector3(0, 1, 5));
		vecs.add(new Vector3(0, 1, 4));
		vecs.add(new Vector3(0, 1, 3));
		vecs.add(new Vector3(0, 1, 2));
		vecs.add(new Vector3(0, 1, 3));
		vecs.add(new Vector3(0, 1, 4));
		vecs.add(new Vector3(0, 1, 5));
		vecs.add(new Vector3(0, 1, 6));
		vecs.add(new Vector3(0, 1, 7));
		vecs.add(new Vector3(0, 1, 8));
		vecs.add(new Vector3(0, 1, 7));
		for (Vector3 v : vecs)
			r.add(v);
		field()[0][2][8].anim = r;
	}

	public void enemyPath1(Color color) {
		field()[2][2][10] = BlockFactory.createEnemy(color, 2,2,10);
		EnemyAnimation r = new EnemyAnimation(cubeApp);
		r.start = new Vector3(2,1,10);
		r.end = new Vector3(3, 1, 10);
		r.field = field;
		List<Vector3> vecs = new ArrayList<>();
		vecs.add(new Vector3(3, 1, 9));
		vecs.add(new Vector3(3, 1, 8));
		vecs.add(new Vector3(3, 1, 7));
		vecs.add(new Vector3(3, 1, 6));
		vecs.add(new Vector3(3, 1, 5));
		vecs.add(new Vector3(3, 1, 4));
		vecs.add(new Vector3(3, 1, 3));
		vecs.add(new Vector3(3, 1, 2));
		vecs.add(new Vector3(3, 1, 1));
		vecs.add(new Vector3(3, 1, 0));
		vecs.add(new Vector3(2, 1, 0));
		vecs.add(new Vector3(2, 1, 1));
		vecs.add(new Vector3(2, 1, 2));
		vecs.add(new Vector3(2, 1, 3));
		vecs.add(new Vector3(2, 1, 4));
		vecs.add(new Vector3(2, 1, 5));
		vecs.add(new Vector3(2, 1, 6));
		vecs.add(new Vector3(2, 1, 7));
		vecs.add(new Vector3(2, 1, 8));
		vecs.add(new Vector3(2, 1, 9));
		vecs.add(new Vector3(2, 1, 10));
		vecs.add(new Vector3(3, 1, 10));
		for (Vector3 v : vecs)
			r.add(v);
		field()[2][2][10].anim = r;
	}

	public void enemyPath2(Color color) {
		field()[0][2][8] = BlockFactory.createEnemy(color, 0,2,8);
		EnemyAnimation r = new EnemyAnimation(cubeApp);
		r.start = new Vector3(0,1,8);
		r.end = new Vector3(0, 1, 7);
		r.field = field;
		List<Vector3> vecs = new ArrayList<>();
		vecs.add(new Vector3(0, 1, 6));
		vecs.add(new Vector3(0, 1, 5));
		vecs.add(new Vector3(0, 1, 4));
		vecs.add(new Vector3(0, 1, 3));
		vecs.add(new Vector3(0, 1, 2));
		vecs.add(new Vector3(1, 1, 2));
		vecs.add(new Vector3(1, 1, 1));
		vecs.add(new Vector3(2, 1, 1));
		vecs.add(new Vector3(2, 1, 0));
		vecs.add(new Vector3(3, 1, 0));
		vecs.add(new Vector3(3, 1, 1));
		vecs.add(new Vector3(4, 1, 1));
		vecs.add(new Vector3(4, 1, 2));
		vecs.add(new Vector3(5, 1, 2));
		vecs.add(new Vector3(5, 1, 3));
		vecs.add(new Vector3(5, 1, 4));
		vecs.add(new Vector3(5, 1, 5));
		vecs.add(new Vector3(5, 1, 6));
		vecs.add(new Vector3(5, 1, 7));
		vecs.add(new Vector3(5, 1, 8));
		vecs.add(new Vector3(4, 1, 8));
		vecs.add(new Vector3(4, 1, 9));
		vecs.add(new Vector3(3, 1, 9));
		vecs.add(new Vector3(3, 1, 10));
		vecs.add(new Vector3(2, 1, 10));
		vecs.add(new Vector3(2, 1, 9));
		vecs.add(new Vector3(1, 1, 9));
		vecs.add(new Vector3(1, 1, 8));
		vecs.add(new Vector3(0, 1, 8));
		vecs.add(new Vector3(0, 1, 7));
		for (Vector3 v : vecs)
			r.add(v);
		field()[0][2][8].anim = r;
	}

	@Override
	public Player getPlayer() {
		return player;
	}

	@Override
	public Block[][][] field() {
		return field;
	}
	@Override
	public int xlength() {
		return 6;
	}
	@Override
	public int ylength() {
		return 3;
	}
	@Override
	public int zlength() {
		return 11;
	}
}
