package domain.maze;

import java.util.ArrayList;

/**
 * Orientations represent the 4 different orientations a robot can have in the maze.
 */
public enum Orientation {
	NORTH(0,1,0), EAST(1,0,1), SOUTH(0,-1,2), WEST(-1,0,-1);

	/**
	 * Turns N,S,E,W into NORTH,SOUTH,...
	 * @param str
	 * @return
	 */
	public static Orientation getOrientation(String str){
		if(str.equals("N")){
			return NORTH;
		}
		else if(str.equals("S")){
			return SOUTH;
		}
		else if(str.equals("E")){
			return EAST;
		}
		else if(str.equals("W")){
			return WEST;
		}
		else{
			System.out.println("Error parsing orientation");
			return NORTH;
		}

	}

	/**
	 * @return The Orientation to the left of this one.
	 */
	public Orientation getLeft(){
		return this.getOffset(-1);
	}

	/**
	 * @return The Orientation to the right of this one.
	 */
	public Orientation getRight(){
		return this.getOffset(1);
	}

	/**
	 * @return The opposite Orientation to this one.
	 */
	public Orientation getBack(){
		return this.getOffset(-2);
	}

	/**
	 * @param offset The amount of places to shift this orientation by. Positive numbers are to the right, negative to the left.
	 * @return The orientation at "offset" right turns relative to this one.
	 */
	private Orientation getOffset(int offset){
		if(Math.abs(offset)>2) {
			throw new IllegalArgumentException("Cannot return the orientation at this offset.");
		} else {
			ArrayList<Orientation> orderedList = new ArrayList<Orientation>();
			for(Orientation o : orderedArray){
				orderedList.add(o);
			}
			if(offset<0){
				return orderedList.get(orderedList.lastIndexOf(this)+offset);
			} else {
				return orderedList.get(orderedList.indexOf(this)+offset);
			}
		}
	}

	/**
	 * An ordered array of the Orientations, with double entries so it's possible to shift EAST 2 places to the left.
	 */
	private static final Orientation[] orderedArray = {NORTH, EAST, SOUTH, WEST, NORTH, EAST};

	private Orientation(int x, int y, int relativeOffset){
		this.x = x;
		this.y = y;	
		this.relativeOffset = relativeOffset;
	}

	private final int x;
	private final int y;
	private final int relativeOffset;

	/**
	 * The x value of the point on the unit circle at this orientation.
	 * @return is an element of {-1,0,1}
	 */
	public int getXValue(){
		return x;
	}
	/**
	 * The y value of the point on the unit circle at this orientation.
	 * @return is an element of {-1,0,1}
	 */
	public int getYValue(){
		return y;	
	}

	/**
	 * Returns the orientation in front of, right of, behind, left of this orientation if "relative" is NORTH, EAST, SOUTH, WEST respectively.
	 * @param relative
	 * @return
	 */
	public Orientation getRelativeOrientation(Orientation relative) {
		return this.getOffset(relative.relativeOffset);
	}


}

