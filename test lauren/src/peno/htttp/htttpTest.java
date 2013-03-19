package peno.htttp;

import java.io.IOException;

import com.rabbitmq.client.*;

public class htttpTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		GameHandler gameHandler = new GameHandler() {
			
			@Override
			public void playerReady(String playerID, boolean isReady) {
				System.out.println("playerReady");
				
			}
			
			@Override
			public void playerJoining(String playerID) {
				System.out.println("playerJoining");
			}
			
			@Override
			public void playerJoined(String playerID) {
				System.out.println("playerJoined");
			}
			
			@Override
			public void playerFoundObject(String playerID, int playerNumber) {
				System.out.println("PlayerFoundObject");
			}
			
			@Override
			public void playerDisconnected(String playerID, DisconnectReason reason) {
				System.out.println("playerDiscon");
			}
			
			@Override
			public void gameStopped() {
				System.out.println("gameStopped");
			}
			
			@Override
			public void gameStarted() {
				System.out.println("gameStarted");
			}
			
			@Override
			public void gamePaused() {
				System.out.println("gamePaused");
			}
		};

		final SpectatorHandler specHandler = new SpectatorHandler() {
			
			@Override
			public void playerReady(String playerID, boolean isReady) {
				System.out.println("playerReady");
			}
			
			@Override
			public void playerJoining(String playerID) {
				System.out.println("PlayerJoining");
			}
			
			@Override
			public void playerJoined(String playerID) {
				System.out.println("playerJoined");
			}
			
			@Override
			public void playerFoundObject(String playerID, int playerNumber) {
				System.out.println("playerFoundObj");
			}
			
			@Override
			public void playerDisconnected(String playerID, DisconnectReason reason) {
				System.out.println("playerDisc");
			}
			
			@Override
			public void gameStopped() {
				System.out.println("gameStopped");
			}
			
			@Override
			public void gameStarted() {
				System.out.println("gameStarted");
			}
			
			@Override
			public void gamePaused() {
				System.out.println("GamePaused");
			}
			
			@Override
			public void playerUpdate(String playerID, int playerNumber, double x,
					double y, double angle, boolean foundObject) {
				System.out.println("updatedPlayer");
			}
		};
		
		PlayerHandler playerHandler = new PlayerHandler() {
			
			@Override
			public void playerReady(String playerID, boolean isReady) {
				System.out.println("ph.playerReady");
			}
			
			@Override
			public void playerJoining(String playerID) {
				System.out.println("ph.playerJoining");
			}
			
			@Override
			public void playerJoined(String playerID) {
				System.out.println("ph.playerJoined");
			}
			
			@Override
			public void playerFoundObject(String playerID, int playerNumber) {
				System.out.println("ph.playerFoundObj");
			}
			
			@Override
			public void playerDisconnected(String playerID, DisconnectReason reason) {
				System.out.println("ph.playerdisc");				
			}
			
			@Override
			public void gameStopped() {
				System.out.println("ph.gameStopped");
			}
			
			@Override
			public void gameStarted() {
				System.out.println("ph.gameStarted");
			}
			
			@Override
			public void gamePaused() {
				System.out.println("ph.gamePaused");
			}
			
			@Override
			public void gameRolled(int playerNumber) {
				System.out.println("ph.gameRolled");
			}
		};
		
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = null;
	    String gameID = "gameIDGroen";
		String playerID = "playerIDGroen4";
		PlayerClient client = null;
		try {
			connection = factory.newConnection();
			client = new PlayerClient(connection, playerHandler, gameID, playerID);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		try {
			client.join(new Callback<Void>() {
				@Override
				public void onSuccess(Void result) {
					System.out.println("Deelname geslaagd.");

				}

				@Override
				public void onFailure(Throwable t) {
					System.err.println("Fout bij deelname: " + t.getMessage());
				}
			});

//			System.out.println(client.getPlayerNumber());
//			client.setReady(true);
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.in.read();
//			client.roll();

			
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    

	}

}
