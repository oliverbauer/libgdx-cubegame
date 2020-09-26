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
import io.libgdx.cubegame.blocks.types.TileElevator.ElevatorDirection;
import io.libgdx.cubegame.blocks.types.TileJumper.JumpDirection;
import io.libgdx.cubegame.blocks.Block;
import io.libgdx.cubegame.blocks.BlockType;
import io.libgdx.cubegame.blocks.factories.TileFactory;
import io.libgdx.cubegame.player.Player;
import io.libgdx.cubegame.score.Score;
import io.libgdx.cubegame.screens.GameScreen;
import io.libgdx.cubegame.blocks.Arrow;

/** Generated from level2.yaml with io.libgdx.cubegame.levels.yaml.YAMLLevelFromDSL - do not modify! */
public class YAMLLevel2 extends Level {
	private Player player;
	private Block[][][] field;
	private GameScreen cubeApp;

	public YAMLLevel2(GameScreen cubeApp, PerspectiveCamera cam) {
		Score.lifeforces = 0;
		this.cubeApp = cubeApp;
		field = new Block[xlength()][ylength()][zlength()];

		field()[5][0][2] = TileFactory.createGround(5, 0, 2);
		field()[5][0][3] = TileFactory.createGround(5, 0, 3);
		field()[5][0][4] = TileFactory.createGround(5, 0, 4);
		field()[5][0][5] = TileFactory.createGround(5, 0, 5);
		field()[5][0][6] = TileFactory.createGround(5, 0, 6);
		field()[5][0][7] = TileFactory.createGround(5, 0, 7);
		field()[5][0][8] = TileFactory.createGround(5, 0, 8);
		field()[5][0][15] = TileFactory.createGround(5, 0, 15);
		field()[5][0][16] = TileFactory.createGround(5, 0, 16);
		field()[5][0][17] = TileFactory.createGround(5, 0, 17);
		field()[5][0][18] = TileFactory.createGround(5, 0, 18);
		field()[5][0][19] = TileFactory.createGround(5, 0, 19);
		field()[5][0][20] = TileFactory.createGround(5, 0, 20);
		field()[5][0][21] = TileFactory.createGround(5, 0, 21);
		field()[4][0][1] = TileFactory.createGround(4, 0, 1);
		field()[4][0][2] = TileFactory.createGround(4, 0, 2);
		field()[4][0][3] = TileFactory.createGround(4, 0, 3);
		field()[4][0][4] = TileFactory.createGround(4, 0, 4);
		field()[4][0][5] = TileFactory.createGround(4, 0, 5);
		field()[4][0][6] = TileFactory.createGround(4, 0, 6);
		field()[4][0][7] = TileFactory.createGround(4, 0, 7);
		field()[4][0][8] = TileFactory.createGround(4, 0, 8);
		field()[4][0][9] = TileFactory.createGround(4, 0, 9);
		field()[4][0][14] = TileFactory.createGround(4, 0, 14);
		field()[4][0][15] = TileFactory.createGround(4, 0, 15);
		field()[4][0][16] = TileFactory.createGround(4, 0, 16);
		field()[4][0][17] = TileFactory.createGround(4, 0, 17);
		field()[4][0][18] = TileFactory.createGround(4, 0, 18);
		field()[4][0][19] = TileFactory.createGround(4, 0, 19);
		field()[4][0][20] = TileFactory.createGround(4, 0, 20);
		field()[4][0][21] = TileFactory.createGround(4, 0, 21);
		field()[4][0][22] = TileFactory.createGround(4, 0, 22);
		field()[3][0][0] = TileFactory.createGround(3, 0, 0);
		field()[3][0][1] = TileFactory.createGround(3, 0, 1);
		field()[3][0][2] = TileFactory.createGround(3, 0, 2);
		field()[3][0][3] = TileFactory.createGround(3, 0, 3);
		field()[3][0][4] = TileFactory.createGround(3, 0, 4);
		field()[3][0][5] = TileFactory.createGround(3, 0, 5);
		field()[3][0][6] = TileFactory.createGround(3, 0, 6);
		field()[3][0][7] = TileFactory.createGround(3, 0, 7);
		field()[3][0][8] = TileFactory.createGround(3, 0, 8);
		field()[3][0][9] = TileFactory.createGround(3, 0, 9);
		field()[3][0][10] = TileFactory.createGround(3, 0, 10);
		field()[3][0][11] = TileFactory.createJumper(Color.BLUE, JumpDirection.RIGHT, 3,0,11);
		field()[3][0][13] = TileFactory.createGround(3, 0, 13);
		field()[3][0][14] = TileFactory.createGround(3, 0, 14);
		field()[3][0][15] = TileFactory.createGround(3, 0, 15);
		field()[3][0][16] = TileFactory.createGround(3, 0, 16);
		field()[3][0][17] = TileFactory.createGround(3, 0, 17);
		field()[3][0][18] = TileFactory.createGround(3, 0, 18);
		field()[3][0][19] = TileFactory.createGround(3, 0, 19);
		field()[3][0][20] = TileFactory.createGround(3, 0, 20);
		field()[3][0][21] = TileFactory.createGround(3, 0, 21);
		field()[3][0][22] = TileFactory.createGround(3, 0, 22);
		field()[3][0][23] = TileFactory.createGround(3, 0, 23);
		field()[2][0][0] = TileFactory.createGround(2, 0, 0);
		field()[2][0][1] = TileFactory.createGround(2, 0, 1);
		field()[2][0][2] = TileFactory.createGround(2, 0, 2);
		field()[2][0][3] = TileFactory.createGround(2, 0, 3);
		field()[2][0][4] = TileFactory.createGround(2, 0, 4);
		field()[2][0][5] = TileFactory.createGround(2, 0, 5);
		field()[2][0][6] = TileFactory.createGround(2, 0, 6);
		field()[2][0][7] = TileFactory.createGround(2, 0, 7);
		field()[2][0][8] = TileFactory.createGround(2, 0, 8);
		field()[2][0][9] = TileFactory.createGround(2, 0, 9);
		field()[2][0][10] = TileFactory.createGround(2, 0, 10);
		field()[2][0][11] = TileFactory.createGround(2, 0, 11);
		field()[2][0][13] = TileFactory.createJumper(Color.BLUE, JumpDirection.LEFT, 2, 0, 13);
		field()[2][0][14] = TileFactory.createGround(2, 0, 14);
		field()[2][0][15] = TileFactory.createGround(2, 0, 15);
		field()[2][0][16] = TileFactory.createGround(2, 0, 16);
		field()[2][0][17] = TileFactory.createGround(2, 0, 17);
		field()[2][0][18] = TileFactory.createGround(2, 0, 18);
		field()[2][0][19] = TileFactory.createGround(2, 0, 19);
		field()[2][0][20] = TileFactory.createGround(2, 0, 20);
		field()[2][0][21] = TileFactory.createGround(2, 0, 21);
		field()[2][0][22] = TileFactory.createGround(2, 0, 22);
		field()[2][0][23] = TileFactory.createGround(2, 0, 23);
		field()[1][0][1] = TileFactory.createGround(1, 0, 1);
		field()[1][0][2] = TileFactory.createGround(1, 0, 2);
		field()[1][0][3] = TileFactory.createGround(1, 0, 3);
		field()[1][0][4] = TileFactory.createGround(1, 0, 4);
		field()[1][0][5] = TileFactory.createGround(1, 0, 5);
		field()[1][0][6] = TileFactory.createGround(1, 0, 6);
		field()[1][0][7] = TileFactory.createGround(1, 0, 7);
		field()[1][0][8] = TileFactory.createGround(1, 0, 8);
		field()[1][0][9] = TileFactory.createGround(1, 0, 9);
		field()[1][0][14] = TileFactory.createGround(1, 0, 14);
		field()[1][0][15] = TileFactory.createGround(1, 0, 15);
		field()[1][0][16] = TileFactory.createGround(1, 0, 16);
		field()[1][0][17] = TileFactory.createGround(1, 0, 17);
		field()[1][0][18] = TileFactory.createGround(1, 0, 18);
		field()[1][0][19] = TileFactory.createGround(1, 0, 19);
		field()[1][0][20] = TileFactory.createGround(1, 0, 20);
		field()[1][0][21] = TileFactory.createGround(1, 0, 21);
		field()[1][0][22] = TileFactory.createGround(1, 0, 22);
		field()[0][0][2] = TileFactory.createGround(0, 0, 2);
		field()[0][0][3] = TileFactory.createGround(0, 0, 3);
		field()[0][0][4] = TileFactory.createGround(0, 0, 4);
		field()[0][0][5] = TileFactory.createGround(0, 0, 5);
		field()[0][0][6] = TileFactory.createGround(0, 0, 6);
		field()[0][0][7] = TileFactory.createGround(0, 0, 7);
		field()[0][0][8] = TileFactory.createGround(0, 0, 8);
		field()[0][0][15] = TileFactory.createGround(0, 0, 15);
		field()[0][0][16] = TileFactory.createGround(0, 0, 16);
		field()[0][0][17] = TileFactory.createGround(0, 0, 17);
		field()[0][0][18] = TileFactory.createGround(0, 0, 18);
		field()[0][0][19] = TileFactory.createGround(0, 0, 19);
		field()[0][0][20] = TileFactory.createGround(0, 0, 20);
		field()[0][0][21] = TileFactory.createGround(0, 0, 21);

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
				enemyPath1(Color.BLACK);
				enemyPath2(Color.RED);
		}

		player = new Player(null, BlockType.PLAYER);
		player.x = 1;
		player.y = 1;
		player.z = 3;
		field()[player.x][player.y][player.z] = player;
		translateAllBlocks();
		field()[3][0][11].getInstance().transform.rotate(Vector3.Y, 90);
		field()[2][0][13].getInstance().transform.rotate(Vector3.Y, 270);
	}

	public void enemyPath0(Color color) {
		field()[0][2][8] = TileFactory.createEnemy(color, 0,2,8);
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
		field()[2][2][10] = TileFactory.createEnemy(color, 2,2,10);
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
		field()[0][2][8] = TileFactory.createEnemy(color, 0,2,8);
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
		return 24;
	}
}
