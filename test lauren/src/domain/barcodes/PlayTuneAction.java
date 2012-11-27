package domain.barcodes;

import lejos.nxt.Sound;
import domain.robots.Robot;

public class PlayTuneAction extends Action {
	
	
	@Override
	public void run(Robot robot) {
		robot.playTune();
	}

	@Override
	public int[] getActionNb() {
		return new int[]{16};
	}

	@Override
	public String toString() {
		return "Play sound";
	}

}
