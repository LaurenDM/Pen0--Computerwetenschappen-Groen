package domain.robots;

import groen.htttp.HtttpImplementation;
import gui.ContentPanel;

import java.util.ArrayList;

import java.util.List;
import gui.ContentPanel;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import controller.Controller;

import peno.htttp.DisconnectReason;
import peno.htttp.PlayerClient;
import peno.htttp.PlayerDetails;
import peno.htttp.PlayerHandler;
import peno.htttp.PlayerType;
import peno.htttp.Tile;

import domain.Position.Pose;
import domain.Position.Position;
import domain.maze.Ball;
import domain.maze.Board;
import domain.maze.MazeElement;
import domain.maze.MazeInterpreter;
import domain.maze.Seesaw;
import domain.maze.Wall;
import domain.maze.WorldSimulator;
import domain.maze.barcodes.Barcode;
import domain.maze.graph.MazePath;
import domain.maze.infrared.InfraredBeamer;
import domain.maze.infrared.RobotIBeamer;
import domain.polygons.RobotPolygon;
import domain.robotFunctions.ExploreMaze;
import domain.robotFunctions.MatchMap;
import domain.util.TimeStamp;

public abstract class RobotPilot implements PlayerHandler{
	
	RobotPolygon robotPolygon;
	private MoveType movement;
	private int number; //0-3
	
	private Board board;
	protected ExploreMaze maze;
	private WorldSimulator worldSimulator;
	
	private Ball ball;
	private int teamNumber;
	private String playerID;
	
	private PlayerClient playerClient;
	private final int WIDTH = 20;
	private final int HEIGHT = 20; // TODO values?
	
	public RobotPilot(String playerID){
		this.worldSimulator=new WorldSimulator(this);
		this.movement=MoveType.STOPPED;
		this.robotPolygon=new RobotPolygon(this);
		this.playerID = playerID;
	}
	
	public abstract PlayerType getPlayerType();
	
	public Pose getInitialPosition(){
		return initialPosition;
	}
	
	public int getWidth(){
		return this.WIDTH;
	}
	
	public int getHeight(){
		return this.HEIGHT;
	}
	
	public PlayerDetails getPlayerDetails(){
		return new PlayerDetails(getPlayerID(), getPlayerType(), getWidth(), getHeight()); 
	}
	
	public void setPlayerClient(PlayerClient playerClient){
		this.playerClient = playerClient;
	}
	
	public void setWorldSimulator(WorldSimulator ws){
		this.worldSimulator = ws;
	}
	
	public WorldSimulator getWorldSimulator(){
		return this.worldSimulator;
	}
	
	public String getPlayerID(){
		return this.playerID;
	}
	
	public void setPlayerNb(int nb){
		this.number = nb;
	}
	
	public RobotPolygon getRobotPolygon(){
		return robotPolygon;
	}
	
	public abstract void UpdateUntil(TimeStamp timestamp);
	
	public int getNumber(){
		return number;
	}
	
	public MoveType getMovementStatus() {
		return movement;
	}
		
	public abstract double getMovingSpeed();
	
	public abstract double getTurningSpeed();
	
	public double getActualMovingSpeed() {
		return getMovingSpeed()*movement.getSpeedMultiplier();
	}
	
	/**
	 * Can be used to get a clone of the current position of the robot
	 * @return a clone of the position
	 */
	public abstract Position getPosition();
	
	public void setBoard(Board board){
		this.board = board;
	}
	
	public Board getFoundBoard(){
		return this.board;
	}
	
	public abstract void forward() throws CannotMoveException;
	
	public void setMovement(MoveType movement){
		this.movement = movement;
	}
	
	public abstract void backward();
	
	public abstract void stop();

	/**
	 * 
	 * @param distance in cm
	 */
	public abstract void move(double distance) throws CannotMoveException;
	
	/**
	 * 
	 * @param amount in degrees
	 */
	public abstract void turn(double amount);
	
	public abstract void turnRight();
	
	public abstract void turnLeft();
	
