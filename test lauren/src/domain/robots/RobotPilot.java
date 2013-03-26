package domain.robots;

import java.util.List;

import domain.Position.InitialPosition;
import domain.Position.Position;
import domain.maze.Ball;
import domain.maze.Board;
import domain.maze.Seesaw;
import domain.maze.Wall;
import domain.maze.graph.MazePath;
import domain.polygons.RobotPolygon;
import domain.robotFunctions.ExploreMaze;
import domain.util.TimeStamp;

public abstract class RobotPilot {
	
	RobotPolygon robotPolygon;
	private MoveType movement;
	private Position finish;
	private int number; //0-3
	
	private Board board;
	
	private Ball ball;
	private int teamNumber;
	
	
	public RobotPilot(int number){
		this.movement=MoveType.STOPPED;
		this.robotPolygon=new RobotPolygon(this);
		this.number = number;
	}
	
	public void setPlayerNb(int nb){
		this.number = nb;
	}
	
	public RobotPolygon getRobotPolygon(){
		return robotPolygon;
	}
	
	public abstract void UpdateUntil(TimeStamp timestamp);
	
	public int getNumber(){
		return number;
	}
	
	public MoveType getMovementStatus() {
		return movement;
	}
		
	public abstract double getMovingSpeed();
	
	public abstract double getTurningSpeed();
	
	public double getActualMovingSpeed() {
		return getMovingSpeed()*movement.getSpeedMultiplier();
	}
	
	/**
	 * Can be used to get a clone of the current position of the robot
	 * @return a clone of the position
	 */
	public abstract Position getPosition();
	
	public void setBoard(Board board){
		this.board = board;
	}
	
	public Board getBoard(){
		return this.board;
	}
	
	public abstract void forward() throws CannotMoveException;
	
	public void setMovement(MoveType movement){
		this.movement = movement;
	}
	
	public abstract void backward();
	
	public abstract void stop();

	/**
	 * 
	 * @param distance in cm
	 */
	public abstract void move(double distance) throws CannotMoveException;
	
	/**
	 * 
	 * @param amount in degrees
	 */
	public abstract void turn(double amount);
	
	public abstract void turnRight();
	
	public abstract void turnLeft();
	
	public abstract int getSensorAngle();
	
	
	/**
	 * returns the current Orientation relative to the original orientation.
	 */
	public abstract double getOrientation();
	/**
	 * 
	 * @param speed in cm/s
	 */
	public abstract void setMovingSpeed(double speed);
	
	public void resetToDefaultSpeeds(){
		setMovingSpeed(getDefaultMovingSpeed());
		setTurningSpeed(getDefaultTurningSpeed());
	}
	
	public void setHighSpeed(){
		setMovingSpeed(getDefaultMovingSpeed()*2);
		setTurningSpeed(getDefaultTurningSpeed()*2);
	}
	
	public void setLowSpeed(){
		setMovingSpeed(getDefaultMovingSpeed()/2);
		setTurningSpeed(getDefaultTurningSpeed()/2);
	}
	
	/**
	 * 
	 * @param speed in degrees/s
	 */
	public abstract void setTurningSpeed(double speed);
	
	//Method for testing whether the robot can move with takin into account the walls and otherobstructions.
	public abstract boolean canMove();
	
//	public abstract boolean isTouching();
	
	public abstract double readLightValue();
	
	public abstract double readUltrasonicValue();
	
	public abstract void calibrateLightHigh();
	
	public abstract void calibrateLightLow();
	
	public abstract void turnUltrasonicSensor(int angle);
	
	public abstract void turnSensorRight();
	
	public abstract void turnSensorLeft();
	
	public abstract void turnSensorForward();
	
	public abstract boolean detectWhiteLine();
	public abstract void blackStraighten();

	public abstract void straighten();
	//Makes the robot make an arc of the specified angle. This method does not return immediately.
	public abstract void steer(double angle);

	public abstract void keepTurning(boolean left);

	public abstract void findWhiteLine();

	public abstract void interrupt();

	public abstract double getDefaultMovingSpeed();

	public abstract double getDefaultTurningSpeed();
	
	public abstract void playSong();

	public abstract void setPose(double orientation, int x, int y);

	public abstract void startExplore();
	
	public abstract void addFoundWall(Wall wall);

	public abstract boolean detectBlackLine();

	public abstract void findBlackLine();

	public abstract void scanBarcode();

	public abstract void setCheckpoint();
	
	public abstract ExploreMaze getMaze();

	public void setFinish(){
		this.finish = getPosition();
		getMaze().setCurrentTileToFinish();
	}

	public abstract void resumeExplore();

	public abstract void driveToFinish();

	public abstract void wait5Seconds();

	public abstract void autoCalibrateLight();
	
	public abstract MazePath getPathToFinish();

	public abstract void setDriveToFinishSpeed();

	public abstract void fetchBall();
	
	public abstract void doNothing();

	public Ball getBall(){
		return this.ball;
	}
	
	public void setBall(Ball ball){
		this.ball = ball;
	}
	
	public boolean hasBall(){
		return (getBall() != null);
	}

	public void setFoundBall(int number) {
		setBall(new Ball(number));
	}

	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}
	
	public int getTeamNumber(){
		return teamNumber;
	}
		
	public void handleSeesaw(int barcodeNb){
		boolean open = detectInfrared();
		getMaze().setNextTileToSeesaw(open);
		if(!open){
			driveOverSeeSaw(barcodeNb);
			getMaze().driveOverSeesaw();
		}
		else{
			doNothing();
		}
	}

	public  boolean detectInfrared(){
		return getInfraredValue()>60; //TODO infrarood francis
	};


	public abstract void driveOverSeeSaw(int barcodeNb);
	// de check van infrarood is reeds gebeurd als deze methode wordt aangeroepen!

	public abstract int getInfraredValue();

	public abstract void turnUltrasonicSensorTo(int angle);
	
	
	InitialPosition initialPosition;
	int playerNb;
	public void setInitialPositionNumber(int playernb){
		this.playerNb = playernb;
//		System.out.println("Setting initial pos to:"+initialPosition.getX()+" y:"+initialPosition.getY());
//		setPose(pos.getOrientation().getAngleToHorizontal(), (int) pos.getX(), (int) pos.getY());
	}
	
	public void teleportToStartPosition(){
		initialPosition = getBoard().getInitialPositionFromPlayer(playerNb);
		System.out.println("Setting initial pos to:"+initialPosition.getX()+" y:"+initialPosition.getY());
		setPose(initialPosition.getOrientation().getAngleToHorizontal(), (int) initialPosition.getX(), (int) initialPosition.getY());
	}
	
}
