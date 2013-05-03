package domain.maze.barcodes;

import controller.Controller;
import domain.robots.RobotPilot;

public class FetchBallAction implements Action {
	private int teamNumber;
	
	
	@Override
	public void run(RobotPilot robot) {
		robot.setTeamNumber(getTeamNumber());
		robot.fetchBall();
		robot.foundBall();
	}
	
	private int getTeamNumber() {
		return teamNumber;
	}

	public FetchBallAction(int teamNumber){
		this.teamNumber=teamNumber;
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
