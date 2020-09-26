package io.libgdx.cubegame.levels.yaml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.libgdx.cubegame.Config;
import io.libgdx.cubegame.animation.EnemyAnimation;
import io.libgdx.cubegame.blocks.BlockType;
import io.libgdx.cubegame.blocks.types.TileElevator.ElevatorDirection;
import io.libgdx.cubegame.blocks.types.TileJumper.JumpDirection;
import io.libgdx.cubegame.levels.yaml.definitions.YAMLDifficulties;
import io.libgdx.cubegame.levels.yaml.definitions.YAMLDifficulty;
import io.libgdx.cubegame.levels.yaml.definitions.YAMLEnemy;
import io.libgdx.cubegame.levels.yaml.definitions.YAMLLevel;
import io.libgdx.cubegame.levels.yaml.definitions.YAMLPath;

/**
 * TODO YAML: Create Groovy Level.file
 * TODO YAML: Menu: Load level from groovy file (dynamic compilation)
 * TODO YAML: Allow to define enemy-speed (int,... 5 is normal, 1 is slow, 10 would be incredible fast) => RollerAnimation.java
 */
public class YAMLLevelFromDSL {

	public static void main(String[] args) throws IOException {
		new YAMLLevelFromDSL().generateFromTo("level1.yaml", "YAMLLevel1.java");
		new YAMLLevelFromDSL().generateFromTo("level2.yaml", "YAMLLevel2.java");
		new YAMLLevelFromDSL().generateFromTo("level3.yaml", "YAMLLevel3.java");
		new YAMLLevelFromDSL().generateFromTo("level4.yaml", "YAMLLevel4.java");
	}
	
	private BufferedWriter bufferedWriter;
	
