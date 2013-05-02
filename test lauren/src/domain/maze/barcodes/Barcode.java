package domain.maze.barcodes;

import java.util.ArrayList;
import java.util.List;

import domain.Position.Position;
import domain.maze.MazeElement;
import domain.maze.Orientation;
import domain.robots.RobotPilot;

public class Barcode extends MazeElement{
	
	private final Action action;
	private Position pos; //centre of barcode ( == centre of square; ex "20,20", always multiple of 20)
	private final Orientation orientation;
	private int[] readBits;
	private List<Integer> legalInts = new ArrayList<Integer>();
	private boolean printed;
	private final int decimal;
	
	public Barcode(int decimal, Position pos, Orientation orientation){
		fillLegals();
		readBits = getBinary(decimal);
		if(!legalInts.contains(decimal)){
			this.decimal = getDecimal(getmirroredBits());
		}else{
			this.decimal=decimal;
		}
		this.pos = pos;
		this.orientation = orientation;
		this.action = getAction(this.decimal);
		
		//System.out.println("Barcode created with value "+this.readBits[5]+this.readBits[4]+this.readBits[3]+this.readBits[2]+this.readBits[1]+this.readBits[0]+" ("+decimal+") at position x:"+pos.getX()+" y:"+pos.getY()+" facing "+this.orientation);
	}
	
	
	public Barcode(int[] bits, Position pos, double angle){
		this.readBits = bits;
		fillLegals();
		if(bits.length != 6){
			throw new IllegalArgumentException();
		}
		int calculatedDecimal=getDecimal(bits);
		if(!legalInts.contains(calculatedDecimal)){
			this.decimal = getDecimal(getmirroredBits());
		}else{
			this.decimal = calculatedDecimal;
		}
		this.pos = pos;
		this.orientation = Orientation.getOrientation(angle);
		
		action = getAction(this.decimal);
//		System.out.println("Barcode created with value "+this.readBits[5]+this.readBits[4]+this.readBits[3]+this.readBits[2]+this.readBits[1]+this.readBits[0]+" ("+decimal+") at position x:"+pos.getX()+" y:"+pos.getY()+" facing "+this.orientation);

	}
	
	

	

	private static int ownBallNumber = 0;
	private static int[] otherBallNumbers = new int[] {1,2,3};
	private static int[] seesawNumbers = new int[] {11,13,15,17,19,21};
	private static int[] checkPointNumbers = new int[]{55,47,43,39,37};
	
	public static void setBallNumber(int ballNumber) {
		ownBallNumber = ballNumber;
	}
	
	public static void setFalseBallNumbers(int[] numbers){
		otherBallNumbers = numbers;
	}
	
	private static boolean isOtherBallNumber(int number){
		if(number<8 && number%4!=ownBallNumber){
			return true;
		}
		return false;
	}
	
	private static boolean isCheckPointNumber(int number){
		for (int n : checkPointNumbers){
			if(n==number)
				return true;
		}
		return false;
	}
	
//	private static boolean isOwnBallNumber(int number){
//		if(number == ownBallNumber){
//			return true;
//		} else {
//			return false;
//		}
//	}
	
	public boolean isSeesawBC(){
		for(int n : seesawNumbers){
			if(n == decimal)
				return true;
		}
		return false;
	}
	
	public Action getAction(int number){
		if((number<8) && (ownBallNumber == number%4)){
			int teamNb = readBits[3];
			return new FetchBallAction(teamNb);
		}
		if(isOtherBallNumber(number)){
			return new DoNothingAction();
		}
		else if(isSeesawBC()){
			Position pos = getCenterPosition().getNewPosition(getOrientation().getAngleToHorizontal(), 60);
			return new SeesawAction(number, pos, getOrientation());
		}
		else if(isCheckPointNumber(number)){
			return new SetCheckPointAction();
		}
			return null;
		
	}
	
	
	public Action getAction(){
		return action;
	}
	
	public void runAction(RobotPilot robot){
		if(action != null){
			action.run(robot);
		}else{
			System.out.println("We tried to run a barcode-action that was null");
			
		}
	}

	//The orientation of the robot when he was reading the bits
	public Orientation getOrientation(){
		return orientation;
	}
	
	//Return the bits in the order they were read.
	public int[] getReadBits(){
		return readBits;
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
			 bit = readBits[distance/2-1];
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
			 bit = readBits[distance/2-1];
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
	
	
	private int[] getmirroredBits(){
		int[] mirroredBits=new int[6];
		for(int i = 0; i<6; i++){
			mirroredBits[i] = readBits[5-i];
		}
		return mirroredBits;
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
		return decimal;
	}

	@Override
	public Position getCenterPosition() {
		return getPos();
	}

	@Override
	public boolean hasPosition(Position position) {
		return containsPosition(position);
	}
	public boolean sameFirstReadOrientation(double robotOrientationToCompare) {
			return(Orientation.getOrientation(robotOrientationToCompare).equals(getOrientation()));
	}
}
