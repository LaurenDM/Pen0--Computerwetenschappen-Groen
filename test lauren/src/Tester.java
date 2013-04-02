import domain.robots.BTRobotPilot;
import domain.robots.RobotPilot;


public class Tester {
public static void main(String[] args) throws Exception{
	RobotPilot btRobot= new BTRobotPilot("bla");
	for(int i=0;i<8;i++){
		btRobot.move(90);
		}
		
}

}
