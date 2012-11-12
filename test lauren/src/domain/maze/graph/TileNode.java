package domain.maze.graph;

import java.util.HashMap;

import domain.maze.ORIENTATION;

public class TileNode extends MazeNode {
	private HashMap<ORIENTATION,MazeNode> connectedNodes;
	private boolean isFullyExpanded;
	
	public TileNode(TileNode currentNode, ORIENTATION orientationToCurrentNode){
		
	}
	
	@Override
	public boolean isFullyExpanded() {
		if(isFullyExpanded){
			return true;
		} else {
			for(MazeNode node:connectedNodes.values()){
				//TODO
			}
			return isFullyExpanded;
		}
		
	}
	
}
