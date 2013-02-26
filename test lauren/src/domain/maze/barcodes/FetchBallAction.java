package domain.maze.barcodes;

import domain.robots.Robot;

public class FetchBallAction implements Action {
	private int teamNumber;
	
	//TODO
	public void run(Robot robot) {
		robot.setTeamNumber(getTeamNumber());
		robot.fetchBall(); 
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
