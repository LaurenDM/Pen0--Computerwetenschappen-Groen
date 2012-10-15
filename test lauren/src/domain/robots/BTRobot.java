package domain.robots;
import domain.Position.Position;
import lejos.geom.Point;

import lejos.robotics.RegulatedMotor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.PilotProps;



public class BTRobot implements Robot {
	
	private DifferentialPilot pilot;
	private RegulatedMotor leftMotor;
	private RegulatedMotor rightMotor;
	
	//The poseProvider is an intantiation of a class that "LeJOS" provides for determining the pose of a NXT Robot.
	private OdometryPoseProvider poseProvider;
	//The wanted rotation Speed of the robot.
	private double rotateSpeed;

	//The wanted travel Speed of the robot.
	private double travelSpeed;
	
	//The current state of movement: forward, backward or stopped.
	private Movement movement;
	
	public void Robot() throws Exception{
		
		PilotProps pp = new PilotProps();
    	pp.loadPersistentValues();
    	float wheelDiameter = Float.parseFloat(pp.getProperty(PilotProps.KEY_WHEELDIAMETER, "5.6"));
    	float trackWidth = Float.parseFloat(pp.getProperty(PilotProps.KEY_TRACKWIDTH, "11.36"));
    	leftMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_LEFTMOTOR, "C"));
    	rightMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_RIGHTMOTOR, "B"));
    	
    	pilot = new DifferentialPilot(wheelDiameter, trackWidth, leftMotor, rightMotor);
    	poseProvider= new OdometryPoseProvider(pilot);
	}
	
	@Override
	public void move(double distance) {
		if (distance > 0)
			movement = Movement.FORWARD;
		else if (distance < 0)
			movement = Movement.BACKWARD;

		pilot.travel(distance);
		movement = Movement.STOPPED;

	}

	@Override
	public void turn(double amount) {
		pilot.rotate(amount);
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
		this.travelSpeed= speed;
		pilot.setTravelSpeed(speed);
	}

	@Override
	public void setTurningSpeed(double speed) {
		this.rotateSpeed=speed;
		pilot.setRotateSpeed(speed);
	}

	@Override
	public void forward() {
		movement=Movement.FORWARD;
		pilot.forward();
	}

	@Override
	public void backward() {
		movement=Movement.BACKWARD;
		pilot.backward();
	}

	@Override
	public void stop() {
		movement=Movement.STOPPED;

		pilot.stop();
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
	public Position getPosition() {
		return new Position(poseProvider.getPose().getLocation());
	}

	
	//TODO checken of de getheading waarden tussen 0 en 180 graden teruggeeft
	@Override
	public double getOrientation() {
		return poseProvider.getPose().getHeading();
	}

	@Override
	public Movement getMovementStatus() {
	return movement;	
	}

	@Override
	public void UpdateUntil(TimeStamp timestamp) {
		// TODO Auto-generated method stub
		
	}

}
