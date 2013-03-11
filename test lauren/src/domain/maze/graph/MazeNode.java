package domain.maze.graph;

public abstract class MazeNode {

	/**
	 * Indicates whether we know for all 4 sides of this tile whether there's a wall or a tile there.
	 * @return
	 */
	public abstract boolean isFullyExpanded();
	/**
	 * A short string representation of the Node.
	 * @return
	 */
	public abstract String toShortString();
	/**
	 * A Node can be indicated to be the finishing point of an exploration.
	 * @return
	 */
	public abstract boolean isFinish();
	
}