	public void generateFromTo(String input, String output) throws IOException {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		mapper.findAndRegisterModules();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		
		
		FileWriter fileWriter = new FileWriter("src/main/java/io/libgdx/cubegame/levels/"+output);
        bufferedWriter = new BufferedWriter(fileWriter);
		
		YAMLLevel yamlLevel = mapper.readValue(new File("src/main/resources/cubegame/levels/"+input), YAMLLevel.class);
		
		Object[] fields = yamlLevel.getFields();
		int xlevel = 0;
		int maxylevel = 0;
		Map<String, List<String>> layers = new HashMap<>();
		for (int i=0; i<=fields.length-1; i++) {
			String currentLine = fields[i].toString().replace("[", "").replace("]", "");
			currentLine = currentLine.replace("{", "").replace("}", "");
			
			if (currentLine.startsWith("0=")) {
				xlevel++;
			}
			
			String s = currentLine.substring(0, currentLine.indexOf("="));
			
			layers.putIfAbsent(s, new ArrayList<>());
			layers.get(s).add(currentLine);
			
			maxylevel = Math.max(maxylevel, Integer.parseInt(s));
		}
		
		
		int x = xlevel;
		int y = maxylevel+3; // 1 definition + 1 player + 1 for enemies/arrows/... at layer 2
		int z = yamlLevel.getFields()[0].toString().split(",").length;
		
		generatePackageDefinition();
		generateImports();
		
		write("/** Generated from "+input+" with "+YAMLLevelFromDSL.class.getName()+" - do not modify! */");
		write("public class "+output.substring(0, output.indexOf("."))+" extends Level {"); 
		write("private Player player;", 1);
		write("private Block[][][] field;", 1);
		write("private GameScreen cubeApp;",1);
		write("");
		write("public "+output.substring(0, output.indexOf("."))+"(GameScreen cubeApp, PerspectiveCamera cam) {",1);
		write("Score.lifeforces = 0;",2);
		write("this.cubeApp = cubeApp;",2);
		write("field = new Block[xlength()][ylength()][zlength()];",2);
		write("");
		
		int playerX = -1;
		int playerY = 1;
		int playerZ = -1;
		
		
		List<String> postRotation = new ArrayList<>();
		int arrowCounter = 1;
		for (String layer : layers.keySet()) {
			List<String> currentLines = layers.get(layer);
			for (int i=0; i<=currentLines.size()-1; i++) {
				
				String currentLine = currentLines.get(i);
				currentLine = currentLine.substring(2);
				String[] parts = currentLine.split(",");
				
				int xRow = x - i - 1;
				for (int j=0; j<=parts.length-1; j++) {
					String current = parts[j].trim();
					
					String field = field(xRow, Integer.valueOf(layer), j);
					
					if (current.equals("1") || current.equals("2")) {
						write(field+" = TileFactory.createGround("+xRow+", "+layer+", "+j+");",2);
					}
					
					if (current.equals("2")) {
						playerX = xRow;
						playerY = Integer.valueOf(layer) + 1;
						playerZ = j;
					}
					
					if (current.equals("3")) {
						write(field+" = TileFactory.createJumper(Color.BLUE, JumpDirection.RIGHT, "+xRow+","+layer+","+j+");",2);
						postRotation.add("\t\t"+field+".getInstance().transform.rotate(Vector3.Y, 90);");
					}
					
					if (current.equals("4")) {
						write(field+" = TileFactory.createJumper(Color.BLUE, JumpDirection.LEFT, "+xRow+", "+layer+", "+j+");",2);
						postRotation.add("\t\t"+field+".getInstance().transform.rotate(Vector3.Y, 270);");
					}
					
					if (current.equals("5")) {
						write(field+" = TileFactory.createJumper(Color.BLUE, JumpDirection.FORWARD, "+xRow+", "+layer+", "+j+");",2);
						postRotation.add("\t\t"+field+".getInstance().transform.rotate(Vector3.Y, 0);");
					}
					if (current.equals("6")) {
						write(field+" = TileFactory.createJumper(Color.BLUE, JumpDirection.BACKWARD, "+xRow+", "+layer+", "+j+");",2);
						postRotation.add("\t\t"+field+".getInstance().transform.rotate(Vector3.Y, 180);");
					}
					
					
					if (current.equals("7")) {
						/*
						 * 
						 * Arrow arrow = new Arrow(BlockType.ELEVATOR_UP);
						 * arrow.start = new Vector3(2, 1, 2);
						 * arrow.end = new Vector3(2, 3, 2);
						 * field()[2][2][2] = arrow; // z=2 damit player nicht entfernt
						 * field()[2][0][2] = TileFactory.createElevator(BlockType.ELEVATOR_UP, 2, 0, 2);
						 */
						int l = Integer.valueOf(layer);
						
						write("Arrow arrow"+arrowCounter+" = new Arrow();", 2);
						write("arrow"+arrowCounter+".start = new Vector3("+xRow+", "+(l+1)+", "+j+");", 2);
						write("arrow"+arrowCounter+".end   = new Vector3("+xRow+", "+(l+3)+", "+j+");", 2);
						write("field()["+xRow+"]["+(l+2)+"]["+j+"] = arrow"+arrowCounter+";", 2);
						write("field()["+xRow+"]["+(l)+"]["+j+"] = TileFactory.createElevator("+ElevatorDirection.class.getSimpleName()+".UP, "+xRow+", "+l+", "+j+");", 2);
						
						arrowCounter++;
					}
					if (current.equals("8")) {
						/*
						 * Arrow arrow2 = new Arrow(BlockType.ELEVATOR_DOWN);
						 * arrow2.start = new Vector3(6, 5, 6);
						 * arrow2.end = new Vector3(6, 1, 6);
						 * field()[6][2][6] = arrow2; // z=2 damit player nicht entfernt
						 * field()[6][4][6] = TileFactory.createElevator(BlockType.ELEVATOR_DOWN, 6, 4, 6);
						 */
						int l = Integer.valueOf(layer);
						
						write("Arrow arrow"+arrowCounter+" = new Arrow();", 2);
						write("arrow"+arrowCounter+".start = new Vector3("+xRow+", "+(l+1)+", "+j+");", 2);
						write("arrow"+arrowCounter+".end   = new Vector3("+xRow+", "+(l-3)+", "+j+");", 2);
						write("field()["+xRow+"]["+(l+2)+"]["+j+"] = arrow"+arrowCounter+";", 2);
						write("field()["+xRow+"]["+(l)+"]["+j+"] = TileFactory.createElevator("+ElevatorDirection.class.getSimpleName()+".DOWN, "+xRow+", "+l+", "+j+");", 2);

						arrowCounter++;
					}
				}
			}		
		}
		
		generateSwitchDifficultyBlock(yamlLevel);
		
		generatePlayerDefinition(playerX, playerY, playerZ);
		write("translateAllBlocks();",2);
		
		for (String s : postRotation) {
			write(s);
		}
		
		write("}",1);

		// Enemies...
		Set<Integer> pathRef = new HashSet<>();
		List<YAMLDifficulties>  difficulties = yamlLevel.getDifficulties();
		for (int i=0; i<=difficulties.size()-1; i++) {
			YAMLDifficulty diff = difficulties.get(i).getVery_easy();
			if (diff != null) {
				for (YAMLEnemy yamlEnemy : diff.getEnemies()) {
					pathRef.add(yamlEnemy.getPath());
				}
			}

			diff = difficulties.get(i).getEasy();
			if (diff != null) {
				for (YAMLEnemy yamlEnemy : diff.getEnemies()) {
					pathRef.add(yamlEnemy.getPath());
				}
			}

			diff = difficulties.get(i).getNormal();
			if (diff != null) {
				for (YAMLEnemy yamlEnemy : diff.getEnemies()) {
					pathRef.add(yamlEnemy.getPath());
				}
			}

			diff = difficulties.get(i).getHard();
			if (diff != null) {
				for (YAMLEnemy yamlEnemy : diff.getEnemies()) {
					pathRef.add(yamlEnemy.getPath());
				}
			}

			diff = difficulties.get(i).getVery_hard();
			if (diff != null) {
				for (YAMLEnemy yamlEnemy : diff.getEnemies()) {
					pathRef.add(yamlEnemy.getPath());
				}
			}
		}
		
		write("");

		List<YAMLPath> path2 = yamlLevel.getPath();
		if (path2 != null) {
			for (int pRef=0; pRef<=path2.size()-1; pRef++) {
				String vectors = path2.get(pRef).getVectors();
					
				write("public void enemyPath"+ pRef + "(Color color) {",1);
				
				String parts[] = vectors.split("\\),\\(");
				
				String first = parts[0].replace("(", "").replace(")", "");
				String second = parts[1].replace("(", "").replace(")", "");
				
				String x1 = first.split(",")[0];
				Integer y1 = Integer.parseInt(first.split(",")[1]);
				String z1 = first.split(",")[2];
	
				
				String x2 = second.split(",")[0];
				String y2 = second.split(",")[1];
				String z2 = second.split(",")[2];
	
				write("field()["+x1+"]["+(y1+1)+"]["+z1+"] = TileFactory.createEnemy(color, "+x1+","+(y1+1)+","+z1+");",2);// hackish workaround
				
				write("EnemyAnimation r = new EnemyAnimation(cubeApp);",2);
				write("r.start = new Vector3("+x1+","+y1+","+z1+");",2);
				write("r.end = new Vector3("+x2+", "+y2+", "+z2+");",2);
				write("r.field = field;",2);
				write("List<Vector3> vecs = new ArrayList<>();",2);
				
				for (int k=2; k<=parts.length-1; k++) {
					String xa = parts[k].split(",")[0].replace("(", "").replace(")", "");
					String ya = parts[k].split(",")[1].replace("(", "").replace(")", "");
					String za = parts[k].split(",")[2].replace("(", "").replace(")", "");
					write("vecs.add(new Vector3("+xa+", "+ya+", "+za+"));",2);
					
				}
				
				write("for (Vector3 v : vecs)",2);
				write("r.add(v);",3);
				write("field()["+x1+"]["+(y1+1)+"]["+z1+"].anim = r;",2); // hackish workaround
				write("}",1);
				write("");
			}
		}
	
		generateMethodGetPlayer();
		generateMethodField();
		generateMethodXLength(x);
		generateMethodYLength(y);
		generateMethodZLength(z);
		
		write("}");
	}

