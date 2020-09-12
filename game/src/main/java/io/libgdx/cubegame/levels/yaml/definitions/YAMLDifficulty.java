package io.libgdx.cubegame.levels.yaml.definitions;

import java.util.List;

public class YAMLDifficulty {
	private List<YAMLEnemy> enemies;
	private Object overrides;

	public List<YAMLEnemy> getEnemies() {
		return enemies;
	}

	public void setEnemies(List<YAMLEnemy> enemies) {
		this.enemies = enemies;
	}

	public Object getOverrides() {
		return overrides;
	}

	public void setOverrides(Object overrides) {
		this.overrides = overrides;
	}
	
	
}
