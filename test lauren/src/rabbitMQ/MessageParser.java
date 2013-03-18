package rabbitMQ;

import controller.Controller;
import domain.robots.RobotPilot;

public class MessageParser {
	
		private Controller controller;
		
		private final String DELIMITER = "_";
	
		public MessageParser(Controller controller){
			this.controller = controller;
		}
		
		public void parse(String routingKey, String body) {
//			System.out.println(routingKey);
			if(!body.contains("start")){
				int identifier = Integer.parseInt(routingKey.split(DELIMITER)[1]);
			RobotPilot robot_temp = controller.getRobotFromIdentifier(identifier);
			if(robot_temp!=null){ 
				if(body.contains("Ball")){
//					controller
					robot_temp.setFoundBall(identifier);
					System.out.println("Ball found--------------------------");
				}
				else{
					robot_temp.setPose((int)Double.parseDouble(body.split(DELIMITER)[2]), (int)Double.parseDouble(body.split(DELIMITER)[0]), (int)Double.parseDouble(body.split(DELIMITER)[1]));
				}
			}
		}
	}
}