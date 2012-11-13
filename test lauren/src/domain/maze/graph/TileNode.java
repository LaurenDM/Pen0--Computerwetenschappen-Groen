package domain.maze.graph;

import java.util.HashMap;

import domain.maze.Orientation;

public class TileNode extends MazeNode {
	private HashMap<Orientation,MazeNode> connectedNodes;
	private boolean fullyExpanded;
	private boolean visited;
	
	public TileNode(TileNode currentNode, Orientation orientationToCurrentNode){
		connectedNodes = new HashMap<Orientation,MazeNode>();
		for(Orientation o : Orientation.values()){
			connectedNodes.put(o, null);
		}
		visited = false;
		setNodeAt(orientationToCurrentNode, currentNode);
	}
	
	@Override
	public boolean isFullyExpanded() {
		if(fullyExpanded){
			return true;
		} else {
			fullyExpanded=true;
			for(MazeNode node:connectedNodes.values()){
				if(node == null){
					fullyExpanded=false;
				}
			}
			return fullyExpanded;
		}
	}

	public MazeNode getNodeAt(Orientation currentRobotOrientation) {
		return getConnectedNodes().get(currentRobotOrientation);
	}
	
	public void setNodeAt(Orientation currentRobotOrientation, MazeNode mazeNode) {
		getConnectedNodes().put(currentRobotOrientation, mazeNode);
	}
	
	private HashMap<Orientation, MazeNode> getConnectedNodes() {
		return connectedNodes;
	}

	public void setVisited() {
		visited  = true;
	}

	@Override
	public boolean isVisited() {
		return visited;
	}
	
	
	
}
