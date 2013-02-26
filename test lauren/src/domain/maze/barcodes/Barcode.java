package domain.maze.barcodes;

import java.util.ArrayList;
import java.util.List;

import domain.Position.Position;
import domain.maze.MazeElement;
import domain.maze.Orientation;
import domain.robots.Robot;

public class Barcode extends MazeElement{

	private Action action;
	private Position pos; //centre of barcode ( == centre of square; ex "20,20", always multiple of 20)
	private Orientation orientation;
	private int[] bits;
	private List<Integer> legalInts = new ArrayList<Integer>();
	private boolean printed;
	
	public Barcode(int[] bits, Position pos, double angle){
		fillLegals();
		if(bits.length != 6){
			throw new IllegalArgumentException();
		}
		this.bits = bits;
		this.pos = pos;
		this.orientation = getOrientation(angle);
		int decimal = getDecimal(bits);
		if(!legalInts.contains(decimal)){
			mirrorBits();
			decimal = getDecimal(this.bits);
		}
		action = getAction(decimal);
		System.out.println("Barcode created with value "+this.bits[5]+this.bits[4]+this.bits[3]+this.bits[2]+this.bits[1]+this.bits[0]+" ("+decimal+") at position x:"+pos.getX()+" y:"+pos.getY()+" facing "+this.orientation);
	}
	
	public Barcode(int decimal, Position pos, Orientation orientation){
		fillLegals();
		bits = getBinary(decimal);
		if(!legalInts.contains(decimal)){
			mirrorBits();
			decimal = getDecimal(this.bits);
		}
		this.pos = pos;
		this.orientation = orientation;
		action = getAction(decimal);
		System.out.println("Barcode created with value "+this.bits[5]+this.bits[4]+this.bits[3]+this.bits[2]+this.bits[1]+this.bits[0]+" ("+decimal+") at position x:"+pos.getX()+" y:"+pos.getY()+" facing "+this.orientation);
	}
	
	public Barcode(int decimal, Position pos, double angle){
		this(decimal, pos, getOrientation(angle));
	}
	
	public static Orientation getOrientation(double angle) {
		final int MARGE = 10;
		if(Math.abs(angle-0)<MARGE) return Orientation.EAST;
		else if(Math.abs(angle-90) <MARGE) return Orientation.SOUTH;
		else if(Math.abs(angle-180) <MARGE || Math.abs(angle+180)<MARGE) return Orientation.WEST;
		else if(Math.abs(angle+90) <MARGE) return Orientation.NORTH;
		else throw new IllegalArgumentException();
	}
	
	private static int BALLBARCODE = 15;
	private static int[] FALSEBARCODES = new int[] {0};
	
	public static void setBallBarcode(int ballBarcode) {
		BALLBARCODE = ballBarcode;
	}
	
	public static void setFalseBallBarcodes(int[] barcodes){
		FALSEBARCODES = barcodes;
	}
	
	private static boolean isFalseBarcode(int number){
		for (int falseBarcodeNbr : FALSEBARCODES) {
			if(falseBarcodeNbr == number)
				return true;
		}
		return false;
	}
	
	public static Action getAction(int number){
		if(BALLBARCODE == number)
			return new FetchBallAction();
		if(isFalseBarcode(number))
			return new DoNothingAction();
		else{
			switch (number){
			case 5: return new TurnLeftAction();
			case 9: return new TurnRightAction();
			case 13: return new SetCheckPointAction();
			case 15: return new PlayTuneAction();
			case 19: return new Wait5Action();
			case 25: return new DriveSlowAction();
			case 37: return new DriveFastAction();
			case 55: return new SetFinishAction();
			default: return null;
			}
		}
	}
	
	
	public Action getAction(){
		return action;
	}
	
	public void runAction(Robot robot){
		if(action != null){
			action.run(robot);
		}
	}
	
	@Override
	public boolean hasPosition(Position hasP){
		if(orientation==Orientation.NORTH || orientation==Orientation.SOUTH){
			return false;
		}
		else{
			return false;
		}
	}
	
	public Orientation getOrientation(){
		return orientation;
	}
	
	public int[] getBits(){
		return bits;
	}
	

	
	public int getBitAtPosition(Position bitPos){
		if(orientation==Orientation.NORTH || orientation==Orientation.SOUTH){
			return getBitAtVerticalPosition(bitPos);
		}
		else if(orientation==Orientation.WEST || orientation==Orientation.EAST){
			return getBitAtHorizontalPosition(bitPos);
		}
		else return 1;
	}
	
