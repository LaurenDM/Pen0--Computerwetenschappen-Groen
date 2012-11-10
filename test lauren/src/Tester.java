import domain.robots.BTRobotPilot;
import domain.robots.Robot;


public class Tester {
public static void main(String[] args) throws Exception{
	Robot btRobot=new Robot( new BTRobotPilot());
	for(int i=0;i<8;i++){
		btRobot.setTurningSpeed(90);
		btRobot.turn(90);
		}
		
}

}
