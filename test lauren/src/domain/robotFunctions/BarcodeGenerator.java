package domain.robotFunctions;

import domain.barcodes.Barcode;
import domain.robots.CannotMoveException;
import domain.robots.Robot;

public class BarcodeGenerator {

	private Robot robot;
	
	public BarcodeGenerator(Robot robot){
		this.robot = robot;
	}
	
// this method is called when the robot has detected a black line
	public void generateBarcode(){
		int[] bits = new int[6];
		for(int i = 0; i<6; i++){
			try {
				robot.move(2);
			} catch (CannotMoveException e) {
				//TODO: Wat doen we hiermee?
			}
			if(robot.detectWhiteLine()){
				bits[i] = 1;
			}
			else{
				bits[i] = 0;
			}
		}
		Barcode barcode = new Barcode(bits);
		robot.getBoard().addBarcode(barcode);
		barcode.runAction(robot);
	}
}
