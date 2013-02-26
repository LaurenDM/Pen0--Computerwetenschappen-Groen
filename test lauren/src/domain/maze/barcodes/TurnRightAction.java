package domain.maze.barcodes;

import bluetooth.CMD;
import domain.robots.Robot;

public class TurnRightAction extends Action {

	@Override
	public void run(Robot robot) {
		robot.turn(360);
	}

//	@Override
//	public int getActionCommandNb() {
//		return CMD.TURN;
//	}
//	
//	@Override
//	public int getActionCommandArg(){
//		return 360;
//		
//	}
	
	
	@Override
	public String toString() {
		return "Turn right";
	}

}
