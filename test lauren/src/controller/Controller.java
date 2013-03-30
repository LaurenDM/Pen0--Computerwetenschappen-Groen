package controller;

import groen.htttp.HtttpImplementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import rabbitMQ.EventPusher;
import rabbitMQ.SubscribeMonitor;

import domain.PolygonDriver;
import domain.Position.Position;
import domain.maze.Ball;
import domain.maze.Board;
import domain.maze.MazeInterpreter;
import domain.maze.RandomMazeGenerator;
import domain.maze.Seesaw;
import domain.maze.WorldSimulator;
import domain.maze.barcodes.Barcode;
import domain.robots.BTRobotPilot;
import domain.robots.CannotMoveException;
import domain.robots.RobotPilot;
import domain.robots.SimRobotPilot;
import domain.util.ColorPolygon;

//Robotclass, generates/keeps track of current positioning.
public class Controller {
	private static boolean STOPPED = false;
	private Thread executingThread;
	private RobotPilot currentRobot;
	private RobotPilot btRobot;
	private RobotPilot simRobot;
	private Thread explorer;
	private WorldSimulator worldSimulator;
//	private final EventPusher ep;
//	private SimRobotPilot simRobotPilot ;
	private HtttpImplementation htttpImplementation;
	
	private String playerID;
	
	public Controller() {
		
		
		int random = (int) (Math.random()*1000000);
//	    random =1;
		this.playerID = "playerIDGroen"+random;
		
		
		
		
		connectNewSimRobot(0, new Position(20,20), playerID);
		
		try{
			htttpImplementation = new HtttpImplementation(this);
		} catch(Exception e){
			System.out.println("Couldn't connect to the remote server.");
		}
		
		setPlayerClient();
		
	}


	  
	public void connectNewBtRobot() {
		if(btRobot==null)
		btRobot = new BTRobotPilot(this.playerID);
		worldSimulator = new WorldSimulator(btRobot);
		currentRobot=btRobot;
		currentRobot.setBoard(new Board());

	}
	
	//This is used to set the robot controlled by this GUI
	public void connectNewSimRobot(double orientation, Position position, String playerID) {
		simRobot = new SimRobotPilot(orientation, position,playerID);
		worldSimulator = new WorldSimulator(simRobot); 
		currentRobot = simRobot ;
		currentRobot.setBoard(new Board());
	}
	
	public void setPlayerClient(){
		currentRobot.setPlayerClient(htttpImplementation.getPlayerClient());
	}
	
	public WorldSimulator getWorldSimulator(){
		return this.worldSimulator;
	}

	
	public void polygonDriver(int numberOfVertices, double edgeLength) {
		PolygonDriver driver = new PolygonDriver(currentRobot);
		driver.drive(numberOfVertices, edgeLength);
		
	}

	public void moveForward() throws CannotMoveException {
		startNewThread(null);
		currentRobot.forward();
		
	}

	public void moveBack() {
		startNewThread(null);
		currentRobot.backward();
	}

	public void rotateRightNonBlocking() {
	startNewThread(new TurnRightThread());	
	}

	public void rotateLeftNonBlocking() {
		startNewThread(new TurnLeftThread());
	}
	public void startNewThread(Thread newThread){
		if (executingThread != null) {
			executingThread.interrupt();
			while(executingThread.isAlive());
		}

		executingThread = newThread;
		if (executingThread != null)
			executingThread.start();
	}
	
	public void cancel() {
		STOPPED = true;
		while(explorer!=null && explorer.isAlive()){}
		currentRobot.interrupt();
		startNewThread(null);
		currentRobot.stop();
		STOPPED = false;
	}
	
	public Position getPosition(){
		return currentRobot.getPosition();
	}
	
	public String getPlayerID(){
		return this.playerID;	}
	
	public double getXCo(){
		return currentRobot.getPosition().getX();
	}

	public double getYCo(){
		return currentRobot.getPosition().getY();
	}

	public double getSpeed() {
		return currentRobot.getMovingSpeed();
//		return currentRobot.getActualMovingSpeed();
	}

	public double getAngle() {
		return currentRobot.getOrientation();
	}

	public List<ColorPolygon> getColorPolygons(){
		List<ColorPolygon> colPolyList=new ArrayList<ColorPolygon>();
		colPolyList.add(currentRobot.getRobotPolygon());
		Iterator<RobotPilot> it = worldSimulator.getOtherRobots().iterator();
	    while (it.hasNext()) {
	        RobotPilot robot = it.next();
	        colPolyList.add(robot.getRobotPolygon());
//	        it.remove(); // avoids a ConcurrentModificationException
	    }
//		for(Robot robot: otherRobots){
//		colPolyList.add(robot.getRobotPolygon());
//		}
		return colPolyList;
	}
	
	private class TurnRightThread extends Thread{
		@Override
		public void run(){
			currentRobot.turnRight();
		}
	}
	
	private class TurnLeftThread extends Thread{
		@Override
		public void run(){
			currentRobot.turnLeft();
		}
	}
	
	public void calibrateLightHigh(){
		currentRobot.calibrateLightHigh();
	}
	
	public void calibrateLightLow(){
		currentRobot.calibrateLightLow();
	}
	
	public double readLightValue(){
		return currentRobot.readLightValue();
	}
	
	public double readUltrasonicValue(){
		return currentRobot.readUltrasonicValue();
	}

	public int getInfraredValue(){
		return currentRobot.getInfraredValue();
	}
	
	public void turnUltrasonicSensor(int angle){
		currentRobot.turnUltrasonicSensor(angle);
	}
	
	public int getSensorAngle(){
		return currentRobot.getSensorAngle();
	}
	
