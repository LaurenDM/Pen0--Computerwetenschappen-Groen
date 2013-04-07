package domain.maze.barcodes;

import domain.robots.RobotPilot;

public class DoNothingAction implements Action{
	@Override
	public void run(RobotPilot robot) {
		robot.indicateDeadEnd();
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
		return "Do nothing, ball did not belong to us";
	}
}
