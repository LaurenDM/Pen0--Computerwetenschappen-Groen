package domain.barcodes;

import domain.robots.Robot;

public class DriveSlowAction extends Action {

	@Override
	public void run(Robot robot) {
		robot.setLowSpeed();

	}

	@Override
	public int[] getActionNb() {
		return new int[]{14};
	}

	@Override
	public String toString() {
		return "Drive slow";
	}

}
