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
import domain.maze.MazeElement;
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
		//drawWalls();
	}

	public static final int IMG_WIDTH = 350;
	public static final int IMG_HEIGHT = 500;

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
		int numberOfLines = 100;
		int lengthOfLines = 10000;
		for(int i=0; i<numberOfLines; i++){
			drawMyLine(-lengthOfLines,MAZECONSTANT*i,lengthOfLines,MAZECONSTANT*i, Color.gray);//horizontal
			drawMyLine(MAZECONSTANT*i,-lengthOfLines,MAZECONSTANT*i,lengthOfLines, Color.gray);//vertical
		}
	}
	
	public void drawWalls(){
		List<Wall> walls = controller.getRobot().getBoard().getWalls();
		for(Wall w: walls){
			drawWall(w);
		}
	}
	
	public void drawWall(Wall wall){
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
		g.setColor(Color.blue);
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