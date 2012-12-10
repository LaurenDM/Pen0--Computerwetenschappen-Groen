package domain.robots;

import gui.ContentPanel;

import java.io.File;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import domain.Position.Position;
import domain.barcodes.Barcode;
import domain.maze.Board;
import domain.maze.Wall;
import domain.maze.graph.MazePath;
import domain.robotFunctions.BarcodeGenerator;
import domain.robotFunctions.ExploreMaze;
import domain.robotFunctions.Straightener;
import domain.util.TimeStamp;


public class SimRobotPilot implements RobotPilot {
	private MoveThread moveThread;
	private double orientation; // Degrees to horizontal
	private Position position;
	private boolean isScanningBarcode;
	private ExploreMaze maze;
	private boolean turningError = true;

	//The wanted rotation Speed of the robot.
	private double rotateSpeed;

	//The wanted travel Speed of the robot.
	private double travelSpeed;
	private TurnThread turnThread;

	private Board board;

	private int sensorAngle;

	private final int defaultMovingSpeed=80; //was 80
	private final int defaultTurningSpeed=200;
	private Robot robot;
	
	private double lastDistance = 0;

	/**
	 * Assenstelsel wordt geinitialiseerd met oorsprong waar de robot begint
	 */
	public SimRobotPilot(){
		this(0, new Position(20,20));
	}
	@Override
	public void setRobot(Robot robot){
		this.robot = robot;
	}

	private Robot getRobot(){
		return robot;
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
		
		double error = 0;
		if(turningError){
			error = randomDouble(1);
		}
		wantedAngleDif = wantedAngleDif + error; 

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
		forward(false);
	}

	public void forward(boolean whiteLine) throws CannotMoveException {
		stop();
		startMoveThread(Movement.FORWARD, whiteLine);
	}

	private void startMoveThread(Movement movement) throws CannotMoveException {
		startMoveThread(movement, false);
	}

	private void startMoveThread(Movement movement, boolean whiteLine) throws CannotMoveException{
		stopThread(moveThread);
		moveThread= new MoveThread(movement, this,whiteLine);
		try{
			moveThread.start();
		}
		catch(RuntimeMoveException e){
			throw new CannotMoveException();
		}
	}

	private void startTurnThread(boolean left) {
		stopThread(turnThread);
		turnThread= new TurnThread(left, this);
		turnThread.start();
	}

