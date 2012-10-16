import domain.PolygonDriver;
import domain.robots.BTRobot;
import domain.robots.Robot;


public class Tester {
public static void main(String[] args) throws Exception{
	Robot btRobot=new BTRobot();
	for(int i=0;i<8;i++){
		btRobot.setTurningSpeed(90);
		btRobot.turn(90);
		}
		
}

}
