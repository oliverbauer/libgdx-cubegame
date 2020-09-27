package io.libgdx.cubegame.animation;

import java.util.HashSet;
import java.util.Set;

import io.libgdx.cubegame.blocks.Block;

public interface Animation {
	public void update(Block block);
	
	Set<AnimationCompletedListener> listeners = new HashSet<>();
	default void addAnimationCompletedListener(AnimationCompletedListener listener) {
		listeners.add(listener);
	}
}
