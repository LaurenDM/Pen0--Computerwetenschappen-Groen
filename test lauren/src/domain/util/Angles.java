package domain.util;

public class Angles {
	//Takes an angle and give the equivalent angle between 180 and -180
	public static double moderateTo180(double rawAngle) {
		double moderatedAngle=rawAngle;
		while (moderatedAngle < -179) {
			moderatedAngle += 360;
		}
		while (moderatedAngle > 180) {
			moderatedAngle -= 360;
		}
		return moderatedAngle;
	}

}
