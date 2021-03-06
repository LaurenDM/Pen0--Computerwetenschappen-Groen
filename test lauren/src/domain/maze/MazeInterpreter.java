package domain.maze;

import java.io.BufferedReader;


import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.spec.PSource;


import domain.Position.Pose;
import domain.Position.Position;
import domain.maze.barcodes.Barcode;
import domain.robotFunctions.MatchMap;

public class MazeInterpreter {
	
	private Board board;
	private int firstLine; //number of line with first command
	
	private HashMap<Position,Integer> barcodePositions;
	
	private static int startX = Integer.MAX_VALUE;
	private static int startY = Integer.MAX_VALUE;
	
	
	public MazeInterpreter(Board board){
		this.board = board;
		barcodePositions = new HashMap<Position,Integer>();
	}
	
	public void readMap(String[][] map, Orientation orientation){
		map = correctOrientation(map, orientation);
		System.out.println("Changed row order");
		System.out.println(MatchMap.matrixToString(map));
		map = fixEntireMap(map);
		System.out.println("FixEntireMap:");
		System.out.println(MatchMap.matrixToString(map));
//		map = eliminateDummies(map);
//		System.out.println("elimDummys:");
//		System.out.println(MatchMap.matrixToString(map));
//		map = addCrosses(map);
//		System.out.println("adCrosses:");
//		System.out.println(MatchMap.matrixToString(map));
//		System.out.println("resultmap after readmap");
//		System.out.println(MatchMap.matrixToString(map));
		firstLine = 0;
		System.out.println("READMAP");
		for(int x = 0; x<map.length; x++){
			for(int y = 0; y<map[x].length; y++){
				readCommand(map[x][y], x, y);
			}
		}
	}
	
	public String[][] fixEntireMap(String[][] map){
		//Cut the column length
		int startXPos = 0;
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if(!map[i][j].contains(MatchMap.dummyString)){
					startXPos = i;
					j = map[0].length;
					i = map.length;
				}		
			}
		}
		int startYPos = 0;
		//Get the row heigth
		for (int i = 0; i < map[0].length; i++) {
			for (int j = 0; j < map.length; j++) {
				if(!map[j][i].contains(MatchMap.dummyString)){
					startYPos = i;
					j = map.length;
					i = map[0].length;
				}
			}
		}
		
		
		String[][] resultList = new String[map.length-startXPos][map[0].length-startYPos];
		int x = 0;
		int y = 0;
		for (int i = startXPos; i < map.length; i++) {
			for (int j = startYPos; j < map[0].length; j++) {
				System.out.println("merging : "+map[i][j]);
				resultList[x][y] = map[i][j];
				y++;
			}
			y = 0;
			x++;
		}
		return addCrosses(resultList);
	}
	
	
	private String[][] correctOrientation(String[][] map,Orientation orientation){
		switch(orientation){
		case NORTH: map= MatchMap.get270Permutation(map); break;
		case WEST: map= MatchMap.get180Permutation(map); break;
		case SOUTH: map = MatchMap.get90Permutation(map); break;
		default:  break;
		}
		System.out.println("correctOrient:");
		System.out.println(MatchMap.matrixToString(map));
		//change row order
		int lastRow = map[0].length-1;
		for(int i=0; i<=lastRow/2;i++){
			for(int j=0;j<map.length;j++){
				String temp = map[j][i];
				map[j][i] = map[j][lastRow-i];
				map[j][lastRow-i] = temp;
			}
		}
		return map;
	}
	
	private String[][] eliminateDummies(String[][] map){
		int firstX=-1, lastY = -1;
		for(int x=0; x<map.length; x++){
			for(int y = 0; y<map[x].length; y++){
				if(!map[x][y].contains("dummy")){
					firstX=(firstX==-1)?x:firstX;
				}
			}
		}
		for(int y=map[0].length-1; y>=0; y--){
			for(int x =0; x<map.length; x++){
				if(!map[x][y].contains("dummy")){
					lastY=(lastY==-1)?y:lastY;
				}
			}
		}
		String[][] newMap = new String[map.length-firstX][lastY];
		for(int x = 0; x<newMap.length ; x++){
			for(int y= 0; y<newMap[x].length; y++){
				newMap[x][y] = map[x+firstX][lastY-y];
			}
		}	
		return newMap;
	}
	
	private String[][] addCrosses(String[][] map){
		int ourX=0;
		int ourY=0;
		int mapX=0;
		int mapY=0;
		List<Barcode> bc = board.getBarcodes();
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map.length; j++) {
				try {
				if(map[i][j].split("\\.")[2]!=null){
					int barcodeNo = Integer.parseInt(map[i][j].split("\\.")[2]);
					for(Barcode b: bc){
						if(b.getDecimal()==barcodeNo){
							ourX = (int)((b.getCenterPosition().getX()-20)/40);
							ourY = (int)((b.getCenterPosition().getY()-20)/40);
							mapX = i;
							mapY = j;
						}
					}
					
				}
				} catch (Exception e) {
	//				System.out.println("There was an exception code 9843");
				}
			}
		}
		int xOffset = ourX - mapX;
		int yOffset = ourY - mapY;
		
		if(xOffset<0 || yOffset<0){
			System.out.println("WARNING; offset less than zero! (mazeinterpreter addcrosses)");
		}
		if(xOffset<0){
			xOffset = 0;
		}
		if(yOffset<0){
			yOffset = 0;
		}
		
		System.out.println("pos in ons coord:"+ ourX +" "+ ourY);
		System.out.println("pos in map:"+ mapX +" "+ mapY);
		
		String[][] newMap = new String[map.length+xOffset][map[0].length+yOffset];
		for(int x=0; x<xOffset;x++){
			for(int y=0; y<newMap[x].length;y++){
				newMap[x][y] = "Cross";
			}
		}
		for(int y=0; y<yOffset; y++){
			for(int x=0; x<newMap.length ; x++){
				newMap[x][y] = "Cross";
			}
		}
		for(int x = xOffset; x<newMap.length;x++){
			for (int y=yOffset ;y<newMap[x].length; y++){
//				System.out.println("xy = " + x + ", " + y);
				if(map[x-xOffset][y-yOffset].equals(MatchMap.dummyString))
					newMap[x][y] = "Cross";
				else
					newMap[x][y] = map[x-xOffset][y-yOffset];
			}
		}
		return newMap;

	}
	
