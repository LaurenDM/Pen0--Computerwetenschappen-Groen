package domain.robotFunctions;

import domain.Position.Position;
import domain.barcodes.Barcode;
import domain.robots.CannotMoveException;
import domain.robots.Robot;

public class BarcodeGenerator extends RobotFunction {

	private Robot robot;
	
	//Voorlopig alleen voor BTRobot!!
	
	public BarcodeGenerator(Robot robot){
		this.robot = robot;
	}
	
// this method is called when the robot has detected a black line
	public void generateBarcode(){
		Position pos1 = robot.getPosition().getNewPosition(robot.getOrientation(), -1);
		//TODO: -1 hangt af van wanneer robot zwarte lijn gedetecteerd heeft en waar hij nu staat
		int[] bits = new int[6];
		for(int i = 0; i<6; i++){
			try {
				robot.move(2);
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
		try {
			robot.move(3);
		} catch (CannotMoveException e) {
			// niets doen?
		}
		Position pos2 = robot.getPosition();
		Barcode barcode = new Barcode(bits, pos1, pos2);
		robot.getBoard().addBarcode(barcode);
		barcode.runAction(robot);
	}
}
