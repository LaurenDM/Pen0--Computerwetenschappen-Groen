package domain.robots;

import domain.Position.Position;
import domain.maze.Board;
import domain.maze.Wall;
import domain.robotFunctions.ExploreMaze;
import domain.robotFunctions.Straightener;
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
		
		private final int defaultMovingSpeed=40;
		private final int defaultTurningSpeed=90;
	
	/**
	 * Assenstelsel wordt geinitialiseerd met oorsprong waar de robot begint
	 */
	public SimRobotPilot(){
		this(0, new Position(20,20));
	}
	
	public SimRobotPilot(double orientation, Position position){
		setOrientation(orientation);
		this.position=position;
		this.setMovingSpeed(defaultMovingSpeed);
		this.setTurningSpeed(defaultTurningSpeed);
		this.sensorAngle = 0;
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
		
	public void setBoard(Board board){
		this.board = board;
	}
	
	public Board getBoard(){
		return board;
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
			if( totalAngleDif>= Math.abs(wantedAngleDif) || Thread.interrupted()){
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
			
			
		}
	}

	public void keepTurningLeft(){
		stop();
		startTurnThread(true);
	}
	
	public void keepTurningRight(){
		stop();
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
		stop();
		try{
		startMoveThread(Movement.FORWARD);
		} catch(RuntimeMoveException e){
			throw new CannotMoveException();
		}
	}
	



	private void startMoveThread(Movement movement) {
		stopThread(moveThread);
		moveThread= new MoveThread(movement, this);
		moveThread.start();
	}
	
	private void startTurnThread(boolean left) {
		stopThread(turnThread);
		turnThread= new TurnThread(left, this);
		turnThread.start();
	}

	@Override
	public void backward(){
		stop();
		startMoveThread(Movement.BACKWARD);

	}

	@Override
	public void stop() {
		stopThread(moveThread);
		stopThread(turnThread);
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
		while(running && !Thread.interrupted()){
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
		if(moveThread.getMovement().equals(Movement.BACKWARD)){
			return canMoveBackward();
		}
		double distance = readUltrasonicValue();	
		int testDistance = 15; 
		if(distance < testDistance || isTouching())
			return false;
		else
			return true;
	}
	
	public boolean canMoveBackward(){
		if(isTouching())
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
			double speed = simRobotPilot.getTurningSpeed();
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
		if(moveThread == null || moveThread.getMovement().equals(Movement.FORWARD)){
			return board.detectWallAt(getPosition().getNewPosition(getOrientation(), 14));
		}
		else{
			return board.detectWallAt(getPosition().getNewPosition(getOrientation()+180, 5));
		}

	}

	@Override
	public double readLightValue() {
		final double WHITE = 100;
		final double WOOD = 0;
		final double BLACK = -100;
		if(detectWhiteLine()) return WHITE;
		else if(detectBlackLine()) return BLACK;
		else return WOOD;
	}
	//TODO: waardes hangen af van kalibratie van echte sensor

	@Override
	public double readUltrasonicValue() {
		final double MAX_VALUE = 255;
		boolean foundWall = false;
		for(int i = 0; i<MAX_VALUE; i++){
			for(int j = -30; j<30; j++){
				Position pos = getPosition().getNewPosition(getOrientation()+ getSensorAngle()+j, i);
				foundWall = board.detectWallAt(pos);
				if(foundWall){
					return getPosition().getDistance(pos);
				}
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
		setSensorAngle(90);
		}

	@Override
	public void turnSensorLeft() {
		setSensorAngle(-90);
	}

	@Override
	public void turnSensorForward() {
		setSensorAngle(0);
	}

	@Override
	public boolean detectWhiteLine() {
		return board.detectWhiteLineAt(getPosition().getNewPosition(getOrientation(), 8));
	}


	@Override
	public void arcForward(boolean left) {
		startMoveThread(Movement.FORWARD);
		startTurnThread(left);
	}

	@Override
	public void arcBackward(boolean left) {
		startMoveThread(Movement.BACKWARD);
		startTurnThread(left);
	}
	@Override
	public void steer(double angle) {
		startMoveThread(Movement.FORWARD);
		turn(angle);
		
	}

	@Override
	public void straighten() {
		(new Straightener(new Robot(this))).straighten(-3);
		
	}

	@Override
	public void keepTurning(boolean left) {
		startTurnThread(left);
	}

	@Override
	public void findWhiteLine(){
		int wantedDetections=1;
		//setMovingSpeed(2);
		boolean found=false;
		try {
			forward();
		} catch (CannotMoveException e) {
			turnRight();
		}
		int consecutiveDetections=0;
		while(consecutiveDetections<wantedDetections){
				if(detectWhiteLine()){
					consecutiveDetections++;
				}
				else consecutiveDetections=0;
				}
		stop();
		//We move 1 cm because otherwise we are standing in the beginnen and not the middle of the white line 
		try {
			move(1);
		} catch (CannotMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void findBlackLine(){
		int wantedDetections=1;
		setMovingSpeed(2);
		boolean found=false;
		try {
			forward();
		} catch (CannotMoveException e) {
			turnRight();
		}
		int consecutiveDetections=0;
		while(consecutiveDetections<wantedDetections){
				if(detectBlackLine()){
					consecutiveDetections++;
				}
				else consecutiveDetections=0;
				}
		stop();
		//We move 1 cm because otherwise we are standing in the beginnen and not the middle of the white line 
		try {
			move(1);
		} catch (CannotMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void interrupt() {
		//The simrobotPilot doesn't need this method right now.
	}

	@Override
	public double getDefaultMovingSpeed() {
		return defaultMovingSpeed;
	}

	@Override
	public double getDefaultTurningSpeed() {
		return defaultTurningSpeed;
	}

	@Override
	public void playSong(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPose(double orientation, int x, int y) {
		this.orientation=orientation;
		this.position=new Position(x,y);
	}

	@Override
	public void startExplore() {
		ExploreMaze maze = new ExploreMaze(this);
		maze.start();
	}
	
	public void addFoundWall(Wall wall){
		board.foundNewWall(wall);
	}

	@Override
	public boolean detectBlackLine() {
		return getBoard().detectBlackLineAt(getPosition().getNewPosition(getOrientation(), 8));
	}


	
	
	
	
	

}
