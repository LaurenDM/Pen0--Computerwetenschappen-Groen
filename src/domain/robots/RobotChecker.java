package domain.robots;

public class RobotChecker {

	
	private static boolean interruptionAllowed=true;

	public static boolean interruptionAllowed() {
		// TODO Auto-generated method stub
		return interruptionAllowed;
	}
	public static void allowInterruption(){
		interruptionAllowed=true;
	};
	
	public static void disAllowInterruption(){
		interruptionAllowed=false;
	};
	

}
