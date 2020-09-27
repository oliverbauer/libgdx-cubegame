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

/** Generated from level3.yaml with io.libgdx.cubegame.levels.yaml.YAMLLevelFromDSL - do not modify! */
public class YAMLLevel3 extends Level {
	private Player player;
	private Block[][][] field;
	private GameScreen cubeApp;

	public YAMLLevel3(GameScreen cubeApp, PerspectiveCamera cam) {
		Score.lifeforces = 0;
		this.cubeApp = cubeApp;
		field = new Block[xlength()][ylength()][zlength()];

		Arrow arrow1 = new Arrow();
		arrow1.start = new Vector3(5, 1, 0);
		arrow1.end   = new Vector3(5, 3, 0);
		field()[5][2][0] = arrow1;
		field()[5][0][0] = TileFactory.createElevator(ElevatorDirection.UP, 5, 0, 0);
		field()[5][0][1] = TileFactory.createGround(5, 0, 1);
		field()[5][0][2] = TileFactory.createGround(5, 0, 2);
		field()[5][0][3] = TileFactory.createGround(5, 0, 3);
		field()[5][0][4] = TileFactory.createGround(5, 0, 4);
		field()[5][0][5] = TileFactory.createGround(5, 0, 5);
		field()[5][0][6] = TileFactory.createGround(5, 0, 6);
		field()[5][0][7] = TileFactory.createGround(5, 0, 7);
		field()[5][0][8] = TileFactory.createGround(5, 0, 8);
		field()[5][0][9] = TileFactory.createGround(5, 0, 9);
		field()[5][0][10] = TileFactory.createGround(5, 0, 10);
		field()[4][0][0] = TileFactory.createGround(4, 0, 0);
		field()[4][0][1] = TileFactory.createGround(4, 0, 1);
		field()[4][0][2] = TileFactory.createGround(4, 0, 2);
		field()[4][0][3] = TileFactory.createGround(4, 0, 3);
		field()[4][0][4] = TileFactory.createGround(4, 0, 4);
		field()[4][0][5] = TileFactory.createGround(4, 0, 5);
		field()[4][0][6] = TileFactory.createGround(4, 0, 6);
		field()[4][0][7] = TileFactory.createGround(4, 0, 7);
		field()[4][0][8] = TileFactory.createGround(4, 0, 8);
		field()[4][0][9] = TileFactory.createGround(4, 0, 9);
		field()[4][0][10] = TileFactory.createGround(4, 0, 10);
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
		field()[1][0][0] = TileFactory.createGround(1, 0, 0);
		field()[1][0][1] = TileFactory.createGround(1, 0, 1);
		field()[1][0][2] = TileFactory.createGround(1, 0, 2);
		field()[1][0][3] = TileFactory.createGround(1, 0, 3);
		field()[1][0][4] = TileFactory.createGround(1, 0, 4);
		field()[1][0][5] = TileFactory.createGround(1, 0, 5);
		field()[1][0][6] = TileFactory.createGround(1, 0, 6);
		field()[1][0][7] = TileFactory.createGround(1, 0, 7);
		field()[1][0][8] = TileFactory.createGround(1, 0, 8);
		field()[1][0][9] = TileFactory.createGround(1, 0, 9);
		field()[1][0][10] = TileFactory.createGround(1, 0, 10);
		field()[0][0][0] = TileFactory.createGround(0, 0, 0);
		field()[0][0][1] = TileFactory.createGround(0, 0, 1);
		field()[0][0][2] = TileFactory.createGround(0, 0, 2);
		field()[0][0][3] = TileFactory.createGround(0, 0, 3);
		field()[0][0][4] = TileFactory.createGround(0, 0, 4);
		field()[0][0][5] = TileFactory.createGround(0, 0, 5);
		field()[0][0][6] = TileFactory.createGround(0, 0, 6);
		field()[0][0][7] = TileFactory.createGround(0, 0, 7);
		field()[0][0][8] = TileFactory.createGround(0, 0, 8);
		field()[0][0][9] = TileFactory.createGround(0, 0, 9);
		field()[0][0][10] = TileFactory.createGround(0, 0, 10);
		field()[5][3][0] = TileFactory.createGround(5, 3, 0);
		field()[5][3][1] = TileFactory.createGround(5, 3, 1);
		field()[5][3][2] = TileFactory.createGround(5, 3, 2);
		field()[5][3][3] = TileFactory.createGround(5, 3, 3);
		field()[5][3][4] = TileFactory.createGround(5, 3, 4);
		field()[5][3][5] = TileFactory.createGround(5, 3, 5);
		field()[5][3][6] = TileFactory.createGround(5, 3, 6);
		field()[5][3][7] = TileFactory.createGround(5, 3, 7);
		field()[5][3][8] = TileFactory.createGround(5, 3, 8);
		field()[5][3][9] = TileFactory.createGround(5, 3, 9);
		field()[5][3][10] = TileFactory.createGround(5, 3, 10);
		Arrow arrow2 = new Arrow();
		arrow2.start = new Vector3(4, 4, 0);
		arrow2.end   = new Vector3(4, 0, 0);
		field()[4][5][0] = arrow2;
		field()[4][3][0] = TileFactory.createElevator(ElevatorDirection.DOWN, 4, 3, 0);
		field()[4][3][1] = TileFactory.createGround(4, 3, 1);
		field()[4][3][2] = TileFactory.createGround(4, 3, 2);
		field()[4][3][3] = TileFactory.createGround(4, 3, 3);
		field()[4][3][4] = TileFactory.createGround(4, 3, 4);
		field()[4][3][5] = TileFactory.createGround(4, 3, 5);
		field()[4][3][6] = TileFactory.createGround(4, 3, 6);
		field()[4][3][7] = TileFactory.createGround(4, 3, 7);
		field()[4][3][8] = TileFactory.createGround(4, 3, 8);
		field()[4][3][9] = TileFactory.createGround(4, 3, 9);
		field()[4][3][10] = TileFactory.createGround(4, 3, 10);
		field()[3][3][0] = TileFactory.createGround(3, 3, 0);
		field()[3][3][1] = TileFactory.createGround(3, 3, 1);
		field()[3][3][9] = TileFactory.createGround(3, 3, 9);
		field()[3][3][10] = TileFactory.createGround(3, 3, 10);
		field()[2][3][0] = TileFactory.createGround(2, 3, 0);
		field()[2][3][1] = TileFactory.createGround(2, 3, 1);
		field()[2][3][9] = TileFactory.createGround(2, 3, 9);
		field()[2][3][10] = TileFactory.createGround(2, 3, 10);
		field()[1][3][0] = TileFactory.createGround(1, 3, 0);
		field()[1][3][1] = TileFactory.createGround(1, 3, 1);
		field()[1][3][2] = TileFactory.createGround(1, 3, 2);
		field()[1][3][3] = TileFactory.createGround(1, 3, 3);
		field()[1][3][4] = TileFactory.createGround(1, 3, 4);
		field()[1][3][5] = TileFactory.createGround(1, 3, 5);
		field()[1][3][6] = TileFactory.createGround(1, 3, 6);
		field()[1][3][7] = TileFactory.createGround(1, 3, 7);
		field()[1][3][8] = TileFactory.createGround(1, 3, 8);
		field()[1][3][9] = TileFactory.createGround(1, 3, 9);
		field()[1][3][10] = TileFactory.createGround(1, 3, 10);
		field()[0][3][0] = TileFactory.createGround(0, 3, 0);
		field()[0][3][1] = TileFactory.createGround(0, 3, 1);
		field()[0][3][2] = TileFactory.createGround(0, 3, 2);
		field()[0][3][3] = TileFactory.createGround(0, 3, 3);
		field()[0][3][4] = TileFactory.createGround(0, 3, 4);
		field()[0][3][5] = TileFactory.createGround(0, 3, 5);
		field()[0][3][6] = TileFactory.createGround(0, 3, 6);
		field()[0][3][7] = TileFactory.createGround(0, 3, 7);
		field()[0][3][8] = TileFactory.createGround(0, 3, 8);
		field()[0][3][9] = TileFactory.createGround(0, 3, 9);
		field()[0][3][10] = TileFactory.createGround(0, 3, 10);

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
	}

