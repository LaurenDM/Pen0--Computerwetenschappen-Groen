import domain.robots.BTRobotPilot;
import domain.robots.Robot;


public class Tester {
public static void main(String[] args) throws Exception{
	Robot btRobot=new Robot( new BTRobotPilot(), 0);
	for(int i=0;i<8;i++){
		btRobot.move(90);
		}
		
}

}
