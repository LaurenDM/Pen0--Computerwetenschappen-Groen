package domain.maze;

import domain.Position.Position;

public class SeaSaw extends MazeElement {
	
	private Position middlePosition;
	private final int EDGE = 80; 
	private int barcodeNb;
	private final Orientation ORIENTATION;
	
	private int infrared; 
	
	public SeaSaw(Position middlePosition, Orientation orientation, int barcodeNb){
		this.middlePosition = middlePosition;
		this.barcodeNb= barcodeNb;
		initialize();
		this.ORIENTATION = orientation;
	}
	
	public void initialize(){
		if(barcodeNb % 4 == 3){
			//eerste barcode van een paar --> open
			infrared = 1;
		}
		else{
			infrared = 0;
		}
	}
	

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

	public Orientation getOrientation() {
		return ORIENTATION;
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
	}
	
	public void rollOver(){
		setInfrared((this.infrared + 1)%2);
	}
	
	
	

}
