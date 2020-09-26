package examples.example1;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.math.Vector3;

public class CubeCornerResult {
	
	public CubeCornerResult(float x, float y, float z, float width, float height, float depth) {
		// manual of the last
		final float hw = width * 0.5f;
		final float hh = height * 0.5f;
		final float hd = depth * 0.5f;
		final float x0 = x - hw, y0 = y - hh, z0 = z - hd, x1 = x + hw, y1 = y + hh, z1 = z + hd;
		
		corner000 = new Vector3().set(x0, y0, z0);
		corner010 = new Vector3().set(x0, y1, z0);
		corner100 = new Vector3().set(x1, y0, z0);
		corner110 = new Vector3().set(x1, y1, z0);
		corner001 = new Vector3().set(x0, y0, z1);
		corner011 = new Vector3().set(x0, y1, z1);
		corner101 = new Vector3().set(x1, y0, z1);
		corner111 = new Vector3().set(x1, y1, z1);
	}
	
	Vector3 corner000, corner010, corner100, corner110, corner001, corner011, corner101, corner111;
	
	public CubeCornerResult alignLeftSideWithRightSideOf(CubeCornerResult prev) {
		// meine linkse seite = rechte seite des letzten
		corner000 = prev.corner001;
		corner010 = prev.corner011;
		corner100 = prev.corner101;
		corner110 = prev.corner111;
		return this;
	}
	
	public enum MOD {
		ONE,
		TWO,
		THREE
	}
	
	public CubeCornerResult modify(MOD mod) {
		if (mod == MOD.ONE) {
			// den boden etwas weiter machen
			corner000.sub(1, 0, 1); // vorne
			corner101.add(1, 0, 1); // hinten
			corner001.add(-1, 0, 1); // vorne rechts
			corner100.add(1, 0, -1); // vorne links
		}
		if (mod == MOD.TWO) {
			// oben eine spitze machen
			float width = 1;
			corner010.add(width/2, 0, width/2); // vorne
			corner111.sub(width/2, 0, width/2); // hinten
			corner011.sub(-width/2, 0, width/2); // vorne rechts
			corner110.sub(width/2, 0, -width/2); // vorne links
		}
		if (mod == MOD.THREE) {
			// etwas erhöhen die rechte seite
	//		corner001 // boden vorne
	//		corner101 // boden hinten
			corner011.add(0, 1, 0); // oben vorne
			corner111.add(0, 1, 0); // obne hinten
		}
		return this;
	}
	
	public CubeCornerResult finialize(MeshPartBuilder mpb) {
		Vector3 tmpV1 = new Vector3();
		Vector3 tmpV2 = new Vector3();

		mpb.ensureVertices(24);
		mpb.ensureRectangleIndices(6);

		float normalm = 0.5f;

		mpb.setColor(Color.RED);
		Vector3 nor = tmpV1.set(corner000).lerp(corner110, normalm).sub(tmpV2.set(corner001).lerp(corner111, normalm)).nor();
		mpb.rect(corner000, corner010, corner110, corner100, nor);
		
		mpb.setColor(Color.BLUE);
		mpb.rect(corner011, corner001, corner101, corner111, nor.scl(-1));
		nor = tmpV1.set(corner000).lerp(corner101, normalm).sub(tmpV2.set(corner010).lerp(corner111, normalm)).nor();
		
		mpb.setColor(Color.YELLOW);
		mpb.rect(corner001, corner000, corner100, corner101, nor);

		mpb.setColor(Color.GREEN);
		mpb.rect(corner010, corner011, corner111, corner110, nor.scl(-1));
		nor = tmpV1.set(corner000).lerp(corner011, normalm).sub(tmpV2.set(corner100).lerp(corner111, normalm)).nor();

		mpb.setColor(Color.BLACK);
		mpb.rect(corner001, corner011, corner010, corner000, nor);

		mpb.setColor(Color.ORANGE);
		mpb.rect(corner100, corner110, corner111, corner101, nor.scl(-1));

		return this;
	}
}