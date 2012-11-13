package domain.maze.graph;

import java.util.ArrayList;

import domain.maze.Orientation;

public class MazeGraph {
	private ArrayList<MazeNode> nodes;
	private Orientation currentRobotOrientation;
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
		startNode.setVisited();
		setCurrentNode(startNode);
		nodes.add(startNode);
		setCurrentRobotOrientation(Orientation.NORTH);
	}
	
	/**
	 * If a MazeGraph has been completed that means that all it's nodes have been fully expanded.
	 * A fully expanded node has the maximum amount of connections for that type of node to other valid nodes.
	 * @return Whether this is a completed MazeGraph
	 */
	public boolean isComplete(){
		boolean isComplete = true;
		for(MazeNode mazeNode:nodes){
			if(!mazeNode.isFullyExpanded()) { isComplete = false;}
		}
		return isComplete;
	}
	
	/**
	 * Move the robot forward in the direction it's currently facing.
	 * Throws RuntimeException if the node in that direction is a WallNode or if there isn't any node in that direction.
	 */
	public void move(){
		MazeNode nextNode = getCurrentNode().getNodeAt(getCurrentRobotOrientation());
		if(nextNode != null && nextNode.getClass().equals(TileNode.class)){
			setCurrentNode((TileNode) nextNode);
			getCurrentNode().setVisited();
		} else {
			throw new RuntimeException("There is no node there or it's a WallNode.");
		}
	}
	
	/**
	 * Make a new TileNode at the given orientation
	 * @param orientation
	 */
	public void generateTileNodeAt(Orientation orientation){
		getCurrentNode().setNodeAt(getCurrentRobotOrientation(), new TileNode(getCurrentNode(),getCurrentRobotOrientation().getBack()));
	}
	
	/**
	 * Make a new WallNode at the given orientation
	 * @param orientation
	 */
	public void generateWallNodeAt(Orientation orientation){
		getCurrentNode().setNodeAt(getCurrentRobotOrientation(), new WallNode(getCurrentNode(),getCurrentRobotOrientation().getBack()));
	}
	
	/**
	 * 
	 * @return Whether the node the robot is currently on is fully expanded
	 */
	public boolean isCurrentNodeFullyExpanded(){
		return getCurrentNode().isFullyExpanded();
	}
	
	/**
	 * Orientation is relative to the robot's current orientation, not the grid.
	 * @param orientation
	 * @return Whether the node at the given orientation with respect to the robot is fully expanded
	 */
	public boolean isNodeThereFullyExpanded(Orientation orientation){
		switch(orientation){
			case NORTH: return getCurrentNode().getNodeAt(getCurrentRobotOrientation()).isFullyExpanded();
			case EAST: return getCurrentNode().getNodeAt(getCurrentRobotOrientation().getLeft()).isFullyExpanded();
			case SOUTH: return getCurrentNode().getNodeAt(getCurrentRobotOrientation().getBack()).isFullyExpanded();
			case WEST: return getCurrentNode().getNodeAt(getCurrentRobotOrientation().getRight()).isFullyExpanded();
		}
		return true;
	}
	
	/**
	 * Orientation is relative to the grid!
	 * @param orientation
	 * @return Whether the node at the given orientation with respect to the robot's current position is fully expanded
	 */
	public boolean isNodeThereFullyExpandedRelativeToGrid(Orientation orientation){
		return getCurrentNode().getNodeAt(orientation).isFullyExpanded();
	}
	
	/**
	 * A conceptually easier way of dealing with the isFullyExpanded methods than the isNodeThereFully...
	 * @return Whether the node in front of the robot is fully expanded
	 */
	public boolean isNodeInFrontFullyExpanded(){
		return isNodeThereFullyExpanded(Orientation.NORTH);
	}
	
	/**
	 * Call when the robot turns to the left.
	 */
	public void turnLeft(){
		setCurrentRobotOrientation(getCurrentRobotOrientation().getLeft());
	}
	
	/**
	 * Call when the robot turns to the right.
	 */
	public void turnRight(){
		setCurrentRobotOrientation(getCurrentRobotOrientation().getRight());
	}
	
	/**
	 * In principle this method shouldn't be necessary but it's included anyway.
	 */
	public void turnBack(){
		setCurrentRobotOrientation(getCurrentRobotOrientation().getBack());
	}

	/**
	 * Using this method shouldn't really be necessary
	 */
	public Orientation getCurrentRobotOrientation() {
		return currentRobotOrientation;
	}

	private void setCurrentRobotOrientation(Orientation orientation) {
		this.currentRobotOrientation = orientation;
	}

	private boolean isCurrentNode(TileNode node){
		//Not .equals(), because it needs to be the same object
		return node==currentNode;
	}

	private void setCurrentNode(TileNode currentNode){
		this.currentNode = currentNode;
	}
	
	private TileNode getCurrentNode(){
		return currentNode;
	}
}
