package domain.robotFunctions;
import domain.Position.Position;
import domain.robots.Robot;

/**
 * A class that contains functions for straightening a robot (real or simulated).
 * @author Joren
 */
public class Straightener extends RobotFunction {
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
	//Starting coordinates of the line
	private double startX, startY;
	//End coordinates of the line
	private double endX, endY;
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
		
	}
	
	/**
	 * The straightening method when the real linewidth is not known
	 */
	private void straightenUnknown(){
		//Just so the robot drives to the other end of the line.
		//We save the linewidth for later use.
		lineWidth=findLineWidth();
		//Reset the "starting position" to the current postion.
		startX=robot.getPosition().getX();
		startY=robot.getPosition().getY();
		moveAndTurn();
		double topX=robot.getPosition().getX();
		double topY=robot.getPosition().getY();
		toggle(forward);
		//Find the edge of the line again
		goUntilRising();
		endX=robot.getPosition().getX();
		endY=robot.getPosition().getY();
		double edgeA = new Position(startX,startY).getDistance(new Position(endX,endY));
		double edgeB = new Position(topX,topY).getDistance(new Position(endX,endY));
		double fixedEdge = new Position(startX,startY).getDistance(new Position(topX,topY));
		//beta=arccos((a²+c²-b²)/(2ac))
		double betaAngle = Math.acos((Math.pow(fixedEdge,2)+Math.pow(edgeA,2)-Math.pow(edgeB,2))/(2*edgeA*fixedEdge));
		//TODO finish this method
		
		
		
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
		startX=robot.getPosition().getX();
		startY=robot.getPosition().getY();
		previousAngle=robot.getOrientation();
		
		goUntilFalling();
		endX=robot.getPosition().getX();
		endY=robot.getPosition().getY();
		nextAngle=robot.getOrientation();
		return new Position(startX,startY).getDistance(new Position(endX,endY));
	}

	private void goUntilFalling() {
		// TODO Auto-generated method stub
		
	}

	private void goUntilRising() {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
