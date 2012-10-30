package domain.robots;

import domain.Position.Position;
import domain.maze.Board;
import domain.util.TimeStamp;


public class SimRobotPilot implements RobotPilot {
	private MoveThread moveThread;
	private double orientation; // Degrees to horizontal
	private Position position;

	//The wanted rotation Speed of the robot.
		private double rotateSpeed;

		//The wanted travel Speed of the robot.
		private double travelSpeed;
		private TurnThread turnThread;
		
		private Board board;
		
		private int sensorAngle;

	
	/**
	 * Assenstelsel wordt geinitialiseerd met oorsprong waar de robot begint
	 */
	public SimRobotPilot(){
		this(0, new Position(0,0));
	}
	
	public SimRobotPilot(double orientation, Position position){
		setOrientation(orientation);
		this.position=position;
		this.setMovingSpeed(30);
		this.setTurningSpeed(90);
		this.sensorAngle = (int) orientation;
		board = new Board();
	}
	
	private void setOrientation(double orientation) {
		if(Math.abs(orientation)>180){
			throw new IllegalArgumentException();
		}
		this.orientation=orientation;
	}
		@Override
	public Position getPosition(){
		return position;
	}
		
	public int getSensorAngle(){
		return sensorAngle;
	}
	
	public void setSensorAngle(int angle){
		this.sensorAngle = angle;
	}
	
	@Override
	public void turn(double wantedAngleDif) {
		double previousAngle = getOrientation();
		boolean turning = true;
		//The turningMethod always turns 1 degree, that's why we first turn the non integer part of WantedAngleDIf.
		double intDoubleDif=wantedAngleDif - (int)wantedAngleDif;
		turnNonIntegerPart(intDoubleDif);
		
		//Now we start turning the integer part of wantedAngleDif
		double totalAngleDif=intDoubleDif;
		if(wantedAngleDif>0){
			keepTurningRight();
		}
		else{
			keepTurningLeft();
		}
		while(turning){
			double currAngle=getOrientation();
			//The Math.min is needed for when degrees go from 180 to -179
			double latestAngleDif=Math.min(Math.abs(previousAngle-currAngle), Math.abs(previousAngle+currAngle));
			totalAngleDif+=latestAngleDif;
			if( totalAngleDif>= Math.abs(wantedAngleDif) || !canMove()){
				turning= false;
				stopThread(turnThread);
			}
			previousAngle=currAngle;
		}
	}
	
