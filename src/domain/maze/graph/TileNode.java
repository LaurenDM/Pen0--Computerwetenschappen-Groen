package domain.maze.graph;

import java.util.HashMap;

import domain.maze.Orientation;

public class TileNode extends MazeNode {
	private HashMap<Orientation,MazeNode> connectedNodes;
	private boolean fullyExpanded = false;
	private boolean finish = false;
	private int x;
	private int y;
	private boolean checkpoint = false;
	private boolean visited = false;
	
	public TileNode(TileNode currentNode, Orientation orientationToCurrentNode){
		connectedNodes = new HashMap<Orientation,MazeNode>();
		for(Orientation o : Orientation.values()){
			connectedNodes.put(o, null);
		}
		if(currentNode==null || orientationToCurrentNode==null){
			x=0;
			y=0;
		} else {
			setNodeAt(orientationToCurrentNode, currentNode);
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
			for(Orientation o:getConnectedNodes().keySet()){
				if(o!= null && getConnectedNodes().get(o) == null){
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

	public void setFinish() {
		finish = true;
	}

	public boolean isFinish() {
		return finish;
	}

	public void setCheckpoint(boolean arg) {
		checkpoint=arg;
	}

	public boolean isCheckpoint(){
		return checkpoint;
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

	public int manhattanDistanceTo(TileNode otherTile) {
		return Math.abs(getX()-otherTile.getX())+Math.abs(getY()-otherTile.getY());
	}

	public void setVisited(){
		visited = true;
	}
	
	public boolean isVisited() {
		return visited ;
	}

	public int numberOfConnections() {
		int ret = 0;
		for(MazeNode node : connectedNodes.values()){
			if(node!=null) ret++;
		}
		return ret;
	}
	
}