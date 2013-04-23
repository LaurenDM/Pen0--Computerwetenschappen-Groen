package domain.maze.graph;

import gui.ContentPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import peno.htttp.Tile;

import domain.Position.Position;
import domain.maze.Orientation;
import domain.maze.Token;
import domain.robots.RobotPilot;

public class MazeGraph {
	private ArrayList<TileNode> tileNodes;
	private Orientation currentRobotOrientation;
	private TileNode startNode;
	private TileNode currentNode;
	private int tileCounter;
	private MazePath shortestPath;
	private boolean drivingToPartner = false;
	
	private static Orientation INITIALORIENTATION = Orientation.NORTH;
	
	/**
	 * A new MazeGraph is initialized with a starting node that represents the robot's current position.
	 * The current orientation of the robot is assumed to be North. (It doesn't matter what it's actual orientation
	 * is because everything is handled relative to the starting orientation)
	 */
	public MazeGraph(){
		tileNodes = new ArrayList<TileNode>();
		startNode = new TileNode(null,null);
		setCurrentTile(startNode);
		tileNodes.add(startNode);
		setCurrentRobotOrientation(INITIALORIENTATION);
		System.out.println("");
	}
	
	public static Orientation getInitialOrientation(){
		return INITIALORIENTATION;
	}
	
	/**
	 * Calculates the shortest path to the finish and makes the given RobotPilot drive there.
	 * @param robotPilot
	 */
	public void driveToFinish(RobotPilot robotPilot){
		shortestPath = findShortestPathToFinish();
		Iterator<TileNode> tileIt = shortestPath.iterator();
		boolean first = true;
		if(tileIt.hasNext()){
			tileIt.next();
			while(tileIt.hasNext()){
				TileNode nextNode = tileIt.next();
				//System.out.println("NEXTNODE"+nextNode.getX()+" "+nextNode.getY());
				Orientation nextOrientation = null;
				for(Orientation o:Orientation.values()){
					if(getCurrentTile().getNodeAt(o)!=null && getCurrentTile().getNodeAt(o).equals(nextNode)){
						nextOrientation = o;
					}
				}
				//For the first turn in the path the robot is allowed to turn back but in all other cases it's nonsensical.
				if((nextOrientation==null || nextOrientation.getBack().equals(getCurrentRobotOrientation())) && !first){
					//Do nothing, see if we can't reach any of the next tiles in the list (handy when driving over seesaws on the way to the finish).
				} else {
					turnToNextOrientation(robotPilot, getCurrentRobotOrientation(), nextOrientation);
					try {
						if(tileCounter%2==0){
							robotPilot.straighten();
							robotPilot.move(20);
						} else {
							double distance = robotPilot.readUltrasonicValue();
							if(distance!=255 &&(distance< 17 || distance%40 > 22)){
								robotPilot.straighten();
								robotPilot.move(20);
							}
							else{
								robotPilot.move(40);
							}
						}
						tileCounter++;
						this.move();
					} catch (Throwable e) {
						System.out.println("Could not move");
					}
				}
				first = false;
			}
		} else {
			throw new NullPointerException("Finish has not yet been found or no path can be found.");
		}
		ContentPanel.writeToDebug("Finish (or starting point) reached!");
	}
	
	private void turnToNextOrientation(RobotPilot robotPilot, Orientation currentOrientation, Orientation nextOrientation) {
		if(currentOrientation==null){
			throw new IllegalArgumentException();
		}
		if(nextOrientation==null){
			throw new IllegalArgumentException();
		}
		if(currentOrientation.equals(nextOrientation)){
			//Don't turn
		} else if(nextOrientation.equals(currentOrientation.getLeft())){
			robotPilot.turnLeft();
			this.turnLeft();
		} else if(nextOrientation.equals(currentOrientation.getRight())){
			robotPilot.turnRight();
			this.turnRight();
		} else if(nextOrientation.equals(currentOrientation.getBack())){
			robotPilot.turnLeft();
			robotPilot.turnLeft();
			this.turnBack();
		} else {
			throw new IllegalArgumentException("Couldn't find a direction to turn in!");
		}
	}

