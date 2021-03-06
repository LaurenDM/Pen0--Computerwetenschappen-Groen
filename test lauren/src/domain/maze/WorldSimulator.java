package domain.maze;

import gui.ContentPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import domain.Position.Pose;
import domain.Position.Position;
import domain.maze.barcodes.Barcode;
import domain.robots.RobotPilot;
import domain.robots.SimRobotPilot;
import domain.robots.RobotPilot.PlayerState;
import peno.htttp.DisconnectReason;
import peno.htttp.PlayerDetails;
import peno.htttp.SpectatorHandler;

public class WorldSimulator implements SpectatorHandler {
	
	private static final int MAZECONSTANT = 40;
	
	private Board simulatorBoard;
	private HashMap<String,RobotPilot> otherRobots;
	private final RobotPilot robot;
	
	public WorldSimulator(RobotPilot robot){
		simulatorBoard = new Board();		
		otherRobots = new HashMap<String, RobotPilot>();
		this.robot = robot;
	}
	
	public Board getBoard(){
		return this.simulatorBoard;
	}
	
	public void addInitialPosition(Pose pos, int nb){
		simulatorBoard.addInitialPosition(pos, nb);
	}
	
	public Pose getInitialPositionFromPlayer(int nb){
		return simulatorBoard.getInitialPositionFromPlayer(nb);
	}
	
	public void newExternalRobot(String id, RobotPilot robot){
		otherRobots.put(id, robot);
	}
	
	public List<RobotPilot> getOtherRobots(){
		return new ArrayList<RobotPilot>(otherRobots.values());
	}
	
	public void playerLeft(int identifier){
		otherRobots.remove(identifier);
	}
	
	public synchronized void addWall(Wall wall){
		simulatorBoard.addWall(wall);
	}
	
	public synchronized void addBall(Ball ball){
		simulatorBoard.addBall(ball);
	}
	
	public synchronized void addSimulatedBarcode(Barcode barcode){
		simulatorBoard.addBarcode(barcode);
	}
	
	public void addSeesaw(Seesaw seesaw){
		if(getSeesaw(seesaw.getCenterPosition()) == null){
			simulatorBoard.addSeesaw(seesaw);
		}
	}
	
//	public boolean checkForOpenSeesawFrom(RobotPilot robot){
//		final int DISTANCE = 80;
//		for(Seesaw seesaw : getSeesaws()){
//			if(seesaw.getInfaredPosition().getDistance(robot.getPosition())<DISTANCE){
//				return true;
//			}
//		}
//		return false;
//	}
	
	public boolean hasSeesawAt(Position pos){
		return simulatorBoard.hasSeesawAt(pos);
	}
	
	
	
	public Ball removeBall(Position position) {
		return simulatorBoard.removeBall(position);
	}
	
	public List<Wall> getWalls(){
		 return simulatorBoard.getWalls();
	}
	
	public List<Barcode> getSimulatedBarcodes(){
		return simulatorBoard.getBarcodes();
	}
	
	public List<Seesaw> getSeesaws(){
		return simulatorBoard.getSeesaws();
	}

	public List<Ball> getBalls(){
		return simulatorBoard.getBalls();
	}
	
	private boolean findWallAt(Position middlePosition, Position position){
		return simulatorBoard.findWallAt(middlePosition, position);
	}
	
	public synchronized boolean detectWallAt(Position position){
		return simulatorBoard.detectWallAt(position);
	}
	
	public synchronized boolean detectWhiteLineAt(Position position){
		final int MARGE = 1; // TODO: hangt af van dikte lijnen
		double x_mod = Math.abs(position.getX()%MAZECONSTANT);
		if(Math.min(x_mod, MAZECONSTANT-x_mod) <MARGE) 
			return true;
		double y_mod = Math.abs(position.getY()%MAZECONSTANT);
		if(Math.min(y_mod,  MAZECONSTANT - y_mod)<MARGE)
			return true;
		for(Barcode barcode : getSimulatedBarcodes()){
			if(barcode.isWhiteAt(position)){
				return true;
			}
		}
		return false;
	}
	
	public synchronized boolean detectBlackLineAt(Position position){
		for(Barcode barcode : getSimulatedBarcodes()){
			if(barcode.isBlackAt(position)){
				//System.out.println("blackline");
				return true;
			}
		}
		return false;
	}
	
	public synchronized boolean detectBarcodeAt(Position position){
		for(Barcode barcode : getSimulatedBarcodes()){
			if(barcode.containsPosition(position)) return true;
		}
		return false;
	}
	
//	public synchronized boolean detectRobotFrom(RobotPilot robot){
//		final int DISTANCE = 50; 
//		if(otherRobots!=null){
//			for(RobotPilot robot2 : otherRobots.values()){
//				if(robot2.getPosition().getDistance(robot.getPosition())<DISTANCE){
//					double distance = robot2.getPosition().getDistance(robot.getPosition());
//					double angle = Math.abs(robot.getOrientation()-robot.getPosition().getAngleTo(robot2.getPosition()));
//					if(angle<135){
//						// andere robot in gezichtsveld van robot
//						robot.turnUltrasonicSensor((int) angle);
//						if(robot.readUltrasonicValue() >= distance){
//							// geen muur tussen robots
//							return true;
//						}
//						//wel muur tussen robots
//					}
//					//robot ziet andere robot niet
//				}
//				// buiten bereik
//			}
//		}
//		return false;
//	}
	
	public boolean checkRobotOn(Position pos){
		final int MARGE = 20;
		if(otherRobots!=null)
			for(RobotPilot robot2 : otherRobots.values()){
				if(robot2.getPosition().getDistance(pos)<MARGE){
					return true;
				}
			}
		return false;
	}
	
