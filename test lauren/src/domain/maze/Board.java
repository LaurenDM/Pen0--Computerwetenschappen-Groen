package domain.maze;


import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import domain.Position.Position;
import domain.maze.barcodes.Barcode;
import domain.robots.RobotPilot;

public class Board {
	
	private static final int MAZECONSTANT = 40;
	
	private HashMap<Position, Wall> walls;
	private List<Wall> foundWalls;
	private List<Ball> balls;
	private List<Barcode> simulatedBarcodes;
	private List<Barcode> foundBarcodes;
	private List<RobotPilot> otherRobots;
	private HashMap<Position, Seesaw> seesaws;
	private List<Seesaw> foundSeesaws;
	
	
	public Board(){
		walls = new HashMap<Position,Wall>();
		simulatedBarcodes = new ArrayList<Barcode>();
		foundBarcodes = new ArrayList<Barcode>();
		foundWalls = new ArrayList<Wall>();
		balls = new ArrayList<Ball>();
		seesaws = new HashMap<Position, Seesaw>();
		foundSeesaws = new ArrayList<Seesaw>();
		}
	
	public synchronized void addWall(Wall wall){
		walls.put(wall.getCenterPosition(),wall);
	}
	
	public synchronized void addBall(Ball ball){
		balls.add(ball);
	}
	
	public synchronized void addRobot(RobotPilot robot){
		otherRobots.add(robot);
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
	
	public List<Ball> getBalls(){
		return balls;
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
	
	public synchronized boolean detectRobotFrom(RobotPilot robot){
		final int DISTANCE = 50; //TODO experimenteel bepalen van bereik van IRsensor
		if(otherRobots!=null){
			for(RobotPilot robot2 : otherRobots){
				if(robot2.getPosition().getDistance(robot.getPosition())<DISTANCE){
					double distance = robot2.getPosition().getDistance(robot.getPosition());
					double angle = Math.abs(robot.getOrientation()-robot.getPosition().getAngleTo(robot2.getPosition()));
					if(angle<135){
						// andere robot in gezichtsveld van robot
						robot.turnUltrasonicSensor((int) angle);
						if(robot.readUltrasonicValue() >= distance){
							// geen muur tussen robots
							return true;
						}
						//wel muur tussen robots
					}
					//robot ziet andere robot niet
				}
				// buiten bereik
			}
		}
		return false;
	}
	
	public void putSeesaw(Seesaw seesaw){
		if(seesaws.get(seesaw.getCenterPosition()) == null){
			seesaws.put(seesaw.getCenterPosition(),seesaw);
		}
	}
	
	public void addFoundSeesaw(Seesaw foundSeesaw){
		if(seesaws.get(foundSeesaw.getCenterPosition()) != null){
			foundSeesaw = seesaws.get(foundSeesaw.getCenterPosition());
		}
		foundSeesaws.add(foundSeesaw);
	}
	
	public boolean checkForOpenSeesawFrom(RobotPilot robot){
		final int DISTANCE = 50;
		for(Seesaw seesaw : seesaws.values()){
			if(seesaw.getInfaredPosition().getDistance(robot.getPosition())<DISTANCE){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasSeaSawAt(Position pos){
		if(seesaws.get(pos) != null){
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
	
	public List<Seesaw> getSeesaws(){
		return new ArrayList<Seesaw>(seesaws.values());
	}
	
	public List<Seesaw> getFoundSeesaws(){
		return foundSeesaws;
	}
	
	public void rollSeeSawWithBarcode(int barcodenb){
		for(Seesaw s : getSeesaws()){
			if(s.hasBarcodeNb(barcodenb)){
				s.rollOver(barcodenb);
			}
		}
	}
	
	public void unlockSeesawWithBarcode(int barcodenb){
		for(Seesaw s : getSeesaws()){
			if(s.hasBarcodeNb(barcodenb)){
				s.unLock();
			}
		}
	}

	

}

	

