package domain.robotFunctions;

import domain.Position.Position;
import domain.barcodes.Barcode;
import domain.robots.CannotMoveException;
import domain.robots.Robot;

public class BarcodeGenerator extends RobotFunction {

	private Robot robot;
	private final int MAZECONSTANT = 40;
	
	
	public BarcodeGenerator(Robot robot){
		this.robot = robot;
	}
	
// this method is called when the robot has detected a black line
	public void generateBarcode(){
		if(robot.getBoard().detectBarcodeAt(robot.getPosition())){
			try {
				robot.move(8);
			} catch (CannotMoveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		robot.setMovingSpeed(0.5);
		robot.backward();
		while(robot.detectBlackLine()){
			// niets
		}
		robot.stop();
		robot.setMovingSpeed(1);
		try {
			robot.move(0.5);
		} catch (CannotMoveException e1) {
			// nietsdoene
		}
		int[] bits = new int[32];
		for(int i = 0; i<32; i++){
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
		Barcode barcode = new Barcode(convertBits(bits), new Position(lowx+20, lowy+20), robot.getOrientation()); 
		robot.getBoard().addFoundBarcode(barcode);
		robot.setMovingSpeed(robot.getDefaultMovingSpeed());
		
		barcode.runAction(robot);
	}
	
	public int[] convertBits(int[] bits){
		int[] realBits = new int[6];
		for(int i = 1; i<7; i++){
			int count1 =0;
			for(int j= 0; j<4; j++){
				if(bits[4*i+j] ==1) count1++;
			}
			if(count1>2) realBits[i-1] = 1;
			else realBits[i-1] = 0;
		}
		return realBits;
	}
	
	public int[] convertBitsErrorMargin(int[] bits){
		double weight3 = 3;
		double weight2 = 2;
		double weight1 = 1;
		double totalWeighted = 0;
		System.out.println(bits);
		for(int i= 0; i<32; i++){
			System.out.println(bits[i]);
		}
		int[] realBits = new int[6];
		for(int i = 0; i<6; i++){
			double weightedResult = bits[4*i]*weight1 + bits[4*i+1]*weight2 + bits[4*i+2]*weight3;
			totalWeighted = totalWeighted + weightedResult;
			if(weightedResult>=3){
				realBits[i] = 1;
			}
			else if (weightedResult<3){
				realBits[i] = 0;
			}
		}
		double totalValue = 0;
		for(int j =0; j<6; j++){
			totalValue = totalValue + (weight1 + weight2 + weight3)*realBits[j];
		}
		double score = totalValue/totalWeighted;
		System.out.println("score: "+score);
		return realBits;
	}
	
}