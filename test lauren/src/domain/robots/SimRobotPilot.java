package domain.robots;

import gui.ContentPanel;

import java.io.File;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import peno.htttp.PlayerType;

import lejos.nxt.addon.AngleSensor;

import domain.Position.Position;
import domain.maze.Board;
import domain.maze.Orientation;
import domain.maze.Wall;
import domain.maze.graph.MazePath;
import domain.maze.infrared.RayDetector;
import domain.robotFunctions.BarcodeGenerator;
import domain.robotFunctions.ExploreMaze;
import domain.robotFunctions.Straightener;
import domain.util.Angles;
import domain.util.TimeStamp;


public class SimRobotPilot extends RobotPilot {
	private static final double DISTANCE_BETWEEN_SENSOR_AND_WHEELS = 9;
	private static final double BARCODE_LENGTH = 8;
	private MoveThread moveThread;
	private double orientation; // Degrees to horizontal
	private Position position;
	private boolean isScanningBarcode;
	private boolean turningError = true;
	//De onderstaande variable wordt momenteel niet gebruikt maar is goed voor debuggen
//	private final InfraredBeamer infraBeamer;
	private final RayDetector infraDetector;

	private final PlayerType TYPE = PlayerType.VIRTUAL;
	

	//The wanted rotation Speed of the robot.
	private double rotateSpeed;

	//The wanted travel Speed of the robot.
	private double travelSpeed;
	private TurnThread turnThread;

	private int sensorAngle;

	private final int defaultMovingSpeed=20; //was 80
	private final int defaultTurningSpeed=100;
	
	private double lastDistance = 0;

	/**
	 * Assenstelsel wordt geinitialiseerd met oorsprong waar de robot begint
	 */
	public SimRobotPilot(String playerID){
		this(0, new Position(20,20),playerID);
	}
	
	@Override
	public PlayerType getPlayerType(){
		return this.TYPE;
	}
	
	
	
	@Override
	public ExploreMaze getMaze(){
		return this.maze;
	}
	
	public SimRobotPilot(double orientation, Position position, String playerID){
		super(playerID);
		setOrientation(orientation);
		this.position=position;
		this.setMovingSpeed(defaultMovingSpeed);
		this.setTurningSpeed(defaultTurningSpeed);
		this.sensorAngle = 0;
		setBoard(new Board());
//		this.infraBeamer= new RobotIBeamer(this);
		this.infraDetector=new RayDetector(this);
	}
	
	@Override
	public double readLongestUltrasonicValue(int extraAngle){
		return readUltrasonicValue();
	};
	

	void setOrientation(double orientation) {
		if(Math.abs(orientation)>180){
			throw new IllegalArgumentException();
		}
		this.orientation=orientation;
	}
	@Override
	public Position getPosition(){
		return position;
	}

