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
	
	
	private List<Wall> foundWalls;
	private List<Barcode> foundBarcodes;
	private List<Seesaw> foundSeesaws;
	
	
	
	public Board(){
		
		
		foundBarcodes = new ArrayList<Barcode>();
		foundWalls = new ArrayList<Wall>();
		foundSeesaws = new ArrayList<Seesaw>();
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
//	public synchronized void removeFoundBarcode(Barcode barcode){
//		foundBarcodes.remove(barcode);
//	}
	
	
	
	
	
	public List<Barcode> getFoundBarcodes(){
		return foundBarcodes;
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
	
	
	public void rollSeeSawWithBarcode(int barcodenb){
		for(Seesaw s : foundSeesaws){
			if(s.hasBarcodeNb(barcodenb)){
				s.rollOver();
			}
		}
	}
	
	
	
	
	
	
	public void addFoundSeesaw(Seesaw foundSeesaw){
		foundSeesaws.add(foundSeesaw);
	}
	
	
	
	
	
	public List<Seesaw> getFoundSeesaws(){
		return foundSeesaws;
	}
	
	

	

}

	