	/**
	 * This method turns a degree between -1 and 1 and makes the thread sleep the right amount of time.
	 * @param intDoubleDif
	 */
	public void turnNonIntegerPart(double intDoubleDif) {
		if(intDoubleDif>=1||intDoubleDif<=-1){
		throw new IllegalArgumentException();
		}
		setOrientation(calcNewOrientation(intDoubleDif));
		long correctionSleeptime=Math.abs(Math.round(intDoubleDif/getTurningSpeed()/1000.0));
		try {
			Thread.sleep(correctionSleeptime);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}

	public void keepTurningLeft(){
		startTurnThread(true);
	}
	
	public void keepTurningRight(){
		startTurnThread(false);
	}


	@Override
	public double getOrientation() {
		return orientation;
	}
	@Override
	public void UpdateUntil(TimeStamp timestamp) {
		//TODO
	}


	@Override
	public double getMovingSpeed() {
		return travelSpeed;
	}

	@Override
	public double getTurningSpeed() {
		return rotateSpeed;
	}

	@Override
	public void forward() throws CannotMoveException {
		startMoveThread(Movement.FORWARD);
	}

	public void startMoveThread(Movement movement) {
		stop();
		moveThread= new MoveThread(movement, this);
		moveThread.start();
	}
	
	public void startTurnThread(boolean left) {
		stop();
		turnThread= new TurnThread(left, this);
		turnThread.start();
	}

	@Override
	public void backward(){
		startMoveThread(Movement.BACKWARD);

	}

	@Override
	public void stop() {
		if (moveThread != null) {
			moveThread.interrupt();
		}
		if (turnThread != null) {
			turnThread.interrupt();
		}
	}
	
	public void stopThread(Thread thread) {
		if (thread != null) {
			thread.interrupt();
		}
	}
	
	@Override
	public void move(double wantedDistance) throws CannotMoveException {
		Position pos1 = getPosition().clone();
		boolean running = true;
		if (wantedDistance > 0) {
			forward();
		} else {
			backward();
		}
		while(running){
			double currDistance=getPosition().getDistance(pos1);
			if(currDistance>=Math.abs(wantedDistance)  || !canMove()){
				running= false;
				stopThread(moveThread);
			}
		}
	}

	@Override
	public void turnRight() {
		turn(90);
	}

	@Override
	public void turnLeft() {
		turn(-90);
	}

	@Override
	public void setMovingSpeed(double speed) {
		this.travelSpeed = speed;
	}

	@Override
	public void setTurningSpeed(double speed) {
		this.rotateSpeed = speed;
	}

	public boolean canMove(){
		double distance = readUltrasonicValue();	
		int testDistance = 10; 
		if(distance < testDistance || isTouching())
			return false;
		else
			return true;
	}
	
	private double calcNewOrientation(double turnAmount) {
			double newOrientation = getOrientation()+turnAmount;
			while (newOrientation < -179) {
				newOrientation += 360;
			}
			while (newOrientation > 180) {
				newOrientation -= 360;
			}
			return newOrientation;
		}
	
	private class TurnThread extends Thread{
		private boolean left;
		private SimRobotPilot simRobotPilot;
		public TurnThread(boolean left, SimRobotPilot simRobotPilot){
			this.simRobotPilot=simRobotPilot;
			this.left=left;
		}

		@Override
		public void run() {
			double speed = simRobotPilot.getMovingSpeed();
			double turnAmount = left ? -1 : 1;
			int sleepTime = Math.abs((int) Math.round( (1000 * turnAmount / speed)));
			while (true) {
				double newOrientation = calcNewOrientation(turnAmount);
				setOrientation(newOrientation);
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					break;
				}
			}
		}

		

	}

	@Override
	public boolean isTouching() {
		return board.detectWallAt(getPosition());
	}

	@Override
	public double readLightValue() {
		final double HIGH = 80;
		final double LOW = 40;
		if(board.detectLineAt(getPosition())) return HIGH;
		else return LOW;
	}

	@Override
	public double readUltrasonicValue() {
		final int MAX_REACH = 50;
		final double MAX_VALUE = 100;
		boolean foundWall = false;
		for(int i = 0; i<MAX_REACH; i++){
			foundWall = board.detectWallAt(getPosition().getNewPosition(getOrientation(), i));
			if(foundWall){
				return getPosition().getDistance(getPosition().getNewPosition(getOrientation(),i));
			}
		}
		return MAX_VALUE;
	}

	@Override
	public void calibrateLightHigh() {
		//doet niets
	}

	@Override
	public void calibrateLightLow() {
		// doet niets
		
	}

	@Override
	public void turnUltrasonicSensor(int angle) {
		int newAngle = getSensorAngle() + angle;
		while(newAngle > 180) newAngle = newAngle - 360;
		while(newAngle < -179) newAngle = newAngle + 360;
		setSensorAngle(newAngle);
	}

	@Override
	public void turnSensorRight() {
		int newAngle = (int) getOrientation() + 90;
		while(newAngle > 180) newAngle = newAngle - 360;
		while(newAngle < -179) newAngle = newAngle + 360;
		setSensorAngle(newAngle);
		//TODO: checken of +90 effectief rechts is
	}

	@Override
	public void turnSensorLeft() {
		int newAngle = (int) getOrientation() - 90;
		while(newAngle > 180) newAngle = newAngle - 360;
		while(newAngle < -179) newAngle = newAngle + 360;
		setSensorAngle(newAngle);
		//TODO: idem 
	}

	@Override
	public void turnSensorForward() {
		setSensorAngle((int) getOrientation());
	}

	@Override
	public boolean detectWhiteLine() {
		return board.detectLineAt(getPosition());
	}

	@Override
	public void makeWallIfNecessary() {
		//do nothing
	}
	
	
	
	

}
