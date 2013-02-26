package domain.robots;

import domain.Position.Position;
import domain.maze.Ball;
import domain.maze.Board;
import domain.maze.graph.MazePath;
import domain.polygons.RobotPolygon;
import domain.util.TimeStamp;

public class Robot {
	RobotPilot robotPilot;
	RobotPolygon robotPolygon;
	private Movement movement;
	private Position finish;
	private int number; //0-3
	private boolean foundBall;
	
	
	
	public Robot(RobotPilot robotPilot, int number){
		this.robotPilot=robotPilot;
		this.movement=Movement.STOPPED;
		this.robotPolygon=new RobotPolygon(this);
		this.number = number;
	}
	
	public RobotPilot getRobotPilot(){
		return this.robotPilot;
	}
	
	
	public RobotPolygon getRobotPolygon(){
		return robotPolygon;
	}

	
	public void UpdateUntil(TimeStamp timestamp) {
		// TODO Auto-generated method stub
	}
	
	public void setBoard(Board board){
		robotPilot.setBoard(board);
	}
	
	public Board getBoard(){
		return robotPilot.getBoard();
	}


	public Movement getMovementStatus() {
		return movement;
	}


	public double getActualMovingSpeed() {
		return robotPilot.getMovingSpeed()*movement.getSpeedMultiplier();
	}
	
	public double getMovingSpeedSetting() {
		return robotPilot.getMovingSpeed();
	}

	public double getTurningSpeed() {
		return robotPilot.getTurningSpeed();
		
	}


	public void forward() throws CannotMoveException{
		this.movement = Movement.FORWARD;
		robotPilot.forward();
	}


	public void backward(){
		this.movement = Movement.BACKWARD;
		robotPilot.backward();
	}


	public void stop(){
		this.movement = Movement.STOPPED;
		robotPilot.stop();
	}


	public void move(double distance) throws CannotMoveException {
		if (distance > 0)
			movement = Movement.FORWARD;
		else if (distance < 0)
			movement = Movement.BACKWARD;
		robotPilot.move(distance);
		movement = Movement.STOPPED;
	}


	public void turn(double amount){
		robotPilot.turn(amount);
	}


	public void turnRight() {
		robotPilot.turnRight();
	}


	public void turnLeft() {
		robotPilot.turnLeft();
	}


	public double getOrientation(){
		return robotPilot.getOrientation();
	}


	public void setMovingSpeed(double speed) {
		robotPilot.setMovingSpeed(speed);
	}


	public void setTurningSpeed(double speed) {
		robotPilot.setTurningSpeed(speed);
	}

	public Position getPosition() {
		return robotPilot.getPosition();
	}
	
	public boolean isTouching(){
		return robotPilot.isTouching();
	}
	
	public double readLightValue(){
		return robotPilot.readLightValue();
	}
	
	public double readUltrasonicValue(){
		return robotPilot.readUltrasonicValue();
	}
	
	public void calibrateLightHigh(){
		robotPilot.calibrateLightHigh();
	}
	
	public void calibrateLightLow(){
		robotPilot.calibrateLightLow();
	}
	
	public void turnUltrasonicSensor(int angle){
		robotPilot.turnUltrasonicSensor(angle);
	}
	
	public int getSensorAngle(){
		return robotPilot.getSensorAngle();
	}
	
	public void turnSensorRight(){
		robotPilot.turnSensorRight();
	}
	
	public void turnSensorLeft(){
		robotPilot.turnSensorLeft();
	}
	
	public void turnSensorForward(){
		robotPilot.turnSensorForward();
	}

	public boolean detectWhiteLine(){
		return robotPilot.detectWhiteLine();
	}
	
	public void findWhiteLine(){
		robotPilot.findWhiteLine();
	}
	
	public void straighten(){
		robotPilot.straighten();
	}
	
//	public void findOrigin(){
//		robotPilot.findOrigin();
//	}
	
	//Starts moving de robot so that it makes an arc forward.
	public void arcForward(boolean left){
		robotPilot.arcForward(left);
	}

	//Starts moving de robot so that it makes an arc backward.
	public void arcBackward(boolean left){
		robotPilot.arcBackward(left);
	}
	
	//Makes the robot make an arc of the specified angle. This method does not return immediately.
	public void steer(double angle){
		robotPilot.steer(angle);
	}
	
	/**
	 * keep turning left or right.
	 * returns immediatly
	 * @param left
	 */
	public void keepTurning(boolean left){
		robotPilot.keepTurning(left);
	}

	//this method should be used for interrupting a movement that would end by itself but should end sooner than what is planned.
	public void interrupt() {
		robotPilot.interrupt();
	}
	
	public void resetToDefaultSpeeds(){
		robotPilot.setMovingSpeed(robotPilot.getDefaultMovingSpeed());
		robotPilot.setTurningSpeed(robotPilot.getDefaultTurningSpeed());
	}
	
	public double getDefaultTurningSpeed(){
		return robotPilot.getDefaultTurningSpeed();
	}
	
	public double getDefaultMovingSpeed(){
		return robotPilot.getDefaultMovingSpeed();
	}


	public void setPose(double orientation, int x, int y) {
		robotPilot.setPose(orientation, x, y);
	}


	public void startExplore() {
		robotPilot.startExplore();
		
	}
	
	public boolean detectBlackLine(){
		return robotPilot.detectBlackLine();
	}
	
	public void setHighSpeed(){
		setMovingSpeed(getDefaultMovingSpeed()*2);
		setTurningSpeed(getDefaultTurningSpeed()*2);
		//TODO checken of x2 snel genoeg/ te snel is
	}
	
	public void setLowSpeed(){
		setMovingSpeed(getDefaultMovingSpeed()/2);
		setTurningSpeed(getDefaultTurningSpeed()/2);
	}
	
	public void setFinish(){
		this.finish = getPosition();
		robotPilot.setFinish();
	}

	public void findBlackLine() {
		robotPilot.findBlackLine();
	}

	public void scanBarcode() {
		robotPilot.scanBarcode();
	}

	public void playTune() {
		robotPilot.playSong();
	}

	public void setCheckpoint() {
		robotPilot.setCheckpoint();
	}

	public void resumeExplore() {
		robotPilot.resumeExplore();
	}

	public void driveToFinish() {
		robotPilot.driveToFinish();
	}

	public void wait5Seconds() {
		robotPilot.wait5Seconds();
	}

	public void autoCalibrateLight() {
		robotPilot.autoCalibrateLight();
	}
	
	public MazePath getPathToFinish(){
		return robotPilot.getPathToFinish();
	}
	
	public int getNumber(){
		return number;
	}
	
	public void fetchBall(){
		robotPilot.fetchBall();
	}
	
	public void doNothing(){
		robotPilot.doNothing();
	}
	
	public boolean hasBall(){
		return (robotPilot.getBall() != null);
	}
	
	public void setFoundBall(){
		foundBall = true;
	}
	
	public boolean getFoundBall(){
		return foundBall;
	}
		
	
	public Ball getBall(){
		return robotPilot.getBall();
	}

	


	
	
}