	private void generatePackageDefinition() throws IOException {
		write("package io.libgdx.cubegame.levels;");
	}

	private void generatePlayerDefinition(int playerX, int playerY, int playerZ) throws IOException {
		write("player = new Player(null, BlockType.PLAYER);",2);
		write("player.x = "+playerX+";",2);
		write("player.y = "+playerY+";",2);
		write("player.z = "+playerZ+";",2);
		write("field()[player.x][player.y][player.z] = player;",2);
	}

	private void generateSwitchDifficultyBlock(YAMLLevel yamlLevel) throws IOException {
		// Enemies based on difficulty:
		List<YAMLDifficulties>  difficulties = yamlLevel.getDifficulties();
		write("");
		write("switch (Config.difficulty) {",2);
		
		write("case VERY_EASY:",3);
		
		for (int i=0; i<=difficulties.size()-1; i++) {
			YAMLDifficulty diff = difficulties.get(i).getVery_easy();
			if (diff != null) {
				for (YAMLEnemy yamlEnemy : diff.getEnemies()) {
					write("enemyPath"+ yamlEnemy.getPath() + "(Color."+yamlEnemy.getColor()+");",4);
				}
			}
		}
		write("break;",4);
		write("case EASY:",3);
		
		for (int i=0; i<=difficulties.size()-1; i++) {
			YAMLDifficulty diff = difficulties.get(i).getEasy();
			if (diff != null) {
				for (YAMLEnemy yamlEnemy : diff.getEnemies()) {
					write("enemyPath"+ yamlEnemy.getPath() + "(Color."+yamlEnemy.getColor()+");",4);
				}
			}
		}
		write("break;",4);
		write("case NORMAL:",3);
		
		for (int i=0; i<=difficulties.size()-1; i++) {
			YAMLDifficulty diff = difficulties.get(i).getNormal();
			if (diff != null) {
				for (YAMLEnemy yamlEnemy : diff.getEnemies()) {
					write("enemyPath"+ yamlEnemy.getPath() + "(Color."+yamlEnemy.getColor()+");",4);
				}
			}
		}
		write("break;",4);
		write("case HARD:",3);
		
		for (int i=0; i<=difficulties.size()-1; i++) {
			YAMLDifficulty diff = difficulties.get(i).getHard();
			if (diff != null) {
				for (YAMLEnemy yamlEnemy : diff.getEnemies()) {
					write("enemyPath"+ yamlEnemy.getPath() + "(Color."+yamlEnemy.getColor()+");",4);
				}
			}
		}
		write("break;",4);
		write("case VERY_HARD:",3);
		
		for (int i=0; i<=difficulties.size()-1; i++) {
			YAMLDifficulty diff = difficulties.get(i).getVery_hard();
			if (diff != null) {
				for (YAMLEnemy yamlEnemy : diff.getEnemies()) {
					write("enemyPath"+ yamlEnemy.getPath() + "(Color."+yamlEnemy.getColor()+");",4);
				}
			}
		}
		write("}",2);
		write("");
	}
	
