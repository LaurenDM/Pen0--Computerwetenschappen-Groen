package domain.robots;

import gui.ContentPanel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import peno.htttp.DisconnectReason;
import peno.htttp.PlayerClient;
import peno.htttp.PlayerDetails;
import peno.htttp.PlayerHandler;
import peno.htttp.PlayerType;
import peno.htttp.Tile;
import controller.Controller;
import domain.Position.Pose;
import domain.Position.Position;
import domain.maze.Ball;
import domain.maze.Board;
import domain.maze.Direction;
import domain.maze.MazeElement;
import domain.maze.MazeInterpreter;
import domain.maze.Orientation;
import domain.maze.Seesaw;
import domain.maze.Wall;
import domain.maze.WorldSimulator;
import domain.maze.barcodes.Barcode;
import domain.maze.graph.MazePath;
import domain.polygons.RobotPolygon;
import domain.robotFunctions.ExploreMaze;
import domain.robotFunctions.MatchMap;
import domain.util.Angles;
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
	private final int WIDTH = 17;
	private final int HEIGHT = 18; // TODO values?
	
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
		double mostAccurateDist = readUltrasonicValue();
		if (mostAccurateDist < MazeElement.getMazeConstant()) {
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
			System.out.println("PLAYER "  + playerNb + " JOINS TEAM " + getTeamNumber());
			
		} catch (IllegalStateException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
		catch(NullPointerException e){
			e.printStackTrace();
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
//		System.out.println("RobotPilot: teamNumber = " + teamNumber);
	}
	
	public int getTeamNumber(){
		return teamNumber;
	}
	
	public void handleSeesaw(int barcodeNb){
		System.out.println("Get barcode at " + getPosition());
		Barcode barcode = board.getBarcodeAt(getPosition());
		System.out.println("Barcode has number " + barcode.getDecimal());
		if(barcode.getDecimal()!=barcodeNb){
			throw new IllegalArgumentException();
		}
		System.out.println("Barcode action = " + barcode.getAction());
		ContentPanel.writeToDebug("NOW!!!");
		barcode.getAction().run(this);
	}
	
	
	public void handleSeesaw(int barcodeNb,Seesaw foundSeesaw){
		boolean upAtThisSide = detectInfrared();
		//System.out.println("set up seesaw based on infrared detection, barcodeNb: "+ barcodeNb+ " up: " + upAtThisSide);
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
				System.out.println("There was a NullPointerException code 8");
				//Dit wil wss zeggen dat we niet met htttp werken 
			}
			try{
				getMaze().driveOverSeesaw();
			} catch( IllegalStateException e){
				e.printStackTrace();
			}
			driveOverSeeSaw(barcodeNb);
			try {
				playerClient.unlockSeesaw();
			} catch (IllegalStateException e) {
				// Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
			}catch(NullPointerException e){
				System.out.println("There was a NullPointerException code 0896");

				//Dit wil wss zeggen dat we niet met htttp werken 
			}
			getMaze().atBarcode(foundSeesaw.getOtherBarcode(barcodeNb));
		}	
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
//			System.out.println("Setting initial pos to:"+initialPosition.getX()+" y:"+initialPosition.getY());
			setPose(initialPosition.getOrientation().getAngleToHorizontal(), (int) initialPosition.getX(), (int) initialPosition.getY());
		}
	}
	
	public ArrayList<peno.htttp.Tile> getFoundTilesList(){
		ArrayList<domain.maze.graph.TileNode> foundTiles = getMaze().getFoundTilesList();
		ArrayList<peno.htttp.Tile> returnList = new ArrayList<peno.htttp.Tile>();
		for(domain.maze.graph.TileNode node : foundTiles){
			if(node.isFullyExpanded()){
				returnList.add(new Tile(node.getX(), node.getY(), node.getToken()));
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
	//	System.out.println("RP.teamConnected");
		printMessage("ph.teamconnected: "+partnerID);
		try {
			playerClient.sendTiles(getFoundTilesList());
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private MatchMap matcher;
	private List<Tile> teamTiles;
	private boolean merged = false;

	@Override
	public void teamTilesReceived(List<Tile> tiles) {
		System.out.println("RP.teamTilesReceived");
		printMessage("ph.Tiles recieved 'List<Tile>");
		this.teamTiles = tiles;
		mergeMazes();
	}
	
	public void mergeMazes(){
		matcher = new MatchMap();
		List<Tile> ourTiles = getFoundTilesList();
		matcher.setOurMazeTiles(ourTiles);
		matcher.setOriginalTiles(teamTiles);
		matcher.merge();
		System.out.println("Merged");
		merged = true;
		MazeInterpreter MI = new MazeInterpreter(board);
		Orientation orientation = this.getInitialPosition().getOrientation();
		MI.readMap(matcher.getResultMap(), orientation);
		//maze.updateWithMap(matcher.getResultMap());
		System.out.println("Maps merged and imported");		
	}
	
	private Pose teamInitialPose;
	private Pose lastUpdatedPose;
	
	private Pose getTeamInitialPose(){
		if(teamInitialPose == null){
			Pose initial = matcher.getInitialPosition();
			initial.setX(initial.getX()*40); initial.setY(initial.getY()*40);
			//System.out.println("RELATIVE TEAM INITAL = " + initial);
			//System.out.println("STARTPOS = " + getInitialPosition());
			teamInitialPose = Position.getAbsolutePose(getInitialPosition(), initial);
		}
		return teamInitialPose;
	}
	
	private void printMessage(String message){
		System.out.println(message);
		ContentPanel.writeToDebug(message);
	}

	@Override
	public void gameWon(int teamNumber) {
		printMessage("ph.GameWon by Team " + teamNumber);
		Controller.setStopped(true);
	}

	@Override
	public void teamPosition(long x, long y, double angle) {
		if(merged){
		System.out.println("ph.teamPosition: (" + x + "," + y +")");
		//printMessage("ph.teamPosition: (" + x + "," + y +")");
		Pose relativePose = new Pose(40*x,40*y,Orientation.getOrientation(angle));
		//System.out.println("relative: " + relativePose);
		//System.out.println("initial: " + getTeamInitialPose());
		Pose absolutePose = Position.getAbsolutePose(getTeamInitialPose(), relativePose);
		//System.out.println("absolute: " + absolutePose);
		relativePose = Position.getRelativePose(getInitialPosition(), absolutePose);
		maze.setPartnerPosition(((int) Math.round(relativePose.getX()/40.0)),(int) Math.round(relativePose.getY()/40.0));
		Position teamPosition = absolutePose.getPosition();
		if(hasWon(teamPosition)){
			won();
		}
		}
	}
	
	private void won(){
		Controller.setStopped(true);
		maze.setInterrupted(true);
		try {
			playerClient.win();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("GEWONNEN");
		printMessage("GEWONNEN!");
	}
	
	public void setReady(boolean ready) {
		try {
			playerClient.setReady(ready);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void updatePosition(int x, int y, double angle){
		if(Controller.interconnected&&playerClient.isPlaying()){
		try {
			playerClient.updatePosition(x, y, angle);
			setLastUpdatedPose(x,y,angle);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

	private void setLastUpdatedPose(int x, int y, double angle) {
		lastUpdatedPose=new Pose(x,y,Orientation.getOrientation(angle));
	}
	
	private Pose getLastUpdatedPose() {
		return lastUpdatedPose;
	}

	//ATtention This method is not used for the simrobot, only has an effect on the real robot
	public abstract void snapPoseToTileMid();
	
	public void recoverToLastUpdatedPose(){
		this.setPose(getLastUpdatedPose().getOrientation().getAngleToHorizontal(), (int)getLastUpdatedPose().getX(), (int)getLastUpdatedPose().getY());
	}

	public boolean detectsCloseRobotAt(Direction dir) {
		double viewOr=Orientation.getOrientation(this.getOrientation()).getOffset(dir.getOffset()).getAngleToHorizontal();
		Position snappedRPos=this.getPosition().clone();
		snappedRPos.snapTo(40, 20, 20);
		Position wallPos=snappedRPos.getNewRoundPosition(viewOr, 20);
		if(getWorldSimulator().detectWallAt(wallPos)){
			return false;
		}
		else{
			return checkRobotSensor(wallPos.getNewRoundPosition(viewOr, 20));
		}
	}
	
	public boolean detectPartnerAtAngle(double angle){
		double viewOr=Orientation.snapAngle(90, 0, angle);
		Position snappedRPos=this.getPosition().clone();
		snappedRPos.snapTo(40, 20, 20);
		Position wallPos=snappedRPos.getNewRoundPosition(viewOr, 20);
		if(getWorldSimulator().detectWallAt(wallPos)){
			return false;
		}
		else{
			return checkRobotSensor(wallPos.getNewRoundPosition(viewOr, 20));
		}
	}
	
	public boolean hasWon(Position teamPosition){
		if(teamPosition.getDistance(getPosition())<=45){
			double angle = getPosition().getAngleTo(teamPosition);
			System.out.println("ANGLE = " + angle);
			if(detectPartnerAtAngle(angle)){
				System.out.println("DETECTING PARTNER");
				return true;
			}				
		}
		return false;
	}
	
	
}
