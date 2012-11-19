package domain.robotFunctions;

import domain.Position.Position;
import domain.barcodes.Barcode;
import domain.maze.Orientation;
import domain.robots.CannotMoveException;
import domain.robots.Robot;

public class BarcodeGenerator extends RobotFunction {

	private Robot robot;
	private final int MAZECONSTANT = 40;
	
	
	public BarcodeGenerator(Robot robot){
		this.robot = robot;
	}
	
// this method is called when the robot has detected a black line
	public void generateBarcode(boolean btrobot){
		robot.setMovingSpeed(0.1);
		robot.backward();
		while(robot.detectBlackLine()){
			// niets
		}
		robot.stop();
		int[] bits = new int[16];
		for(int i = 0; i<16; i++){
			try {
				robot.move(0.5);
			} catch (CannotMoveException e) {
				//TODO: Wat doen we hiermee?
			}
			if(robot.detectBlackLine()){
				bits[i] = 0;
			}
			else{
				bits[i] = 1;
			}
		}
		Position pos = robot.getPosition();
		int lowx = (int) (Math.floor((pos.getX())/MAZECONSTANT))*MAZECONSTANT;
		int lowy = (int) (Math.floor((pos.getY())/MAZECONSTANT))*MAZECONSTANT;
			
		
		Barcode barcode = new Barcode(bits, new Position(lowx+20, lowy+20), getOrientation(robot.getOrientation())); 
		if(btrobot) robot.getBoard().addFoundBarcode(barcode);
		barcode.runAction(robot);
	}

	public Orientation getOrientation(double angle) {
		final int MARGE = 10;
		if(Math.abs(angle-0)<MARGE) return Orientation.EAST;
		else if(Math.abs(angle-90) <MARGE) return Orientation.SOUTH;
		else if(Math.abs(angle-180) <MARGE) return Orientation.WEST;
		else if(Math.abs(angle+90) <MARGE) return Orientation.NORTH;
		else throw new IllegalArgumentException();
	}
}