	@Override
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
		startTurnThread(MoveType.TURNLEFT);
	}

	public void keepTurningRight(){
		stop();
		startTurnThread(MoveType.TURNRIGHT);
	}


	@Override
	public double getOrientation() {
		return orientation;
	}
	@Override
	public void UpdateUntil(TimeStamp timestamp) {
		
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
		setMovement(MoveType.FORWARD);
		forward(false);
	}

	public void forward(boolean whiteLine) throws CannotMoveException {
		stop();
		startMoveThread(MoveType.FORWARD, whiteLine);
	}

	private void startMoveThread(MoveType movement) throws CannotMoveException {
		startMoveThread(movement, false);
	}

	private void startMoveThread(MoveType movement, boolean whiteLine) throws CannotMoveException{
		stopThread(moveThread);
		moveThread= new MoveThread(movement, this,whiteLine);
		try{
			moveThread.start();
		}
		catch(RuntimeMoveException e){
			throw new CannotMoveException();
		}
	}

	private void startTurnThread(MoveType moveType) {
		stopThread(turnThread);
		turnThread= new TurnThread(moveType, this);
		turnThread.start();
	}

	@Override
	public void backward(){
		setMovement(MoveType.BACKWARD);
		stop();
		try {
			startMoveThread(MoveType.BACKWARD);
		} catch (CannotMoveException e) {
			
		}

	}

	@Override
	public void stop() {
		setMovement(MoveType.STOPPED);
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
		move(wantedDistance,false);
	}

	public synchronized void move(double wantedDistance, boolean ignoreBarcodes)
			throws CannotMoveException {
		if (wantedDistance > 0)
			setMovement(MoveType.FORWARD);
		else if (wantedDistance < 0)
			setMovement(MoveType.BACKWARD);
		Position pos1 = getPosition().clone();
		boolean running = true;
		if (wantedDistance > 0) {
			if (wantedDistance < 1) {
				this.getPosition().move(getOrientation(), wantedDistance);
				try {
					// deze gaf altijd 1 waardoor robot over barcode vloog (de
					// 50 was 1)
					double offset = 50;
					if (getMovingSpeed() > getDefaultMovingSpeed()) {
						offset = 25;
					} else if (getMovingSpeed() < getDefaultMovingSpeed()) {
						offset = 75;
					}
					Thread.sleep(Double.valueOf(
							wantedDistance / getMovingSpeed()).intValue() + 50);
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
		double turnSpeed = getTurningSpeed();
		double moveSpeed = getMovingSpeed();
		while (running && !Thread.interrupted()) {

			double currDistance = getPosition().getDistance(pos1);
			if (detectBlackLine()) {
				if (!isScanningBarcode  &&!ignoreBarcodes) {
					Position pos = getPosition().getNewPosition(
							getOrientation(),
							DISTANCE_BETWEEN_SENSOR_AND_WHEELS);
					if (!getFoundBoard().detectBarcodeAt(pos)) {

						isScanningBarcode = true;
						BarcodeGenerator bg = new BarcodeGenerator(this);
						try {
							bg.generateBarcode();
							// move(-DISTANCE_BETWEEN_SENSOR_AND_WHEELS);
							// //Francis heeft deze code gecomment omdat die
							// nergens nuttig voor lijkt te zijn en problemen
							// veroorzaakt
							isScanningBarcode = false;
						} catch (IllegalArgumentException e) {
							ContentPanel.setActionLabel("Could not read barcode, trying again");
							setMovingSpeed(moveSpeed);
							setTurningSpeed(turnSpeed);
							move(-40);
							// turn(180);
							findWhiteLine();
							straighten();
							// turn(180);
							isScanningBarcode = false;
						}
					} else if (getFoundBoard().getBarcodeAt(pos)!=null &&getFoundBoard().getBarcodeAt(pos).isSeesawBC()&&wantedDistance>0) {
						if (getFoundBoard().getBarcodeAt(pos).sameFirstReadOrientation(this.getOrientation())) {
							isScanningBarcode = true;

							stop();
							System.out.println("Going to run already known seesaw Action");
							getFoundBoard().getBarcodeAt(pos).runAction(this);
							isScanningBarcode = false;
						} else {
							System.out.println("Not same firstReadOrientation");
						}

					}
					forward();
				}
			}
			if(currDistance>=Math.abs(wantedDistance)  || !canMove()){
				running= false;
				stopThread(moveThread);
			}
		}
		setMovement(MoveType.STOPPED);
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

	@Override
	public boolean canMove(){
		if(moveThread.getMovement().equals(MoveType.BACKWARD)){
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

	double calcNewOrientation(double turnAmount) {
		double newOrientation = Angles.moderateTo180(getOrientation()+turnAmount);
		
		return newOrientation;
	}



	
	private boolean isTouching() {
		if(moveThread == null || moveThread.getMovement().equals(MoveType.FORWARD)){
			return getWorldSimulator().detectWallAt(getPosition().getNewPosition(getOrientation(), 14));
		}
		else{
			return getWorldSimulator().detectWallAt(getPosition().getNewPosition(getOrientation()+180, 5));
		}

	}

	@Override
	public double readLightValue() {
		final double WHITE = 100;
		final double WOOD = 0;
		final double BLACK = -200;
		return WOOD+detectWhiteLineGradient()*WHITE+detectBlackLineGradient()*BLACK+randomDouble(10);
	}
	

	@Override
	public double readUltrasonicValue() {
		final double MAX_VALUE = 255;
		double min_value = lastDistance<15?0:lastDistance-15;
		double shortestDistance = MAX_VALUE;
		boolean foundWall = false;
		for(int i = 1; i<MAX_VALUE; i++){
			for(int j = -15; j<15; j++){
				Position pos = getPosition().getNewPosition(getOrientation()+ getSensorAngle()+j, i);
				foundWall = getWorldSimulator().detectWallAt(pos);
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
		return getWorldSimulator().detectWhiteLineAt(getPosition().getNewPosition(getOrientation(), DISTANCE_BETWEEN_SENSOR_AND_WHEELS));
	}
	
	public double detectWhiteLineGradient() {
		Position checkPosition = getPosition().getNewPosition(getOrientation(), DISTANCE_BETWEEN_SENSOR_AND_WHEELS);
		double aggregate = 0;
		boolean center = getWorldSimulator().detectWhiteLineAt(checkPosition);
		for(domain.maze.Orientation o: domain.maze.Orientation.values()){
			boolean out = getWorldSimulator().detectWhiteLineAt(new Position(checkPosition.getX()+o.getXValue()*0.5,checkPosition.getY()+o.getYValue()*0.5));
			if(center==out){
				aggregate+=(center?0.25:0);
			} else {
				boolean outCenter = getWorldSimulator().detectWhiteLineAt(new Position(checkPosition.getX()+o.getXValue()*0.25,checkPosition.getY()+o.getYValue()*0.25));
				aggregate+=0.25*((center?0.5:0)+(outCenter?0.25:0)+(out?0.25:0));
			}
		}
		return aggregate;
	}
	
	public double detectBlackLineGradient() {
		Position checkPosition = getPosition().getNewPosition(getOrientation(), DISTANCE_BETWEEN_SENSOR_AND_WHEELS);
		double aggregate = 0;
		boolean center = getWorldSimulator().detectBlackLineAt(checkPosition);
		for(domain.maze.Orientation o: domain.maze.Orientation.values()){
			boolean out = getWorldSimulator().detectBlackLineAt(new Position(checkPosition.getX()+o.getXValue()*0.5,checkPosition.getY()+o.getYValue()*0.5));
			if(center==out){
				aggregate+=(center?0.25:0);
			} else {
				boolean outCenter = getWorldSimulator().detectBlackLineAt(new Position(checkPosition.getX()+o.getXValue()*0.25,checkPosition.getY()+o.getYValue()*0.25));
				aggregate+=0.25*((center?0.5:0)+(outCenter?0.25:0)+(out?0.25:0));
			}
		}
		return aggregate;
	}


	@Override
	public void steer(double angle) {
		try {
			startMoveThread(MoveType.FORWARD);
		} catch (CannotMoveException e) {
			e.printStackTrace();
		}
		turn(angle);

	}

	@Override
	public void straighten() {
		(new Straightener(this)).straighten(0, true);

	}

	@Override
	public void keepTurning(boolean left) {
		MoveType moveType= left?MoveType.TURNLEFT:MoveType.TURNRIGHT;
		startTurnThread(moveType);
	}

	@Override
	public void findWhiteLine(){
		int wantedDetections = 1;
		int detections = 0;
		try {
			forward(true);
		} catch (CannotMoveException e1) {
			e1.printStackTrace();
		}
		while(detections<wantedDetections){
			if(detectWhiteLine()) detections++;
		}
		//We move 1 cm because otherwise we are standing in the beginnen and not the middle of the white line 
		try {
			move(1);
		} catch (CannotMoveException e) {
			e.printStackTrace();
		}
	}

	public void fixWall() {
		turnSensorLeft();
		double leftValue = readUltrasonicValue();
		turnSensorRight();
		double rightValue = readUltrasonicValue();
		turnSensorForward();
		if(leftValue < rightValue)
			turn(90);
	}

	@Override
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
	public void addFoundWall(Wall wall){
		getFoundBoard().addWall(wall);
	}

	@Override
	public boolean detectBlackLine() {
		return getWorldSimulator().detectBlackLineAt(getPosition().getNewPosition(getOrientation(), DISTANCE_BETWEEN_SENSOR_AND_WHEELS));
	}
	@Override
	public void scanBarcode() {
		BarcodeGenerator bg = new BarcodeGenerator(this);
		bg.generateBarcode();
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
	public void setDriveToFinishSpeed() {
		setMovingSpeed(150);
	}
	@Override
	public void fetchBall() {
		maze.setNextTileToDeadEnd();
		try {
			move(40);
		} catch (CannotMoveException e) {
			e.printStackTrace();
		}
		setBall(getWorldSimulator().removeBall(getPosition()));
		try {
			move(-40);
		} catch (CannotMoveException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void driveOverSeeSaw(int barcodeNb) {
		try {
			blackStraighten();
//			addSeesawBarcodePositions(); //This is to avoid detecting a barcode when driving on a seesaw // WERKT niet
			move(65,true);

			getWorldSimulator().rollSeeSawWithBarcode(barcodeNb);

			move(55,true);
			blackStraighten();
			move(-6,true);
			//TODO values of infrared needs to be taken into consideration !
		} catch (CannotMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Override
	public void blackStraighten() {
		(new Straightener(this)).straighten(0, false);
	}

	@Override
	public int getInfraredValue() {
//			return getWorldSimulator().detectRobotFrom(this) || getWorldSimulator().checkForOpenSeesawFrom(this)?70:0;
	return infraDetector.getInfraredValue();
	}

	@Override
	public void turnUltrasonicSensorTo(int angle) {
		setSensorAngle(angle);
		
	}



	@Override
	public void snapPoseToTileMid() {
		//This method is not used for the simrobot
	}
	
	
	
	


}
