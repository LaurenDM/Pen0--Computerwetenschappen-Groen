package domain.robots;
public class TurnThread extends DriveThread{

	public TurnThread(MoveType moveType, SimRobotPilot simRobotPilot){
		super(moveType,simRobotPilot);
	}
	@Override
	public void run() {
		double speed = simRobotPilot.getTurningSpeed();
		double turnAmount = 1*moveType.getSpeedMultiplier();
		int sleepTime = Math.abs((int) Math.round( (500 * turnAmount / speed)));
		while (true) {
			double newOrientation = simRobotPilot.calcNewOrientation(turnAmount);
			simRobotPilot.setOrientation(newOrientation);
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
