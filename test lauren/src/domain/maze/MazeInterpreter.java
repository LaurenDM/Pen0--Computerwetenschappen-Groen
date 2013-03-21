package domain.maze;

import java.io.BufferedReader;


import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import domain.Position.Position;
import domain.maze.barcodes.Barcode;
import domain.maze.barcodes.SeesawAction;

public class MazeInterpreter {
	
	private Board board;
	private int firstLine; //number of line with first command
	
	private HashMap<Position,Integer> barcodePositions;
	
	
	public MazeInterpreter(Board board){
		this.board = board;
		barcodePositions = new HashMap<Position,Integer>();
	}
	
	public void readFile(String fileLocation){
	try{
		firstLine = -1;
		  FileInputStream fstream = new FileInputStream(fileLocation);
		  DataInputStream in = new DataInputStream(fstream);
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  String strLine;
		  int lineCounter = 0;

		  while ((strLine = br.readLine()) != null)   {
//		  System.out.println (strLine);
		  readline(strLine, lineCounter);
		  lineCounter++;
		  }
		  in.close();
		    }
	catch (Exception e){
		e.printStackTrace();
		  System.err.println("Error reading maze.");
		  }
	}
	
	public void readline(String strLine, int numberOfLine){
		if((strLine.startsWith("DeadEnd") || strLine.startsWith("Corner")
		|| strLine.startsWith("Straight") || strLine.startsWith("T.") || strLine.startsWith("SeeSaw"))){
			if(firstLine==-1){
				firstLine = numberOfLine; 
			}
			String[] splitList = strLine.split("	");
			List<String> strList = new ArrayList<String>();
			for(String s: splitList){
				if(s.length()>0){
					strList.add(s);
				}
			}
			for(int i=0; i<strList.size(); i++){
				readCommand(strList.get(i), i, numberOfLine);
			}
		}
	}
	
	public void readCommand(String command, int XCoo, int YCoo){
		YCoo = YCoo - firstLine;
		String[] commandSplit = command.split("\\.");
		
		if(commandSplit[0].equals("DeadEnd")){
			createDeadEnd(XCoo, YCoo, Orientation.getOrientation(commandSplit[1]));
		}
		else if(commandSplit[0].equals("T")){
			createT(XCoo, YCoo, Orientation.getOrientation(commandSplit[1]));
		}
		else if(commandSplit[0].equals("Corner")){
			createCorner(XCoo, YCoo, Orientation.getOrientation(commandSplit[1]));
		}
		else if(commandSplit[0].equals("Straight")){
			createStraight(XCoo, YCoo, Orientation.getOrientation(commandSplit[1]));
		}
		else if(commandSplit[0].equals("SeeSaw")){
			createSeesaw(XCoo, YCoo, Orientation.getOrientation(commandSplit[1]));
		}
		if(commandSplit.length>2){
			if(commandSplit[2].equals("B")){
				System.out.println("Ball");
				createBall(XCoo, YCoo);
			}
			else if(!commandSplit[2].equals("00")){
				createBarcode(commandSplit[2], XCoo, YCoo, Orientation.getOrientation(commandSplit[1]));
			}
		}
	}
	
	public void createBall(int XCoo, int YCoo){
		int MAZECONSTANT = MazeElement.getMazeConstant();
		int xBall = XCoo*MAZECONSTANT+MAZECONSTANT/2;
		int yBall = YCoo*MAZECONSTANT+MAZECONSTANT/2;
		Ball ball = new Ball(new Position(xBall, yBall));
		board.addBall(ball);
	}


	public void createDeadEnd(int XCoo, int YCoo, Orientation orientation){
		Position pos = new Position(XCoo, YCoo);
		switch (orientation) {
        case NORTH: 
        	//   ||
        		 makeWall(pos, Orientation.NORTH);
        		 makeWall(pos, Orientation.EAST);
        		 makeWall(pos, Orientation.WEST);
                 break;
		case EAST:
			//	=|
				makeWall(pos, Orientation.NORTH);
				makeWall(pos, Orientation.SOUTH);
				makeWall(pos, Orientation.EAST);
				break;
		case SOUTH:
			//	|_|
				makeWall(pos, Orientation.EAST);
				makeWall(pos, Orientation.SOUTH);
				makeWall(pos, Orientation.WEST);
				break;		
		case WEST:
			//	|=
				makeWall(pos, Orientation.NORTH);
				makeWall(pos, Orientation.SOUTH);
				makeWall(pos, Orientation.WEST);
				break;	
		}
	}
	
	public void createCross(){
		//do nothing
	}
	
	public void createCorner(int XCoo, int YCoo, Orientation orientation){
		Position pos = new Position(XCoo, YCoo);
		switch (orientation) {
        case NORTH: 
        	//   -|
        		 makeWall(pos, Orientation.NORTH);
        		 makeWall(pos, Orientation.WEST);
                 break;
		case EAST:
			//	|-
				makeWall(pos, Orientation.NORTH);
				makeWall(pos, Orientation.EAST);
				break;
		case SOUTH:
			//	_|
				makeWall(pos, Orientation.EAST);
				makeWall(pos, Orientation.SOUTH);
				break;		
		case WEST:
			//	|_
				makeWall(pos, Orientation.SOUTH);
				makeWall(pos, Orientation.WEST);
				break;	
		}
	}
	
	public void createT(int XCoo, int YCoo, Orientation orientation){
		Position pos = new Position(XCoo, YCoo);
		switch (orientation) {
        case NORTH: 
        		 makeWall(pos, Orientation.NORTH);
                 break;
		case EAST:
				makeWall(pos, Orientation.EAST);
				break;
		case SOUTH:
				makeWall(pos, Orientation.SOUTH);
				break;		
		case WEST:
				makeWall(pos, Orientation.WEST);
				break;	
		}
	}
	