	/**
	 * If a MazeGraph has been completed that means that all it's nodes have been fully expanded.
	 * A fully expanded node has the maximum amount of connections for that type of node to other valid nodes.
	 * @return Whether this is a completed MazeGraph
	 */
	public boolean isComplete(){
		boolean isComplete = true;
		for(TileNode tileNode:tileNodes){
			if(!tileNode.isFullyExpanded()) { isComplete = false;}
		}
		return isComplete;
	}
	
	/**
	 * @return Whether the starting node has been fully expanded.
	 */
	public boolean isStartNodeComplete(){
		return startNode.isFullyExpanded();
	}
	
	/**
	 * Move the robot forward in the direction it's currently facing.
	 * Throws RuntimeException if the node in that direction is a WallNode or if there isn't any node in that direction.
	 */
	public void move(){
		MazeNode nextNode = getCurrentTile().getNodeAt(getCurrentRobotOrientation());
		if(nextNode != null && (nextNode.getClass().equals(TileNode.class) || nextNode.getClass().equals(SeesawNode.class))){
			setCurrentTile((TileNode) nextNode);
			decreaseAllBlockNavigationCounts(); //Eventually unblocks blocked tiles.
		} else {
//			throw new RuntimeException("There is no node there or it's a WallNode.");
		}
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
	 * Call when the robot turns 180 degrees.
	 */
	public void turnBack(){
		setCurrentRobotOrientation(getCurrentRobotOrientation().getBack());
	}

	private void decreaseAllBlockNavigationCounts() {
		for(TileNode t : tileNodes){
			t.decreaseBlockNavigationCount();
		}
	}
	
	

	/**
	 * Returns the orientation to move in to continue exploring. 
	 * This is accomplished by calculating the shortest path to the next unexplored node and 
	 * finding out in which direction the robot needs to turn in order to get there.
	 * If multiple paths of equal length exist the path will be chosen that makes it turn, in order of priority:
	 * Left, Not, Right, Back
	 * If a path can not be found, it returns the orientation to the front/back of the current orientation,
	 * depending on whether there's a wall in front of the robot.
	 * @return
	 */
	public Orientation getNextMoveOrientation(){
		//Just a safeguard so the robot will only drive forward on seesaws, this code should (in principle) never be called.
		if(getCurrentTile().getClass().equals(SeesawNode.class)){
			return Orientation.NORTH;
		}
		ArrayList<TileNode> unexpanded = new ArrayList<TileNode>();
		//If we're driving towards our teammate we'll only consider paths to their location.
		//If not all unexpanded tiles are added to the list.
		if(drivingToPartner){
			unexpanded.add(getFinishNode());
		} else {
			for(TileNode node : tileNodes){
				if(!node.isFullyExpanded()){
					unexpanded.add(node);
				}
			}
		}
		//TODO add code so that the robot will attempt to follow the most sensible path it can if no path to a goal is found
		MazePath lastPath = null;
		//Fill a new SortedPathSet with paths containing only the current tile and having the unexpanded tiles as finish
		if(unexpanded.size()!=0){
			SortedPathSet possiblePaths = new SortedPathSet();
			for(TileNode node : unexpanded){
				possiblePaths.add(new MazePath(getCurrentTile(),getCurrentRobotOrientation(),node));
			}
			int shortestFound = Integer.MAX_VALUE;
			ArrayList<MazePath> candidates = new ArrayList<MazePath>();
			do{
				while(!possiblePaths.firstPathReachesGoal()){
					possiblePaths.expand();
				}
				while(possiblePaths.firstPathReachesGoal() && possiblePaths.size()>0){
					candidates.add(possiblePaths.first());
					possiblePaths.remove(possiblePaths.first());
					int currentLength = candidates.get(candidates.size()-1).getCurrentLength();
					shortestFound = currentLength<shortestFound?currentLength:shortestFound;
				}
				//In the while condition the path will only get added if it's valid but it'll always be removed from possiblePaths.
			}while( possiblePaths.size()>0 && possiblePaths.first().getCurrentLength()<=shortestFound);
			Iterator<MazePath> it = candidates.iterator();
			while(it.hasNext()){
				MazePath path = it.next();
				if(path.getCurrentLength()>shortestFound){
					it.remove();
				}
			}
			ArrayList<Orientation> orderedOrientations = new ArrayList<Orientation>();
			//The order of the following lines determines the order in which we'll consider possible paths
			orderedOrientations.add(Orientation.WEST);
			orderedOrientations.add(Orientation.NORTH);
			orderedOrientations.add(Orientation.EAST);
			orderedOrientations.add(Orientation.SOUTH);
			for(Orientation o : orderedOrientations){
				for(MazePath path : candidates){
					if(path.contains(getCurrentTile().getNodeAt(getCurrentRobotOrientation().getRelativeOrientation(o))))
						return o;
				}
			}
		}
		MazeNode frontNode = getCurrentTile().getNodeAt(getCurrentRobotOrientation());
		return frontNode!=null?(frontNode.isAccessible()?Orientation.NORTH:Orientation.SOUTH):null;
	}
	
	/**
	 * Make a new TileNode at the given orientation relative to the robot's current orientation.
	 * @param orientation
	 */
	public void generateTileNodeAt(Orientation orientation){
		Orientation absoluteOrientation = getCurrentRobotOrientation().getRelativeOrientation(orientation);
		generateTileNodeAt(getCurrentTile(), absoluteOrientation);
	}
	
	private void generateTileNodeAt(TileNode tile, Orientation orientation){
		Orientation absoluteOrientation = orientation;
		Orientation orientationToCurrent = absoluteOrientation.getBack();
		TileNode newNode = new TileNode(tile,orientationToCurrent);
		boolean thisNodeAlreadyExists = false;
		//Cycle the collection of all tilenodes, check if the node that we're trying to create already exists
		//Also check if we have info about neighbouring nodes so we can set walls at the correct places.
		//Note that connections to nodes are not automatically made if we don't know if there's a wall or not!
		for(TileNode node1:tileNodes){
			//Deduplication
			if((node1).getX()==newNode.getX() && (node1).getY()==newNode.getY()){
				tile.setNodeAt(absoluteOrientation, node1);
				node1.setNodeAt(orientationToCurrent, tile);
				thisNodeAlreadyExists = true;
				break;
			}
			//Wallinfo check
			for(Orientation o: Orientation.values()){
				if(node1.getX()==newNode.getX()+o.getXValue()&&node1.getY()==newNode.getY()+o.getYValue()){
					MazeNode nodeAtOBack = node1.getNodeAt(o.getBack());
					if(nodeAtOBack!=null && nodeAtOBack.getClass().equals(WallNode.class)){
						newNode.setNodeAt(o, new WallNode(node1,o));

					}
				}
			}
		}
		if(!thisNodeAlreadyExists){
			tile.setNodeAt(absoluteOrientation, newNode);
			tileNodes.add(newNode);
		}
	}
	
	private TileNode getTileNodeAt(int x, int y){
		TileNode r = null;
		for(TileNode t : tileNodes){
			if(t.getX() == x && t.getY() == y){
				r = t;
				break;
			}
		}
		return r;
	}
	
	/**
	 * Make a new WallNode at the given orientation relative to the robot's current orientation.
	 * @param orientation
	 * @return true if the wall has been added, false if it's clear that there can be no wall at that location.
	 */
	public boolean generateWallNodeAt(Orientation orientation){
		Orientation absoluteOrientation = getCurrentRobotOrientation().getRelativeOrientation(orientation);
		return generateWallNodeAt(getCurrentTile(),absoluteOrientation);
	}
	
	/**
	 * Make a new WallNode at the given orientation connected to the given TileNode.
	 * @param orientation
	 * @return true if the wall has been added, false if it's clear that there can be no wall at that location.
	 */
	private boolean generateWallNodeAt(TileNode tile, Orientation orientation){
		Orientation absoluteOrientation = orientation;
		Orientation orientationToCurrent = absoluteOrientation.getBack();
		WallNode newNode = new WallNode(tile,orientationToCurrent);
		
		//If this tile has a wall at a certain orientation it's neighbouring tile will have one in the opposite orientation
		//A new tile isn't generated there because then we would generate nodes outside of the maze's edge
		TileNode otherSide = null;
		for(TileNode node : tileNodes){
			if(node.getX()==tile.getX()+absoluteOrientation.getXValue() && node.getY()==tile.getY()+absoluteOrientation.getYValue()){
				otherSide = node;
				break;
			}
		}
		if(otherSide != null){
			otherSide.setNodeAt(orientationToCurrent, newNode);
		}
		
		//If a node already has a connection to a (semi-)completed node at a certain orientation we can be sure of the fact that there
		//is no wall there so we won't add it
		MazeNode previousNode = tile.getNodeAt(absoluteOrientation);
		if(previousNode!=null && previousNode.getClass().equals(TileNode.class) && (((TileNode) previousNode).isVisited() || ((TileNode)previousNode).numberOfConnections()>=2)){
			System.out.println("Wrong measurement. There can be no wall there.");
			return false;
		} else {
			tile.setNodeAt(absoluteOrientation, newNode);
			return true;
		}
	}
	
	private void generateSeesawNodeAt(Orientation orientation, boolean isUpAtThisSide){
		generateSeesawNodeAt(getCurrentTile(), orientation, isUpAtThisSide);
	}
	
	private void generateSeesawNodeAt(TileNode tile, Orientation orientation, boolean isUpAtThisSide) {
		Orientation absoluteOrientation = getCurrentRobotOrientation().getRelativeOrientation(orientation);
		Orientation orientationToCurrent = absoluteOrientation.getBack();
		SeesawNode newNode = new SeesawNode(tile,orientationToCurrent);
		newNode.setUp(isUpAtThisSide);
		boolean thisNodeAlreadyExists = false;
		
		for(TileNode node:tileNodes){
			//Deduplication
			if((node).getX()==newNode.getX() && (node).getY()==newNode.getY()){
				tile.setNodeAt(absoluteOrientation, node);
				node.setNodeAt(orientationToCurrent, tile);
				if(node.getClass().equals(SeesawNode.class)) {
					((SeesawNode)node).setUp(isUpAtThisSide);
					thisNodeAlreadyExists = true;
				} else {
					tileNodes.remove(node);
					System.out.println("A TileNode was replaced by a SeesawNode... Was this wanted behaviour?");
				}
				break;
			}
			//Wallinfo check
			for(Orientation o: Orientation.values()){
				if(node.getX()==newNode.getX()+o.getXValue()&&node.getY()==newNode.getY()+o.getYValue()){
					MazeNode nodeAtOBack = node.getNodeAt(o.getBack());
					if(nodeAtOBack!=null && nodeAtOBack.getClass().equals(WallNode.class)){
						newNode.setNodeAt(o, new WallNode(node,o));

					}
				}
			}
		}
		if(!thisNodeAlreadyExists){
			tile.setNodeAt(absoluteOrientation, newNode);
			tileNodes.add(newNode);
			tileNodes.add(newNode.getPairedNode());
		}
	}

	public boolean isDrivingToPartner() {
		return drivingToPartner;
	}

	public void setDrivingToPartner(boolean drivingToPartner) {
		this.drivingToPartner = drivingToPartner;
	}

	/**
	 * 
	 * @return Whether the node the robot is currently on is fully expanded
	 */
	public boolean isCurrentNodeFullyExpanded(){
		return getCurrentTile().isFullyExpanded();
	}
	
	/**
	 * Use isNodeInFrontFullyExpanded() when applicable.
	 * Orientation is relative to the robot's current orientation, not the grid.
	 * @param orientation
	 * @return Whether the node at the given orientation with respect to the robot is fully expanded
	 */
	public boolean isNodeThereFullyExpanded(Orientation orientation){
		return getCurrentTile().getNodeAt(getCurrentRobotOrientation().getRelativeOrientation(orientation)).isFullyExpanded();
	}
	
	/**
	 * Use isNodeInFrontFullyExpanded() when applicable.
	 * Orientation is relative to the grid! (So to the robot's orientation when it started exploring (NORTH))
	 * @param orientation
	 * @return Whether the node at the given orientation with respect to the robot's current position is fully expanded
	 */
	public boolean isNodeThereFullyExpandedRelativeToGrid(Orientation orientation){
		return getCurrentTile().getNodeAt(orientation).isFullyExpanded();
	}
	
	/**
	 * Returns if the node in front of the robot is fully expanded.
	 * A conceptually easier way of dealing with the isFullyExpanded methods than the isNodeThereFully...
	 * @return Whether the node in front of the robot is fully expanded
	 */
	public boolean isNodeInFrontFullyExpanded(){
		return isNodeThereFullyExpanded(Orientation.NORTH);
	}
	
	/**
	 * Returns the robot's current orientation relative to it's starting orientation (NORTH)
	 * @return
	 */
	public Orientation getCurrentRobotOrientation() {
		return currentRobotOrientation;
	}

	
	private void setCurrentRobotOrientation(Orientation orientation) {
		this.currentRobotOrientation = orientation;
	}
	
	/**
	 * Sets the current TileNode as the Finish node.
	 */
	public void setCurrentTileToFinish() {
		setTileToFinish(getCurrentTile());
	}
	
	private void setTileToFinish(TileNode tile) {
		for(TileNode node:tileNodes){
			if(node.isFinish() && !node.equals(tile)){
				node.setFinish(false);
			}
		}
		tile.setFinish(true);
	}
	
	/**
	 * Sets the current TileNode as a Checkpoint node.
	 */
	public void setCurrentTileToCheckpoint() {
		getCurrentTile().setCheckpoint(true);
	}
	
	/**
	 * Set the current tile's barcode.
	 * @param barcodeNumber
	 */
	public void setCurrentTileBarcode(int barcodeNumber) {
		setTileBarcode(getCurrentTile(),barcodeNumber);
	}

	private void setTileBarcode(TileNode tile, int barcodeNumber) {
		tile.setBarcode(barcodeNumber);
	}

	/**
	 * Implements the A* shortest path algorithm for this MazeGraph to find the shortest path to the Finish node, which is marked
	 * by the "Finish" barcode in the real maze.
	 * @return Null if the finish hasn't been found yet.
	 */
	public MazePath findShortestPathToFinish(){
		ArrayList<TileNode> checkpoints = getCheckpoints();
		//TODO find a way of finding a path through multiple checkpoints (very low priority)
		TileNode checkpoint = checkpoints.size()!=0?checkpoints.get(0):null;
		if(checkpoint == null){
			ContentPanel.writeToDebug("No checkpoint found, driving straight to finish.");
			checkpoint = getCurrentTile();
		}
		TileNode finishNode = getFinishNode();
		if(finishNode == null){
			ContentPanel.writeToDebug("No finish barcode found, driving to starting node instead.");
			finishNode = startNode;
		}
		return findShortestPathFromTo(getCurrentTile(),checkpoint).append(findShortestPathFromTo(checkpoint,finishNode));
	}
	
	public ArrayList<TileNode> getCheckpoints(){
		ArrayList<TileNode> checkpoints = new ArrayList<TileNode>();
		for(TileNode node: tileNodes){
			if(node.isCheckpoint()) checkpoints.add(node);
		}
		return checkpoints;
	}

	/**
	 * Returns the MazePath representation of the shortest path from startNode to finishNode.
	 * @param finishNode
	 * @return
	 */
	public MazePath findShortestPathFromTo(TileNode startNode, TileNode finishNode) {
		SortedPathSet searchSet = new SortedPathSet(new MazePath(startNode,getCurrentRobotOrientation(),finishNode));
		while(!searchSet.isEmpty() && !searchSet.firstPathReachesGoal()){
			searchSet.expand();
		}
		if(searchSet.isEmpty()){
			System.out.println("No path found...");
			return new MazePath(getCurrentTile(),getCurrentRobotOrientation(),finishNode);
		} else {
			System.out.println(searchSet.first());
			return searchSet.first();
		}
	}
	
	private TileNode getFinishNode() {
		for(TileNode node: tileNodes){
			if(node.isFinish()) return node;
		}
		return null;
	}

	private boolean isCurrentTile(TileNode node){
		//Not .equals(), because it needs to be the same object
		return node==currentNode;
	}

	private void setCurrentTile(TileNode currentNode){
		this.currentNode = currentNode;
	}
	
	/**
	 * Returns the TileNode the robot's currently on.
	 * @return
	 */
	public TileNode getCurrentTile(){
		return currentNode;
	}

	/**
	 * Assume the current coordinates are the given x and y and that the robot is in Orientation o.
	 * @param x
	 * @param y
	 * @param o
	 */
	public void continueExploring(int x, int y, Orientation o) {
		if(o!=null){
			for(TileNode node : tileNodes){
				if(node.getX()==x && node.getY()==y){
					setCurrentTile(node);
				}
			}
			setCurrentRobotOrientation(o);
			ContentPanel.writeToDebug("Starting exploration from "+"("+x+","+y+")"+" facing "+o);
		} else {
			ContentPanel.writeToDebug("Continuing exploration.");
		}
		
	}

	/**
	 * Turn back, move a tile and make a wallNode where the robot bumped.
	 */
	public void recoverFromBump() {
		turnBack();
		move();
		generateWallNodeAt(Orientation.SOUTH);
	}
	
	/**
	 * Return the latest calculated path to the finish.
	 * @return
	 */
	public MazePath getShortestPath(){
		return shortestPath;
	}

	/**
	 * Sets the tile in front of the robot to a dead end, meaning that walls are added in the North, West and East orientations.
	 * As with the generateWallNodeAt method, if we know that there can't be any walls there they will not be added.
	 * A robot will never explore a known dead end.
	 */
	public void setNextTileToDeadEnd() {
		if(getCurrentTile().getNodeAt(getCurrentRobotOrientation())==null){
			generateTileNodeAt(Orientation.NORTH); //This is a relative orientation. In this case it just means in front of the robot.
		}
		if(getCurrentTile().getNodeAt(getCurrentRobotOrientation())!=null && getCurrentTile().getNodeAt(getCurrentRobotOrientation()).getClass().equals(TileNode.class)){
			TileNode deadEndNode = (TileNode)getCurrentTile().getNodeAt(getCurrentRobotOrientation());
			generateWallNodeAt(deadEndNode, getCurrentRobotOrientation().getRelativeOrientation(Orientation.WEST));
			generateWallNodeAt(deadEndNode, getCurrentRobotOrientation().getRelativeOrientation(Orientation.NORTH));
			generateWallNodeAt(deadEndNode, getCurrentRobotOrientation().getRelativeOrientation(Orientation.EAST));
		} else {
			ContentPanel.writeToDebug("Couldn't create a dead end at the position in front of the robot.");
		}
		
	}

	/**
	 * Sets the tile in front of the robot to a Seesaw tile, creating the node that should be there if it doesn't exist yet.
	 * The SeesawNode connected to that node is/was automatically created upon construction of the first node.
	 * Walls are placed to the sides of the SeesawNodes and an additional node is generated at the far side of the seesaw.
	 * @param isUpAtThisSide
	 */
	public void setNextTileToSeesaw(boolean isUpAtThisSide) {
		if(getCurrentTile().getNodeAt(getCurrentRobotOrientation())==null){
			generateSeesawNodeAt(Orientation.NORTH,isUpAtThisSide); //This is a relative orientation. In this case it just means in front of the robot.
			SeesawNode seesaw1 = (SeesawNode)getCurrentTile().getNodeAt(getCurrentRobotOrientation());
			generateWallNodeAt(seesaw1, getCurrentRobotOrientation().getRelativeOrientation(Orientation.WEST));
			generateWallNodeAt(seesaw1, getCurrentRobotOrientation().getRelativeOrientation(Orientation.EAST));
			generateWallNodeAt(seesaw1.getPairedNode(), getCurrentRobotOrientation().getRelativeOrientation(Orientation.WEST));
			generateWallNodeAt(seesaw1.getPairedNode(), getCurrentRobotOrientation().getRelativeOrientation(Orientation.EAST));
			generateTileNodeAt(seesaw1.getPairedNode(), getCurrentRobotOrientation());
			generateWallNodeAt((TileNode)seesaw1.getPairedNode().getNodeAt(getCurrentRobotOrientation()), getCurrentRobotOrientation().getRelativeOrientation(Orientation.WEST));
			generateWallNodeAt((TileNode)seesaw1.getPairedNode().getNodeAt(getCurrentRobotOrientation()), getCurrentRobotOrientation().getRelativeOrientation(Orientation.EAST));
			System.out.println(seesaw1+","+seesaw1.getPairedNode());
		}
		if(getCurrentTile().getNodeAt(getCurrentRobotOrientation())!=null && getCurrentTile().getNodeAt(getCurrentRobotOrientation()).getClass().equals(SeesawNode.class)){
			SeesawNode seesaw1 = (SeesawNode)getCurrentTile().getNodeAt(getCurrentRobotOrientation());
			seesaw1.setUp(isUpAtThisSide);
		} else {
			ContentPanel.writeToDebug("Couldn't create a seesaw at the position in front of the robot.");
		}
	}
	
	/**
	 * True if the next tile is a SeesawNode.
	 */
	public boolean nextTileIsSeesaw(){
		return getCurrentTile().getNodeAt(getCurrentRobotOrientation()).getClass().equals(SeesawNode.class);
	}

	/**
	 * The tiles we've already seen or visited, in the order they were seen.
	 * The last fully expanded tile in this list is the current tile.
	 * @return
	 */
	public ArrayList<TileNode> getFoundTilesList() {
		return tileNodes;
	}

	/**
	 * The MazeGraph will be updated with the information contained in this mapstringmatrix.
	 * Tiles that are next to currently known tiles will be added iteratively until no more
	 * tiles can be added, so separated islands of tiles are not added (that's an inconsistent state any way).
	 * @param resultMap
	 */
	public void updateWithMap(String[][] resultMap) {	
		boolean changed;
		do{
			changed = false;
			for(int i=0; i<=resultMap.length; i++){
				for(int j=0; j<=resultMap[0].length; j++){
					if(getTileNodeAt(i, j)!=null && resultMap[i][j]!=null && !resultMap[i][j].equals("dummy")){
						for(Orientation o : Orientation.values()){
							int x = o.getXValue(); int y = o.getYValue();
							if(resultMap[i+x][j+y]!=null && !resultMap[i][j].equals("dummy")){
								if(resultMap[i+x][j+y].startsWith("Seesaw")){
									generateSeesawNodeAt(getTileNodeAt(i, j), o, false); //Assumed to be down at this side
								} else {
									generateTileNodeAt(getTileNodeAt(i, j), o);
								}
								updateTileFromToken(getTileNodeAt(i+x, j+y), resultMap[i+x][j+y]);
								changed = true;
							}
						}
					}
				}
			}
		} while(changed);
	}

	private void updateTileFromToken(TileNode tileNodeAt, String string) {
		String[] split = string.split(".");
		for(Orientation o : Orientation.values()){
			if(Token.valueOf(split[0].toUpperCase()).hasWallAt(o.getRelativeOrientation(Orientation.getOrientation(split[1])))){
				generateWallNodeAt(tileNodeAt, o);
			}
		}
		try{
			int barcode = -1;
			if(split.length >= 3){
				barcode = Integer.parseInt(split[2]);
				setTileBarcode(tileNodeAt, barcode);
			}
		} catch(NumberFormatException e){
			//Nothing
		}
	}

	/**
	 * Sets the finish to the teammate's position and sets the drivingToPartner flag so the robot will explore
	 * in the direction of the teammate.
	 * @param partnerX
	 * @param partnerY
	 */
	public void setPartnerPosition(int partnerX, int partnerY) {
		setTileToFinish(getTileNodeAt(partnerX, partnerY));
		setDrivingToPartner(true);
	}
}