	public int getBitAtVerticalPosition(Position bitPos){	
		//if pos not in barcode; return 1
		if(!containsPosition(bitPos)){
			return 1;
		}
		// y-coordinate of lowest point of lowest outer black line
		int bottomBlackLineBottomInt = 0;
		if(orientation==Orientation.SOUTH || orientation==Orientation.EAST){
		bottomBlackLineBottomInt = ((int) pos.getY()) - 8;
		}
		else if (orientation==Orientation.NORTH || orientation==Orientation.WEST){
		bottomBlackLineBottomInt = ((int) pos.getY()) + 7;
		}
		int distance = Math.abs((int)bitPos.getY() - bottomBlackLineBottomInt);
		int bit;
		if(distance<2 || distance >= 14){
			 return 0;
		}
		else
			 bit = bits[distance/2-1];
		return bit;	
	}
	
	public int getBitAtHorizontalPosition(Position bitPos){		
		//if pos not in barcode; return 1
		if(!containsPosition(bitPos)){
			return 1;
		}
		// y-coordinate of lowest point of lowest outer black line
		int bottomBlackLineBottomInt = 0;
		if(orientation==Orientation.EAST){
		bottomBlackLineBottomInt = ((int) pos.getX()) - 8;
		}
		else if (orientation==Orientation.WEST){
		bottomBlackLineBottomInt = ((int) pos.getX()) + 7;
		}
		int distance = Math.abs((int)bitPos.getX() - bottomBlackLineBottomInt);
		int bit;
		if(distance<2 || distance >= 14){
			 return 0;
		}
		else
			 bit = bits[distance/2-1];
		return bit;	
	}
	
	public boolean containsPosition(Position bitPos){
		if(orientation==Orientation.NORTH || orientation==Orientation.SOUTH){
			if(bitPos.getX()>pos.getX()-20 && bitPos.getX()<pos.getX()+20){
			if(bitPos.getY()>pos.getY()-9 && bitPos.getY()<pos.getY()+8){
				return true;
			}
			else return false;
			}
			else return false;
		}
		else if(orientation==Orientation.WEST || orientation==Orientation.EAST){
			if(bitPos.getY()>pos.getY()-20 && bitPos.getY()<pos.getY()+20){
			if(bitPos.getX()>pos.getX()-9 && bitPos.getX()<pos.getX()+8){
				return true;
			}
			else return false;
			}
			else return false;
		}
		else return false;
	}	
	
	public boolean isBlackAt(Position pos){
		if(!containsPosition(pos)) return false;
		if(getBitAtPosition(pos) == 0){
			return true;
		}
		else return false;
	}
	
	public boolean isWhiteAt(Position pos){
		if(!containsPosition(pos)) return false;
		if(getBitAtPosition(pos) == 1){
			return true;
		}
		else return false;
	}
	
	
	private void mirrorBits(){
		for(int i = 0; i<3; i++){
			int temp = bits[i];
			bits[i] = bits[5-i];
			bits[5-i] = temp;
		}
		if(orientation==Orientation.NORTH){
			orientation = Orientation.SOUTH;
		}
		else if(orientation==Orientation.SOUTH){
			orientation = Orientation.NORTH;
		}
		else if(orientation==Orientation.WEST){
			orientation = Orientation.EAST;
		}
		else if(orientation==Orientation.EAST){
			orientation = Orientation.WEST;
		}
	}
	
	private int getDecimal(int[] bits){
		int result = 0;
		for(int i = 0; i< 6; i++){
			result = (int) (result + bits[i]*Math.pow(2, i));
		}
		return result;
	}
	
	
	private int[] getBinary(int decimal){
		int[] result = new int[6];
		for(int i = 0; i< 6; i++){
			result[i] = decimal%2;
			decimal = decimal/2;
		}
		return result;
	}
	
	public Position getPos(){
		return pos;
	}
	
	
	private void fillLegals(){
		legalInts.add(1);
		legalInts.add(2);
		legalInts.add(3);
		legalInts.add(4);
		legalInts.add(5);
		legalInts.add(6);
		legalInts.add(7);
		legalInts.add(9);
		legalInts.add(10);
		legalInts.add(11);
		legalInts.add(13);
		legalInts.add(14);
		legalInts.add(15);
		legalInts.add(17);
		legalInts.add(19);
		legalInts.add(21);
		legalInts.add(23);
		legalInts.add(25);
		legalInts.add(27);
		legalInts.add(29);
		legalInts.add(31);
		legalInts.add(35);
		legalInts.add(37);
		legalInts.add(39);
		legalInts.add(43);
		legalInts.add(47);
		legalInts.add(55);
	}
	
	public boolean getPrinted(){
		return printed;
	}
	
	public void setPrinted(boolean printed){
		this.printed = printed;
	}
	
	public int getPossibleDecimal(){
		if(!legalInts.contains(getDecimal(this.bits))){
			mirrorBits();
		}
		return getDecimal(this.bits);
	}

	@Override
	public Position getCenterPosition() {
		return getPos();
	}
}
