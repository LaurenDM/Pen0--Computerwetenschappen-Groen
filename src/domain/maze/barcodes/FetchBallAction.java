package domain.maze.barcodes;

import controller.Controller;
import domain.robots.RobotPilot;

public class FetchBallAction implements Action {
	private int teamNumber;
	
	
	public void run(RobotPilot robot) {
		robot.setTeamNumber(getTeamNumber());
		robot.fetchBall(); 
		Controller.setStopped(true);
	}
	
	private int getTeamNumber() {
		return teamNumber;
	}

	private void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
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