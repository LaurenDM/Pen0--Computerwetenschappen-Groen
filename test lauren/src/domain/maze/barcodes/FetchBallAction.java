package domain.maze.barcodes;

import domain.robots.RobotPilot;

public class FetchBallAction implements Action {
	//TODO
	public void run(RobotPilot robot) {
		robot.fetchBall(); 
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
		return "Fetch ball";
	}
}
