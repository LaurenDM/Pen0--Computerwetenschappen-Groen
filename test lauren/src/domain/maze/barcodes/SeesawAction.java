package domain.maze.barcodes;


import domain.Position.Position;
import domain.maze.Orientation;
import domain.maze.Seesaw;
import domain.robots.RobotPilot;

public class SeesawAction implements Action {
	
	private static final int MAZECONSTANT = 40;
	private int barcodeNb;
	private Position center;
	private Orientation orientation;
	public SeesawAction(int barcodeNb, Position center, Orientation orientation){
		this.barcodeNb = barcodeNb;
		this.center = center;
		this.orientation=orientation;
		if(orientation.equals(Orientation.NORTH)||orientation.equals(Orientation.SOUTH)){
		center.snapTo(MAZECONSTANT, MAZECONSTANT/2, 0);
		}else{
			center.snapTo(MAZECONSTANT, 0, MAZECONSTANT/2);
		}
	}

	@Override
	public void run(RobotPilot robot) {
		Seesaw foundSeesaw=robot.getFoundBoard().getSeesaw(center);
		
		if (foundSeesaw == null) {
			foundSeesaw = new Seesaw(center, orientation, barcodeNb, false);
			robot.getFoundBoard().addSeesaw(foundSeesaw);
			int snappedOrientation = Orientation.snapAngle(90, 0,
					robot.getOrientation());
			final Position otherBarcodePosition = robot.getPosition()
					.getNewPosition(snappedOrientation,
							MAZECONSTANT * 3);
			otherBarcodePosition.snapTo(MAZECONSTANT,MAZECONSTANT/2, MAZECONSTANT/2);
			Orientation otherBarcodeOrientation = Orientation.getOrientation(
					snappedOrientation).getBack();
			
			Barcode newBarcode = new Barcode(foundSeesaw.getOtherBarcode(),
					otherBarcodePosition, otherBarcodeOrientation);

			robot.getFoundBoard().addBarcode(newBarcode);
		System.out.println("We are going to handle a newly found seesaw");

		}
		else{
			System.out.println("We are going to handle an already found seesaw");
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
