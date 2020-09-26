package examples.example3;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class Block {
	public int x;
	public int y;
	public int z;
	
	public ModelInstance instance;
	
	public Block(int x, int y, int z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3 getPosition() {
		return new Vector3(x, y, z);
	}
}