	public Seesaw getSeesaw(Position pos){
		return simulatorBoard.getSeesaw(pos);
	}
	
	public void rollSeeSawWithBarcode(int barcodenb){
		for(Seesaw s : getSeesaws()){
			if(s.hasBarcodeNb(barcodenb)){
				s.lock();
				s.rollOver(barcodenb);
			}
		}
	}
	
	public void unlockSeesawWithBarcode(int barcodenb){
		for(Seesaw s : getSeesaws()){
			if(s.hasBarcodeNb(barcodenb)){
				s.unLock();
			}
		}
	}
	
	
	

	@Override
	public void playerReady(String playerID, boolean isReady) {
	//	if(isReady)
			printMessage("sh.playerReady: " + playerID);
		//moeten wij niets met doen
	}
	
	@Override
	public void playerJoining(String playerID) {
		//printMessage("sh.PlayerJoining: " + playerID);
		//moeten wij niets met doen
	}
	
	@Override
	public void playerJoined(String playerID) {
		printMessage("sh.playerJoined: " + playerID);
//		htttpImplementation.updateOtherPlayers(playerID);
	}
	
	@Override
	public void playerFoundObject(String playerID, int playerNumber) {
	//	printMessage("sh.playerFoundObj: player "+playerID+" found its ball");
		RobotPilot robot = otherRobots.get(playerID);
		robot.setPlayerState(PlayerState.FOUND_OBJECT);
		//moeten wij niets met doen
	}
	
	@Override
	public void playerDisconnected(String playerID, DisconnectReason reason) {
		printMessage("sh.playerDisc: " + playerID + ", " + reason);
		RobotPilot robot = otherRobots.get(playerID);
		robot.setPlayerState(PlayerState.DISCONNECTED);
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
	public void gameWon(int teamNumber) {
		printMessage("sh.GameWon by Team " + teamNumber);
	}

	@Override
	public void playerUpdate(PlayerDetails playerDetails, int playerNumber, long x,long y, double angle, boolean foundObject) {
		if(!otherRobots.containsKey(playerDetails.getPlayerID()) && !robot.getPlayerID().equals(playerDetails.getPlayerID())){
			//ID not yet in system and not your own ID
			connectExternalSimRobot(angle, new Position(x,y), playerDetails.getPlayerID());
			//System.out.println("NEW ROBOT ADDED "+playerDetails.getPlayerID());
		}
		
//		printMessage("sh.updatedPlayer ID: "+playerID+" no:"+playerNumber+" x:"+x+" y:"+y);
		RobotPilot robot = otherRobots.get(playerDetails.getPlayerID());
		if(robot!=null){
			Pose initialPose = getInitialPositionFromPlayer(playerNumber);
			Pose relativePose = new Pose(40*x,40*y,Orientation.getOrientation(angle));
			Pose newPose = Position.getAbsolutePose(initialPose, relativePose);
			robot.setPose(newPose.getOrientation().getAngleToHorizontal(), (int)newPose.getX(), (int)newPose.getY());
			if(foundObject){
				robot.setFoundBall(playerNumber);
				robot.setPlayerState(PlayerState.FOUND_OBJECT);
			}
		}
		else{
//			System.err.println("Can't get robot from playerID to update position");
		}
	}
		
		//This is used to set the robot controlled by other GUI
		public void connectExternalSimRobot(double orientation, Position position, String playerID) {
			SimRobotPilot otherSimRobot = new SimRobotPilot(orientation, position,playerID);
			otherSimRobot.setBoard(new Board()); 
			newExternalRobot(playerID, otherSimRobot);
		}

	@Override
	public void lockedSeesaw(String playerID, int playerNumber, int barcode) {
		System.out.println("Player " + playerNumber + " on seesaw " + barcode);
		printMessage("sh.lockedSeesaw with barcodenb " + barcode + " by player " + playerNumber);
		if(!robot.getPlayerID().equals(playerID)){
		rollSeeSawWithBarcode(barcode);
		}
	}

	@Override
	public void unlockedSeesaw(String playerID, int playerNumber, int barcode) {
	//	printMessage("sh.unlockedSeesaw by player ID: " + playerID + " no: " + playerNumber);
		if(!robot.getPlayerID().equals(playerID)){
		unlockSeesawWithBarcode(barcode);
		}
	}
	
	private void printMessage(String message){
	//	System.out.println(message);
		ContentPanel.writeToDebug(message);
	}

	public synchronized PlayerState getPlayerState(int playernb){
		for(RobotPilot robot : otherRobots.values()){
			if(robot.getPlayerNb() == playernb){
				return robot.getPlayerState();
			}
		}
		return robot.getPlayerState();
	}
	
	@Override
	public void playerRolled(PlayerDetails playerDetails, int playerNumber) {
		if(!otherRobots.containsKey(playerDetails.getPlayerID()) && !robot.getPlayerID().equals(playerDetails.getPlayerID())){
			//ID not yet in system and not your own ID
			connectExternalSimRobot(0,new Position(0,0),playerDetails.getPlayerID());
			//System.out.println("NEW ROBOT ADDED "+playerDetails.getPlayerID());
		}
		RobotPilot robot = otherRobots.get(playerDetails.getPlayerID());
		if(robot!=null){
			Pose initialPose = getInitialPositionFromPlayer(playerNumber);
			robot.setPlayerNb(playerNumber);
			robot.setPlayerState(PlayerState.STARTED);
			robot.setPose(initialPose.getOrientation().getAngleToHorizontal(), (int)initialPose.getX(), (int)initialPose.getY());
		}
	}
	
	

}