	public abstract int getSensorAngle();
	
	
	/**
	 * returns the current Orientation relative to the original orientation.
	 */
	public abstract double getOrientation();
	/**
	 * 
	 * @param speed in cm/s
	 */
	public abstract void setMovingSpeed(double speed);
	
	public void resetToDefaultSpeeds(){
		setMovingSpeed(getDefaultMovingSpeed());
		setTurningSpeed(getDefaultTurningSpeed());
	}
	
	public void setHighSpeed(){
		setMovingSpeed(getDefaultMovingSpeed()*2);
		setTurningSpeed(getDefaultTurningSpeed()*2);
	}
	
	public void setLowSpeed(){
		setMovingSpeed(getDefaultMovingSpeed()/2);
		setTurningSpeed(getDefaultTurningSpeed()/2);
	}
	
	/**
	 * 
	 * @param speed in degrees/s
	 */
	public abstract void setTurningSpeed(double speed);
	
	//Method for testing whether the robot can move with takin into account the walls and otherobstructions.
	public abstract boolean canMove();
	
//	public abstract boolean isTouching();
	
	public abstract double readLightValue();
	
	public abstract double readUltrasonicValue();
	
	public double readLongestUltrasonicValue(int extraAngle){
		extraAngle=Math.abs(extraAngle);
		double normalDist = readUltrasonicValue();
		double mostAccurateDist = normalDist;
		if (normalDist < MazeElement.getMazeConstant()) {
			turnUltrasonicSensor(extraAngle);
			
			double littleLeftDist = readUltrasonicValue();
			if (littleLeftDist < MazeElement.getMazeConstant()) {
				turnUltrasonicSensor(-2*extraAngle);
				double littleRightDist = readUltrasonicValue();
				turnUltrasonicSensor(extraAngle);


				if (littleRightDist >MazeElement.getMazeConstant()) {
					mostAccurateDist = Math.round(Math.cos(extraAngle)
							* littleRightDist);
				}
			}
			else{
				turnUltrasonicSensor(-extraAngle);
				mostAccurateDist=littleLeftDist*Math.cos(extraAngle);
				
			}
		}
		return mostAccurateDist;

	}
	
	public abstract void calibrateLightHigh();
	
	public abstract void calibrateLightLow();
	
	public abstract void turnUltrasonicSensor(int angle);
	
	public abstract void turnSensorRight();
	
	public abstract void turnSensorLeft();
	
	public abstract void turnSensorForward();
	
	public abstract boolean detectWhiteLine();
	public abstract void blackStraighten();

	public abstract void straighten();
	//Makes the robot make an arc of the specified angle. This method does not return immediately.
	public abstract void steer(double angle);

	public abstract void keepTurning(boolean left);

	public abstract void findWhiteLine();

	public abstract void interrupt();

	public abstract double getDefaultMovingSpeed();

	public abstract double getDefaultTurningSpeed();
	
	public abstract void playSong();

	public abstract void setPose(double orientation, int x, int y);

	public void startExplore() {
		maze = new ExploreMaze(this);
		maze.start();
	}	
	public abstract void addFoundWall(Wall wall);

	public abstract boolean detectBlackLine();

	public abstract void findBlackLine();

	public abstract void scanBarcode();

	public final void setCheckpoint() {
		maze.setCurrentTileToCheckpoint();
	}
	
	public ExploreMaze getMaze(){
		return maze;
	}

	public final void setFinish(){
		maze.setCurrentTileToFinish();
	}
	
	public final void resumeExplore() {
		if(maze!=null){
			maze.resumeExplore(0, 0, null);
		} else {
			ContentPanel.writeToDebug("You haven't started exploring yet!");
		}
	}

	public final void driveToFinish() {
		if(maze!=null){
			maze.stopExploring();
			maze.driveToFinish();
		} else {
			ContentPanel.writeToDebug("You haven't started exploring yet!");
		}		
	}
	
	public abstract void wait5Seconds();

