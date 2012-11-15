package domain.maze.graph;

public abstract class MazeNode {

	public abstract boolean isFullyExpanded();
	public abstract boolean isVisited();
	public abstract String toShortString();
	
}
