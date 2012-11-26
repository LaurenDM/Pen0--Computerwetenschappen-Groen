package domain.maze;

import java.util.List;
import java.util.ArrayList;

import domain.Position.Position;
import domain.barcodes.Barcode;

public class Board {
	
	private static final int MAZECONSTANT = 40;
	
	private List<Wall> walls;
	private List<Wall> foundWalls;
	private List<Barcode> simulatedBarcodes;
	private List<Barcode> foundBarcodes;
	
	
	public Board(){
		walls = new ArrayList<Wall>();
		simulatedBarcodes = new ArrayList<Barcode>();
		foundBarcodes = new ArrayList<Barcode>();
		foundWalls = new ArrayList<Wall>();
		}
	
	public synchronized void addWall(Wall wall){
		walls.add(wall);
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
		return walls;
	}
	
	public List<Barcode> getSimulatedBarcodes(){
		return simulatedBarcodes;
	}
	public List<Barcode> getFoundBarcodes(){
		return foundBarcodes;
	}
	
	
	public boolean detectWallAt(Position position){
		for(Wall wall: walls){
			if(wall.hasPosition(position)) return true;
		}
		return false;
	}
	
	public boolean detectBarcodeAt(Position position){
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
	
	public boolean detectWhiteLineAt(Position position){
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
	
	public boolean detectBlackLineAt(Position position){
		for(Barcode barcode : simulatedBarcodes){
			if(barcode.isBlackAt(position)){
				//System.out.println("blackline");
				return true;
			}
		}
		return false;
	}

}