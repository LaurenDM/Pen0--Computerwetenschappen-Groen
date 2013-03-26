package domain.maze.barcodes;


import domain.Position.Position;
import domain.maze.Orientation;
import domain.maze.Seesaw;
import domain.robots.RobotPilot;

public class SeesawAction implements Action {
	
	private int barcodeNb;
	private Position center;
	private Orientation orientation;
	
	public SeesawAction(int barcodeNb, Position center, Orientation orientation){
		this.barcodeNb = barcodeNb;
		this.center = center;
		this.orientation=orientation;
	}

	@Override
	public void run(RobotPilot robot) {
		Seesaw foundSeesaw = new Seesaw(center, orientation, barcodeNb);
		foundSeesaw = robot.getBoard().addFoundSeesaw(foundSeesaw);
		if(!foundSeesaw.isLocked()){
			robot.handleSeesaw(barcodeNb);
		}
		Position barcodePos = center.getNewPosition(orientation.getAngleToHorizontal(), 60);
		int newBarcodeNb;
		if(barcodeNb % 4 == 3){
			newBarcodeNb = foundSeesaw.getOtherBarcodeNb();
		}
		else{
			newBarcodeNb = foundSeesaw.getBarcodeNb();
		}
		Barcode barcode = new Barcode(newBarcodeNb, barcodePos, orientation);
		robot.getBoard().addFoundBarcode(barcode);	
	}

}
