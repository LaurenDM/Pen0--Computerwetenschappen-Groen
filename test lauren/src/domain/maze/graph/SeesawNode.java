package domain.maze.graph;

import domain.maze.Orientation;

/**
 * @author joren
 *
 */
public class SeesawNode extends TileNode {
	private boolean up;
	private final SeesawNode pairedSeesawNode;

	/**
	 * Makes 2 connected Seesaw Nodes (one on the tile specified and the other 1 tile further from in the specified orientation)
	 * @param currentNode
	 * @param orientationToCurrentNode
	 */
	public SeesawNode(TileNode currentNode, Orientation orientationToCurrentNode) {
		super(currentNode, orientationToCurrentNode);
		pairedSeesawNode = new SeesawNode(this, orientationToCurrentNode);

	}

	/**
	 * This constructor is used to automatically construct the other side of a seesaw
	 * @param otherSeesawNode
	 * @param orientationToOther
	 * @param justUseTrue
	 */
	private SeesawNode(SeesawNode otherSeesawNode, Orientation orientationToOther) {
		super(otherSeesawNode, orientationToOther);
		pairedSeesawNode = otherSeesawNode;
	}

	/**
	 * @return the up state
	 */
	public boolean isUp() {
		return up;
	}

	/**
	 * Sets the up state for this seesawnode and corrects the up state for the paired seesaw node
	 * @param up whether the seesaw is up
	 */
	public void setUp(boolean up) {
		this.up = up;
		if(pairedSeesawNode.isUp()==this.isUp()){
			pairedSeesawNode.setUp(!up);
		}
	}

	/**
	 * Example: S(1,3,up) or S(5,-7,down)
	 */
	@Override
	public String toShortString() {
		return "S("+getX()+","+getY()+(isUp()?",up)":",down)");
	}

	/**
	 * The finish can not be on a seesaw.
	 */
	@Override
	public boolean isFinish() {
		return false;
	}

	/**
	 * ==!isUp()
	 */
	@Override
	public boolean isAccessible() {
		return !isUp();
	}
	
	public SeesawNode getPairedNode(){
		return pairedSeesawNode;
	}

}