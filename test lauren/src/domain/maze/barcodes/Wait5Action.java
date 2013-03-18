package domain.maze.barcodes;

import domain.robots.RobotPilot;

public class Wait5Action implements Action {

	@Override
	public void run(RobotPilot robot) {
		robot.wait5Seconds();
	}

//	@Override
//	public int getActionCommandNb() {
//		return CMD.WAIT5;
//	}

	@Override
	public String toString() {
		return "Wait 5";
	}

}
