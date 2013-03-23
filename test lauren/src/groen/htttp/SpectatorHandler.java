package groen.htttp;

import gui.ContentPanel;
import peno.htttp.DisconnectReason;

public class SpectatorHandler implements peno.htttp.SpectatorHandler {

	@Override
	public void playerReady(String playerID, boolean isReady) {
		printMessage("sh.playerReady");
	}
	
	@Override
	public void playerJoining(String playerID) {
		printMessage("sh.PlayerJoining");
	}
	
	@Override
	public void playerJoined(String playerID) {
		printMessage("sh.playerJoined");
	}
	
	@Override
	public void playerFoundObject(String playerID, int playerNumber) {
		printMessage("sh.playerFoundObj");
	}
	
	@Override
	public void playerDisconnected(String playerID, DisconnectReason reason) {
		printMessage("sh.playerDisc");
	}
	
	@Override
	public void gameStopped() {
		printMessage("sh.gameStopped");
	}
	
	@Override
	public void gameStarted() {
		printMessage("sh.gameStarted");
	}
	
	@Override
	public void gamePaused() {
		printMessage("sh.GamePaused");
	}
	
	@Override
	public void playerUpdate(String playerID, int playerNumber, double x, double y, double angle, boolean foundObject) {
		printMessage("sh.updatedPlayer ID: "+playerID+" no:"+playerNumber+" x:"+x+" y:"+y);
	}
	
	private void printMessage(String message){
		System.out.println(message);
		ContentPanel.writeToDebug(message);
	}
	
}
