package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import controller.Controller;
import domain.Position.Position;
import domain.barcodes.Barcode;
import domain.maze.MazeElement;
import domain.maze.Orientation;
import domain.maze.Wall;
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
	}

	public static final int IMG_WIDTH = 700;
	public static final int IMG_HEIGHT = 600;

	private BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT,
			BufferedImage.TYPE_INT_ARGB);

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
		List<Wall> walls = controller.getRobot().getBoard().getWalls();
		for(Wall w: walls){
			drawWall(w, true);
		}
		drawSimulatedBarcodes();
	}
	
	public void drawFoundWalls(){
		List<Wall> walls = controller.getRobot().getBoard().getFoundWalls();
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
			g.setColor(Color.black);
		}
		g.drawPolygon(pol);
		g.fillPolygon(pol);
		totalGui.repaint();
		
	}
	
	public void drawSimulatedBarcodes(){
		List<Barcode> barcodes = controller.getRobot().getBoard().getSimulatedBarcodes();
		for(Barcode b: barcodes){
			drawBarcode(b, true);
		}
	}
	
	public void drawFoundBarcodes(){
		List<Barcode> barcodes = controller.getRobot().getBoard().getFoundBarcodes();
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

	// This method makes the panel white agains
	public void clear() {
		g.setColor(Color.white);
		g.fillRect(0, 0, IMG_WIDTH, IMG_HEIGHT);
		totalGui.repaint();
	}

	public void reDrawMyPolygon(ColorPolygon colorPoly) {
		Polygon previousPoly = previousPolygons.get(colorPoly);
		if (previousPoly != null) {
			g.setColor(Color.gray);
			fillCenteredPolygon(previousPoly);
		}
		Polygon polygon = colorPoly.getPolygon();
		previousPolygons.put(colorPoly, polygon);
		g.setColor(colorPoly.getColor());
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
//			centeredPoly.xpoints[i]=originalPoly.xpoints[i]+IMG_WIDTH/2;
//			centeredPoly.ypoints[i]=originalPoly.ypoints[i]+IMG_HEIGHT/2;
		}
		g.fillPolygon(centeredPoly);
	}

}