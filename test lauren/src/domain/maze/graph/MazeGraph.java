package domain.maze.graph;

import java.util.ArrayList;

import domain.maze.ORIENTATION;

public class MazeGraph {
	private ArrayList<MazeNode> nodes;
	private ORIENTATION currentRobotOrientation;
	private TileNode startNode;
	private TileNode currentNode;
	
	/**
	 * A new MazeGraph is initialized with a starting node that represents the robot's current position.
	 * The current orientation of the robot is assumed to be North. (It doesn't matter what it's actual orientation
	 * is because everything is handled relative to the starting orientation)
	 */
	public MazeGraph(){
		nodes = new ArrayList<MazeNode>();
		startNode = new TileNode(null,null);
		setCurrentNode(startNode);
		nodes.add(startNode);
		setCurrentRobotOrientation(ORIENTATION.NORTH);
	}
	
	private void setCurrentRobotOrientation(ORIENTATION orientation) {
		this.currentRobotOrientation = orientation;
	}

	private void setCurrentNode(TileNode currentNode){
		this.currentNode = currentNode;
	}
	
	private boolean isCurrentNode(TileNode node){
		//Not equals, because it needs to be the same object so the addresses must match.
		return node==currentNode;
	}
	
	public boolean isComplete(){
		boolean isComplete = true;
		for(MazeNode mazeNode:nodes){
			if(!mazeNode.isFullyExpanded()) { isComplete = false;}
		}
		return isComplete;
	}
	
	/**
	 * Move the robot forward in the direction it's currently facing.
	 * Throws RuntimeException if the node in that direction is a WallNode or if there isn't a node in that direction.
	 */
	public void move(){
		
	}
	
	/**
	 * Call when the robot turns to the left;
	 */
	public void turnLeft(){
		//setCurrentRobotOrientation(getCurrentRobotOrientation().getLeft());
		//TODO
	}
	
	/**
	 * C
	 */
	public void turnRight(){
		//TODO
	}

	private Object getCurrentRobotOrientation() {
		// TODO Auto-generated method stub
		return null;
	}
}
