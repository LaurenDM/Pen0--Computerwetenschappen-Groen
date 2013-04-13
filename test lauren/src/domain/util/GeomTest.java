package domain.util;

import static org.junit.Assert.assertEquals;

import javax.swing.*;

import domain.Position.Position;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

public class GeomTest extends JPanel {
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setStroke(new BasicStroke(2.0f));

		for (int i = 0; i < 1000; i++) {
			// Graphics2D g2 = (Graphics2D) jPanel.getGraphics();
			g2.setPaint(Color.BLUE);
			int orientation = (int) (Math.random() * 180)
					* (Math.random() > 0.5 ? 1 : -1);
			int viewAngle = (int) (Math.random() * 360);
			double start = orientation - viewAngle / 2;
			double extent = viewAngle;
			int startX = (int) (Math.random() * 100);
			int startY = (int) (Math.random() * 100);
			Position viewPos = new Position(startX, startY);
			Arc2D arc2DPie = new Arc2D.Double(startX - 200, startY - 200, 400,
					400, start, extent, Arc2D.PIE);
			int checkX = (int) (Math.random() * 100);
			int checkY = (int) (Math.random() * 100);
			Point2D checkPoint2D = new Point2D.Double(checkX, checkY);
			Position checkPos = new Position(checkX, checkY);

			System.out.print(i + " cp:" + checkPos + " sp:" + viewPos + " o:"
					+ orientation + " va:" + viewAngle + " ca:"
					+ (int) viewPos.getAngleTo(checkPos));

			System.out.println(" exp:"
					+ arc2DPie.contains(checkPoint2D)
					+ " act:"
					+ Geometrics.fallsInCoveredArea(checkPos, viewPos,
							orientation, viewAngle));
			if (arc2DPie.contains(checkPoint2D) != Geometrics
					.fallsInCoveredArea(checkPos, viewPos, orientation,
							viewAngle)) {
				g2.fill(arc2DPie);
				g2.setPaint(Color.GREEN);
				g2.drawOval((int) arc2DPie.getCenterX(),
						(int) arc2DPie.getCenterY(), 15, 15);
				g2.setPaint(Color.RED);

				g2.drawOval((int) startX, (int) startY, 10, 10);
				g2.setPaint(Color.BLACK);
				g2.drawOval((int) checkX, (int) checkY, 10, 10);

				break;
			}

		}
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Draw Arch Demo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel applet = new GeomTest();
        frame.getContentPane().add(applet);
        frame.pack();
        frame.setSize(new Dimension(400, 450));
        frame.setVisible(true);
    }
}