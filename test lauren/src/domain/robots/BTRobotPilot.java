package domain.robots;
import java.io.IOException;

import domain.Position.Position;
import domain.maze.Board;
import domain.maze.Wall;
import domain.util.TimeStamp;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.PilotProps;



public class BTRobotPilot implements RobotPilot  {
	
	private DifferentialPilot pilot;
	private RegulatedMotor leftMotor;
	private RegulatedMotor rightMotor;
	private RegulatedMotor sensorMotor;
	
	//The poseProvider is an instantiation of a class that "LeJOS" provides for determining the pose of a NXT Robot.
	private OdometryPoseProvider poseProvider;
	
	private final double defaultTravelSpeed = 15;
	private final double defaultTurnSpeed = 70;
	
	private TouchSensor touchSensor;
	private UltrasonicSensor ultrasonicSensor;
	private LightSensor lightSensor;
	
	private Board board;
	
	public BTRobotPilot(){
		
		PilotProps pp = new PilotProps();
    	try {
			pp.loadPersistentValues();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
    	float wheelDiameter = 5.55F;
    	float trackWidth = 11.22F;
    	leftMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_LEFTMOTOR, "C"));
    	rightMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_RIGHTMOTOR, "B"));
    	sensorMotor = Motor.A; 
    	
    	pilot = new DifferentialPilot(wheelDiameter, trackWidth, leftMotor, rightMotor);
    	setMovingSpeed(defaultTravelSpeed);
    	setTurningSpeed(defaultTurnSpeed);
    	poseProvider= new OdometryPoseProvider(pilot);
    	touchSensor = new TouchSensor(SensorPort.S1);
    	ultrasonicSensor = new UltrasonicSensor(SensorPort.S2);
    	lightSensor = new LightSensor(SensorPort.S4);
    	
    	board = new Board();
    	
	}
	
	public void setBoard(Board board){
		this.board=  board;
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
		if(canMove())
			pilot.forward();
		else{
			throw new CannotMoveException();
		}
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
		return new Position(poseProvider.getPose().getLocation());
	}
	
	//TODO checken of de getheading waarden tussen 0 en 180 graden teruggeeft
	@Override
	public double getOrientation() {
		return poseProvider.getPose().getHeading();
	}

	public boolean isMoving(){
		return pilot.isMoving();
	}


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


	@Override
	public void move(double distance) throws CannotMoveException {
		int testdistance = 10;
		int n = (int) distance/testdistance;
		for(int i = 0; i<n ; i++){
			if(canMove()){
				pilot.travel(Math.min(testdistance, distance));
				distance = distance - testdistance;
			}
			else{
				throw new CannotMoveException();
			}
		}
	}

	@Override
	public void turnRight() {
		pilot.rotate(90);
	}

	@Override
	public void turnLeft() {
		pilot.rotate(90);
	}
	
	public boolean isTouching(){
		return touchSensor.isPressed();
	}
	
	public boolean canMove(){
		int distance = ultrasonicSensor.getDistance();	
		int testDistance = 10; 
		if(distance < testDistance || isTouching())
			return false;
		else
			return true;
	}
	
	
	public double readLightValue(){
		return lightSensor.readValue();
	}
	
	public double readUltrasonicValue(){
		return ultrasonicSensor.getDistance();
	}
	
	public void turnUltrasonicSensor(int angle){
		sensorMotor.rotate(angle);
	}
	
	public void turnSensorRight(){
		sensorMotor.rotateTo(-90);
	}
	
	public void turnSensorLeft(){
		sensorMotor.rotateTo(90);
	}
	
	public void turnSensorForward(){
		sensorMotor.rotateTo(0);
	}
	
	public void calibrateLightHigh(){
		lightSensor.calibrateHigh();
		lightSensor.setHigh(100);
		//TODO klopt nog niet
	}
	
	public void calibrateLightLow(){
		lightSensor.calibrateLow();
		lightSensor.setLow(0);
	}

	@Override
	public boolean detectWhiteLine() {
		if(readLightValue() > 600) return true;
		else return false;
		//TODO: moet nog gespecifieerd worden
	}

//	@Override
//	public void makeWall() {
//		sensorMotor.setSpeed(2);
//		Position pos1 = getPosition().getNewPosition(getOrientation(), readUltrasonicValue());
//		if(board.detectWallAt(pos1)) return;
//		turnUltrasonicSensor(5);
//		Position pos2 = getPosition().getNewPosition(5+getOrientation(), readUltrasonicValue());
//		if(board.detectWallAt(pos2)) return;
//		turnUltrasonicSensor(-10);
//		Position pos3 = getPosition().getNewPosition(-5+getOrientation(), readUltrasonicValue());
//		if(board.detectWallAt(pos3)) return;
//		turnSensorForward();
//		board.addWall(new Wall(pos1, pos2, pos3));
//		sensorMotor.setSpeed(720);
//	}

}
