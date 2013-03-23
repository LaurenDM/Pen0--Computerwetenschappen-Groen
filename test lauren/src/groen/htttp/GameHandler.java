package groen.htttp;

import gui.ContentPanel;
import peno.htttp.DisconnectReason;

public class GameHandler implements peno.htttp.GameHandler {

	@Override
	public void playerReady(String playerID, boolean isReady) {
		printMessage("gh.playerReady: player "+playerID);
		
	}
	
	@Override
	public void playerJoining(String playerID) {
		printMessage("gh.playerJoining: "+playerID);
	}
	
	@Override
	public void playerJoined(String playerID) {
		printMessage("gh.playerJoined: "+playerID);
	}
	
	@Override
	public void playerFoundObject(String playerID, int playerNumber) {
		printMessage("gh.PlayerFoundObject: "+playerID+" number:"+playerNumber+" found object");
	}
	
	@Override
	public void playerDisconnected(String playerID, DisconnectReason reason) {
		printMessage("gh.playerdisc: "+playerID+" disconnected, reason: "+reason);				
	}
	
	@Override
	public void gameStopped() {
		printMessage("gh.gameStopped");
	}
	
	@Override
	public void gameStarted() {
		printMessage("gh.gameStarted");
	}
	
	@Override
	public void gamePaused() {
		printMessage("gh.gamePaused");
	}
	
	private void printMessage(String message){
		System.out.println(message);
		ContentPanel.writeToDebug(message);
	}
	
}
