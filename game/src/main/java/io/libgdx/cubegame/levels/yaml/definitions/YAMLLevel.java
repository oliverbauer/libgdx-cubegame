package io.libgdx.cubegame.levels.yaml.definitions;

import java.util.List;

public class YAMLLevel {
	private Object[] fields;
	private List<YAMLFieldDefinition> definitions;
	private List<YAMLDifficulties> difficulties;
	private List<YAMLPath> path;

	public List<YAMLPath>  getPath() {
		return path;
	}

	public void setPath(List<YAMLPath>  path) {
		this.path = path;
	}

	public Object[] getFields() {
		return fields;
	}

	public void setFields(Object[] fields) {
		this.fields = fields;
	}

	public List<YAMLFieldDefinition> getDefinitions() {
		return definitions;
	}

	public void setDefinitions(List<YAMLFieldDefinition> definitions) {
		this.definitions = definitions;
	}

	public List<YAMLDifficulties>  getDifficulties() {
		return difficulties;
	}

	public void setDifficulties(List<YAMLDifficulties>  difficulties) {
		this.difficulties = difficulties;
	}

}
