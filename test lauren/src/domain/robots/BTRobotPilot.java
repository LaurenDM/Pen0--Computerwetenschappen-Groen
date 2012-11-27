package domain.robots;
import java.io.IOException;

import lejos.nxt.remote.NXTCommand;
import bluetooth.BTCommPC;
import bluetooth.CMD;
import domain.Position.Position;
import domain.barcodes.Barcode;
import domain.maze.Board;
import domain.maze.Wall;
import domain.robotFunctions.ExploreMaze;
import domain.robotFunctions.Straightener;
import domain.util.TimeStamp;
import exceptions.ConnectErrorException;



public class BTRobotPilot implements RobotPilot  {
	
	private DifferentialPilot pilot;
//	private RegulatedMotor sensorMotor;
	
	
	private final double defaultTravelSpeed = 15;
	private final double defaultTurnSpeed = 10;
	
//	private TouchSensor touchSensor;
//	private UltrasonicSensor ultrasonicSensor;
//	private LightSensor lightSensor;
	
	private Board board;
	private NXTCommand nxtCommand;
	private final float wheelDiameterLeft = 5.43F;
	private final float wheelDiameterRight = 5.43F;
	private final float trackWidth = 16.62F;
	private int prevLightValue;
	private int prevUltrasonicValue;
	private boolean prevTouchBool;
	private int prevSensorAngle;
	private String bluetoothAdress="00:16:53:05:40:4c";
	private final BTCommPC btComm;
	public BTRobotPilot(){

			try {
			btComm = (new BTCommPC());
			btComm.open(null,bluetoothAdress );
			pilot = new DifferentialPilot(wheelDiameterLeft, wheelDiameterRight, trackWidth, btComm, 1, 2);
			pilot.setPose(getOrientation(), 260, 180);
			setMovingSpeed(defaultTravelSpeed);
			setTurningSpeed(defaultTurnSpeed);

			}
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
	
	@Override
	public int getSensorAngle(){
		if(RobotChecker.interruptionAllowed())
			updateSensorValues(false);
		return prevSensorAngle;
	}
	
	public boolean isTouching(){
		if(RobotChecker.interruptionAllowed())
			updateSensorValues(false);
		return prevTouchBool;
	}

	public boolean canMove(){
		int distance = (int) readUltrasonicValue();	
		int testDistance = 10; 
		if(distance < testDistance || isTouching()){
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
			System.out.println("could not read light value from robot ");
			return -100;
			//TODO i3+
		}
	}
	
	public double readUltrasonicValue() {
		if (RobotChecker.interruptionAllowed())
			updateSensorValues(false);
		return prevUltrasonicValue;
	}
	
	private void updateSensorValues(boolean forced) {
		int[] sensorValues = btComm.sendCommand(CMD.GETSENSORVALUES);
		prevUltrasonicValue = sensorValues[1];
		prevLightValue = sensorValues[0];
		prevTouchBool = sensorValues[2] > 0;
		prevSensorAngle=sensorValues[3];
	}

	public void turnUltrasonicSensor(int angle){
		btComm.sendCommand(new int[]{CMD.TURNSENSOR,angle});
	}
	
	public void turnSensorRight(){
		btComm.sendCommand(new int[]{CMD.TURNSENSORTO,-90});
//		canMove();
	}
	
	public void turnSensorLeft(){
		btComm.sendCommand(new int[]{CMD.TURNSENSORTO,90});
//		canMove();
	}
	
	public void turnSensorForward(){
		btComm.sendCommand(new int[]{CMD.TURNSENSORTO,0});
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
	
//	public void findOrigin(){
//		setMovingSpeed(0.1); //TODO: testen
//		Straightener s = new Straightener(new Robot(this));
//		boolean found = false;
//		while(!found){
//			pilot.forward();
//			found = detectWhiteLine();
//		}
//		pilot.stop();
//		s.straighten();
//		pilot.travel(20);
//		turnRight();
//		found= false;
//		while(!found){
//			pilot.forward();
//			found = detectWhiteLine();
//		}
//		pilot.stop();
//		turnLeft();
//		pilot.travel(20);
//		// the robot is in position (0,0)
//	}
	
	public void straighten(){
		new Straightener(new Robot(this)).straighten();
	}
	
	public int getBatteryVoltage(){
		try {
		if(RobotChecker.interruptionAllowed())
				return nxtCommand.getBatteryLevel();
		else return 0; //TODO
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
//		double turnrate=calcTurnRate(angle<0);
//		pilot.steer(turnrate, angle); TODO i2
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
//		//TODO kijken of left en right wel kloppen
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
			found = detectWhiteLine();
		}
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
	public void playSong(String name) {
		try {
			nxtCommand.playSoundFile(name, false);
		} catch (IOException e) {
			System.out.println("There is no sound file with this name!");
		}
	}

	@Override
	public void setPose(double orientation, int x, int y) {
		pilot.setPose(orientation, x, y);		
	}

	@Override
	public void startExplore() {
		ExploreMaze maze = new ExploreMaze(this);
		maze.start();
	}

	@Override
	public boolean detectBlackLine() {
		//TODO: waarden checken en kalibreren
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
		setMovingSpeed(2);
		boolean found=false;
		try {
			forward();
		} catch (CannotMoveException e) {
			turnRight();
		}
		while(!found){
			found = detectBlackLine();
		}
	}

	@Override
	public void setRobot(Robot robot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scanBarcode() {
		int[] results = btComm.sendCommand(CMD.SCANBARCODE);
		Barcode barcode = new Barcode(results[2], new Position(results[0], results[1]), results[3]);
		getBoard().addFoundBarcode(barcode);
		int[] command = barcode.getAction().getActionNb();
		if(command != null) btComm.sendCommand(command);
	}

	
	


}
