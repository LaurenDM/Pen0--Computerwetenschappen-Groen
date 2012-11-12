package domain.barcodes;

import domain.robots.Robot;

public class Barcode {

	private Action action;
	
	
	public Barcode(int[] bits){
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
	
	public Barcode(Action action){
		this.action = action;
	}
	
	public Action getAction(){
		return action;
	}
	
	public void runAction(Robot robot){
		action.run(robot);
	}
	
}
