package domain.util;
/** 
 * this class is not used for the moment but could be usefull for debugging. I
 * if the interruptionAllowedVariable would be set to false, then statusUpdate-requests would be blocked 
 * @author duvimint
 *
 */
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
