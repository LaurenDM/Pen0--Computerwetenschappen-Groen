package domain.util;

import java.awt.geom.Line2D;

import lejos.nxt.addon.AngleSensor;

import domain.Position.Position;

public class Geometrics {
	// Berekent of 2 lijnstukken een snijpunt hebben
	public static boolean linesIntersect(Position lineSeg1P1,
			Position lineSeg1P2, Position lineSeg2P1, Position lineSeg2P2) {
		double x11 = lineSeg1P1.getX();
		double y11 = lineSeg1P1.getY();
		double x12 = lineSeg1P2.getX();
		double y12 = lineSeg1P2.getY();

		double x21 = lineSeg2P1.getX();
		double y21 = lineSeg2P1.getY();
		double x22 = lineSeg2P2.getX();
		double y22 = lineSeg2P2.getY();
		return Line2D.linesIntersect(x11, y11, x12, y12, x21, y21, x22, y22);

	}

	public static boolean fallsInCoveredArea(Position toCheckPos,
			Position viewPos, double orientation, double viewAngle) {
		if (viewAngle >= 360) {
			return true;
		}
		
		final double toCheckAngle = calcAngle(toCheckPos, viewPos);
		return Math.abs(Angles.moderateTo180(toCheckAngle-orientation))<viewAngle/2;
	}

	static double calcAngle(Position toCheckPos, Position viewPos) {
		double toCheckYDiff = toCheckPos.getY() - viewPos.getY();
		double toCheckXDiff = toCheckPos.getX() - viewPos.getX();

		double tanToCheck = (toCheckYDiff) / (toCheckXDiff);
		final double checkPosAngleinRad;
		if(tanToCheck==0){
			if(toCheckXDiff>0){
				checkPosAngleinRad = Math.atan(tanToCheck);
			} else {
				checkPosAngleinRad = Math.atan(tanToCheck) + Math.PI;
			}
		}
		else if (tanToCheck < 0) {
			if (toCheckYDiff < 0) {
				checkPosAngleinRad = Math.atan(tanToCheck);
			} else {
				checkPosAngleinRad = Math.atan(tanToCheck) + Math.PI;
			}
		} else {// tanToCheck>0
			if (toCheckYDiff > 0) {
				checkPosAngleinRad = Math.atan(tanToCheck);
			} else {
				checkPosAngleinRad = Math.atan(tanToCheck) - Math.PI;
			}
		}
		return -checkPosAngleinRad*180/Math.PI;//De min is omdat het coordinatenstelsel linksBOVEN begint ipv linksonder
	}

}
