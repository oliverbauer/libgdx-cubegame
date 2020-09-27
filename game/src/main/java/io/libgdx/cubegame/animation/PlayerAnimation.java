package io.libgdx.cubegame.animation;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import io.libgdx.cubegame.blocks.Block;

public class PlayerAnimation implements Animation {
	private List<Vector3> vectors = new ArrayList<>();
	
	private float alpha = 0;
	private float speed = 3;
	
	public PlayerAnimation(List<Vector3> v) {
		if (v != null) {
			vectors.addAll(v);
		}
	}
	
	@Override
	public void update(Block block) {
		final float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f);
		
		alpha += delta * speed;
		
		if (alpha > 1) {
			block.anim = null;
			for (AnimationCompletedListener listener : listeners) {
				listener.animationCompleted();
			}
			return;
		}
		
		float angle = 0;
		Vector3 tmpV = new Vector3();
		
		Vector3 axis;
		if (vectors.get(0).x == vectors.get(vectors.size()-1).x) {
			axis = Vector3.X;
		} else {
			axis = Vector3.Z;
		}
		
		Vector3 start;
		Vector3 end;
		if (alpha < 0.5f) {
			start = vectors.get(0);
			end = vectors.get(1);
		} else {
			start = vectors.get(1);
			end = vectors.get(2);
		}
		
		tmpV.set(start).lerp(end, alpha);
		block.getInstance().transform.setToRotation(axis, angle);
		block.getInstance().transform.setTranslation(tmpV);
	}
}
