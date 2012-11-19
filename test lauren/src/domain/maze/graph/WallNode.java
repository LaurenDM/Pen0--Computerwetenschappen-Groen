package domain.maze.graph;

import domain.maze.Orientation;

public class WallNode extends MazeNode {
	
	private TileNode tile;

	public WallNode(TileNode currentNode, Orientation orientationToCurrentNode) {
		tile = currentNode;
	}

	/**
	 * A wall node has all the connections it needs to be fully expanded when it is constructed, so returns true.
	 */
	@Override
	public boolean isFullyExpanded() {
		return true;
	}
	
	/**
	 * Always false.
	 */
	@Override
	public boolean isFinish(){
		return false;
	}

	@Override
	public String toShortString() {
		return "Wall";
	}
	
	
}
