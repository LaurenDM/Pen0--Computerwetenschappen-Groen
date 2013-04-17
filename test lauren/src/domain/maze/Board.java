package domain.maze;


import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import domain.Position.InitialPosition;
import domain.Position.Position;
import domain.maze.barcodes.Barcode;
import domain.robots.RobotPilot;

public class Board {
	
	private static final int MAZECONSTANT = 40;
	
	
	private HashMap<Position, Wall> walls;
	private List<Barcode> barcodes;
	private HashMap<Position, Seesaw> seesaws;
	
	
	
	public Board(){
		barcodes = new ArrayList<Barcode>();
		walls = new HashMap<Position,Wall>();
		seesaws = new HashMap<Position, Seesaw>();
	}
	
	public synchronized void addWall(Wall wall){
		walls.put(wall.getCenterPosition(), wall);
	}
	
	public List<Wall> getWalls(){
		return new ArrayList<Wall>(walls.values());
	}
	
	public boolean findWallAt(Position middlePosition, Position position){
		Wall wall = walls.get(middlePosition);
		if(wall == null) {
			return false;
			}
		else {
			return wall.hasPosition(position);
		}
	}
	
	public synchronized void addBarcode(Barcode barcode){
		barcodes.add(barcode);
	}
//	public synchronized void removeFoundBarcode(Barcode barcode){
//		foundBarcodes.remove(barcode);
//	}
	
	public List<Barcode> getBarcodes(){
		return barcodes;
	}
	
	public synchronized boolean detectBarcodeAt(Position position){
		for(Barcode barcode : barcodes){
			if(barcode.containsPosition(position)) return true;
		}
		return false;
	}
	
	public Barcode getBarcodeAt(Position pos){
		for(Barcode barcode : barcodes){
			if(barcode.containsPosition(pos)) return barcode;
		}
		return null;
	}
	
	
	public void rollSeeSawWithBarcode(int barcodenb){
		for(Seesaw s : getSeesaws()){
			if(s.hasBarcodeNb(barcodenb)){
				s.rollOver();
			}
		}
	}
	
	public void addSeesaw(Seesaw foundSeesaw){
		seesaws.put(foundSeesaw.getCenterPosition(), foundSeesaw);
	}
	
	public List<Seesaw> getSeesaws(){
		return new ArrayList<Seesaw>(seesaws.values());
	}
	
	public Seesaw getSeesaw(Position pos){
		return seesaws.get(pos);
	}
	
	

	

}

	

