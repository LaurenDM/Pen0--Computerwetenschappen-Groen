package domain.barcodes;

import bluetooth.CMD;
import domain.robots.Robot;

public class SetFinishAction extends Action {

	@Override
	public void run(Robot robot) {
		robot.setFinish();
	}

//	@Override
//	public int getActionCommandNb() {
//		return CMD.INVALID;
//	}

	@Override
	public String toString() {
		return "Set finish";
	}
	
	

}
