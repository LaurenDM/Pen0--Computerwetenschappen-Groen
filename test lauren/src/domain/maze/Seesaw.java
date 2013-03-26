package domain.maze;

import domain.Position.Position;
import domain.maze.barcodes.Action;
import domain.maze.barcodes.Barcode;

public class Seesaw extends MazeElement {
	
	private Position middlePosition;
	private final int EDGE = 80; 
	private int barcodeNb;
	private int otherBarcodeNb;
	private final Orientation ORIENTATION;
	private boolean locked;
	
	// barcodeNb --->orientatie--> otherBarcodeNb 
	// of otherBarcodeNb <--orientatie<-- barcodeNb
	
	private int infrared; 
	
	public Seesaw(Position middlePosition, Orientation orientation, int barcodeNb){
		this.middlePosition = middlePosition;
		this.barcodeNb= barcodeNb;
		initialize();
		this.ORIENTATION = orientation;
		this.locked = false;
	}
	
	public void initialize(){
		if(barcodeNb % 4 == 3){
			//eerste barcode van een paar --> open
			infrared = 0;
			otherBarcodeNb = barcodeNb +2;
		}
		else{
			infrared = 1;
			otherBarcodeNb = barcodeNb;
			barcodeNb = otherBarcodeNb -2;
		}
	}
	
	//Uitleg infrared
	// 		infrared = 0 als kant van barcodeNb= open
	//		infrared = 1 als kant van otherBarcodeNb = open
	

	@Override
	public boolean hasPosition(Position position) {
		int xRange, yRange;
		if(getOrientation().equals(Orientation.EAST) || getOrientation().equals(Orientation.WEST )){
			xRange = EDGE/2;
			yRange = EDGE/4;
		}
		else {
			xRange = EDGE/4;
			yRange = EDGE/2;
		}
		if(position.getX() <= middlePosition.getX() + xRange && position.getX() >= middlePosition.getX() - xRange){
			if(position.getY() <= middlePosition.getY() + yRange && position.getY() >= middlePosition.getY() - yRange){
				return true;
			}
		}
		return false;
	}

	@Override
	public Position getCenterPosition() {
		return middlePosition;
	}
	
	public int getBarcodeNb(){
		return barcodeNb;
	}
	
	public int getOtherBarcodeNb(){
		return otherBarcodeNb;
	}

	public Orientation getOrientation() {
		return ORIENTATION;
	}
	
	public boolean hasBarcodeNb(int nb){
		return ((barcodeNb == nb) || (otherBarcodeNb ==nb));
	}
	
	public Position getInfaredPosition(){
		return getInfrareds()[infrared];
	}
	
	public void setInfrared(int infrared){
		this.infrared = infrared;
	}
	
	public Position[] getInfrareds(){
		return new Position[]{getCenterPosition().getNewPosition(getOrientation().getAngleToHorizontal(), 20),
				getCenterPosition().getNewPosition(getOrientation().getAngleToHorizontal(), -20)};
		// index 0 geeft positie langs de voorkant (volgens orientatie)
		// index 1 geeft positie langs de achterkant (volgens orientatie)
	}
	
	public void rollOver(){
		setInfrared((this.infrared + 1)%2);
	}
	
	public void rollOver(int nb){
		// nb = barcodenb read by robot that drives over seesaw
		if(nb == barcodeNb){
			setInfrared(0);
		}
		else if(nb == otherBarcodeNb){
			setInfrared(1);
		}
		locked = true;
	}
	
	public void unLock(){
		locked = false;
	}
	
	public boolean isLocked(){
		return this.locked;
	}

	public Barcode getBarcode(int robotOrientation, Action action) {
		int oppositeOrientation=robotOrientation+180;
		if(oppositeOrientation>180){
			oppositeOrientation-=360;
		}
		return new Barcode(otherBarcodeNb, middlePosition.getNewPosition(Orientation.snapAngle(90,0,robotOrientation), 60), Orientation.getOrientation(robotOrientation), oppositeOrientation, action);
	}
	
	
	

}
