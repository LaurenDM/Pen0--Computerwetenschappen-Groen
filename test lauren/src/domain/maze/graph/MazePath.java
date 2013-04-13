package domain.maze.graph;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

import domain.maze.Orientation;

public class MazePath implements Comparable<MazePath>, Iterable<TileNode> {

	private ArrayList<TileNode> nodeList = new ArrayList<TileNode>();
	private TileNode goal;
	private int currentCost;
	private Orientation startingOrientation;

	/**
	 * 
	 * @param startingTile The starting tile for this path, automatically added to it.
	 * @param goalTile The goal tile for this path, not added unless a proper connection to it is made! Needed for shortest path finding.
	 */
	public MazePath(TileNode startingTile, Orientation startingOrientation, TileNode goalTile){
		nodeList.add(startingTile);
		goal = goalTile;
		currentCost = 0;
		this.startingOrientation=startingOrientation;
	}

	/**
	 * A copy of the given MazePath.
	 * @param pathToCopy
	 */
	public MazePath(MazePath pathToCopy){
		for(TileNode node: pathToCopy){
			nodeList.add(node);
		}
		this.goal = pathToCopy.getGoalTile();
		this.currentCost = pathToCopy.getCurrentCost();
	}

	/**
	 * An extension of the given MazePath with the given TileNode.
	 * @param pathToExtend
	 * @param extension
	 */
	public MazePath(MazePath pathToExtend, TileNode extension){
		this(pathToExtend);
		nodeList.add(extension);
		boolean turn = false;
		if(extension!=null){
			TileNode currentEndTile = pathToExtend.getCurrentEndTile();
			TileNode tileBefore = pathToExtend.getTileBefore(currentEndTile);
			turn = extension.relativeOrientationTo(currentEndTile).equals(tileBefore!=null?currentEndTile.relativeOrientationTo(tileBefore):pathToExtend.getStartingOrientation());
		} else {
			throw new IllegalArgumentException();
		}
		this.currentCost = pathToExtend.getCurrentCost()+(turn?2:1);
	}

	private TileNode getTileBefore(TileNode tile) {
		Iterator<TileNode> it = this.iterator();
		TileNode previous = it.next();
		TileNode next = null;
		while(it.hasNext()){
			next = it.next();
			if(next.equals(tile)){
				break;
			}
			previous = next;
		}
		return next==null?null:previous;
	}

	private Orientation getStartingOrientation(){
		return startingOrientation;
	}

	/**
	 * The A* F-score for this path.
	 * @return
	 */
	public int getFScore(){
		return getCurrentLength()+getHeuristic();
	}

	/**
	 * The amount of nodes travelled to in this path.
	 * @return
	 */
	public int getCurrentLength(){
		return nodeList.size()-1;
	}

	/**
	 * The cost of this path: the number of visited nodes + number of turns (so equal weighting between moves and turns)
	 * @return
	 */
	public int getCurrentCost(){
		return currentCost;
	}

	/**
	 * Manhattan distance between this node and the goal.
	 * @return
	 */
	public int getHeuristic(){
		return getCurrentEndTile().manhattanDistanceTo(getGoalTile());
	}

	/**
	 * Goal for this path.
	 * @return
	 */
	public TileNode getGoalTile(){
		return goal;
	}

	/**
	 * Current last tile in this path.
	 * @return 
	 */
	public TileNode getCurrentEndTile(){
		return nodeList.get(nodeList.size()-1);
	}

	/**
	 * First tile in the path.
	 * @return
	 */
	public TileNode getStartingTile(){
		return nodeList.get(0);
	}

	/**
	 * Checks if this path contains "node"
	 * @param node
	 * @return
	 */
	public boolean contains(MazeNode node){
		return TileNode.class.isAssignableFrom(node.getClass())?nodeList.contains(node):false;
	}

	@Override
	public int compareTo(MazePath otherPath) {
		return getFScore()<otherPath.getFScore()?-1:(getFScore()>otherPath.getFScore()?1:0);
	}

	@Override
	public Iterator<TileNode> iterator() {
		return nodeList.iterator();
	}

	/**
	 * A* expansion of this MazePath.
	 * @return A collection of 0 to 3 MazePaths that are an extension of this path to a neighbouring node.
	 */
	public Collection<MazePath> expand() {
		ArrayList<MazePath> returnList = new ArrayList<MazePath>();
		for(Orientation o : Orientation.values()){
			MazeNode neighbourNode = getCurrentEndTile().getNodeAt(o);
			//A tileNode is added if it was not blocked less than a certain number of tiles ago 
			//If we know the node at the other side of the seesaw and the closest seesawnode is accessible the entire seesaw and that node are added.
			if(neighbourNode!=null && neighbourNode.isAccessible() && !this.contains(neighbourNode)){
				MazePath pathToAdd = new MazePath(this,(TileNode) neighbourNode);
				if(neighbourNode.getClass().equals(SeesawNode.class)){
					SeesawNode pairedNode = ((SeesawNode)neighbourNode).getPairedNode();
					MazePath extendedSeesaw = new MazePath(pathToAdd,pairedNode);
					MazeNode nodeAt = pairedNode.getNodeAt(o);
					if(nodeAt!=null && !nodeAt.getClass().equals(WallNode.class)){
						TileNode nA = (TileNode)nodeAt;
						returnList.add(new MazePath(extendedSeesaw,nA));
					}
				} else {
					returnList.add(pathToAdd);
				}
			}
		}
		return returnList;
	}

	@Override
	public String toString(){
		String ret = "";
		for(TileNode node:nodeList){
			ret+="("+node.getX()+","+node.getY()+")";
		}
		return ret;
	}

	/**
	 * Append the other path to this path to create a path that passes through all of the nodes in both paths.
	 * Necessary checks are done to ensure that the path created is consistent.
	 * @param otherPath
	 * @return
	 * @throws IllegalArgumentException If the other path given doesn't start at this path's last tile (or a neighboring tile).
	 */
	public MazePath append(MazePath otherPath) {
		MazePath ret = new MazePath(this);
		if(otherPath!=null){
			Iterator<TileNode> otherPathIt = otherPath.iterator();
			if(otherPath.getStartingTile().equals(this.getCurrentEndTile())){
				otherPathIt.next();
			}
			if(otherPathIt.hasNext()){
				boolean valid = false;
				TileNode otherFirst = otherPathIt.next();
				for(Orientation o: Orientation.values()){
					valid |= getCurrentEndTile().getNodeAt(o)!=null && getCurrentEndTile().getNodeAt(o).equals(otherFirst);
				}
				if(!valid) throw new IllegalArgumentException();
				ret = new MazePath(ret,otherFirst);
				while(otherPathIt.hasNext()){
					ret = new MazePath(ret,otherPathIt.next());
				}
			}
		}
		return ret;
	}

	@Override
	public boolean equals(Object arg0){
		if(arg0==null || !arg0.getClass().equals(this.getClass())){
			return false;
		} else {
			Iterator<TileNode> thisIt = this.iterator();
			Iterator<TileNode> otherIt = ((MazePath) arg0).iterator();
			while(thisIt.hasNext() && otherIt.hasNext()){
				if(!thisIt.next().equals(otherIt.next())) return false;
			}
		}
		return this.getGoalTile().equals(((MazePath) arg0).getGoalTile());
	}

}