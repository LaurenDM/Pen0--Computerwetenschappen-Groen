package Robots;


public class SimRobot implements Robot {
	
	public double orientation; // Degrees to horizontal
	public Position position; 
	
	/**
	 * Assenstelsel wordt geinitialiseerd met oorsprong waar de robot begint
	 */
	public SimRobot(){
		this(0, new Position(0,0));
	}

	public SimRobot(double orientation){
		this(orientation, new Position(0,0));
	}
	
	public SimRobot(double orientation, Position position){
		setOrientation(orientation);
		setPosition(position);
	}
	
	public SimRobot(double orientation, double x, double y){
		this(orientation, new Position(x,y));
	}
	
	public Position getPosition(){
		return position;
	}
	
	public double getOrientation(){
		return orientation;
	}
	
	public void setPosition(Position position){
		this.position = position;
	}
	
	public void setOrientation(double orientation){
		this.orientation = orientation;
	}
	
	@Override
	public void forward() {
		
	}

	@Override
	public void backward() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void move(double distance) {
		getPosition().move(orientation, distance);
	}

	@Override
	public void turn(double amount) {
		setOrientation(getOrientation()+amount);
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
		// TODO Auto-generated method stub
	}

	@Override
	public void setTurningSpeed(double speed) {
		// TODO Auto-generated method stub
	}

}