	public void turnSensorRight(){
		currentRobot.turnSensorRight();
	}
	
	public void turnSensorLeft(){
		currentRobot.turnSensorLeft();
	}
	
	public void turnSensorForward(){
		currentRobot.turnSensorForward();
	}
	
	public boolean detectWhiteLine(){
		return currentRobot.detectWhiteLine();
	}
	
	public boolean wall(){
		final int DISTANCE_LIMIT= 50;
		return readUltrasonicValue() < DISTANCE_LIMIT;
	}

	public RobotPilot getRobot() {
		return currentRobot;
	}

	public void readMazeFromFile(String fileLocation) {
		MazeInterpreter MI = new MazeInterpreter(worldSimulator);
		if(fileLocation.equals("nullnull")){
		RandomMazeGenerator RMG = new RandomMazeGenerator(MI);
		}
		else
		MI.readFile(fileLocation);
		
	}

	public void findLineAndStraighten() {
		Runnable straightener = new Runnable() {

			@Override
			public void run() {
				//currentRobot.findOpening();
				currentRobot.straighten();
			}
		};
		(new Thread(straightener)).start();
	}



	public void rotateAmount(int i) {
		currentRobot.turn(i);		
	}
	
	public void findBlackLineAndCreateBarcode(){
		Runnable barcodeGen = new Runnable() {

			@Override
			public void run() {
				currentRobot.findBlackLine();
				currentRobot.scanBarcode();
			}
		};
		(new Thread(barcodeGen)).start();
	}

	public void startExplore() {
		if(explorer!=null){
			STOPPED = true;
			while(explorer.isAlive()){}
			STOPPED = false;
		}
		Runnable explore = new Runnable() {

			@Override
			public void run() {
				currentRobot.startExplore();
			}
		};
		explorer = new Thread(explore);
		explorer.start();
	}

	public void resumeExplore() {
		STOPPED = true;
		while(explorer.isAlive()){}
		STOPPED = false;
		explorer = new Thread(new Runnable() {
			@Override
			public void run(){
				currentRobot.resumeExplore();
			}
		});
		explorer.start();
	}

	public void driveToFinish() {
		STOPPED = true;
		while(explorer.isAlive()){}
		STOPPED = false;
		explorer = new Thread(new Runnable() {
			@Override
			public void run(){
				currentRobot.driveToFinish();
			}
		});
		explorer.start();
	}
	
	public void reset(){
		STOPPED = true;
		while(explorer!=null && explorer.isAlive()){}
		int random = (int) (Math.random()*1000000);
//	    random =1;
		String playerID = "playerIDGroen"+random;
		connectNewSimRobot(0, new Position(20,20), playerID);
	}

	public static boolean isStopped() {
		return STOPPED ;
	}
	
	public static void setStopped(boolean stopped) {
		Controller.STOPPED = stopped;
	}



	public void autoCalibrateLight() {
		currentRobot.autoCalibrateLight();}
	
//	public void disableError() {
//		otherRobots.get(1).setPose(3, 100, 100); 
////		simRobotPilot.disableError();
//	}
	
//	//set the simRobot of this gui to the first robot
//	public void setFirstRobot(){
//		connectNewSimRobot(0, new Position(20,20), 0);
//	}
//	
//	//set the simRobot of this gui to the first robot
//	public void setSecondRobot(){
//		connectNewSimRobot(0, new Position(100,20), 1);
//	}


	public RobotPilot getRobotFromIdentifier(int identifier) {
////		System.out.println("IDENTIFIER"+identifier);
////		System.out.println("currIDEN:"+ep.getRobotRandomIdentifier());
//		if(identifier==ep.getRobotRandomIdentifier()){
//			//message komt van deze robot
//			return null;
//		}
//		else{
//			if(otherRobots.containsKey(identifier)){
//				return otherRobots.get(identifier);
//			}
//			else{
//				connectExternalSimRobot(2, new Position(220, 20), identifier);
//				return getRobotFromIdentifier(identifier);
//			}
//		}
		return null;
	}
	

	public void setBallNumber(int number){
		Barcode.setBallNumber(number);
	}



	public boolean ballInPossesion() {
		return currentRobot.hasBall();
	}

	public Ball getBall(){
		return getRobot().getBall();
	}
	
	public void ballFoundByOtherRobot(RobotPilot robot){
		robot.setFoundBall(robot.getNumber());
	}

	
	public int getTeamNumber(){
		return currentRobot.getTeamNumber();
	}
	
	public void setPlayerNb(int nb){
		currentRobot.setPlayerNb(nb);
	}


	public void setFalseBallNumbers(int[] falseBallBarcodes) {
		Barcode.setFalseBallNumbers(falseBallBarcodes);
	}
	
	public void playerJoined(int identifier){
		//RobotPilot newRobot = new RobotPilot(identifier);
		//otherRobots.put(identifier, newRobot);
	}
	
	



	public void launchCatapult() {
		turnUltrasonicSensorTo(180);		
	}
	private void turnUltrasonicSensorTo(int angle) {
		currentRobot.turnUltrasonicSensorTo(angle);
	}



	public boolean detectInfrared() {
		return currentRobot.detectInfrared();
	}

	public void setReady(boolean ready) {
		currentRobot.setReady(ready);
	}
	
	public void setInitialPositionNumber(int playerNb){
		currentRobot.setInitialPositionNumber(playerNb);
	}

	public void teleport() {
		currentRobot.teleportToStartPosition();
	}


	public void mousePressed(int x, int y) {
		List<Seesaw> seesaws = worldSimulator.getSeesaws();
		for(Seesaw s: seesaws){
			if(s.hasPosition(new Position(x, y))){
			s.rollOver();
			}
		}
	}
}
