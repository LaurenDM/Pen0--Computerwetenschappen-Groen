import lejos.robotics.localization.OdometryPoseProvider;


public class Position {
	private OdometryPoseProvider OPP;
	public Position(OdometryPoseProvider OPP){
		this.OPP=OPP;
	}
public float getX(){
	return OPP.getPose().getX();}
public float getY(){
	return  OPP.getPose().getY();}
public float getAngle(){
	return  OPP.getPose().getHeading();}
}
