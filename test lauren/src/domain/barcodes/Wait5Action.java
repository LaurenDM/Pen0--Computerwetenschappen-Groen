package domain.barcodes;

import domain.robots.Robot;

public class Wait5Action extends Action {

	@Override
	public void run(Robot robot) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO ?
		}
	}

	@Override
	public int[] getActionNb() {
		return new int[]{17};
	}

	@Override
	public String toString() {
		return "Wait 5";
	}

}
