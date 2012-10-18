package gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.*;

import controller.Controller;

public class DrawingPanel extends JPanel
{
private Controller controller;
private JPanel totalGui;
public DrawingPanel(ContentPanel contentPanel) {
	this.controller=contentPanel.getController();
	totalGui= contentPanel.getTotalGuiPanel();
}


public static final int IMG_WIDTH = 350;
public static final int IMG_HEIGHT = 500;

   private BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT,
            BufferedImage.TYPE_INT_ARGB);
   
  protected void paintComponent(Graphics g) 
  {
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
public Graphics getGraphics()
{
 return image.getGraphics();
}

//This method makes the panel white agains
public void clear() {
	// TODO Auto-generated method stub
	
}

}