	public void createStraight(int XCoo, int YCoo, Orientation orientation){
		Position pos = new Position(XCoo, YCoo);
		switch (orientation) {
        case NORTH: 
        	//   | |
        		 makeWall(pos, Orientation.EAST);
        		 makeWall(pos, Orientation.WEST);
                 break;
		case EAST:
			//	=
				makeWall(pos, Orientation.NORTH);
				makeWall(pos, Orientation.SOUTH);
				break;
		case SOUTH:
			//	| |
				makeWall(pos, Orientation.EAST);
				makeWall(pos, Orientation.WEST);
				break;		
		case WEST:
			//	=
				makeWall(pos, Orientation.NORTH);
				makeWall(pos, Orientation.SOUTH);
				break;	
		}
	}
	
	public void makeWall(Position pos, Orientation orientation){
			// pos is positie van vakje
			// voorbeeld pos 11 met orientation EAST is de oostzijde van vakje 0,0
			// dit zou dan x=40 en y = 0..40
			Position position1 = null;
			Position position2 = null;
			Position posConflict = null; //checkt of er al muren staan op de positie halverwege
			int MAZECONSTANT = MazeElement.getMazeConstant();
			switch (orientation) {
	        case NORTH: 
	        	 position1 = new Position((pos.getX()+0)*MAZECONSTANT,(pos.getY()+0)*MAZECONSTANT);
	        	 position2 = new Position((pos.getX()+1)*MAZECONSTANT,(pos.getY()+0)*MAZECONSTANT);
	        	 posConflict = new Position((pos.getX()+0.5)*MAZECONSTANT,(pos.getY()+0)*MAZECONSTANT);
	             break;
	        case EAST: 
	        	 position1 = new Position((pos.getX()+1)*MAZECONSTANT,(pos.getY()+0)*MAZECONSTANT);
	        	 position2 = new Position((pos.getX()+1)*MAZECONSTANT,(pos.getY()+1)*MAZECONSTANT); 
	        	 posConflict = new Position((pos.getX()+1)*MAZECONSTANT,(pos.getY()+0.5)*MAZECONSTANT);
	             break;
	        case SOUTH: 
	        	 position1 = new Position((pos.getX()+0)*MAZECONSTANT,(pos.getY()+1)*MAZECONSTANT);
	        	 position2 = new Position((pos.getX()+1)*MAZECONSTANT,(pos.getY()+1)*MAZECONSTANT); 
	        	 posConflict = new Position((pos.getX()+0.5)*MAZECONSTANT,(pos.getY()+1)*MAZECONSTANT);
	             break;
	        case WEST: 
	        	 position1 = new Position((pos.getX()+0)*MAZECONSTANT,(pos.getY()+0)*MAZECONSTANT);
	        	 position2 = new Position((pos.getX()+0)*MAZECONSTANT,(pos.getY()+1)*MAZECONSTANT);
	        	 posConflict = new Position((pos.getX()+0)*MAZECONSTANT,(pos.getY()+0.5)*MAZECONSTANT);
	             break;
			}
			if(posConflict!=null){
				if(board.detectWallAt(posConflict)==false){
					board.addWall(new Wall(position1, position2));
				}
			}
	}
	
	private void createBarcode(String string, int xCoo, int yCoo, Orientation orientation) {
		int dec = Integer.parseInt(string);
		int MAZECONSTANT = MazeElement.getMazeConstant();
		xCoo = xCoo*MAZECONSTANT+(MAZECONSTANT/2);
		yCoo = yCoo*MAZECONSTANT+(MAZECONSTANT/2);
		Position pos = new Position(xCoo,yCoo);
		if(!board.detectBarcodeAt(pos)){
			board.addSimulatedBarcode(new Barcode(dec, pos, orientation));
		}
		if((dec>10) && (dec<22) && (dec%2==1)){
			// barcode is voor wip
			barcodePositions.put(pos, dec);
		}
	}
	
	private void createSeesaw(int xCoo, int yCoo, Orientation orientation){
		int MAZECONSTANT = MazeElement.getMazeConstant();
		xCoo = xCoo*MAZECONSTANT+(MAZECONSTANT/2);
		yCoo = yCoo*MAZECONSTANT+(MAZECONSTANT/2);
		Position pos = new Position(xCoo,yCoo);
		Position pos2 = pos.getNewPosition(orientation.getAngleToHorizontal(), MAZECONSTANT);
		if(barcodePositions.get(pos2) == null){
			pos2 = pos.getNewPosition(orientation.getAngleToHorizontal(), -MAZECONSTANT);
		}
		int dec = barcodePositions.get(pos2);
		board.putSeesaw(new Seesaw(pos2, orientation,dec));
	}
	
//	private void testBarcode(){
//		for(Barcode b: board.getSimulatedBarcodes()){
//			for(int i = 0; i<200; i++){
//			boolean test1 = b.isBlackAt(new Position(20, i));
//			boolean test2 = b.isBlackAt(new Position(60, i));
//			boolean test3 = b.isBlackAt(new Position(100, i));
//			boolean test4 = b.isBlackAt(new Position(140, i));
//			boolean test5 = b.isBlackAt(new Position(180, i));
//			boolean test6 = b.isBlackAt(new Position(220, i));
//			System.out.println("Y:"+i+" "+test1+" "+test2+" "+test3+" "+test4+" "+test5+" "+test6);
//			}
//		}
//	}
	
}