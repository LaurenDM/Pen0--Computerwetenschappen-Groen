package domain.robots;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.PilotProps;



public class BTRobot implements Robot {
	
	private DifferentialPilot pilot;
	private RegulatedMotor leftMotor;
	private RegulatedMotor rightMotor;
	
	
	public void Robot() throws Exception{
		
		PilotProps pp = new PilotProps();
    	pp.loadPersistentValues();
    	float wheelDiameter = Float.parseFloat(pp.getProperty(PilotProps.KEY_WHEELDIAMETER, "5.6"));
    	float trackWidth = Float.parseFloat(pp.getProperty(PilotProps.KEY_TRACKWIDTH, "11.36"));
    	leftMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_LEFTMOTOR, "C"));
    	rightMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_RIGHTMOTOR, "B"));
    	
    	pilot = new DifferentialPilot(wheelDiameter, trackWidth, leftMotor, rightMotor);
    	
	}
	
	@Override
	public void move(double distance) {
		pilot.travel(distance);
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
		pilot.setTravelSpeed(speed);
	}

	@Override
	public void setTurningSpeed(double speed) {
		pilot.setRotateSpeed(speed);
	}

	@Override
	public void forward() {
		pilot.forward();
	}

	@Override
	public void backward() {
		pilot.backward();
	}

	@Override
	public void stop() {
		pilot.stop();
	}

}
