package domain.barcodes;

import domain.robots.Robot;

public class DriveFastAction extends Action {

	@Override
	public void run(Robot robot) {
		robot.setHighSpeed();
	}

}
