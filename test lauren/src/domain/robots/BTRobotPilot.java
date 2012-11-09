package domain.robots;
import java.io.IOException;

import domain.Position.Position;
import domain.maze.Board;
import domain.maze.NotAWallException;
import domain.maze.Wall;
import domain.robotFunctions.Straightener;
import domain.util.TimeStamp;
import exceptions.ConnectErrorException;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.remote.NXTCommand;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTCommandConnector;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;
import lejos.robotics.RegulatedMotor;



public class BTRobotPilot implements RobotPilot  {
	
	private DifferentialPilot pilot;
	private RegulatedMotor leftMotor;
	private RegulatedMotor rightMotor;
	private RegulatedMotor sensorMotor;
	
	
	private final double defaultTravelSpeed = 15;
	private final double defaultTurnSpeed = 70;
	
	private TouchSensor touchSensor;
	private UltrasonicSensor ultrasonicSensor;
	private LightSensor lightSensor;
	
	private Board board;
	private NXTCommand nxtCommand;
	private final float wheelDiameterLeft = 5.47F;
	private final float wheelDiameterRight = 5.515F;
	private final float trackWidth = 17.22F;
	public BTRobotPilot(){

			NXTComm nxtComm;
			NXTConnector conn = new NXTConnector();
			NXTInfo[] nxtInfo= conn.search("GroeneHulk", null, NXTCommFactory.BLUETOOTH);
			try {
				nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
				System.out.println("We found this NXT: "+nxtInfo[0].name);//TODO
				nxtComm.open(nxtInfo[0]);
				nxtCommand=new NXTCommand(nxtComm);
				System.out.println("Bluetooth succeeded with " + nxtCommand.getFriendlyName());
			
			NXTCommandConnector.setNXTCommand(nxtCommand);

			sensorMotor = Motor.A;
			pilot = new DifferentialPilot(wheelDiameterLeft, wheelDiameterRight, trackWidth, nxtCommand, 1, 2);

			setMovingSpeed(defaultTravelSpeed);
			setTurningSpeed(defaultTurnSpeed);
			touchSensor = new TouchSensor(SensorPort.S1);
			ultrasonicSensor = new UltrasonicSensor(SensorPort.S2);
			lightSensor = new LightSensor(SensorPort.S3);
			} catch (NXTCommException e) {
				System.out.println("unable to connect");
				throw new ConnectErrorException();
			}	 catch (IOException e) {
				throw new ConnectErrorException();
				// TODO i3+
			} //TODO
			catch(ArrayIndexOutOfBoundsException indE){
				throw new ConnectErrorException();
				//TODO i3+
			}
		

		board = new Board();


	}
	
	public void setBoard(Board board){
		this.board=  board;
	}
	
	public Board getBoard(){
		return board;
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
//		if(canMove())
			pilot.forward();
//		else{
//			throw new CannotMoveException();
//		}
//		canMove();
	}

	@Override
	public void backward() {
		pilot.backward();
	}

	@Override
	public void stop() {
		pilot.stop();
	}

	@Override
	public Position getPosition() {
		return pilot.getPosition();
	}
	
	//TODO checken of de getheading waarden tussen 0 en 180 graden teruggeeft
	@Override
	public double getOrientation() {
	return pilot.getRotation();
	}

//	public boolean isMoving(){
//		return pilot.isMoving();
//	}


	@Override
	public void UpdateUntil(TimeStamp timestamp) {
		// TODO Auto-generated method stub
		
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
		pilot.travel(distance);
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
	
	public boolean isTouching(){
		return touchSensor.isPressed();
	}
	
	public boolean canMove(){
		int distance = ultrasonicSensor.getDistance();	
		int testDistance = 10; 
		if(distance < testDistance || isTouching()){
			makeWall();
			return false;
		}
		else
			return true;
	}
	
	
	public double readLightValue(){
		try{
			return lightSensor.readValue();
		}catch(Exception e){
			System.out.println("could not read light value from robot ");
			return -100;
			//TODO i3+
		}
	}
	
	public double readUltrasonicValue(){
		return ultrasonicSensor.getDistance();
	}
	
	public void turnUltrasonicSensor(int angle){
		sensorMotor.rotate(angle);
	}
	
	public void turnSensorRight(){
		sensorMotor.rotateTo(-90);
		canMove();
	}
	
	public void turnSensorLeft(){
		sensorMotor.rotateTo(90);
		canMove();
	}
	
	public void turnSensorForward(){
		sensorMotor.rotateTo(0);
		canMove();
	}
	
	public void calibrateLightHigh(){
		lightSensor.calibrateHigh();
		//TODO klopt nog niet
	}
	
	public void calibrateLightLow(){
		lightSensor.calibrateLow();
	}

	@Override
	public boolean detectWhiteLine() {
		if(readLightValue() > 50) return true;
		else return false;
		//TODO: moet nog gespecifieerd worden
	}

	public void makeWall() {
		sensorMotor.setSpeed(2);
		Position pos1 = getPosition().getNewPosition(getOrientation(), readUltrasonicValue());
		if(board.detectWallAt(pos1)) return;
		turnUltrasonicSensor(5);
		Position pos2 = getPosition().getNewPosition(5+getOrientation(), readUltrasonicValue());
		if(board.detectWallAt(pos2)) return;
		turnUltrasonicSensor(-10);
		Position pos3 = getPosition().getNewPosition(-5+getOrientation(), readUltrasonicValue());
		if(board.detectWallAt(pos3)) return;
		turnSensorForward();
		try {
			Wall wall = new Wall(pos1,pos2,pos3);
			board.addWall(wall);
		} catch (NotAWallException e) {
			// no real wall was detected
		}
		sensorMotor.setSpeed(720);
	}
	
	public void findOrigin(){
		setMovingSpeed(0.1); //TODO: testen
		Straightener s = new Straightener(new Robot(this));
		boolean found = false;
		while(!found){
			pilot.forward();
			found = detectWhiteLine();
		}
		pilot.stop();
		s.straighten();
		pilot.travel(20);
		turnRight();
		found= false;
		while(!found){
			pilot.forward();
			found = detectWhiteLine();
		}
		pilot.stop();
		turnLeft();
		pilot.travel(20);
		// the robot is in position (0,0)
	}
	
	public void straighten(){
		new Straightener(new Robot(this)).straighten();
	}
	
	public int getBatteryVoltage(){
		try {
			return nxtCommand.getBatteryLevel();
		} catch (IOException e) {
			return 0;
		}
	}

	@Override
	public void arcForward(boolean left) {
		
//		pilot.steer(calcTurnRate(left)); TODO i2
	}

	@Override
	public void arcBackward(boolean left) {
//		pilot.steerBackward(calcTurnRate(left)); TODO i2
	}

	@Override
	public void steer(double angle) {
		double turnrate=calcTurnRate(angle<0);
//		pilot.steer(turnrate, angle); TODO i2
	}
	
	/**
	 * Calculates the turnrate (asked by the steer method of differentialPilot)
	 * to simulate adding the turn-speed to the forward speed
	 */
	private double calcTurnRate(boolean left) {
		double turnWheelSpeed = defaultTurnSpeed * trackWidth * Math.PI / 360;
		if (left)
			return (defaultTravelSpeed + turnWheelSpeed)
					/ (defaultTravelSpeed - turnWheelSpeed);
		else
			return (defaultTravelSpeed - turnWheelSpeed)
					/ (defaultTravelSpeed + turnWheelSpeed);
		//TODO kijken of left en right wel kloppen
	}

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
			found = detectWhiteLine();
		}
	}


}
