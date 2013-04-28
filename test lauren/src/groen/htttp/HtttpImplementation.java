package groen.htttp;

import gui.ContentPanel;
import gui.GUI;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import peno.htttp.Callback;
import peno.htttp.PlayerClient;
import peno.htttp.SpectatorClient;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import controller.Controller;

import domain.Position.Position;
import domain.robots.RobotPilot;

public class HtttpImplementation {
	
	
	RobotPilot playerHandler;
	GameHandler gameHandler;
	
	PlayerClient playerClient;
	SpectatorClient spectatorClient;
	Controller controller;
	
	public HtttpImplementation(Controller controller){
		
		this.controller = controller;
		
		gameHandler = new GameHandler();
		
		ConnectionFactory factory = new ConnectionFactory();
		
		//*******************************************************
		// Comment the section below depending on your connection
		
		// SSH
	    factory.setHost("localhost");
	    factory.setPort(8888);
	    
//	    
	    //CAMPUSNET
	    //factory.setHost("leuven.cs.kotnet.kuleuven.be");
	    //factory.setPort(5672);
	    
	    //*******************************************************
	    	    
	    playerHandler = controller.getRobot();
	    Connection connection = null;
	    String gameID = "gameIDGroenSerge3";
	    if(GUI.getGameID()!=null){
	    	gameID = GUI.getGameID();
	    }
	    String playerID = controller.getPlayerID();
	    
		try {
			connection = factory.newConnection();
			playerClient = new PlayerClient(connection, controller.getRobot(), gameID, playerID);
			spectatorClient = new SpectatorClient(connection, controller.getWorldSimulator(), gameID);
			spectatorClient.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		try {
			playerClient.join(new Callback<Void>() {
				@Override
				public void onSuccess(Void result) {
					System.out.println("Deelname geslaagd.");
					ContentPanel.writeToDebug("Deelname geslaagd.");
				}

				@Override
				public void onFailure(Throwable t) {
					System.err.println("Fout bij deelname: " + t.getMessage());
					ContentPanel.writeToDebug("Fout bij deelname: " + t.getMessage());
				}
			});

						

			
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	
	
	public Controller getController(){
		return controller;
	}
	
	public PlayerClient getPlayerClient(){
		return playerClient;
	}
	
	public SpectatorClient getSpectatorClient(){
		return spectatorClient;
	}
	

	
//	private void sendPositions(){
//		PlayerClient playerClient = getPlayerClient();
//		RobotPilot robot = getController().getRobot();
//		try {
//			playerClient.updatePosition(robot.getPosition().getX(), robot.getPosition().getY(), robot.getOrientation());
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void startSendingPositionsThread(){
//		
//		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//		executor.scheduleAtFixedRate(new Runnable() {
//
//			@Override
//			public void run() {
//				sendPositions();
//			}
//			
//		}, 0, 1000, TimeUnit.MILLISECONDS);
//
//	}
	
	

}
		


