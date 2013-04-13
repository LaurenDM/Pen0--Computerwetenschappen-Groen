package domain.maze;

import domain.Position.Position;
import domain.maze.barcodes.Action;
import domain.maze.barcodes.Barcode;
import domain.robots.RobotPilot;

public class Seesaw extends MazeElement {
	
	private Position middlePosition;
	private final int EDGE = 80; 
	private final int lowBarcodeNb;
	private final int highBarcodeNb;
	public final boolean isFromTXTfile; 
	private final int makeBarcode;
	private final Orientation ORIENTATION;
	private boolean locked;
	
	// lowBarcodeNb --->orientatie--> highBarcodeNb 
	// of highBarcodeNb <--orientatie<-- lowBarcodeNb
	
	private int infrared=1; 
	
	/**
	 * Belangrijk de meegeven orientatie is niet per se de ORIENTATION van de
	 * wip, behalve als isFromTXTfile true is. Deze kan veranderd worden om te
	 * zorgen dat de lage barcode voor de wip staat. Voor de wip betekent een positie terug t.o.v. de orientatie.
	 * 
	 * @param orientation
	 *            De orientatie van de robot wanneer hij de barcode vindt die
	 *            meegegeven wordt.
	 * @param barcodeNb
	 *            de barcode die tot de ontdekking van de seesaw leidt of gewoon 1 van de twee barcodes als het van een TXTfile komt.
	 */
	public Seesaw(Position middlePosition, Orientation orientation, int barcodeNb, boolean isFromTXTfile){
		this.isFromTXTfile=isFromTXTfile;
		this.makeBarcode=barcodeNb;
		this.middlePosition = middlePosition;
		
		//Initialize barcodes
		if(isALowBcNb(barcodeNb)){
			lowBarcodeNb=makeBarcode;
			highBarcodeNb = makeBarcode +2;
			this.ORIENTATION = orientation;
		}
		else{
			highBarcodeNb = makeBarcode;
			lowBarcodeNb = makeBarcode - 2;
			if (isFromTXTfile) {
				this.ORIENTATION = orientation;
			} else {
				this.ORIENTATION = orientation.getBack();
			}
		}
		
		// als --> = orientatie van de wip dan: laag nr --> hoog nr
		
		this.locked = false;//TODO kijken of die wel weg mag
		System.out.println("We have created a " + !isFromTXTfile  + " Seesaw with orientation " +  ORIENTATION);
	}
	
	
	
	
	//Uitleg infrared
	// 		infrared = 0 als kant van barcodeNb= open
	//		infrared = 1 als kant van otherBarcodeNb = open
	
	public void setUpAt(int barcodeNb, boolean upAtGivenBarcodeSide){
		if(barcodeNb==getHighBarcodeNb()){
			if(upAtGivenBarcodeSide){
				setInfrared(1);
			}
			else{
			setInfrared(0);
			}
		}
		else if(barcodeNb==getLowBarcodeNb()){
		
			if(upAtGivenBarcodeSide){
				setInfrared(0);
			}
			else{
			setInfrared(1);
			}
		}
		else{
			System.out.println("Men heeft de wip proberen instellen met een verkeerde barcode");
			throw new RuntimeException();
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
	
	public int getLowBarcodeNb(){
		return lowBarcodeNb;
	}
	
	public int getHighBarcodeNb(){
		return highBarcodeNb;
	}

	public Orientation getOrientation() {
		return ORIENTATION;
	}
	
	public boolean hasBarcodeNb(int nb){
		return ((lowBarcodeNb == nb) || (highBarcodeNb ==nb));
	}
	
	public Position getInfaredPosition(){
		return getInfrareds()[infrared];
	}
	
	public void setInfrared(int infrared){
		try{
		System.out.println("We are setting a " + !isFromTXTfile  + " Seesaw's infrared-position from " + getInfrareds()[this.infrared] + " To" + getInfrareds()[infrared]);
		}catch(NullPointerException n){
//			this means the setInfrared happens in the constructor
		}
		this.infrared = infrared;
	}

	public Position[] getInfrareds() {
		return new Position[] {
				getCenterPosition().getNewRoundedPosition(
						getOrientation().getAngleToHorizontal(), -20),
				getCenterPosition().getNewRoundedPosition(
						getOrientation().getAngleToHorizontal(), 20) };
		// index 0 geeft positie langs de kant met de kleine barcode
		// index 1 geeft positie langs de kant met de grote barcode
	}
	
	public void rollOver(){
		setInfrared((this.infrared + 1)%2);
	}
	//TODO de lock hieruit halen zodat het duidelijk is wat deze methode doet en in de plaats lock gebruiken in playerhandler om te locken. 
	public void rollOver(int nb){
		 //nb is barcodenb read by robot that drives over seesaw
		if(nb == lowBarcodeNb){
			setInfrared(0);
		}
		else if(nb == highBarcodeNb){
			setInfrared(1);
		}
//		locked=true;
	}
	
	public void lock(){
		locked = true;
	}
	public void unLock(){
		locked = false;
	}
	
	public boolean isLocked(){
		return this.locked;
	}
	

	public int getOtherBarcode(){
		return highBarcodeNb==makeBarcode?lowBarcodeNb:highBarcodeNb;
		
	}




	public static boolean isALowBcNb(int barcodeNb) {
		return barcodeNb%4==3;
	}
	
	

}
