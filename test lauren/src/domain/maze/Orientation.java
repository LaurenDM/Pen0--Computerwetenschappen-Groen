package domain.maze;

import java.util.ArrayList;

public enum Orientation {
	NORTH(0,1,90), EAST(1,0,0), SOUTH(0,-1,-90), WEST(-1,0,180);

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
		final int MARGE = 10;
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

	private Orientation(int x, int y, double angle){
		this.x = x;
		this.y = y;	
		this.angle = angle;
	}

	private final int x;
	private final int y;
	private final double angle;
	private ArrayList<Orientation> orderedList;

	public int getXValue(){
		return x;
	}
	public int getYValue(){
		return y;
	}


}

