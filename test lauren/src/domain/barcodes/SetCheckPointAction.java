package domain.barcodes;

import domain.robots.Robot;

public class SetCheckPointAction extends Action {

	@Override
	public void run(Robot robot) {
		robot.setCheckpoint();
	}

	@Override
	public int[] getActionNb() {
		return null;
	}

	@Override
	public String toString() {
		return "Set checkpoint";
	}

}
