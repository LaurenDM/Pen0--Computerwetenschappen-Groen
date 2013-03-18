package domain.robots;


import java.io.IOException;

import bluetooth.BTCommPC;
import bluetooth.CMD;
import domain.Position.Position;
import domain.maze.Ball;
import domain.maze.Board;
import domain.maze.Wall;
import domain.maze.barcodes.Action;
import domain.maze.barcodes.Barcode;
import domain.maze.graph.MazePath;
import domain.robotFunctions.ExploreMaze;
import domain.util.RobotChecker;
import domain.util.TimeStamp;
import exceptions.ConnectErrorException;
import gui.ContentPanel;



public class BTRobotPilot extends RobotPilot  {
	
	private BluetoothDriver pilot;
//	private RegulatedMotor sensorMotor;
	
	
	private final double defaultTravelSpeed = 10;
	private final double defaultTurnSpeed = 200;
	

	
	private Board board;
	private  final float wheelsDiameter = 5.43F;
	//	private static final float wheelDiameterLeft = 5.43F;
	//	private static final float wheelDiameterRight = 5.43F;
	private  final float trackWidth = 16.43F;
	private int prevLightValue;
	private int prevUltrasonicValue;
	private int prevIRValue;
	private int prevSensorAngle;
	private String bluetoothAdress="00:16:53:05:40:4c";
	private final BTCommPC btComm;
	private ExploreMaze maze;
	


	private long lastSensorUpdateTime;

	
	public BTRobotPilot(int number){
		super(number);
		try {
			btComm = (new BTCommPC(this));
			btComm.open(null,bluetoothAdress );
			pilot = new BluetoothDriver(wheelsDiameter, trackWidth, btComm);
			pilot.setPose(getOrientation(), 260, 180);
			setMovingSpeed(defaultTravelSpeed);
			setTurningSpeed(defaultTurnSpeed);

		}
		catch(ArrayIndexOutOfBoundsException indE){
			System.out.println("we will throw ConnectErrorException because of ArrayIndexOutOfBoundsException after trying to make a BTRobotPilot");
			throw new ConnectErrorException();

		}
		board = new Board();
	}
	
	public BTRobotPilot(){
		this(0);
	}
	
	public void setBoard(Board board){
		this.board=  board;
	}
	
	public Board getBoard(){
		return board;
	}
	
	public ExploreMaze getMaze(){
		return this.maze;
	}

	@Override
	public void turn(double amount) {
		pilot.rotate(amount);
	}

	@Override
	public void setMovingSpeed(double speed) {
		pilot.setTravelSpeed(speed);
	}

	@Override
	public void setTurningSpeed(double speed) {
		pilot.setRotateSpeed(speed);
	}

	@Override
	public void forward() throws CannotMoveException {
		setMovement(Movement.FORWARD);
//		if(canMove())
			pilot.forward();
//		else{
//			throw new CannotMoveException();
//		}
//		canMove();
	}

	@Override
	public void backward() {
		setMovement(Movement.BACKWARD);
		pilot.backward();
	}

	@Override
	public void stop() {
		setMovement(Movement.STOPPED);
		pilot.stop();
	}

	@Override
	public Position getPosition() {
		if(RobotChecker.interruptionAllowed()){
		return pilot.getPosition();}
		else return new Position(0,0);
	}
	
	
	@Override
	public double getOrientation() {
		if(RobotChecker.interruptionAllowed())
			return pilot.getRotation();
		else return 0;
		
	}

//	public boolean isMoving(){
//		return pilot.isMoving();
//	}


	@Override
	public void UpdateUntil(TimeStamp timestamp) {
		
		
	}


	@Override
	public double getMovingSpeed() {
		return pilot.getTravelSpeed();
	}


	@Override
	public double getTurningSpeed() {
		return pilot.getRotateSpeed();
	}


	// TESTEN! Indien te schokkerig, moet geleidelijker
	@Override
	public void move(double distance) throws CannotMoveException {
//		int testdistance = 50;
//		int n = (int) distance/testdistance;
//		for(int i = 0; i<n ; i++){
//			if(canMove()){
//				pilot.travel(Math.min(testdistance, distance));
//				distance = distance - testdistance;
//			}
//			else{
//				throw new CannotMoveException();
//			}
//		}
		if(distance>0)
			setMovement(Movement.FORWARD);
		else if(distance>0)
			setMovement(Movement.BACKWARD);
		pilot.travel(distance);
		setMovement(Movement.STOPPED);
	}

