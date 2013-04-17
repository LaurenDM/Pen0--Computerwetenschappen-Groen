package domain.maze;

import java.util.ArrayList;

/**
 * Orientations represent the 4 different orientations a robot can have in the maze.
 */
public enum Orientation {
	NORTH(0,1,-90,0), EAST(1,0,0,1), SOUTH(0,-1, 90,2), WEST(-1,0,180,-1);

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
	
	
	public static Orientation getOrientation(double angle){
		final int MARGE = 20;
		if(Math.abs(angle-0)<MARGE) return EAST;
		else if(Math.abs(angle-90) <MARGE) return SOUTH;
		else if(Math.abs(angle-180) <MARGE || Math.abs(angle+180)<MARGE) return WEST;
		else if(Math.abs(angle+90) <MARGE) return NORTH;
		else throw new IllegalArgumentException();
	}

	public Orientation getLeft(){
		return this.getOffset(-1);
	}
	
	public double getAngleToHorizontal(){
		return this.angle;
	}

	public Orientation getRight(){
		return this.getOffset(1);
	}

	public Orientation getBack(){
		return this.getOffset(-2);
	}

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

	private static final Orientation[] orderedArray = {NORTH, EAST, SOUTH, WEST, NORTH, EAST};

	private Orientation(int x, int y, double angle, int relativeOffset){
		this.x = x;
		this.y = y;	
		this.angle = angle;
		this.relativeOffset = relativeOffset;
	}

	private final int x;
	private final int y;
	private final double angle;
	private final int relativeOffset;
	private ArrayList<Orientation> orderedList;

	public int getXValue(){
		return x;
	}
	public int getYValue(){
		return y;
	}
	
	/**
	 * Returns the orientation in front of, right of, behind, left of this orientation if "relative" is NORTH, EAST, SOUTH, WEST respectively.
	 * @param relative
	 * @return
	 */
	public Orientation getRelativeOrientation(Orientation relative){
		return this.getOffset(relative.getRelativeOffset());
	}

	private int getRelativeOffset() {
		return relativeOffset;
	}


		public static int snapAngle(int mod, int offset, double notSnapped) {
			boolean positive=notSnapped>=0;
			notSnapped*=(positive?1:-1);

			int intNotSnapped=(int) notSnapped-offset;

			int snappedNumber=(intNotSnapped/mod)*mod;
			if(intNotSnapped-snappedNumber> mod/2){
				snappedNumber+=mod;
			}
			return (positive?1:-1)*(snappedNumber+offset);
	}


}

