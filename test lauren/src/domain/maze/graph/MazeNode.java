package domain.maze.graph;

public abstract class MazeNode {

	public abstract boolean isFullyExpanded();
	public abstract String toShortString();
	public abstract boolean isFinish();
	
}
