package domain.maze.graph;

import domain.maze.Orientation;

public class WallNode extends MazeNode {
	
	private TileNode tile;
	private int trust;

	public WallNode(TileNode currentNode, Orientation orientationToCurrentNode) {
		tile = currentNode;
		trust = 1;
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

	@Override
	public boolean isAccessible() {
		return false;
	}

	public int getTrust() {
		return trust;
	}
	
	public void increaseTrust() {
		trust++;
	}
	
	
}
