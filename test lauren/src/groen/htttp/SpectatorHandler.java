package groen.htttp;

import java.util.HashMap;

import domain.Position.Position;
import domain.maze.Board;
import domain.robots.RobotPilot;
import gui.ContentPanel;
import peno.htttp.DisconnectReason;

public class SpectatorHandler implements peno.htttp.SpectatorHandler {

	
	HtttpImplementation htttpImplementation;
	
	public SpectatorHandler(HtttpImplementation htttpImplementation) {
		this.htttpImplementation = htttpImplementation;
	}
	
	@Override
	public void playerReady(String playerID, boolean isReady) {
		printMessage("sh.playerReady");
		//moeten wij niets met doen
	}
	
	@Override
	public void playerJoining(String playerID) {
		printMessage("sh.PlayerJoining");
		//moeten wij niets met doen
	}
	
	@Override
	public void playerJoined(String playerID) {
		printMessage("sh.playerJoined");
//		htttpImplementation.updateOtherPlayers(playerID);
	}
	
	@Override
	public void playerFoundObject(String playerID, int playerNumber) {
		printMessage("sh.playerFoundObj: player "+playerID+" found its ball");
		//moeten wij niets met doen
	}
	
	@Override
	public void playerDisconnected(String playerID, DisconnectReason reason) {
		printMessage("sh.playerDisc");
		//moeten wij niets met doen
	}
	
	@Override
	public void gameStopped() {
		printMessage("sh.gameStopped");
		//wordt afgehandeld in PlayerHandler volgens mij?
	}
	
	@Override
	public void gameStarted() {
		printMessage("sh.gameStarted");
		//wordt afgehandeld in PlayerHandler volgens mij?
	}
	
	@Override
	public void gamePaused() {
		printMessage("sh.GamePaused");
		//wordt afgehandeld in PlayerHandler volgens mij?
	}
	
	@Override
	public void playerUpdate(String playerID, int playerNumber, double x, double y, double angle, boolean foundObject) {
		HashMap<String, RobotPilot> otherRobots = htttpImplementation.getController().getOtherRobots();
		if(!otherRobots.containsKey(playerID) && !htttpImplementation.getPlayerClient().getPlayerID().equals(playerID)){
			//ID not yet in system and not your own ID
			htttpImplementation.getController().connectExternalSimRobot(angle, new Position(x,y), playerID);
			System.out.println("NEW ROBOT ADDED "+playerID);
		}
				
//		printMessage("sh.updatedPlayer ID: "+playerID+" no:"+playerNumber+" x:"+x+" y:"+y);
		RobotPilot robot = htttpImplementation.getController().getOtherRobots().get(playerID);
		if(robot!=null){
			robot.setPose(angle, (int) x, (int) y);
			// TODO boolean foundObject ergens bijhouden?
		}
		else{
			System.err.println("Can't get robot from playerID to update position");
		}
	}
	
	@Override
	public void lockedSeesaw(String playerID, int playerNumber, int barcode) {
		printMessage("sh.lockedSeesaw by player ID: " + playerID + " no: " + playerNumber);
		Board board = htttpImplementation.getController().getRobot().getBoard();
		board.rollSeeSawWithBarcode(barcode);
	}

	@Override
	public void unlockedSeesaw(String playerID, int playerNumber, int barcode) {
		printMessage("sh.unlockedSeesaw by player ID: " + playerID + " no: " + playerNumber);
		Board board = htttpImplementation.getController().getRobot().getBoard();
		board.unlockSeesawWithBarcode(barcode);
	}
	
	private void printMessage(String message){
		System.out.println(message);
		ContentPanel.writeToDebug(message);
	}

	@Override
	public void gameWon(int teamNumber) {
		// TODO Auto-generated method stub
		
	}
	
}
