package domain.barcodes;

import domain.robots.Robot;

public class SetFinishAction extends Action {

	@Override
	public void run(Robot robot) {
		robot.setFinish();
	}

	@Override
	public int[] getActionNb() {
		return null;
	}

	@Override
	public String toString() {
		return "Set finish";
	}
	
	

}
