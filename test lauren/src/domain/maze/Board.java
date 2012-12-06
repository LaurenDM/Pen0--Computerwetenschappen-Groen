package domain.maze;

import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import domain.Position.Position;
import domain.barcodes.Barcode;

public class Board {
	
	private static final int MAZECONSTANT = 40;
	
	private HashMap<Position, Wall> walls;
	private List<Wall> foundWalls;
	private List<Barcode> simulatedBarcodes;
	private List<Barcode> foundBarcodes;
	
	
	public Board(){
		walls = new HashMap<Position,Wall>();
		simulatedBarcodes = new ArrayList<Barcode>();
		foundBarcodes = new ArrayList<Barcode>();
		foundWalls = new ArrayList<Wall>();
		}
	
	public synchronized void addWall(Wall wall){
		walls.put(wall.getCenterPosition(),wall);
	}
	
	public synchronized void foundNewWall(Wall wall){
		foundWalls.add(wall);
	}
	
	public List<Wall> getFoundWalls(){
		return foundWalls;
	}
	public synchronized void addFoundBarcode(Barcode barcode){
		foundBarcodes.add(barcode);
	}
	public synchronized void addSimulatedBarcode(Barcode barcode){
		simulatedBarcodes.add(barcode);
	}
	
	public List<Wall> getWalls(){
		 return new ArrayList<Wall>(walls.values());
	}
	
	public List<Barcode> getSimulatedBarcodes(){
		return simulatedBarcodes;
	}
	public List<Barcode> getFoundBarcodes(){
		return foundBarcodes;
	}
	
	
	public synchronized boolean detectWallAt(Position position){
		int x =(int) (Math.round((position.getX())/MAZECONSTANT))*MAZECONSTANT;
		int y = (int) (Math.round((position.getY())/MAZECONSTANT))*MAZECONSTANT;
		final int MARGE = 1;
		if(!(position.getX()%MAZECONSTANT < MARGE || position.getX()%MAZECONSTANT > (MAZECONSTANT-MARGE)) 
				&& !(position.getY()%MAZECONSTANT<MARGE || position.getY()%MAZECONSTANT > (MAZECONSTANT-MARGE))){
			// positie is op een vakje
			return false;
		}
		else if((position.getX()%MAZECONSTANT < MARGE || position.getX()%MAZECONSTANT > (MAZECONSTANT-MARGE))
				&& !(position.getY()%MAZECONSTANT<MARGE || position.getY()%MAZECONSTANT > (MAZECONSTANT-MARGE))){
			// verticale muren
			y = (int) Math.floor((position.getY())/MAZECONSTANT)*MAZECONSTANT;
			return findWallAt(new Position(x,y+20), position);
		}
		else if(!(position.getX()%MAZECONSTANT < MARGE || position.getX()%MAZECONSTANT > (MAZECONSTANT-MARGE))
				&& (position.getY()%MAZECONSTANT<MARGE || position.getY()%MAZECONSTANT > (MAZECONSTANT-MARGE))){
			// horizontale muren
			x = (int) Math.floor((position.getX())/MAZECONSTANT)*MAZECONSTANT;
			return findWallAt(new Position(x+20,y), position);
		}
		else{
			for(Orientation orientation : Orientation.values()){
				if(findWallAt(new Position(x+20*orientation.getXValue(),y+20*orientation.getYValue()),position)){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean findWallAt(Position middlePosition, Position position){
		Wall wall = walls.get(middlePosition);
		if(wall == null) {
			return false;
			}
		else {
			return wall.hasPosition(position);
		}
	}
	
	public synchronized boolean detectBarcodeAt(Position position){
		for(Barcode barcode : foundBarcodes){
			if(barcode.containsPosition(position)) return true;
		}
		return false;
	}
	
	public Barcode getBarcodeAt(Position pos){
		for(Barcode barcode : foundBarcodes){
			if(barcode.containsPosition(pos)) return barcode;
		}
		return null;
	}
	
	public synchronized boolean detectWhiteLineAt(Position position){
		final int MARGE = 1; // TODO: hangt af van dikte lijnen
		double x_mod = Math.abs(position.getX()%MAZECONSTANT);
		if(Math.min(x_mod, MAZECONSTANT-x_mod) <MARGE) 
			return true;
		double y_mod = Math.abs(position.getY()%MAZECONSTANT);
		if(Math.min(y_mod,  MAZECONSTANT - y_mod)<MARGE)
			return true;
		for(Barcode barcode : simulatedBarcodes){
			if(barcode.isWhiteAt(position)){
				return true;
			}
		}
		return false;
	}
	
	public synchronized boolean detectBlackLineAt(Position position){
		for(Barcode barcode : simulatedBarcodes){
			if(barcode.isBlackAt(position)){
				//System.out.println("blackline");
				return true;
			}
		}
		return false;
	}

}