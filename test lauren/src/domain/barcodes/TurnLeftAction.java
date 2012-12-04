package domain.barcodes;

import bluetooth.CMD;
import domain.robots.Robot;

public class TurnLeftAction extends Action {

	@Override
	public void run(Robot robot) {
		robot.turn(-360); //TODO checken of dit links is
	}

//	@Override
//	public int getActionCommandNb() {
//		return CMD.TURN;
//	}
//	
//	@Override
//	public int getActionCommandArg(){
//		return -360;
//	}
	
	@Override
	public String toString() {
		return "Turn left";
	}

}
