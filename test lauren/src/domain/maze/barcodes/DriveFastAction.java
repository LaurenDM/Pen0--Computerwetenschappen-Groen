package domain.maze.barcodes;

import bluetooth.CMD;
import domain.robots.Robot;

public class DriveFastAction implements Action {

	@Override
	public void run(Robot robot) {
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