//	private String[][] addCrosses(String[][] map){
//		String[][] newMap = new String[map.length+startX][map[0].length+startY];
//		for(int x=0; x<startX;x++){
//			for(int y=0; y<newMap[x].length;y++){
//				newMap[x][y] = "Cross";
//			}
//		}
//		for(int y=0; y<startY; y++){
//			for(int x=0; x<newMap.length ; x++){
//				newMap[x][y] = "Cross";
//			}
//		}
//		for(int x = startX; x<newMap.length;x++){
//			for (int y=startY ;y<newMap[x].length; y++){
//				System.out.println("xy = " + x + ", " + y);
//				newMap[x][y] = map[x-startX][y-startY];
//			}
//		}
//		return newMap;
//	}
	
	public void readFile(String fileLocation){
	try{
		firstLine = -1;
		  FileInputStream fstream = new FileInputStream(fileLocation);
		  DataInputStream in = new DataInputStream(fstream);
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  String strLine;
		  int lineCounter = 0;
		  while ((strLine = br.readLine()) != null)   {
//		  	System.out.println (strLine);
			  readline(strLine, lineCounter);
			  lineCounter++;
		  }
		  in.close();
		    }
	catch (Exception e){
		e.printStackTrace();
		  System.err.println("Error reading maze.");
		  }
//	System.out.println("X,Y = " + startX + " " + startY);
	}
	
	
	public void readline(String strLine, int numberOfLine){
		if((strLine.startsWith("DeadEnd") || strLine.startsWith("Corner")
		|| strLine.startsWith("Straight") || strLine.startsWith("T.") || strLine.startsWith("Seesaw")
		|| strLine.startsWith("Closed") || strLine.startsWith("Cross"))){
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
	
//	// tiles van links naar rechts, boven naar onder
//	public void readTiles(List<Tile> tiles){
//		int width = 4; //TODO aanpassen aan echte dimensie!
//		int counter = 0;
//		for(Tile t : tiles){
//			int XCoo = counter%width;
//			int YCoo = counter/width;
//			String command = t.getToken();
//			readCommand(command,XCoo,YCoo);
//			counter++;
//		}
//	}
	
	public void readCommand(String command, int XCoo, int YCoo){
		YCoo = YCoo - firstLine;
		String[] commandSplit = command.split("\\.");
		if(!commandSplit[0].equals("Cross")){
			startX = Math.min(startX,XCoo);
			startY = Math.min(startY,YCoo);
		}
		
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
		else if(commandSplit[0].equals("Seesaw")){
			createSeesaw(XCoo, YCoo, Orientation.getOrientation(commandSplit[1]));
		}
		else if(commandSplit[0].equals("Closed")){
			createClosed(XCoo, YCoo);
		}
		if(commandSplit.length>2){
			if(commandSplit[2].equals("V")){
//				System.out.println("Object");
				createBall(XCoo, YCoo);
			}
			else if(commandSplit[2].contains("START")){
				// do nothing
			}
			else if(commandSplit[2].contains("S")){
				char[] args = commandSplit[2].toCharArray();
				createInitialPosition(XCoo,YCoo,String.valueOf(args[2]),Integer.parseInt(String.valueOf(args[1])));
			}
			else {
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
	
	public void createInitialPosition(int XCoo, int YCoo, String orientString, int playernb){
//		System.out.println("1 Xcoo "+XCoo+" YCoo"+YCoo);
		int MAZECONSTANT = MazeElement.getMazeConstant();
		XCoo = XCoo*MAZECONSTANT + MAZECONSTANT/2;
		YCoo = YCoo*MAZECONSTANT + MAZECONSTANT/2;
		Orientation orientation = Orientation.getOrientation(orientString);
//		System.out.println("1 Xcoo "+XCoo+" YCoo"+YCoo);
		Pose pos = new Pose(XCoo,YCoo,orientation);
		board.addInitialPosition(pos, playernb);
	}
	
	public void createClosed(int XCoo, int YCoo){
		Position pos = new Position(XCoo, YCoo);
		for(Orientation o : Orientation.values()){
			makeWall(pos, o);
		}
	}


	public void createDeadEnd(int XCoo, int YCoo, Orientation orientation){
		if(XCoo == 0 && YCoo == 6){
			int a =0;
		}
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
			board.addBarcode(new Barcode(dec, pos, orientation));
		}
		if((dec>10) && (dec<22) && (dec%2==1)){
			// barcode is voor wip
			barcodePositions.put(pos, dec);
		}
	}
	
	/**
	 * Opbouw mazefile: straight.Or.barcodenr seesaw.Or seesaw.Or
	 * straight.Or.barcodenr
	 * metr Or voor orientatie
	 * Uit voorzorg gebruiken we de zin die meegegeven is bij de seesaw niet, wel de richting.
	 *Voor infrarood-instellingsredenen willen we dat de orientatie zo is: lage barcode, seesaw, hoge barcode.
	 *Vb: orientatie West: links: hoge barcode, midden: seesaw, rechts: lage barcode
	 */
	
	private void createSeesaw(int xCoo, int yCoo, Orientation orientation){
		int MAZECONSTANT = MazeElement.getMazeConstant();
		xCoo = xCoo*MAZECONSTANT+(MAZECONSTANT/2);
		yCoo = yCoo*MAZECONSTANT+(MAZECONSTANT/2);
		final Position posSeesawPart = new Position(xCoo,yCoo);
		
		// We bepalen eerst aan welke kant van dit seesaw-stuk dat er een
		// barcode ligt en zetten de orientatie er naartoe gericht (wordt later
		// nog veranderd)
		Position posBarcodeTry1 = posSeesawPart.getNewPosition(
				orientation.getAngleToHorizontal(), MAZECONSTANT);
				//We snap to tile center because the getNewPosition Method can give none-rounded results
		posBarcodeTry1.snapTo(MAZECONSTANT,MAZECONSTANT/2, MAZECONSTANT/2);
		Position posBarcodeTry2 = posSeesawPart.getNewPosition(
				orientation.getAngleToHorizontal(), -MAZECONSTANT);
		posBarcodeTry2.snapTo(MAZECONSTANT,MAZECONSTANT/2, MAZECONSTANT/2);

		if(board.hasSeesawAt(posBarcodeTry1)||board.hasSeesawAt(posBarcodeTry2)){
			return;
		}
		Orientation towardsBarcodeOrientation = null;
		int barcodeNb;
		if (barcodePositions.get(posBarcodeTry1) != null) {
			towardsBarcodeOrientation = orientation;
			barcodeNb = barcodePositions.get(posBarcodeTry1);
		} else if (barcodePositions.get(posBarcodeTry2) != null) {
			towardsBarcodeOrientation = orientation.getBack();
			barcodeNb = barcodePositions.get(posBarcodeTry2);

		} else {
			throw new IllegalStateException("De mazefile klopt niet, er zou voor of na een seesawstuk een barcode te vinden moeten zijn");
		}	
		// Als er een lage barcode voor het te verwerken wipstuk staat tov
		// towardsBarcodeOrientation, dan moet de orientatie andersom zijn tov
		// towardsBarcodeOrientation.
		Orientation finalSeesawOrientation = Seesaw.isALowBcNb(barcodeNb) ? towardsBarcodeOrientation
				.getBack() : towardsBarcodeOrientation;
		final Position posSeesawCenter = posSeesawPart.getNewRoundPosition(
				towardsBarcodeOrientation.getBack().getAngleToHorizontal(), MAZECONSTANT / 2);
		if(!board.hasSeesawAt(posSeesawCenter)){
			board.addSeesaw(new Seesaw(posSeesawCenter, finalSeesawOrientation,barcodeNb, true,board));
		}
		//else wip staat er al
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
