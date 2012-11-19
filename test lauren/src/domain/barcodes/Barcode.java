package domain.barcodes;

import java.util.ArrayList;
import java.util.List;

import domain.Position.Position;
import domain.maze.Orientation;
import domain.robots.Robot;

public class Barcode {

	private Action action;
//	private Position pos1; 
//	private Position pos2; 
	private Position pos; //centre of barcode ( == centre of square; ex "20,20", always multiple of 20)
	private Orientation orientation;
	private int[] bits;
	private List<Integer> legalInts = new ArrayList<Integer>();
	
	public Barcode(int[] bits, Position pos, Orientation orientation){
		fillLegals();
		if(bits.length != 6){
			throw new IllegalArgumentException();
		}
		this.bits = bits;
		this.pos = pos;
		this.orientation = orientation;
		int decimal = getDecimal(bits);
		if(!legalInts.contains(decimal)){
			mirrorBits();
			decimal = getDecimal(this.bits);
		}
		action = getAction(decimal);
		System.out.println("Barcode created with value "+bits[5]+bits[4]+bits[3]+bits[2]+bits[1]+bits[0]+" ("+decimal+") at position x:"+pos.getX()+" y:"+pos.getY()+" facing "+orientation);
	}
	
	public Barcode(int decimal, Position pos, Orientation orientation){
		fillLegals();
		if(!legalInts.contains(decimal)){
			throw new IllegalArgumentException();
		}
		this.pos = pos;
		this.orientation = orientation;
		bits = getBinary(decimal);
		action = getAction(decimal);
		System.out.println("Barcode created with value "+bits[5]+bits[4]+bits[3]+bits[2]+bits[1]+bits[0]+" ("+decimal+") at position x:"+pos.getX()+" y:"+pos.getY()+" facing "+orientation);

	}
	
	
	public Action getAction(int number){
		switch (number){
		case 5: return new TurnLeftAction();
		case 9: return new TurnRightAction();
		case 15: return new PlayTuneAction();
		case 19: return new Wait5Action();
		case 25: return new DriveSlowAction();
		case 37: return new DriveFastAction();
		case 55: return new SetFinishAction();
		default: return null;
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
	
//	public boolean hasPosition(Position pos){
//		if(horizontal()){
//			int lowx = (int) (Math.floor((pos1.getX())/MAZECONSTANT))*MAZECONSTANT;
//			if(pos.getX()<=lowx || pos.getX() >= lowx + MAZECONSTANT) return false;
//			else if((pos.getY() <= pos1.getY() && pos.getY() >= pos2.getY()) || 
//					pos.getY() >= pos1.getY() && pos.getY() <= pos2.getY()) return true;
//		}
//		else {
//			int lowy = (int) (Math.floor((pos1.getY())/MAZECONSTANT))*MAZECONSTANT;
//			if(pos.getY()<=lowy || pos.getY() >= lowy + MAZECONSTANT) return false;
//			else if((pos.getX() <= pos1.getX() && pos.getX() >= pos2.getX()) || 
//					pos.getX() >= pos1.getX() && pos.getX() <= pos2.getX()) return true;
//		}
//		return false;
//	}
	
//	public int getBitAtPosition(Position pos){
//		int distance;
//		if(horizontal()){
//			 distance = (int) Math.abs(pos.getY()- pos1.getY());
//		}
//		else{
//			 distance = (int) Math.abs(pos.getX() - pos1.getX());
//		}
//		return bits[distance/2 - 1];
//	}
	
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
			result += bits[5-i]*2^(i);
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
	
}