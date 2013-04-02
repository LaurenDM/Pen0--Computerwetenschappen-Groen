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
		final Seesaw foundSeesaw;
		if(robot.getBoard().getFoundSeesawAt(center)==null){
		foundSeesaw = new Seesaw(center, orientation, barcodeNb,false);
		robot.getBoard().addFoundSeesaw(foundSeesaw);
		Barcode newBarcode= foundSeesaw.getBarcode((int) robot.getOrientation(), new SeesawAction(foundSeesaw.getOtherBarcode(),center,orientation ), robot);
		robot.getBoard().addFoundBarcode(newBarcode);
		System.out.println("We are going to handle a newly found seesaw");
		}
		else{
			System.out.println("We are going to handle an already found seesaw");
			foundSeesaw=robot.getBoard().getFoundSeesawAt(center);
		}
			
			//Note that in the handleSeesaw, we take into account whether the seesaw is locked or not.
		robot.handleSeesaw(barcodeNb, foundSeesaw);
	
//		Position barcodePos = center.getNewPosition(orientation.getAngleToHorizontal(), 60);
//		int newBarcodeNb;
//		if(barcodeNb % 4 == 3){
//			newBarcodeNb = foundSeesaw.getOtherBarcodeNb();
//		}
//		else{
//			newBarcodeNb = foundSeesaw.getBarcodeNb();
//		}
//		Barcode barcode = new Barcode(newBarcodeNb, barcodePos, orientation);
//		robot.getBoard().addFoundBarcode(barcode);	
	}

}
