package rabbitMQ;

import controller.Controller;
import domain.robots.RobotPilot;
import peno.htttp.Handler;

public class BrobotHandler implements Handler {
	
	private Controller controller;
	
	public BrobotHandler(Controller controller){
		this.controller = controller;
	}

	@Override
	public void gameStarted() {
		controller.startExplore();
		//TODO checken!
	}

	@Override
	public void gameStopped() {
		controller.reset();
	}

	@Override
	public void gamePaused() {
		controller.cancel();
	}

	@Override
	public void gameRolled(int playerNumber) {
		controller.setPlayerNb(playerNumber);
	}

	@Override
	public void playerJoined(String playerID) {
		controller.playerJoined(Integer.parseInt(playerID));
	}

	@Override
	public void playerLeft(String playerID) {
		controller.playerLeft(Integer.parseInt(playerID));
	}

	@Override
	public void playerPosition(String playerID, double x, double y, double angle) {
		RobotPilot robot = controller.getRobotFromIdentifier(Integer.parseInt(playerID));
		robot.setPose(angle,(int) x, (int) y);
	}

	@Override
	public void playerFoundObject(String playerID) {
		int identifier = Integer.parseInt(playerID);
		controller.getRobotFromIdentifier(identifier).setFoundBall(identifier);
	}

}
