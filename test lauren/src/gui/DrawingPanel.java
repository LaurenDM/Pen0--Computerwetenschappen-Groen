package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import controller.Controller;
import domain.Position.Position;
import domain.maze.Ball;
import domain.maze.MazeElement;
import domain.maze.Orientation;
import domain.maze.Seesaw;
import domain.maze.Wall;
import domain.maze.barcodes.Barcode;
import domain.maze.graph.MazePath;
import domain.maze.graph.TileNode;
import domain.util.ColorPolygon;

public class DrawingPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<ColorPolygon, Polygon> previousPolygons;
	private Controller controller;
	private JPanel totalGui;
	private Graphics g;
	private final int OFFSET = 80; //offset of (0,0) against top left corner
	public DrawingPanel(ContentPanel contentPanel) {
		g=image.getGraphics();
		this.controller = contentPanel.getController();
		totalGui = contentPanel.getTotalGuiPanel();
		previousPolygons = new HashMap<ColorPolygon, Polygon>();
		drawWhiteLines();
//		drawWalls();
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				int x = e.getX()-OFFSET;
				int y = e.getY()-OFFSET;
				controller.mousePressed(x,y);
//				System.out.println(x+" "+y);
			}
			
			public void mouseReleased(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
		});
		
			
	}

	public static final int IMG_WIDTH = 700;
	public static final int IMG_HEIGHT = 600;

	private BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT,
			BufferedImage.TYPE_INT_ARGB);

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null) {
			g.drawImage(image, 0, 0, null);
		}
	}
	
	

	public void drawMyLine(int x1, int y1, int x2, int y2, Color color) {
		g.setColor(color);
		g.drawLine(x1, y1, x2, y2);
		totalGui.repaint();
	}
	
	public void drawWhiteLines(){
		int MAZECONSTANT = MazeElement.getMazeConstant();
		int numberOfLines = 1000;
		int lengthOfLines = 100000;
		for(int i=0; i<numberOfLines; i++){
			drawMyLine(-lengthOfLines,MAZECONSTANT*i,lengthOfLines,MAZECONSTANT*i, Color.gray);//horizontal
			drawMyLine(MAZECONSTANT*i,-lengthOfLines,MAZECONSTANT*i,lengthOfLines, Color.gray);//vertical
		}
	}
	
	public void drawSimulatedWalls(){
		List<Wall> walls = controller.getRobot().getWorldSimulator().getWalls();
		for(Wall w: walls){
			drawWall(w, true);
		}
		drawSimulatedBarcodes();
		drawSeesaws();
	}
	
	public void drawFoundWalls(){
		List<Wall> walls = controller.getRobot().getBoard().getWalls();
		try {
			for(Wall w: walls){
			drawWall(w, false);
			}
		} catch (Exception e) {
			//Gets called if no wall is currently found -> nullpoint expected.
		}
		
	}
	
	public void drawWall(Wall wall, boolean simulated){
		Polygon pol = new Polygon();
	//	Position pos = null;
		int pos1X = (int)wall.getPos1().getX()+OFFSET;
		int pos1Y = (int)wall.getPos1().getY()+OFFSET;
		int pos2X = (int)wall.getPos2().getX()+OFFSET;
		int pos2Y = (int)wall.getPos2().getY()+OFFSET;

		if(wall.getPos1().getY()==wall.getPos2().getY()){
			pol.addPoint(pos1X, pos1Y+1);
			pol.addPoint(pos1X, pos1Y-1);
			pol.addPoint(pos2X, pos2Y-1);
			pol.addPoint(pos2X, pos2Y+1);
		}
		else if(wall.getPos1().getX()==wall.getPos2().getX()){
			pol.addPoint(pos1X+1, pos1Y);
			pol.addPoint(pos1X-1, pos1Y);
			pol.addPoint(pos2X-1, pos2Y);
			pol.addPoint(pos2X+1, pos2Y);	
		}
		if(simulated){
			g.setColor(Color.lightGray);
		}
		else{
			boolean goodWall = false;
			List<Wall> simWalls = controller.getRobot().getWorldSimulator().getWalls();
			if(simWalls.size()>0){
				for(Wall w: simWalls){
					if(w.hasPosition(wall.getCenterPosition())){
						goodWall = true;
					}
				}
			}
			if(simWalls.size()<1){
				goodWall = true;
			}
			if(!goodWall){
				g.setColor(Color.red);
			}
			else
			g.setColor(Color.black);
		}
		g.drawPolygon(pol);
		g.fillPolygon(pol);
		totalGui.repaint();
		
	}
	
	public void drawSimulatedBarcodes(){
		List<Barcode> barcodes = controller.getRobot().getWorldSimulator().getSimulatedBarcodes();
		for(Barcode b: barcodes){
			drawBarcode(b, true);
		}
	}
	
	public void drawFoundBarcodes(){
		List<Barcode> barcodes = controller.getRobot().getBoard().getBarcodes();
		for(Barcode b: barcodes){
			drawBarcode(b, false);
		}
	}
	
	public void drawBarcode(Barcode barcode, boolean simulated){
		Polygon pol = new Polygon();
		Orientation orientation = barcode.getOrientation();
		int posX = (int)barcode.getPos().getX()+OFFSET;
		int posY = (int)barcode.getPos().getY()+OFFSET;
		
		if(orientation == Orientation.NORTH || orientation == Orientation.SOUTH){
			pol.addPoint(posX-20, posY-8);
			pol.addPoint(posX+20, posY-8);
			pol.addPoint(posX+20, posY+8);
			pol.addPoint(posX-20, posY+8);
		}
		
		else if(orientation == Orientation.EAST || orientation == Orientation.WEST){
			pol.addPoint(posX-8, posY-20);
			pol.addPoint(posX+8, posY-20);
			pol.addPoint(posX+8, posY+20);
			pol.addPoint(posX-8, posY+20);
		}
		
		if(simulated){
			g.setColor(Color.lightGray);
		}
		else{
			g.setColor(Color.black);
		}
		g.drawPolygon(pol);
		g.fillPolygon(pol);
		totalGui.repaint();
		if(orientation == Orientation.NORTH || orientation == Orientation.SOUTH){
			drawHorizontalWhiteLines(barcode);
		}
		else{
			drawVerticalWhiteLines(barcode);
		}
	}
	
	public void drawSeesaws(){
		List<Seesaw> seesaws = controller.getRobot().getWorldSimulator().getSeesaws();
		for(Seesaw s : seesaws){
			drawSeesaw(s,true);
		}
	}
	
	public void drawInfraredPositions(){
		List<Seesaw> seesaws = controller.getRobot().getWorldSimulator().getSeesaws();
		for(Seesaw s : seesaws){
			drawSeesawInfrared(s);
		}
	}
	
	public void drawFoundSeesaws(){
		List<Seesaw> seesaws = controller.getRobot().getBoard().getSeesaws();
		for(Seesaw s : seesaws){
			drawSeesaw(s,false);
		}
	}
	
	public void drawSeesaw(Seesaw seesaw, boolean simulated){
		Polygon pol = new Polygon();
		int posX = (int) (seesaw.getCenterPosition().getX() + OFFSET);
		int posY = (int) (seesaw.getCenterPosition().getY() + OFFSET);
		
		int xRange,yRange;
		if(seesaw.getOrientation().equals(Orientation.EAST) || seesaw.getOrientation().equals(Orientation.WEST)){
			xRange = 40; yRange = 20;
		}
		else{
			xRange = 20; yRange = 40;
		}
		
		pol.addPoint(posX-xRange, posY-yRange);
		pol.addPoint(posX+xRange, posY-yRange);
		pol.addPoint(posX+xRange, posY+yRange);
		pol.addPoint(posX-xRange, posY+yRange);
		
		if(simulated){
			g.setColor(Color.lightGray);
		}
		else{
			g.setColor(Color.darkGray);
		}
		g.drawPolygon(pol);
		g.fillPolygon(pol);
		
		
		totalGui.repaint();
	}
	
	public void drawSeesawInfrared(Seesaw seesaw){
		int posX = (int) (seesaw.getInfaredPosition().getX() + OFFSET);
		int posY = (int) (seesaw.getInfaredPosition().getY() + OFFSET);
		g.setColor(Color.red);
		g.drawOval(posX, posY, 5, 5);
		g.fillOval( posX, posY, 5, 5);
		
		totalGui.repaint();
	}
	
	public void drawHorizontalWhiteLines(Barcode barcode){
		int LINEWIDTH = 2;
		Orientation orientation = barcode.getOrientation();
		int posXLowestLine = (int)barcode.getPos().getX()+OFFSET; 
		int posYLowestLine = (int)barcode.getPos().getY()+OFFSET+6; //lowest line NOT including the outer black lines
		int[] bits = barcode.getBits();

		if(orientation == Orientation.NORTH){
			for(int i=0; i<bits.length; i++){
				if(bits[i]==1){
					drawWhiteLine(posXLowestLine-20, posYLowestLine-LINEWIDTH*i-1, posXLowestLine+20, posYLowestLine-LINEWIDTH*(i+1));
				}
			}
		}
		else if(orientation == Orientation.SOUTH){
			for(int i=0; i<bits.length; i++){
				if(bits[i]==1){
					drawWhiteLine(posXLowestLine-20, posYLowestLine-12+LINEWIDTH*(i+1)-1 , posXLowestLine+20, posYLowestLine-12+LINEWIDTH*i);
				}
			}
		}
	}
	
	public void drawVerticalWhiteLines(Barcode barcode){
		int LINEWIDTH = 2;
		Orientation orientation = barcode.getOrientation();
		int posXLowestLine = (int)barcode.getPos().getX()+OFFSET-6; 
		int posYLowestLine = (int)barcode.getPos().getY()+OFFSET; //lowest line NOT including the outer black lines
		int[] bits = barcode.getBits();

		if(orientation == Orientation.EAST){
			for(int i=0; i<bits.length; i++){
				if(bits[i]==1){
					drawWhiteLine(posXLowestLine+LINEWIDTH*i, posYLowestLine+20, posXLowestLine+LINEWIDTH*(i+1)-1, posYLowestLine-20);
				}
			}
		}
		else if(orientation == Orientation.WEST){
			for(int i=0; i<bits.length; i++){
				if(bits[i]==1){
					drawWhiteLine(posXLowestLine+12-LINEWIDTH*i, posYLowestLine+20, posXLowestLine+12-LINEWIDTH*(i+1)+1, posYLowestLine-20);
				}
			}
		}
	}
	
	//used for barcodes
	public void drawWhiteLine(int pos1X, int pos1Y, int pos2X, int pos2Y){
		Polygon pol = new Polygon();
		pol.addPoint(pos1X, pos1Y);
		pol.addPoint(pos2X, pos1Y);
		pol.addPoint(pos2X, pos2Y);
		pol.addPoint(pos1X, pos2Y);
		g.setColor(Color.white);
		g.drawPolygon(pol);
		g.fillPolygon(pol);
		totalGui.repaint();
	}

	@Override
	public Graphics getGraphics() {
		return image.getGraphics();
	}
	
	public void drawRawSensorData(){
		double distance = controller.readUltrasonicValue();
		if(distance<250){
		int orientation = ((int) controller.getAngle()) + controller.getSensorAngle();
		Position pos = controller.getPosition().getNewPosition(orientation, distance);
		Polygon pol = new Polygon();
		int x = (int) pos.getX() + OFFSET; 
		int y = (int) pos.getY() + OFFSET;
		pol.addPoint(x, y);
		pol.addPoint(x, y+1);
		pol.addPoint(x-1, y+1);
		pol.addPoint(x-1, y);
		g.setColor(Color.red);
//		g.drawLine(x, y, x, y);
		g.drawPolygon(pol);
		g.fillPolygon(pol);
		totalGui.repaint();
		}
	}
	
	private boolean drawn;
	private Position initPos;
	boolean firstPos = true;
	private int zeroX;
	private int zeroY;
	private double beginOrient = 9000;
	
	public void drawLineToFinish(){
		try{
			if(beginOrient == 9000)
			beginOrient = controller.getRobot().getOrientation();
		}
		catch(Exception e){
			
		}
				
		try{
			
			controller.getRobot().getPathToFinish();
			if(controller.getRobot().getPathToFinish()!=null){
				initPos = controller.getRobot().getPosition();
				drawn = true;
				MazePath shortestPath = controller.getRobot().getPathToFinish();
				Iterator<TileNode> tileIt = shortestPath.iterator();
				if(tileIt.hasNext()){
					TileNode nextNode1 = tileIt.next();
					while(tileIt.hasNext()){
						TileNode nextNode2 = tileIt.next();
						Polygon pol = new Polygon();
						int MAZECONSTANT = 40;
						int robotX =(int) (Math.floor((initPos.getX())/MAZECONSTANT))*MAZECONSTANT + 20;
						int robotY =(int) (Math.floor((initPos.getY())/MAZECONSTANT))*MAZECONSTANT + 20;
						
						int x1=0; int x2=0; int y1=0; int y2 = 0;
						
						if(beginOrient<45 && beginOrient>-45){
						
						if(firstPos){
						zeroX = (robotX - nextNode1.getY()*40);
						zeroY = (robotY - nextNode1.getX()*40);
						//System.out.println("ZEROPOS "+zeroX+" "+zeroY);
						firstPos = false;
						}
						
						//oost
						x1 = Math.abs(nextNode1.getY()*40 + OFFSET + zeroX); 
						y1 = Math.abs(nextNode1.getX()*40 + OFFSET + zeroY);
						x2 = Math.abs(nextNode2.getY()*40 + OFFSET + zeroX); 
						y2 = Math.abs(nextNode2.getX()*40 + OFFSET + zeroY);
						//System.out.println("POS "+x1+" "+y1+";  "+x2+" "+y2);
						
						}
//						
//						else if(beginOrient<-135 && beginOrient>135){
//							
//							System.out.println("lus 2");
//							if(firstPos){
//							System.out.println("ROBOTPOS:"+robotX+" "+robotY);
//							zeroX = (robotX - Math.abs((int) nextNode1.getX()*40));
//							zeroY = (robotY - Math.abs((int) nextNode1.getY()*40));
//							System.out.println("ZEROPOS "+zeroX+" "+zeroY);
//							firstPos = false;
//							}
//							
//							//oost
//							x1 = Math.abs((int) nextNode1.getX()*40) + OFFSET + zeroX; 
//							y1 = Math.abs((int) nextNode1.getY()*40) + OFFSET + zeroY;
//							x2 = Math.abs((int) nextNode2.getX()*40) + OFFSET + zeroX; 
//							y2 = Math.abs((int) nextNode2.getY()*40) + OFFSET + zeroY;
//							System.out.println("POS "+(x1-OFFSET)+" "+(y1-OFFSET)+";  "+(x2-OFFSET)+" "+(y2-OFFSET));
//							
//							}

					
						if(y1==y2){
							pol.addPoint(x1, y1+1);
							pol.addPoint(x1, y1-1);
							pol.addPoint(x2, y2-1);
							pol.addPoint(x2, y2+1);
						}
						else if(x1==x2){
							pol.addPoint(x1+1, y1);
							pol.addPoint(x1-1, y1);
							pol.addPoint(x2-1, y2);
							pol.addPoint(x2+1, y2);

						}
						g.setColor(Color.red);
						g.drawPolygon(pol);
						g.fillPolygon(pol);
						totalGui.repaint();
						nextNode1 = nextNode2;
					}
				}
				}
		}
		catch(Exception e){
			
		}
		
	}
	
	
	public void drawBalls(){
		List<Ball> balls = controller.getRobot().getWorldSimulator().getBalls();
		for(Ball b: balls){
			drawBall(b);
		}
	}
	
	public void drawBall(Ball ball){
		g.setColor(Color.red);
		g.fillOval((int)ball.getPosition().getX()-5+OFFSET, (int)ball.getPosition().getY()-5+OFFSET, 10, 10);
	}
	
	public void removeBall(Ball ball){
		g.setColor(Color.white);
		try {
			g.fillOval((int)ball.getPosition().getX()-5+OFFSET, (int)ball.getPosition().getY()-5+OFFSET, 10, 10);
		} catch (Exception e) {}
		
	}
	

	// This method makes the panel white agains
	public void clear() {
		g.setColor(Color.white);
		g.fillRect(0, 0, IMG_WIDTH, IMG_HEIGHT);
		previousPolygons.clear();
		totalGui.repaint();
	}

	public void reDrawMyPolygon(ColorPolygon colorPoly, Color color) {
		Polygon previousPoly = previousPolygons.get(colorPoly);
		if (previousPoly != null) {
			g.setColor(new Color(240, 240, 240));
			fillCenteredPolygon(previousPoly);
		}
		Polygon polygon = colorPoly.getPolygon();
		previousPolygons.put(colorPoly, polygon);
		g.setColor(color);
		fillCenteredPolygon(polygon);
		totalGui.repaint();
		
	}

	private void fillCenteredPolygon(Polygon originalPoly) {
		int npoints=originalPoly.npoints;
		Polygon centeredPoly= new Polygon();
		centeredPoly.npoints=npoints;
		for(int i=0;i<npoints; i++){
			centeredPoly.xpoints[i]=originalPoly.xpoints[i]+OFFSET;
			centeredPoly.ypoints[i]=originalPoly.ypoints[i]+OFFSET;
		}
		g.fillPolygon(centeredPoly);
	}
	
	public void reset(){
		image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT,
				BufferedImage.TYPE_INT_ARGB);
		g=image.getGraphics();
		previousPolygons = new HashMap<ColorPolygon, Polygon>();
		drawWhiteLines();
		
		drawn = false;
		firstPos = true;
		beginOrient = 9000;
	}
	
	

}

