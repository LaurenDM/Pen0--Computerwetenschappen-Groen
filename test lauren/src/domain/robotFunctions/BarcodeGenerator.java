package domain.robotFunctions;

import domain.Position.Position;
import domain.maze.barcodes.Barcode;
import domain.robots.CannotMoveException;
import domain.robots.RobotPilot;

public class BarcodeGenerator {

	private RobotPilot robot;
	private final int MAZECONSTANT = 40;
	
	
	public BarcodeGenerator(RobotPilot robot){
		this.robot = robot;
	}
	
// this method is called when the robot has detected a black line
	public void generateBarcode(){
		double turnSpeed = robot.getTurningSpeed();
		double moveSpeed = robot.getMovingSpeed();
		if(robot.getFoundBoard().detectBarcodeAt(robot.getPosition())){
			try {
				System.out.println("ROBOT MOVES 8; BARCODE FOUND");
				robot.move(8);
			} catch (CannotMoveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		Barcode barcode = new Barcode(convertBits(bits), new Position(lowx+20, lowy+20), robot.getOrientation(),robot); 
		robot.getFoundBoard().addBarcode(barcode);
//		robot.setMovingSpeed(robot.getDefaultMovingSpeed());
		robot.setMovingSpeed(moveSpeed);
		robot.setTurningSpeed(turnSpeed);
		barcode.runAction(robot);
		
	}
	
	public int[] convertBits(int[] bits){
		int[] realBits = new int[6];
		for(int i = 1; i<7; i++){
			int count1 =0;
			for(int j= 0; j<4; j++){
				if(bits[4*i+j] ==1) count1++;
			}
			if(count1==2){ throw new IllegalArgumentException();}
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
		return realBits;
	}
	
}