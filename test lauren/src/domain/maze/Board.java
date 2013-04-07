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
	private List<Ball> balls;
	private HashMap<Integer,InitialPosition> initialPositions;
	
	
	
	public Board(){
		barcodes = new ArrayList<Barcode>();
		walls = new HashMap<Position,Wall>();
		seesaws = new HashMap<Position, Seesaw>();
		balls = new ArrayList<Ball>();
		initialPositions = new HashMap<Integer,InitialPosition>();
	}
	
	public void addInitialPosition(InitialPosition pos, int nb){
		System.out.println("init pos added: x:"+pos.getX()+" y:"+pos.getY()+" nb:"+nb);
		initialPositions.put(nb, pos);
	}
	
	public InitialPosition getInitialPositionFromPlayer(int nb){
		System.out.println("-----------------");
		System.out.println("inpos");
		for(int i =0; i<4; i++){
			if(initialPositions.get(i)!=null)
			System.out.println(initialPositions.get(i));			
		}
		System.out.println("-----------------");
		return initialPositions.get(nb);
	}
	
	public synchronized void addBall(Ball ball){
		balls.add(ball);
	}
	
	public Ball removeBall(Position position) {
		Ball ball2remove = null;
		for(Ball ball : balls){
			if(ball.getPosition().getDistance(position)<10){
				ball2remove = ball;
			}
		}
		if(ball2remove !=null) {
			balls.remove(ball2remove);
		}
		return ball2remove;
	}
	
	public List<Ball> getBalls(){
		return balls;
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

	// TODO checken of het wel goed is dat als er een seesaw wordt toegevoegd
	// die al op dezelfde positie bestaat, dat die dan de andere vervangt
	public void addSeesaw(Seesaw foundSeesaw){
		seesaws.put(foundSeesaw.getCenterPosition(), foundSeesaw);
	}
	
	public List<Seesaw> getSeesaws(){
		return new ArrayList<Seesaw>(seesaws.values());
	}
	
	public Seesaw getSeesaw(Position pos){
		return seesaws.get(pos);
	}
	
	public boolean hasSeesawAt(Position pos){
		if(getSeesaw(pos) != null){
			return true;
		}
		else {
			for(Seesaw s : getSeesaws()){
				if(s.hasPosition(pos)){
					return true;
				}
			}
		}
		return false;
	}
	
	
	//TODO Francis laten gebruiken
	public void setLockForSeesawWithBarcode(int barcodenb, boolean lock) {
		for (Seesaw s : getSeesaws()) {
			if (s.hasBarcodeNb(barcodenb)) {
				if(lock){
				s.lock();
				}
				else{
				s.unLock();
				}
			}
		}
	}

}



