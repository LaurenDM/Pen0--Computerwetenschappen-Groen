package domain.maze.barcodes;

import domain.robots.RobotPilot;

public class DriveFastAction implements Action {

	@Override
	public void run(RobotPilot robot) {
		robot.setHighSpeed();
	}

//	@Override
//	public int getActionCommandNb() {
//		return CMD.DRIVEFAST;
//	}

	@Override
	public String toString() {
		return "Drive fast";
	}

}