	public void enemyPath0(Color color) {
		field()[0][2][8] = TileFactory.createEnemy(color, 0,2,8);
		EnemyAnimation r = new EnemyAnimation(cubeApp,0,2,8);
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
		EnemyAnimation r = new EnemyAnimation(cubeApp,2,2,10);
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
		field()[0][5][8] = TileFactory.createEnemy(color, 0,5,8);
		EnemyAnimation r = new EnemyAnimation(cubeApp,0,5,8);
		r.start = new Vector3(0,4,8);
		r.end = new Vector3(0, 4, 7);
		r.field = field;
		List<Vector3> vecs = new ArrayList<>();
		vecs.add(new Vector3(0, 4, 6));
		vecs.add(new Vector3(0, 4, 5));
		vecs.add(new Vector3(0, 4, 4));
		vecs.add(new Vector3(0, 4, 3));
		vecs.add(new Vector3(0, 4, 2));
		vecs.add(new Vector3(1, 4, 2));
		vecs.add(new Vector3(1, 4, 1));
		vecs.add(new Vector3(2, 4, 1));
		vecs.add(new Vector3(2, 4, 0));
		vecs.add(new Vector3(3, 4, 0));
		vecs.add(new Vector3(3, 4, 1));
		vecs.add(new Vector3(4, 4, 1));
		vecs.add(new Vector3(4, 4, 2));
		vecs.add(new Vector3(5, 4, 2));
		vecs.add(new Vector3(5, 4, 3));
		vecs.add(new Vector3(5, 4, 4));
		vecs.add(new Vector3(5, 4, 5));
		vecs.add(new Vector3(5, 4, 6));
		vecs.add(new Vector3(5, 4, 7));
		vecs.add(new Vector3(5, 4, 8));
		vecs.add(new Vector3(4, 4, 8));
		vecs.add(new Vector3(4, 4, 9));
		vecs.add(new Vector3(3, 4, 9));
		vecs.add(new Vector3(3, 4, 10));
		vecs.add(new Vector3(2, 4, 10));
		vecs.add(new Vector3(2, 4, 9));
		vecs.add(new Vector3(1, 4, 9));
		vecs.add(new Vector3(1, 4, 8));
		vecs.add(new Vector3(0, 4, 8));
		vecs.add(new Vector3(0, 4, 7));
		for (Vector3 v : vecs)
			r.add(v);
		field()[0][5][8].anim = r;
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
		return 6;
	}
	@Override
	public int zlength() {
		return 11;
	}
}
