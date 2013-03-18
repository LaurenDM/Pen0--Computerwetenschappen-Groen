package domain.maze.barcodes;

import domain.robots.RobotPilot;

public class PlayTuneAction implements Action {
	
	
	@Override
	public void run(RobotPilot robot) {
		robot.playSong();
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
