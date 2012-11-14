package domain.barcodes;

import domain.Position.Position;
import domain.robots.Robot;

public class Barcode {

	private final int MAZECONSTANT = 40;
	private Action action;
	private Position pos1;
	private Position pos2;
	private int[] bits;
	
	public Barcode(int[] bits, Position pos1, Position pos2){
		if(bits.length != 6){
			throw new IllegalArgumentException();
		}
		try{
			generateActions();
		} catch(AssertionError e){
			mirrorBits();
			generateActions();
			// TODO: checken!!
		}
		this.bits = bits;
		this.pos1 = pos1;
		this.pos2 = pos2;
	}
	
	public void generateActions(){
		if(bits[0] == 0){
			if(bits[1] == 0){
				if(bits[2] == 0){
					assert(bits[3] == 1 && bits[4] == 0 && bits[5] == 1);
					action = new TurnLeftAction();
				}
				else{
					if(bits[3] == 0){
						assert(bits[4] == 0 && bits[5] == 1);
						action = new TurnRightAction();
					}
					else{
						assert(bits[4] == 1 && bits[5] == 1);
						action = new PlayTuneAction();
					}
				}
			}
			else{
				if(bits[2] == 0){
					assert(bits[3] == 0 && bits[4] == 1 && bits[5] == 1);
					action = new Wait5Action();
				}
				else{
					assert(bits[3] == 0 && bits[4] == 0 && bits[5] == 1);
					action = new DriveSlowAction();
				}
			}
		}
		else{
			if(bits[1] == 0){
				assert(bits[2] == 0 && bits[3] == 1 && bits[4] == 0 && bits[5] == 1);
				action = new DriveFastAction();
			}
			else{
				assert(bits[2] == 0 && bits[3] == 1 && bits[4] == 1 && bits[5] == 1);
				action = new SetFinishAction();
			}
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
		action.run(robot);
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
	
}
