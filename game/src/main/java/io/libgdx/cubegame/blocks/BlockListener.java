package io.libgdx.cubegame.blocks;

import io.libgdx.cubegame.animation.EnemyAnimation;

/**
 * Used for e.g. TileTrappingDoor to get informed when player/enemy walks over a door (other position than the TileTrappingDoor)
 *
 */
public interface BlockListener {
	public void playerMovedOnBlock(Block block);
	public void enemyMovedOnBlock(Block block, EnemyAnimation enemy);
}
