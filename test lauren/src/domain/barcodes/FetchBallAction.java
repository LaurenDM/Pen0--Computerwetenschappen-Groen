package domain.barcodes;

import domain.robots.Robot;

public class FetchBallAction extends Action {
	//TODO
	public void run(Robot robot) {
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
