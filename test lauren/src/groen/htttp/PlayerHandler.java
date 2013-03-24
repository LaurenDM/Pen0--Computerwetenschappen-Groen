package groen.htttp;

import gui.ContentPanel;

import java.io.IOException;
import java.util.List;

import domain.robots.RobotPilot;

import peno.htttp.DisconnectReason;
import peno.htttp.PlayerClient;
import peno.htttp.Tile;

public class PlayerHandler implements peno.htttp.PlayerHandler {

		
	HtttpImplementation htttpImplementation;
	
	public PlayerHandler(HtttpImplementation htttpImplementation) {
		this.htttpImplementation = htttpImplementation;
	}

	@Override
	public void playerReady(String playerID, boolean isReady) {
		printMessage("ph.playerReady: "+playerID+" is ready");
		//moeten wij niets met doen
	}
	
	@Override
	public void playerJoining(String playerID) {
		printMessage("ph.playerJoining: "+playerID+" is joining");
		//moeten wij niets met doen
	}
	
	@Override
	public void playerJoined(String playerID) {
		printMessage("ph.playerJoined: "+playerID+" joined");
		//moeten wij niets met doen
	}
	
	@Override
	public void playerFoundObject(String playerID, int playerNumber) {
		printMessage("ph.playerFoundObj: "+playerID+" number:"+playerNumber+" found object");
		//moeten we zelf checken of dit teammate is?
	}
	
	@Override
	public void playerDisconnected(String playerID, DisconnectReason reason) {
		printMessage("ph.playerdisc: "+playerID+" disconnected, reason: "+reason);				
		//moeten wij niets met doen
	}
	
	@Override
	public void gameStopped() {
		printMessage("ph.gameStopped");
		htttpImplementation.getController().cancel();
		//TODO: ik zou reset doen
	}
	
	@Override
	public void gameStarted() {
		printMessage("ph.gameStarted, starting to send position");
//		htttpImplementation.startSendingPositionsThread();
		//TODO: verkenalgoritme starten, ik stel voor dit handmatig te doen
		// waarom?
	}
	
	@Override
	public void gamePaused() {
		printMessage("ph.gamePaused");
		htttpImplementation.getController().cancel();
	}
	
	@Override
	public void gameRolled(int playerNumber, int objectNumber) {
		printMessage("ph.gameRolled: playerNumber:"+playerNumber+" objectNumber:"+objectNumber);
	}

	@Override
	public void teamConnected(String partnerID) {
		printMessage("ph.teamconnected: "+partnerID);
	}

	@Override
	public void teamTilesReceived(List<Tile> tiles) {
		printMessage("ph.Tiles recieved 'List<Tile>");
	}
	
	private void printMessage(String message){
		System.out.println(message);
		ContentPanel.writeToDebug(message);
	}

	
//	private void sendPositions(){
//		PlayerClient client = htttpImplementation.getPlayerClient();
//		RobotPilot robot = htttpImplementation.getController().getRobot();
//		try {
//			client.updatePosition(robot.getPosition().getX(), robot.getPosition().getY(), robot.getOrientation());
//			System.out.println("Updated pos to "+robot.getPosition().getX()+" "+robot.getPosition().getY());
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	private void startSendingPositionsThread(){
//
//		Thread posThread = new Thread(){
//		    @Override
//			public void run(){
//		    	while (true) {
//					sendPositions();
//					try {
//						Thread.sleep(2000);
//					} catch (InterruptedException e) {
//						break;
//					}
//		    }
//		  };
//		};
//		posThread.run();
//	}

}
