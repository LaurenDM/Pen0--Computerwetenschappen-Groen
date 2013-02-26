package domain.maze.barcodes;

import domain.robots.Robot;

public class DoNothingAction implements Action{
	public void run(Robot robot) {
		robot.doNothing();
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
