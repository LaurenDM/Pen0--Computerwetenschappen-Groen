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
		this.ORIENTATION = orientation;
	}

	@Override
	public boolean hasPosition(Position position) {
		if(position.getX() <= middlePosition.getX() + EDGE/2 && position.getX() >= middlePosition.getX() - EDGE/2){
			if(position.getY() <= middlePosition.getY() + EDGE/2 && position.getY() >= middlePosition.getY() - EDGE/2){
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