	@Override
	public void turnRight() {
		pilot.rotate(90);
//		canMove();
	}

	@Override
	public void turnLeft() {
		pilot.rotate(-90);
//		canMove();
	}
	
	@Override
	public int getSensorAngle(){
		if(RobotChecker.interruptionAllowed())
			updateSensorValues(false);
		return prevSensorAngle;
	}
	
//	public boolean isTouching(){
//		if(RobotChecker.interruptionAllowed())
//			updateSensorValues(false);
//		return prevTouchBool;
//	}

	public boolean canMove(){
		int distance = (int) readUltrasonicValue();	
		int testDistance = 10; 
		if(distance < testDistance){
			//makeWall();
			return false;
		}
		else
			return true;
	}
	
	
	public double readLightValue(){
		try{
			if(RobotChecker.interruptionAllowed())
				updateSensorValues(false);
			return prevLightValue;
		}catch(Exception e){
			System.out.println();
			System.out.println(e.getClass()+" "+e.getMessage());
			e.printStackTrace();
			System.out.println("could not read light value from robot ");
			return -100;
			
		}
	}
	
	public double readUltrasonicValue() {
		if (RobotChecker.interruptionAllowed())
			updateSensorValues(false);
		return prevUltrasonicValue;
	}
	
	public boolean detectInfrared(){
		if (RobotChecker.interruptionAllowed())
			updateSensorValues(false);
		return prevIRValue>130;	//TODO: waarde moet experimenteel bepaald worden
	}
	
	private void updateSensorValues(boolean forced) {
		if (lastSensorUpdateTime + 100 < System.currentTimeMillis()) {
			int[] sensorValues = btComm.sendCommand(CMD.GETSENSORVALUES);
			if (sensorValues != null) {
				prevUltrasonicValue = sensorValues[1];
				prevLightValue = sensorValues[0];
				prevIRValue = sensorValues[2];
				prevSensorAngle = sensorValues[3];
				lastSensorUpdateTime = System.currentTimeMillis();
			}
		}
	}

	public void turnUltrasonicSensor(int angle){
		btComm.sendCommand(CMD.TURNSENSOR,angle);
	}
	
	public void turnSensorRight(){
		btComm.sendCommand(CMD.TURNSENSORTO,-90);
//		canMove();
	}
	
	public void turnSensorLeft(){
		btComm.sendCommand(CMD.TURNSENSORTO,90);
//		canMove();
	}
	
	public void turnSensorForward(){
		btComm.sendCommand(CMD.TURNSENSORTO,0);
//		canMove();
	}
	
	public void calibrateLightHigh(){
		btComm.sendCommand(CMD.CALIBRATELSHIGH);
	}
	
	public void calibrateLightLow(){
		btComm.sendCommand(CMD.CALIBRATELSLOW);
	}

	@Override
	public boolean detectWhiteLine() {
		if(readLightValue() > 50) return true;
		else return false;
	}
	
	public void straighten(){
		btComm.sendCommand(CMD.STRAIGHTEN);
	}
	

	public int getBatteryVoltage(){
		if(RobotChecker.interruptionAllowed())
			return btComm.sendCommand(CMD.BATTERY)[0];
			else return 0; 
		
	}

	@Override
	public void arcForward(boolean left) {
		
//		pilot.steer(calcTurnRate(left)); 
	}

	@Override
	public void arcBackward(boolean left) {
//		pilot.steerBackward(calcTurnRate(left)); 
	}

	@Override
	public void steer(double angle) {
//		double turnrate=calcTurnRate(angle<0);
//		pilot.steer(turnrate, angle); 
	}
	
//	/**
//	 * Calculates the turnrate (asked by the steer method of differentialPilot)
//	 * to simulate adding the turn-speed to the forward speed
//	 */
//	private double calcTurnRate(boolean left) {
//		double turnWheelSpeed = defaultTurnSpeed * trackWidth * Math.PI / 360;
//		if (left)
//			return (defaultTravelSpeed + turnWheelSpeed)
//					/ (defaultTravelSpeed - turnWheelSpeed);
//		else
//			return (defaultTravelSpeed - turnWheelSpeed)
//					/ (defaultTravelSpeed + turnWheelSpeed);
//		
//	}

