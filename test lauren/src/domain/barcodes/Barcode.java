package domain.barcodes;

import java.util.ArrayList;
import java.util.List;

import domain.Position.Position;
import domain.robots.Robot;

public class Barcode {

	private final int MAZECONSTANT = 40;
	private Action action;
	private Position pos1;
	private Position pos2;
	private int[] bits;
	private List<Integer> legalInts = new ArrayList<Integer>();
	
	public Barcode(int[] bits, Position pos1, Position pos2){
		fillLegals();
		if(bits.length != 6){
			throw new IllegalArgumentException();
		}
		this.bits = bits;
		this.pos1 = pos1;
		this.pos2 = pos2;
		int decimal = getDecimal(bits);
		if(!legalInts.contains(decimal)){
			mirrorBits();
			decimal = getDecimal(this.bits);
		}
		action = getAction(decimal);
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
	
	public Barcode(Action action, Position pos1, Position pos2){
		this.action = action;
		this.pos1 = pos1;
		this.pos2 = pos2;
	}
	
	public Action getAction(){
		return action;
	}
	
	public void runAction(Robot robot){
		if(action != null){
			action.run(robot);
		}
	}
	
	public boolean hasPosition(Position pos){
		if(horizontal()){
			int lowx = (int) (Math.floor((pos1.getX())/MAZECONSTANT))*MAZECONSTANT;
			if(pos.getX()<=lowx || pos.getX() >= lowx + MAZECONSTANT) return false;
			else if((pos.getY() <= pos1.getY() && pos.getY() >= pos2.getY()) || 
					pos.getY() >= pos1.getY() && pos.getY() <= pos2.getY()) return true;
		}
		else {
			int lowy = (int) (Math.floor((pos1.getY())/MAZECONSTANT))*MAZECONSTANT;
			if(pos.getY()<=lowy || pos.getY() >= lowy + MAZECONSTANT) return false;
			else if((pos.getX() <= pos1.getX() && pos.getX() >= pos2.getX()) || 
					pos.getX() >= pos1.getX() && pos.getX() <= pos2.getX()) return true;
		}
		return false;
	}
	
	public int getBitAtPosition(Position pos){
		int distance;
		if(horizontal()){
			 distance = (int) Math.abs(pos.getY()- pos1.getY());
		}
		else{
			 distance = (int) Math.abs(pos.getX() - pos1.getX());
		}
		return bits[distance/2 - 1];
	}
	
	public boolean isBlackAt(Position pos){
		if(!hasPosition(pos)) return false;
		final int MARGE = 1;
		if(getBitAtPosition(pos) == 0){
			return true;
		}
		else if(pos.getDistance(pos1) < MARGE || pos.getDistance(pos2) < MARGE){
			return true;
		}
		else return false;
	}
	
	public boolean isWhiteAt(Position pos){
		if(!hasPosition(pos)) return false;
		if(getBitAtPosition(pos) == 1){
			return true;
		}
		else return false;
	}
	
	public boolean horizontal(){
		final int MARGE = 1; //TODO: testen!
		if(Math.abs(pos1.getX()-pos2.getX()) < MARGE)
			return true;
		else return false;
	}
	
	private void mirrorBits(){
		for(int i = 0; i<3; i++){
			int temp = bits[i];
			bits[i] = bits[5-i];
			bits[5-i] = temp;
		}
	}
	
	private int getDecimal(int[] bits){
		int result = 0;
		for(int i = 0; i< 6; i++){
			result += bits[5-i]*2^(i);
		}
		return result;
	}
	
//	private int[] getBinary(int decimal){
//		int[] result = new int[6];
//		for(int i = 0; i< 6; i++){
//			result[i] = decimal%2;
//			decimal = decimal/2;
//		}
//		return result;
//	}
	
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
