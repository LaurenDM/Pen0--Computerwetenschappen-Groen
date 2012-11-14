package domain.barcodes;

import domain.robots.Robot;

public class DriveSlowAction extends Action {

	@Override
	public void run(Robot robot) {
		robot.setLowSpeed();

	}

}
