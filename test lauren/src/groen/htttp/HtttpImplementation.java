package groen.htttp;

import gui.ContentPanel;

import java.io.IOException;

import peno.htttp.Callback;
import peno.htttp.PlayerClient;
import peno.htttp.SpectatorClient;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import controller.Controller;

import domain.robots.RobotPilot;

public class HtttpImplementation {
	
	
	PlayerHandler playerHandler;
	GameHandler gameHandler;
	SpectatorHandler spectatorHandler;
	
	PlayerClient playerClient;
	SpectatorClient spectatorClient;
	Controller controller;
	
	public HtttpImplementation(Controller controller){
		
		this.controller = controller;
		
		playerHandler = new PlayerHandler(this);
		gameHandler = new GameHandler();
		spectatorHandler = new SpectatorHandler();
		
		ConnectionFactory factory = new ConnectionFactory();
		
		//*******************************************************
		// Comment the section below depending on your connection
		
		// SSH
	    factory.setHost("localhost");
	    factory.setPort(8888);
	    
//	    //CAMPUSNET
//	    factory.setHost("leuven.cs.kotnet.kuleuven.be");
//	    factory.setPort(5672);
	    
	    //*******************************************************
	    	    
	    
	    Connection connection = null;
	    String gameID = "gameIDGroen";
	    int random = (int) (Math.random()*1000000);
		String playerID = "playerIDGroen"+random;
		try {
			connection = factory.newConnection();
			playerClient = new PlayerClient(connection, playerHandler, gameID, playerID);
			spectatorClient = new SpectatorClient(connection, spectatorHandler, gameID);
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

	public void setReady(boolean ready) {
		try {
			playerClient.setReady(ready);
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
	
	private void sendPositions(){
		PlayerClient playerClient = getPlayerClient();
		RobotPilot robot = getController().getRobot();
		try {
			playerClient.updatePosition(robot.getPosition().getX(), robot.getPosition().getY(), robot.getOrientation());
			System.out.println("Updated pos to "+robot.getPosition().getX()+" "+robot.getPosition().getY());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startSendingPositionsThread(){
		PlayerClient playerClient = getPlayerClient();
		RobotPilot robot = getController().getRobot();
		try {
			playerClient.updatePosition(robot.getPosition().getX(), robot.getPosition().getY(), robot.getOrientation());
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

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
	}
	
//	private void sendPositions(){
//		try {
//			client.updatePosition(robot.getPosition().getX(), robot.getPosition().getY(), robot.getOrientation());
//		} catch (IllegalStateException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	private void startSendingPositions(){
//		Thread posThread = new Thread(){
//		    @Override
//			public void run(){
//		    	while (true) {
//					sendPositions();
//					try {
//						Thread.sleep(100);
//					} catch (InterruptedException e) {
//						break;
//					}
//		    }
//		  };
//		};
//		posThread.run();
//	}
}
		


