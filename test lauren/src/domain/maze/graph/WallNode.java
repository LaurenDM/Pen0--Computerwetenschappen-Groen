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
	 * Always true. If you made it you've seen it, and in the case of a wall node that means you've been there.
	 */
	@Override
	public boolean isVisited(){
		return true;
	}

	@Override
	public String toShortString() {
		return "Wall";
	}
	
	
}
