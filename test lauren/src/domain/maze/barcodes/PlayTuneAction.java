package domain.maze.barcodes;

import bluetooth.CMD;
import lejos.nxt.Sound;
import domain.robots.Robot;

public class PlayTuneAction extends Action {
	
	
	@Override
	public void run(Robot robot) {
		robot.playTune();
	}

//	@Override
//	public int getActionCommandNb() {
//		return CMD.PLAYTUNE;
//	}

	@Override
	public String toString() {
		return "Play sound";
	}

}