	public void generateMethodGetPlayer() throws IOException {
		write("@Override",1);
		write("public Player getPlayer() {",1);
		write("return player;",2);
		write("}",1);
		write("");
	}
	
	public void generateMethodField() throws IOException {
		write("@Override",1);
		write("public Block[][][] field() {",1);
		write("return field;",2);
		write("}",1);
	}
	
	public void generateMethodXLength(int x) throws IOException {
		write("@Override",1);
		write("public int xlength() {",1);
		write("return "+x+";",2);
		write("}",1);
	}
	
	public void generateMethodYLength(int y) throws IOException {
		write("@Override",1);
		write("public int ylength() {",1);
		write("return "+y+";",2);
		write("}",1);
	}										

	public void generateMethodZLength(int z) throws IOException {
		write("@Override",1);
		write("public int zlength() {",1);
		write("return "+z+";",2);
		write("}",1);
	}

	private void generateImports() throws IOException {
		write("import java.util.ArrayList;");
		write("import java.util.List;");

		write("import com.badlogic.gdx.Gdx;");
		write("import com.badlogic.gdx.files.FileHandle;");
		write("import com.badlogic.gdx.graphics.Color;");
		write("import com.badlogic.gdx.graphics.PerspectiveCamera;");
		write("import com.badlogic.gdx.graphics.Texture;");
		write("import com.badlogic.gdx.math.Vector3;");

		
		
		write("import "+Config.class.getName()+";");
		write("import "+EnemyAnimation.class.getName()+";");
		write("import "+ElevatorDirection.class.getName().replace("$", ".")+";"); // inner class
		write("import "+JumpDirection.class.getName().replace("$", ".")+";"); // inner class
		write("import io.libgdx.cubegame.blocks.Block;");
		write("import io.libgdx.cubegame.blocks.BlockType;");
		write("import io.libgdx.cubegame.blocks.factories.TileFactory;");
		write("import io.libgdx.cubegame.player.Player;");
		write("import io.libgdx.cubegame.score.Score;");
		write("import io.libgdx.cubegame.screens.GameScreen;");
		write("import io.libgdx.cubegame.blocks.Arrow;");
		
		write("");
	}
	
	private String field(int x, int y, int z) {
		return "field()["+x+"]["+y+"]["+z+"]";
	}
	
	private void write(String message, int tabs) throws IOException {
		StringBuilder sb = new StringBuilder();
		for (int i=1; i<=tabs; i++) {
			sb.append("\t");
		}
		bufferedWriter.write(sb.toString() + message);
		bufferedWriter.newLine();
		bufferedWriter.flush();
	}
	
	private void write(String message) throws IOException {
		bufferedWriter.write(message);
		bufferedWriter.newLine();
		bufferedWriter.flush();
	}
}
