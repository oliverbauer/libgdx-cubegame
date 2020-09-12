package io.libgdx.cubegame.blocks;

public enum BlockType {
	/**
	 * Indicates the player/user/cube
	 */
	PLAYER,
	/**
	 * Default ground
	 */
	GROUND,
	HOLE,        // does not make sence now... later...fall down on next layer or die
	WALL,        // not allowed to move on
	ELEVATOR_UP,
	ELEVATOR_DOWN,
	LEFT_SHIFT,
	RIGHT_SHIFT,
	FORWARD_SHIFT,
	BACKWARD_SHIFT,
	LIFEFORCE, // Each level you need 10 of them
	POINT, // 100 Points
	ENEMY, // Spawns randomly
	/**
	 * A Block that falls from sky (high z-value).
	 * When it reaches a ground-block, the ground-block will be destroyed
	 */
	SKY_FALLING_BLOCK,
	/**
	 * A transport block moves from position p1 to p2, a player could move on the transport block
	 */
	TRANSPORT_BLOCK,
	/**
	 * A block which destroys itself after n times someone moves over it
	 * (only Player?)
	 */
	N_TIMES_BLOCK,
	/**
	 * Opens/closes a trap door on a different position
	 */
	TRAP_DOOR,
	/**
	 * Live lifeforce which moves ever x seconds.
	 */
	LIFEFORCE_MOVABLE,
	/**
	 * A lifeforce with a number on it. This is specific for levels containing hard coded 9 lifeforces at specific positions.
	 */
	LIFEFORCE_N,
	
	MOVEABLE_WALL
}