	@Override
	public void backward(){
		stop();
		try {
			startMoveThread(Movement.BACKWARD);
		} catch (CannotMoveException e) {
			
		}

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
	public synchronized void move(double wantedDistance) throws CannotMoveException {
		Position pos1 = getPosition().clone();
		boolean running = true;
		if (wantedDistance > 0) {
			if(wantedDistance <1){
				this.getPosition().move(getOrientation(), wantedDistance);
				try {
					//deze gaf altijd 1 waardoor robot over barcode vloog (de 50 was 1)
					double offset = 50;
					if(getMovingSpeed()>getDefaultMovingSpeed()){
						offset = 25;
					}
					else if(getMovingSpeed()<getDefaultMovingSpeed()){
						offset = 75;
					}
					Thread.sleep(Double.valueOf(wantedDistance/getMovingSpeed()).intValue()+50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				running = false;
			} else {
				forward();
			}
		} else {
			backward();
		}
		double turnSpeed = robot.getTurningSpeed();
		double moveSpeed = robot.getMovingSpeedSetting();
		while(running && !Thread.interrupted()){
			double currDistance=getPosition().getDistance(pos1);
			if(detectBlackLine() && !isScanningBarcode){
				Position pos = robot.getPosition().getNewPosition(robot.getOrientation(), 8);
				if(!robot.getBoard().detectBarcodeAt(pos)){
					isScanningBarcode = true;
					BarcodeGenerator bg = new BarcodeGenerator(getRobot());
					try {
						bg.generateBarcode();
						move(-8);
					} catch(IllegalArgumentException e){
						ContentPanel.writeToDebug("Could not read barcode, trying again");
						setMovingSpeed(moveSpeed);
						setTurningSpeed(turnSpeed);
						move(-40);
						//turn(180);
						findWhiteLine();
						straighten();
						//turn(180);
						isScanningBarcode = false;
					}
					
				}
				forward();
				isScanningBarcode = false;
			}
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
		int testDistance = 10; 
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
			int sleepTime = Math.abs((int) Math.round( (500 * turnAmount / speed)));
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
		final double BLACK = -200;
		return WOOD+detectWhiteLineGradient()*WHITE+detectBlackLineGradient()*BLACK+randomDouble(10);
	}
	//TODO: waardes hangen af van kalibratie van echte sensor

	@Override
	public double readUltrasonicValue() {
		final double MAX_VALUE = 255;
		double min_value = lastDistance<15?0:lastDistance-15;
		double shortestDistance = MAX_VALUE;
		boolean foundWall = false;
		for(int i = 1; i<MAX_VALUE; i++){
			for(int j = -15; j<15; j++){
				Position pos = getPosition().getNewPosition(getOrientation()+ getSensorAngle()+j, i);
				foundWall = board.detectWallAt(pos);
				if(foundWall){
					if(getPosition().getDistance(pos)<shortestDistance){
						shortestDistance = getPosition().getDistance(pos);
					}
				}
			}
			if(foundWall) break;
		}
		lastDistance = shortestDistance;
		return shortestDistance;
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
	
	public double detectWhiteLineGradient() {
		Position checkPosition = getPosition().getNewPosition(getOrientation(), 8);
		double aggregate = 0;
		boolean center = board.detectWhiteLineAt(checkPosition);
		for(domain.maze.Orientation o: domain.maze.Orientation.values()){
			boolean out = board.detectWhiteLineAt(new Position(checkPosition.getX()+o.getXValue()*0.5,checkPosition.getY()+o.getYValue()*0.5));
			if(center==out){
				aggregate+=(center?0.25:0);
			} else {
				boolean outCenter = board.detectWhiteLineAt(new Position(checkPosition.getX()+o.getXValue()*0.25,checkPosition.getY()+o.getYValue()*0.25));
				aggregate+=0.25*((center?0.5:0)+(outCenter?0.25:0)+(out?0.25:0));
			}
		}
		return aggregate;
	}
	
	public double detectBlackLineGradient() {
		Position checkPosition = getPosition().getNewPosition(getOrientation(), 8);
		double aggregate = 0;
		boolean center = board.detectBlackLineAt(checkPosition);
		for(domain.maze.Orientation o: domain.maze.Orientation.values()){
			boolean out = board.detectBlackLineAt(new Position(checkPosition.getX()+o.getXValue()*0.5,checkPosition.getY()+o.getYValue()*0.5));
			if(center==out){
				aggregate+=(center?0.25:0);
			} else {
				boolean outCenter = board.detectBlackLineAt(new Position(checkPosition.getX()+o.getXValue()*0.25,checkPosition.getY()+o.getYValue()*0.25));
				aggregate+=0.25*((center?0.5:0)+(outCenter?0.25:0)+(out?0.25:0));
			}
		}
		return aggregate;
	}


	@Override
	public void arcForward(boolean left) {
		try {
			startMoveThread(Movement.FORWARD);
		} catch (CannotMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startTurnThread(left);
	}

	@Override
	public void arcBackward(boolean left) {
		try {
			startMoveThread(Movement.BACKWARD);
		} catch (CannotMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startTurnThread(left);
	}
	@Override
	public void steer(double angle) {
		try {
			startMoveThread(Movement.FORWARD);
		} catch (CannotMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		turn(angle);

	}

	@Override
	public void straighten() {
		(new Straightener(new Robot(this))).straighten(0);

	}

	@Override
	public void keepTurning(boolean left) {
		startTurnThread(left);
	}

	@Override
	public void findWhiteLine(){
		int wantedDetections = 1;
		int detections = 0;
		try {
			forward(true);
		} catch (CannotMoveException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(detections<wantedDetections){
			if(detectWhiteLine()) detections++;
		}
		//We move 1 cm because otherwise we are standing in the beginnen and not the middle of the white line 
		try {
			move(1);
		} catch (CannotMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void fixWall() {
		robot.turnSensorLeft();
		double leftValue = robot.readUltrasonicValue();
		robot.turnSensorRight();
		double rightValue = robot.readUltrasonicValue();
		robot.turnSensorForward();
		if(leftValue < rightValue)
			robot.turn(90);
	}

	public void findBlackLine(){
		int wantedDetections=1;
		setMovingSpeed(2);
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
	public void playSong() {
		try{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("tune.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}catch(Exception ex){
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	@Override
	public void setPose(double orientation, int x, int y) {
		this.orientation=orientation;
		this.position=new Position(x,y);
	}

	@Override
	public void startExplore() {
		maze = new ExploreMaze(this);
		maze.start();
	}

	public void addFoundWall(Wall wall){
		board.foundNewWall(wall);
	}

	@Override
	public boolean detectBlackLine() {
		return getBoard().detectBlackLineAt(getPosition().getNewPosition(getOrientation(), 8));
	}
	@Override
	public void scanBarcode() {
		BarcodeGenerator bg = new BarcodeGenerator(new Robot(this));
		bg.generateBarcode();
	}

	@Override
	public void setCheckpoint() {
		maze.setCurrentTileToCheckpoint();

	}
	@Override
	public void setFinish() {
		maze.setCurrentTileToFinish();		
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



	private double randomDouble(double max){
//		return 0;
		Random rand = new Random();
		double rd = rand.nextGaussian();
		rd = Math.abs(rd)>2.5?0:rd;
		return (rd * max);
	}
	
	@Override
	public void wait5Seconds() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void autoCalibrateLight() {
		//Do nothing
	}

	public void disableError() {
			turningError = false;
	}
	@Override
	public MazePath getPathToFinish() {
		return maze.getPathToFinish();
	}
	@Override
	public void setDriveToFinishSpeed() {
		setMovingSpeed(150);
	}


}
