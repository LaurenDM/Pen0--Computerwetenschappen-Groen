package domain.util;

import java.awt.geom.Line2D;

import domain.Position.Position;

public class IntersectionCalc {
	//Berekent of 2 lijnstukken een snijpunt hebben
public static boolean linesIntersect(Position lineSeg1P1,Position lineSeg1P2,Position lineSeg2P1,Position lineSeg2P2){
	double x11= lineSeg1P1.getX();
	double y11= lineSeg1P1.getY();
	double x12= lineSeg1P2.getX();
	double y12= lineSeg1P2.getY();
	
	double x21= lineSeg2P1.getX();
	double y21= lineSeg2P1.getY();
	double x22= lineSeg2P2.getX();
	double y22= lineSeg2P2.getY();
	return	Line2D.linesIntersect(x11, y11, x12, y12, x21, y21, x22, y22);

}

}
