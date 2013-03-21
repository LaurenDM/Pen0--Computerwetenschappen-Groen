package rabbitMQ;

import controller.Controller;
import domain.robots.RobotPilot;
import peno.htttp.DisconnectReason;
import peno.htttp.GameHandler;

public class BrobotHandler implements GameHandler {
	
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
	public void playerJoined(String playerID) {
		controller.playerJoined(Integer.parseInt(playerID));
	}

	@Override
	public void playerJoining(String playerID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerReady(String playerID, boolean isReady) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerFoundObject(String playerID, int playerNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerDisconnected(String playerID, DisconnectReason reason) {
		// TODO Auto-generated method stub
		
	}

}
