package domain.maze.graph;

import java.util.HashMap;

import domain.maze.Orientation;

public class TileNode extends MazeNode {
	private HashMap<Orientation,MazeNode> connectedNodes;
	private boolean fullyExpanded;
	private boolean visited;
	private int x;
	private int y;
	
	public TileNode(TileNode currentNode, Orientation orientationToCurrentNode){
		connectedNodes = new HashMap<Orientation,MazeNode>();
		for(Orientation o : Orientation.values()){
			connectedNodes.put(o, null);
		}
		visited = false;
		setNodeAt(orientationToCurrentNode, currentNode);
		if(currentNode==null || orientationToCurrentNode==null){
			x=0;
			y=0;
		} else {
			x=currentNode.getX()-orientationToCurrentNode.getXValue();
			y=currentNode.getY()-orientationToCurrentNode.getYValue();
		}
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

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public String toString(){
		return "Tile("+x+","+y+","+(connectedNodes.get(Orientation.NORTH)!=null)+","+(connectedNodes.get(Orientation.EAST)!=null)+","+(connectedNodes.get(Orientation.SOUTH)!=null)+","+(connectedNodes.get(Orientation.WEST)!=null)+")";
	}
	
	@Override
	public String toShortString(){
		return "("+x+","+y+")";
	}
	
}
