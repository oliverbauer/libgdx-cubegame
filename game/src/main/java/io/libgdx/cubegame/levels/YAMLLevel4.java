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

/** Generated from level4.yaml with io.libgdx.cubegame.levels.yaml.YAMLLevelFromDSL - do not modify! */
public class YAMLLevel4 extends Level {
	private Player player;
	private Block[][][] field;
	private GameScreen cubeApp;

	public YAMLLevel4(GameScreen cubeApp, PerspectiveCamera cam) {
		Score.lifeforces = 0;
		this.cubeApp = cubeApp;
		field = new Block[xlength()][ylength()][zlength()];

		Arrow arrow1 = new Arrow();
		arrow1.start = new Vector3(9, 1, 0);
		arrow1.end   = new Vector3(9, 3, 0);
		field()[9][2][0] = arrow1;
		field()[9][0][0] = TileFactory.createElevator(ElevatorDirection.UP, 9, 0, 0);
		field()[9][0][1] = TileFactory.createGround(9, 0, 1);
		field()[9][0][2] = TileFactory.createGround(9, 0, 2);
		field()[9][0][3] = TileFactory.createGround(9, 0, 3);
		field()[9][0][5] = TileFactory.createGround(9, 0, 5);
		field()[9][0][6] = TileFactory.createGround(9, 0, 6);
		field()[9][0][7] = TileFactory.createGround(9, 0, 7);
		Arrow arrow2 = new Arrow();
		arrow2.start = new Vector3(9, 1, 8);
		arrow2.end   = new Vector3(9, 3, 8);
		field()[9][2][8] = arrow2;
		field()[9][0][8] = TileFactory.createElevator(ElevatorDirection.UP, 9, 0, 8);
		field()[8][0][0] = TileFactory.createGround(8, 0, 0);
		field()[8][0][1] = TileFactory.createGround(8, 0, 1);
		field()[8][0][2] = TileFactory.createGround(8, 0, 2);
		field()[8][0][3] = TileFactory.createGround(8, 0, 3);
		field()[8][0][5] = TileFactory.createGround(8, 0, 5);
		field()[8][0][6] = TileFactory.createGround(8, 0, 6);
		field()[8][0][7] = TileFactory.createGround(8, 0, 7);
		field()[8][0][8] = TileFactory.createGround(8, 0, 8);
		field()[7][0][0] = TileFactory.createGround(7, 0, 0);
		field()[7][0][1] = TileFactory.createGround(7, 0, 1);
		field()[7][0][2] = TileFactory.createGround(7, 0, 2);
		field()[7][0][3] = TileFactory.createGround(7, 0, 3);
		field()[7][0][5] = TileFactory.createGround(7, 0, 5);
		field()[7][0][6] = TileFactory.createGround(7, 0, 6);
		field()[7][0][7] = TileFactory.createGround(7, 0, 7);
		field()[7][0][8] = TileFactory.createGround(7, 0, 8);
		field()[6][0][0] = TileFactory.createGround(6, 0, 0);
		field()[6][0][1] = TileFactory.createGround(6, 0, 1);
		field()[6][0][2] = TileFactory.createGround(6, 0, 2);
		field()[6][0][3] = TileFactory.createGround(6, 0, 3);
		field()[6][0][5] = TileFactory.createGround(6, 0, 5);
		field()[6][0][6] = TileFactory.createGround(6, 0, 6);
		field()[6][0][7] = TileFactory.createGround(6, 0, 7);
		field()[6][0][8] = TileFactory.createGround(6, 0, 8);
		field()[5][0][0] = TileFactory.createGround(5, 0, 0);
		field()[5][0][1] = TileFactory.createGround(5, 0, 1);
		field()[5][0][2] = TileFactory.createGround(5, 0, 2);
		field()[5][0][3] = TileFactory.createGround(5, 0, 3);
		field()[5][0][5] = TileFactory.createGround(5, 0, 5);
		field()[5][0][6] = TileFactory.createGround(5, 0, 6);
		field()[5][0][7] = TileFactory.createGround(5, 0, 7);
		field()[5][0][8] = TileFactory.createGround(5, 0, 8);
		field()[3][0][0] = TileFactory.createGround(3, 0, 0);
		field()[3][0][1] = TileFactory.createGround(3, 0, 1);
		field()[3][0][2] = TileFactory.createGround(3, 0, 2);
		field()[3][0][3] = TileFactory.createGround(3, 0, 3);
		field()[3][0][5] = TileFactory.createGround(3, 0, 5);
		field()[3][0][6] = TileFactory.createGround(3, 0, 6);
		field()[3][0][7] = TileFactory.createGround(3, 0, 7);
		field()[3][0][8] = TileFactory.createGround(3, 0, 8);
		field()[2][0][0] = TileFactory.createGround(2, 0, 0);
		field()[2][0][1] = TileFactory.createGround(2, 0, 1);
		field()[2][0][2] = TileFactory.createGround(2, 0, 2);
		field()[2][0][3] = TileFactory.createGround(2, 0, 3);
		field()[2][0][5] = TileFactory.createGround(2, 0, 5);
		field()[2][0][6] = TileFactory.createGround(2, 0, 6);
		field()[2][0][7] = TileFactory.createGround(2, 0, 7);
		field()[2][0][8] = TileFactory.createGround(2, 0, 8);
		field()[1][0][0] = TileFactory.createGround(1, 0, 0);
		field()[1][0][1] = TileFactory.createGround(1, 0, 1);
		field()[1][0][2] = TileFactory.createGround(1, 0, 2);
		field()[1][0][3] = TileFactory.createGround(1, 0, 3);
		field()[1][0][5] = TileFactory.createGround(1, 0, 5);
		field()[1][0][6] = TileFactory.createGround(1, 0, 6);
		field()[1][0][7] = TileFactory.createGround(1, 0, 7);
		field()[1][0][8] = TileFactory.createGround(1, 0, 8);
		Arrow arrow3 = new Arrow();
		arrow3.start = new Vector3(0, 1, 0);
		arrow3.end   = new Vector3(0, 3, 0);
		field()[0][2][0] = arrow3;
		field()[0][0][0] = TileFactory.createElevator(ElevatorDirection.UP, 0, 0, 0);
		field()[0][0][1] = TileFactory.createGround(0, 0, 1);
		field()[0][0][2] = TileFactory.createGround(0, 0, 2);
		field()[0][0][3] = TileFactory.createGround(0, 0, 3);
		field()[0][0][5] = TileFactory.createGround(0, 0, 5);
		field()[0][0][6] = TileFactory.createGround(0, 0, 6);
		field()[0][0][7] = TileFactory.createGround(0, 0, 7);
		Arrow arrow4 = new Arrow();
		arrow4.start = new Vector3(0, 1, 8);
		arrow4.end   = new Vector3(0, 3, 8);
		field()[0][2][8] = arrow4;
		field()[0][0][8] = TileFactory.createElevator(ElevatorDirection.UP, 0, 0, 8);
		field()[9][6][0] = TileFactory.createGround(9, 6, 0);
		field()[9][6][1] = TileFactory.createGround(9, 6, 1);
		field()[9][6][2] = TileFactory.createGround(9, 6, 2);
		Arrow arrow5 = new Arrow();
		arrow5.start = new Vector3(9, 7, 3);
		arrow5.end   = new Vector3(9, 3, 3);
		field()[9][8][3] = arrow5;
		field()[9][6][3] = TileFactory.createElevator(ElevatorDirection.DOWN, 9, 6, 3);
		field()[9][6][4] = TileFactory.createGround(9, 6, 4);
		Arrow arrow6 = new Arrow();
		arrow6.start = new Vector3(9, 7, 5);
		arrow6.end   = new Vector3(9, 3, 5);
		field()[9][8][5] = arrow6;
		field()[9][6][5] = TileFactory.createElevator(ElevatorDirection.DOWN, 9, 6, 5);
		field()[9][6][6] = TileFactory.createGround(9, 6, 6);
		field()[9][6][7] = TileFactory.createGround(9, 6, 7);
		field()[9][6][8] = TileFactory.createGround(9, 6, 8);
		field()[8][6][0] = TileFactory.createGround(8, 6, 0);
		field()[8][6][1] = TileFactory.createGround(8, 6, 1);
		field()[8][6][2] = TileFactory.createGround(8, 6, 2);
		field()[8][6][3] = TileFactory.createGround(8, 6, 3);
		field()[8][6][4] = TileFactory.createGround(8, 6, 4);
		field()[8][6][5] = TileFactory.createGround(8, 6, 5);
		field()[8][6][6] = TileFactory.createGround(8, 6, 6);
		field()[8][6][7] = TileFactory.createGround(8, 6, 7);
		field()[8][6][8] = TileFactory.createGround(8, 6, 8);
		field()[7][6][0] = TileFactory.createGround(7, 6, 0);
		field()[7][6][1] = TileFactory.createGround(7, 6, 1);
		field()[7][6][4] = TileFactory.createGround(7, 6, 4);
		field()[7][6][7] = TileFactory.createGround(7, 6, 7);
		field()[7][6][8] = TileFactory.createGround(7, 6, 8);
		field()[6][6][0] = TileFactory.createGround(6, 6, 0);
		field()[6][6][1] = TileFactory.createGround(6, 6, 1);
		field()[6][6][4] = TileFactory.createGround(6, 6, 4);
		field()[6][6][7] = TileFactory.createGround(6, 6, 7);
		field()[6][6][8] = TileFactory.createGround(6, 6, 8);
		field()[5][6][0] = TileFactory.createGround(5, 6, 0);
		field()[5][6][1] = TileFactory.createGround(5, 6, 1);
		field()[5][6][2] = TileFactory.createGround(5, 6, 2);
		field()[5][6][3] = TileFactory.createGround(5, 6, 3);
		field()[5][6][4] = TileFactory.createGround(5, 6, 4);
		field()[5][6][5] = TileFactory.createGround(5, 6, 5);
		field()[5][6][6] = TileFactory.createGround(5, 6, 6);
		field()[5][6][7] = TileFactory.createGround(5, 6, 7);
		field()[5][6][8] = TileFactory.createGround(5, 6, 8);
		field()[4][6][0] = TileFactory.createGround(4, 6, 0);
		field()[4][6][1] = TileFactory.createGround(4, 6, 1);
		field()[4][6][2] = TileFactory.createGround(4, 6, 2);
		field()[4][6][3] = TileFactory.createGround(4, 6, 3);
		field()[4][6][4] = TileFactory.createGround(4, 6, 4);
		field()[4][6][5] = TileFactory.createGround(4, 6, 5);
		field()[4][6][6] = TileFactory.createGround(4, 6, 6);
		field()[4][6][7] = TileFactory.createGround(4, 6, 7);
		field()[4][6][8] = TileFactory.createGround(4, 6, 8);
		field()[3][6][0] = TileFactory.createGround(3, 6, 0);
		field()[3][6][1] = TileFactory.createGround(3, 6, 1);
		field()[3][6][4] = TileFactory.createGround(3, 6, 4);
		field()[3][6][7] = TileFactory.createGround(3, 6, 7);
		field()[3][6][8] = TileFactory.createGround(3, 6, 8);
		field()[2][6][0] = TileFactory.createGround(2, 6, 0);
		field()[2][6][1] = TileFactory.createGround(2, 6, 1);
		field()[2][6][4] = TileFactory.createGround(2, 6, 4);
		field()[2][6][7] = TileFactory.createGround(2, 6, 7);
		field()[2][6][8] = TileFactory.createGround(2, 6, 8);
		field()[1][6][0] = TileFactory.createGround(1, 6, 0);
		field()[1][6][1] = TileFactory.createGround(1, 6, 1);
		field()[1][6][2] = TileFactory.createGround(1, 6, 2);
		field()[1][6][3] = TileFactory.createGround(1, 6, 3);
		field()[1][6][4] = TileFactory.createGround(1, 6, 4);
		field()[1][6][5] = TileFactory.createGround(1, 6, 5);
		field()[1][6][6] = TileFactory.createGround(1, 6, 6);
		field()[1][6][7] = TileFactory.createGround(1, 6, 7);
		field()[1][6][8] = TileFactory.createGround(1, 6, 8);
		field()[0][6][0] = TileFactory.createGround(0, 6, 0);
		field()[0][6][1] = TileFactory.createGround(0, 6, 1);
		field()[0][6][2] = TileFactory.createGround(0, 6, 2);
		Arrow arrow7 = new Arrow();
		arrow7.start = new Vector3(0, 7, 3);
		arrow7.end   = new Vector3(0, 3, 3);
		field()[0][8][3] = arrow7;
		field()[0][6][3] = TileFactory.createElevator(ElevatorDirection.DOWN, 0, 6, 3);
		field()[0][6][4] = TileFactory.createGround(0, 6, 4);
		Arrow arrow8 = new Arrow();
		arrow8.start = new Vector3(0, 7, 5);
		arrow8.end   = new Vector3(0, 3, 5);
		field()[0][8][5] = arrow8;
		field()[0][6][5] = TileFactory.createElevator(ElevatorDirection.DOWN, 0, 6, 5);
		field()[0][6][6] = TileFactory.createGround(0, 6, 6);
		field()[0][6][7] = TileFactory.createGround(0, 6, 7);
		field()[0][6][8] = TileFactory.createGround(0, 6, 8);

		switch (Config.difficulty) {
			case VERY_EASY:
				break;
			case EASY:
				break;
			case NORMAL:
				break;
			case HARD:
				break;
			case VERY_HARD:
		}

		player = new Player(null, BlockType.PLAYER);
		player.x = 3;
		player.y = 1;
		player.z = 3;
		field()[player.x][player.y][player.z] = player;
		translateAllBlocks();
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
		return 10;
	}
	@Override
	public int ylength() {
		return 9;
	}
	@Override
	public int zlength() {
		return 9;
	}
}
