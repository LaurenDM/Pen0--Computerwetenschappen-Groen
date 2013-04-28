package domain.maze.barcodes;


import domain.robotFunctions.BarcodeGenerator;
import domain.robots.BTRobotPilot;
import domain.robots.CannotMoveException;
import domain.robots.RobotPilot;

public class BarcodesTester {

	private static RobotPilot robot;
	
	public static void main(String[] args) throws CannotMoveException{
		robot = new BTRobotPilot("blabla");
		
		
		robot.forward();
		while(!robot.detectBlackLine()){
			//niks
		}
		robot.stop();
		BarcodeGenerator bg = new BarcodeGenerator(robot);
		bg.generateBarcode();
	}
}
