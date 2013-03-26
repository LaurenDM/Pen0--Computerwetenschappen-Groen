package groen.htttp;

import gui.ContentPanel;

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
		spectatorHandler = new SpectatorHandler(this);
		
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
//	    random =1;
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
	
//	public void updateOtherPlayers(String newPlayerPlayerID){
//		System.out.println("updating other players");
//		Set<String> players = playerClient.getPlayers();
//		for(String player: players){
//			if(!player.equals(playerClient.getPlayerID())){
//			if(controller.getOtherRobots().containsKey(player)){
//				System.out.println("-----------Already contains key: "+player);
//			}
//			else{
//				//aangezien er volgens mij geen manier is om de beginpositie op te vragen zet ik hem op 20,20
//				controller.connectExternalSimRobot(0, new Position(20,20), player);
//				System.out.println("-----------Toegevoegd: "+player);
//			}
//			}
//		}
//	}
	
	private void sendPositions(){
		PlayerClient playerClient = getPlayerClient();
		RobotPilot robot = getController().getRobot();
		try {
			playerClient.updatePosition(robot.getPosition().getX(), robot.getPosition().getY(), robot.getOrientation());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startSendingPositionsThread(){
		
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				sendPositions();
			}
			
		}, 0, 200, TimeUnit.MILLISECONDS);

//		Thread posThread = new Thread() {
//		    @Override
//			public void run(){
//		    	while (true) {
//					sendPositions();
//					try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e) {
//						break;
//					}
//		    }
//		  };
//		};
//		posThread.run();
	}
	
	public void foundBall(){
		try {
			playerClient.foundObject();
		} catch (IllegalStateException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//TODO: lock en unlock aanroepen als we over wip rijden
	
	public void lockSeesaw(int barcodeNb){
		try {
			playerClient.lockSeesaw(barcodeNb);
		} catch (IllegalStateException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void unlockSeesaw(){
		try {
			playerClient.unlockSeesaw();
		} catch (IllegalStateException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
	}

}
		


