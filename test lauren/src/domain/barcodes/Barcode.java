package domain.barcodes;

import domain.Position.Position;
import domain.robots.Robot;

public class Barcode {

	private Action action;
	private Position pos1;
	private Position pos2;
	private int[] bits;
	
	public Barcode(int[] bits, Position pos1, Position pos2){
		generateActions();
		this.bits = bits;
		this.pos1 = pos1;
		this.pos2 = pos2;
	}
	
	public void generateActions(){
		if(bits.length != 6){
			throw new IllegalArgumentException();
		}
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
	
	public Barcode(Action action, Position pos){
		this.action = action;
	}
	
	public Action getAction(){
		return action;
	}
	
	public void runAction(Robot robot){
		action.run(robot);
	}
	
	public int getBitAtPosition(Position pos){
		int distance = (int) pos.getDistance(pos1);
		return bits[distance/2 - 1];
		}
	
	public boolean isBlackAt(Position pos){
		if(getBitAtPosition(pos) == 0){
			return true;
		}
		else return false;
	}
	
	public boolean isWhiteAt(Position pos){
		return !isBlackAt(pos);
	}
	
}
