package domain.robotFunctions;
import domain.Position.Position;
import domain.robots.BTRobotPilot;
import domain.robots.CannotMoveException;
import domain.robots.Robot;

/**
 * A class that contains functions for straightening a robot (real or simulated).
 * @author Joren
 */
public class Straightener extends RobotFunction {
	//TODO: hier de echte lengte van de robot van maken
	private static final int DISTANCE_BETWEEN_SENSOR_AND_WHEELS = 8;
	//The robot that needs to be straightened
	private Robot robot;
	//The actual width of the line 
	private double realLineWidth;
	//Whether we know the real linewidth
	private boolean knowRealLineWidth;
	//The most recent measurement of the linewidth
	private double lineWidth;
	//The previous measurement of the linewidth
	private double previousLineWidth;
	//The current angle (reference point is not really important)
	private double angle;
	//Angle before and after adjustment
	private double previousAngle, nextAngle;
	//Whether the algorithm decreases or increases the angle
	private boolean angleDecrease = true;
	//Whether the robot has to drive forward or backward to cross the line
	private boolean forward = true;
	//The precision of the linewidth measurements
	private double precision = 0.01;
	
	/**
	 * Create a straightener for the given robot.
	 * Use this constructor if the real linewidth is NOT known.
	 * @param robot
	 */
	public Straightener(Robot robot){
		this.robot = robot;
		this.realLineWidth = 0d;
		this.knowRealLineWidth = false;
	}
	
	/**
	 * Create a straightener for the given robot.
	 * Use this constructor if the real linewidth IS known.
	 * @param robot
	 * @param realLineWidth
	 */
	public Straightener(Robot robot, double realLineWidth){
		this.robot = robot;
		this.realLineWidth = realLineWidth;
		this.knowRealLineWidth = true;
	}
	
	/**
	 * Position the robot perpendicular to a straight line using the light sensor.
	 * If the real linewidth is known it just calculates the angle that the robot needs to turn to straighten itself and then
	 * moves it into position.
	 * If the linewidth isn't known it crosses the line, turns and checks if that decreases the 
	 * measured linewidth. If not it turns in the opposite direction and checks again.
	 */
	public void straighten(){
		if(knowRealLineWidth){
			straightenKnown();
		}
		else{
			straightenUnknown();
		}
	}
	
	/**
	 * The straightening method when the real linewidth is known
	 */
	private void straightenKnown(){
		robot.setMovingSpeed(0.1);
		while(robot.detectWhiteLine()){
			robot.backward();
		}
		robot.stop();
		Position pos1 = robot.getPosition();
		try {
			robot.forward();
		} catch (CannotMoveException e) {
			//TODO: mogen we ervan uitgaan dat bij het rechtzetten geen muren in de buurt zijn?
		}
		while(robot.detectWhiteLine()){
			try {
				robot.forward();
			} catch (CannotMoveException e) {
				
			}
		}
		robot.stop();
		Position pos2 = robot.getPosition();
		double l = pos1.getDistance(pos2);
		double alpha = Math.acos(lineWidth/l)*180/Math.PI;
		
		robot.turn(90 - alpha); //TODO: robot moet naar rechts of links draaien? hoe bepalen?
		try {
			robot.move(-lineWidth/2);
		} catch (CannotMoveException e) {
			
		}
		robot.setMovingSpeed(2); //TODO: wat is standaardsnelheid robot?
	}
	
	/**
	 * The straightening method when the real linewidth is not known
	 */
	private void straightenUnknown(){
		//Just so the robot drives to the other end of the line.
		//We save the linewidth for later use.
		lineWidth=findLineWidth();
		//Reset the "starting position" to the current postion.
		Position startpos = robot.getPosition();
		moveAndTurn();
		Position toppos = robot.getPosition();
		toggle(forward);
		//Find the edge of the line again
		goUntilRising();
		Position endpos = robot.getPosition();
		double edgeA = startpos.getDistance(endpos);
		double edgeB = toppos.getDistance(endpos);
		double fixedEdge = startpos.getDistance(toppos);
		//beta=arccos((a^2+c^2-b^2)/(2ac))
		double betaAngle = Math.acos((Math.pow(fixedEdge,2)+Math.pow(edgeA,2)-Math.pow(edgeB,2))/(2*edgeA*fixedEdge));
		//TODO finish this method
		
		
		
	}
	
	public void straightenNew(){
		boolean rightOrLeft = true;
		boolean detect = true;
		boolean wood = false;
		try {
			robot.move(DISTANCE_BETWEEN_SENSOR_AND_WHEELS);
		} catch (CannotMoveException e) {
			try { robot.move(-DISTANCE_BETWEEN_SENSOR_AND_WHEELS/2); } catch (CannotMoveException e1) {}
			turnUntil(detect, rightOrLeft);
			try {
				robot.move(DISTANCE_BETWEEN_SENSOR_AND_WHEELS);
			} catch (CannotMoveException e1) {
				//I don't really know if this is possible
				System.out.println("Apparently you were wrong.");
			}
		}
		turnUntil(detect, rightOrLeft);
		//Now the robot is aligned with the line
		robot.turn(-105);
		robot.setMovingSpeed(15);
		robot.setTurningSpeed(10);
		
	}
	
	private void turnUntil(boolean detect, boolean rightOrLeft) {
		robot.setTurningSpeed(5);
		int consecutiveDetections = 0;
		boolean onLine = false;
		while(consecutiveDetections < 5){
			System.out.println("test");
			robot.turn(1);
			if(robot.detectWhiteLine()){
				if(onLine){
					consecutiveDetections++;
				} else {
					onLine=true;
				}
			} else {
				onLine=false;
				consecutiveDetections=0;
			}
		}
	}

	private void turnUntilWood(boolean right) {
		while(robot.detectWhiteLine()){
			robot.turnRight();
		}
	}

	/**
	 * Toggles the given boolean.
	 */
	private void toggle(boolean bool) {
		bool=!bool;
	}
	
	/**
	 * Move in the forward/backward directions
	 */
	private void moveAndTurn() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Drive until the beginning of the line is reached. Record the coordinates. Then drive until the end is reached.
	 * Again record the coordinates. The return value is the distance between these 2 points.
	 * @param robot
	 */
	private double findLineWidth() {
		goUntilRising();
		Position startPos = robot.getPosition();
		previousAngle=robot.getOrientation();
		
		goUntilFalling();
		Position endPos = robot.getPosition();
		nextAngle=robot.getOrientation();
		return startPos.getDistance(endPos);
	}

	private void goUntilFalling() {
		// TODO Auto-generated method stub
		
	}

	private void goUntilRising() {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
