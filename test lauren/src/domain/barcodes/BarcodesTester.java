package domain.barcodes;


import domain.robotFunctions.BarcodeGenerator;
import domain.robots.BTRobotPilot;
import domain.robots.CannotMoveException;
import domain.robots.Robot;

public class BarcodesTester {

	private static Robot robot;
	
	public static void main(String[] args) throws CannotMoveException{
		robot = new Robot(new BTRobotPilot());
		
		
		robot.forward();
		while(!robot.detectBlackLine()){
			//niks
		}
		robot.stop();
		BarcodeGenerator bg = new BarcodeGenerator(robot);
		bg.generateBarcode();
	}
}
