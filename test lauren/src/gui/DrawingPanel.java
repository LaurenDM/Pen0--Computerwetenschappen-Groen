package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.*;

import controller.Controller;
import domain.util.ColorPolygon;

public class DrawingPanel extends JPanel {
	private HashMap<ColorPolygon, Polygon> previousPolygons;
	private Controller controller;
	private JPanel totalGui;

	public DrawingPanel(ContentPanel contentPanel) {
		this.controller = contentPanel.getController();
		totalGui = contentPanel.getTotalGuiPanel();
		previousPolygons = new HashMap<ColorPolygon, Polygon>();
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
		Graphics g = image.getGraphics();
		g.setColor(color);
		g.drawLine(x1, y1, x2, y2);
		totalGui.repaint();
	}

	public Graphics getGraphics() {
		return image.getGraphics();
	}

	// This method makes the panel white agains
	public void clear() {
		Graphics g = image.getGraphics();
		g.setColor(Color.white);
		g.drawRect(0, 0, IMG_WIDTH, IMG_HEIGHT);
		totalGui.repaint();
	}

	public void reDrawMyPolygon(ColorPolygon colorPoly) {
		Graphics g = image.getGraphics();
		Polygon previousPoly = previousPolygons.get(colorPoly);
		if (previousPoly != null) {
			g.setColor(Color.gray);
			fillCenteredPolygon(previousPoly, g);
		}
		Polygon polygon = colorPoly.getPolygon();
		previousPolygons.put(colorPoly, polygon);
		g.setColor(colorPoly.getColor());
		fillCenteredPolygon(polygon, g);
		totalGui.repaint();
		
	}

	private void fillCenteredPolygon(Polygon originalPoly, Graphics g) {
		int npoints=originalPoly.npoints;
		Polygon centeredPoly= new Polygon();
		centeredPoly.npoints=npoints;
		for(int i=0;i<npoints; i++){
			centeredPoly.xpoints[i]=originalPoly.xpoints[i]+IMG_WIDTH/2;
			centeredPoly.ypoints[i]=originalPoly.ypoints[i]+IMG_HEIGHT/2;
		}
		g.fillPolygon(centeredPoly);
	}

}