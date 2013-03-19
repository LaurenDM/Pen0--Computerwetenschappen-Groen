package domain.maze.barcodes;

import domain.robots.RobotPilot;

public class DriveSlowAction implements Action {

	@Override
	public void run(RobotPilot robot) {
		robot.setLowSpeed();

	}

//	@Override
//	public int getActionCommandNb() {
//		return CMD.DRIVESLOW;
//	}

	@Override
	public String toString() {
		return "Drive slow";
	}

}
