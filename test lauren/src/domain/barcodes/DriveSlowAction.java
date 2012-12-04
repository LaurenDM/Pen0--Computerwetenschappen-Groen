package domain.barcodes;

import bluetooth.CMD;
import domain.robots.Robot;

public class DriveSlowAction extends Action {

	@Override
	public void run(Robot robot) {
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
