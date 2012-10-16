package domain.robots;
import domain.Position.Position;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.PilotProps;



public class BTRobot extends Robot implements iRobot  {
	
	private DifferentialPilot pilot;
	private RegulatedMotor leftMotor;
	private RegulatedMotor rightMotor;
	
	//The poseProvider is an intantiation of a class that "LeJOS" provides for determining the pose of a NXT Robot.
	private OdometryPoseProvider poseProvider;
	
	
	public BTRobot() throws Exception{
		
		PilotProps pp = new PilotProps();
    	pp.loadPersistentValues();
    	float wheelDiameter = Float.parseFloat(pp.getProperty(PilotProps.KEY_WHEELDIAMETER, "5.6"));
    	float trackWidth = Float.parseFloat(pp.getProperty(PilotProps.KEY_TRACKWIDTH, "11.72"));
    	leftMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_LEFTMOTOR, "C"));
    	rightMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_RIGHTMOTOR, "B"));
    	    	
    	pilot = new DifferentialPilot(wheelDiameter, trackWidth, leftMotor, rightMotor);
    	pilot.setTravelSpeed(10);
    	pilot.setRotateSpeed(90);
    	poseProvider= new OdometryPoseProvider(pilot);
	}
	

	@Override
	public void turn(double amount) {
		pilot.rotate(amount);
	}

	@Override
	public void setMovingSpeed(double speed) {
		pilot.setTravelSpeed(speed);
		super.setMovingSpeed(speed);
	}

	@Override
	public void setTurningSpeed(double speed) {
		pilot.setRotateSpeed(speed);
		super.setTurningSpeed(speed);
	}

	@Override
	public void forward() {
		pilot.forward();
		super.forward();
	}

	@Override
	public void backward() {
		pilot.backward();
		super.forward();
	}

	@Override
	public void stop() {
		pilot.stop();
		super.stop();
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
	public boolean canMove() {
		return true;
	}
	
	public boolean isMoving(){
		return pilot.isMoving();
	}
	
	@Override
	public void move(double distance) {

//TODO
		pilot.travel(distance);
		
	}

}