	public abstract void autoCalibrateLight();
	
	
	public final MazePath getPathToFinish() {
		return maze.getPathToFinish();
	}
	public abstract void setDriveToFinishSpeed();

	public abstract void fetchBall();
	
	public void indicateDeadEnd(boolean accessible) {
		getMaze().setNextTileToDeadEnd(accessible);
	}
	
	public void foundBall(){
		try {
			playerClient.foundObject();
			playerClient.joinTeam(getTeamNumber());
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(NullPointerException e){
			//voor wanneer er geen htttp gebruikt wordt
		}
	}


	public Ball getBall(){
		return this.ball;
	}
	
	public void setBall(Ball ball){
		this.ball = ball;
	}
	
	public boolean hasBall(){
		return (getBall() != null);
	}

	public void setFoundBall(int number) {
		setBall(new Ball(number));
	}

	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}
	
	public int getTeamNumber(){
		return teamNumber;
	}
	
	
	public void handleSeesaw(int barcodeNb,Seesaw foundSeesaw){
		boolean upAtThisSide = detectInfrared();
		//System.out.println("We change the seesaw based on infrared detection, barcodeNb: "+ barcodeNb+ " upats: " + upAtThisSide);
		foundSeesaw.setUpAt(barcodeNb, upAtThisSide);
		
		getMaze().setNextTileToSeesaw(upAtThisSide || foundSeesaw.isLocked());
		if (!foundSeesaw.isLocked() && !upAtThisSide) {
			try {
				playerClient.lockSeesaw(barcodeNb);
			} catch (IllegalStateException e) {
				// Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}catch(NullPointerException e){
				//Dit wil wss zeggen dat we niet met htttp werken 
			}
			driveOverSeeSaw(barcodeNb);
			getMaze().driveOverSeesaw();
			try {
				playerClient.unlockSeesaw();
			} catch (IllegalStateException e) {
				// Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}catch(NullPointerException e){
				//Dit wil wss zeggen dat we niet met htttp werken 
			}
		}
		getMaze().atBarcode(barcodeNb);
	}

	public  boolean detectInfrared(){
		return getInfraredValue()>30; //TODO infrarood francis
	};


	public abstract void driveOverSeeSaw(int barcodeNb);
	// de check van infrarood is reeds gebeurd als deze methode wordt aangeroepen!

	public abstract int getInfraredValue();
	
	public abstract void turnUltrasonicSensorTo(int angle);
	
	
	Pose initialPosition;
	int playerNb;
	public void setInitialPositionNumber(int playernb){
		this.playerNb = playernb;
//		System.out.println("Setting initial pos to:"+initialPosition.getX()+" y:"+initialPosition.getY());
//		setPose(pos.getOrientation().getAngleToHorizontal(), (int) pos.getX(), (int) pos.getY());
	}
	
	public void teleportToStartPosition(){
		initialPosition = getWorldSimulator().getInitialPositionFromPlayer(playerNb);
		if(initialPosition != null){
			System.out.println("Setting initial pos to:"+initialPosition.getX()+" y:"+initialPosition.getY());
			setPose(initialPosition.getOrientation().getAngleToHorizontal(), (int) initialPosition.getX(), (int) initialPosition.getY());
		}
	}
	
	public ArrayList<peno.htttp.Tile> getFoundTilesList(){
		ArrayList<domain.maze.graph.TileNode> foundTiles = getMaze().getFoundTilesList();
		ArrayList<peno.htttp.Tile> returnList = new ArrayList<peno.htttp.Tile>();
		Position pos = getMaze().findMostNegativePosition();
		for(domain.maze.graph.TileNode node : foundTiles){
			if(node.isFullyExpanded()){
				returnList.add(new Tile(node.getX()-(int)pos.getX(), node.getY()-(int)pos.getY(), node.getToken()));
			}
		}
		return returnList;
	}
	
