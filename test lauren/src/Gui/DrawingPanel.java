package Gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class DrawingPanel extends JPanel
{

public static final int IMG_WIDTH = 400;
public static final int IMG_HEIGHT = 400;

   private BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT,
            BufferedImage.TYPE_INT_ARGB);
   
  protected void paintComponent(Graphics g) 
  {
      super.paintComponent(g);
      if (image != null) {
         g.drawImage(image, 0, 0, null);
      }
  }
  
public void drawMyLine(int x1, int y1, int x2, int y2) {
 Graphics g = image.getGraphics();
    g.setColor(Color.black);
    g.drawLine(x1, y1, x2, y2);
    this.repaint();
}
public Graphics getGraphics()
{
 return image.getGraphics();
}
}