	@Override
	public void keepTurning(boolean left) {
		pilot.keepTurning(left);
	}
	
	@Override
	public void findWhiteLine(){
		setMovingSpeed(2);
		boolean found=false;
		try {
			forward();
		} catch (CannotMoveException e) {
			turnRight();
		}
		while(!found){
			if(wallDetected())
				fixWall();
			found = detectWhiteLine();
		}
		stop();
	}

	private void fixWall() {
		turnSensorLeft();
		double leftValue = readUltrasonicValue();
		turnSensorRight();
		double rightValue = readUltrasonicValue();
		turnSensorForward();
		if(leftValue < rightValue)
			turn(-10);
	}

	private boolean wallDetected() {
		if(readUltrasonicValue() < 10)
			return false;
		return false;
	}

	@Override
	public void interrupt() {
		pilot.interrupt();
	}

	@Override
	public double getDefaultMovingSpeed() {
		return defaultTravelSpeed;
	}

	@Override
	public double getDefaultTurningSpeed() {
		return defaultTurnSpeed;
	}

	@Override
	public void playSong() {
		btComm.sendCommand(CMD.PLAYTUNE);
	}

	@Override
	public void setPose(double orientation, int x, int y) {
		pilot.setPose(orientation, x, y);		
	}

	@Override
	public void startExplore() {
		maze = new ExploreMaze(this);
		maze.start();
	}

	@Override
	public boolean detectBlackLine() {
		
		if(readLightValue()<-100){
			return true;
		}
		else return false;
	}

	@Override
	public void addFoundWall(Wall wall) {
		board.foundNewWall(wall);
	}

	@Override
	public void findBlackLine() {
//		setMovingSpeed(2);
//		boolean found=false;
//		try {
//			forward();
//		} catch (CannotMoveException e) {
//			turnRight();
//		}
//		while(!found){
//			found = detectBlackLine();
//		}
//		stop();
//		setMovingSpeed(getDefaultMovingSpeed());
	}

	//  using this method makes all other bluetooth-commands be thrown
	// away until this method returns 

	@Override
	public void scanBarcode() {
		int[] results = btComm.sendCommand(CMD.SCANBARCODE);
		makeBarcode(results);
	}

	@Override
	public void setCheckpoint() {
		maze.setCurrentTileToCheckpoint();
	}

	public void makeBarcode(int[] data) {
		Barcode barcode = new Barcode(data[2], new Position(data[0], data[1]),
				data[3]);
		getBoard().addFoundBarcode(barcode);
		Action action = barcode.getAction();
		if(action != null) action.run(this);
	}

	@Override
	public void resumeExplore() {
		if(maze!=null){
			maze.resumeExplore(0, 0, null);
		} else {
			ContentPanel.writeToDebug("You haven't started exploring yet!");
		}
		
	}

	@Override
	public void driveToFinish() {
		if(maze!=null){
			maze.stopExploring();
			maze.driveToFinish();
		} else {
			ContentPanel.writeToDebug("You haven't started exploring yet!");
		}		
	}

	@Override
	public void wait5Seconds() {
		btComm.sendCommand(CMD.WAIT5);
	}

	@Override
	public void autoCalibrateLight() {
		btComm.sendCommand(CMD.AUTOCALIBRATELS,8);
	}
	public MazePath getPathToFinish() {
		return maze.getPathToFinish();
	}

	@Override
	public void setDriveToFinishSpeed() {
		btComm.sendCommand(CMD.DRIVEFAST);
	}

	@Override
	public void fetchBall() {
		btComm.sendCommand(CMD.FETCHBALL);
		setBall(new Ball());
		maze.setNextTileToDeadEnd();
	}

	@Override
	public void doNothing() {
		maze.setNextTileToDeadEnd();
	}

	@Override
	public boolean checkForSeaSawInfrared() {
		return detectInfrared();
	}
	

}