	public boolean checkRobotSensor(Position pos){
		return worldSimulator.checkRobotOn(pos);
	}
	
	
	//////////////////////////////////////////////////////////////////////////////
	//					VANAF HIER IMPLEMENTATIE PLAYER HANDLER					//	
	//////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void playerReady(String playerID, boolean isReady) {
		//printMessage("ph.playerReady: "+playerID+" is ready");
		//moeten wij niets met doen
	}
	
	@Override
	public void playerJoining(String playerID) {
		//printMessage("ph.playerJoining: "+playerID+" is joining");
		//moeten wij niets met doen
	}
	
	@Override
	public void playerJoined(String playerID) {
		//printMessage("ph.playerJoined: "+playerID+" joined");
		//moeten wij niets met doen
	}
	
	@Override
	public void playerFoundObject(String playerID, int playerNumber) {
		//printMessage("ph.playerFoundObj: "+playerID+" number:"+playerNumber+" found object");
		//moeten we zelf checken of dit teammate is?
	}
	
	@Override
	public void playerDisconnected(String playerID, DisconnectReason reason) {
		//printMessage("ph.playerdisc: "+playerID+" disconnected, reason: "+reason);				
		//moeten wij niets met doen
	}
	
	@Override
	public void gameStopped() {
		//printMessage("ph.gameStopped");
	//	htttpImplementation.getController().cancel();
		//TODO: ik zou reset doen
	}
	
	@Override
	public void gameStarted() {
	//	printMessage("ph.gameStarted, starting to send position");
		updatePosition(0,0,0);
//		startSendingPositionsThread();
	}
	
	@Override
	public void gamePaused() {
	//	printMessage("ph.gamePaused");
	//	htttpImplementation.getController().cancel();
	}
	
	@Override
	public void gameRolled(int playerNumber, int objectNumber) {
		printMessage("ph.gameRolled: playerNumber:"+playerNumber+" objectNumber:"+objectNumber);
		setInitialPositionNumber(playerNumber);
		Barcode.setBallNumber(objectNumber);
		setReady(true);
	}

	@Override
	public void teamConnected(String partnerID) {
		System.out.println("RP.teamConnected");
		printMessage("ph.teamconnected: "+partnerID);
		try {
			playerClient.sendTiles(getFoundTilesList());
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void teamTilesReceived(List<Tile> tiles) {
		System.out.println("RP.teamTilesReceived");
		printMessage("ph.Tiles recieved 'List<Tile>");
		MatchMap matcher = new MatchMap();
		List<Tile> ourTiles = getFoundTilesList();
		// TODO: koen, methodes in MatchMap niet meer static maken
		matcher.setOurMazeTiles(ourTiles.toArray(new Tile[ourTiles.size()]));
		matcher.setOriginalTiles(tiles.toArray(new Tile[tiles.size()]));
		matcher.merge();
		System.out.println("Merged");
		MazeInterpreter MI = new MazeInterpreter(board);
		MI.readMap(matcher.getResultMap());
		//maze.updateWithMap(matcher.getResultMap());
		//int partnerX = 0; int partnerY = 0; //TODO update to code retrieving actual location tile
		//maze.setPartnerPosition(partnerX, partnerY);
		System.out.println("Maps merged and imported");
	}
	
	private void printMessage(String message){
		System.out.println(message);
		ContentPanel.writeToDebug(message);
	}

	@Override
	public void gameWon(int teamNumber) {
		//printMessage("ph.GameWon by Team " + teamNumber);
	}

	@Override
	public void teamPosition(long x, long y, double angle) {
		printMessage("ph.teamPosition: (" + x + "," + y +")");
	//	int partnerX = (int)x/40; int partnerY = (int)y/40; 
		maze.setPartnerPosition((int) x, (int) y);
		//TODO update to code retrieving actual location tile

	}
	
	public void setReady(boolean ready) {
		try {
			playerClient.setReady(ready);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void updatePosition(int x, int y, double angle){
		if(Controller.interconnected){
		try {
			playerClient.updatePosition(x, y, angle);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

	//ATtention This method is not used for the simrobot, only has an effect on the real robot
	public abstract void snapPoseToTileMid();
	
	
}
