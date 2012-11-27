package domain.barcodes;





import domain.robots.Robot;







public abstract class Action {

	
	public abstract void run(Robot robot);
	
	
	
	
	
	
	public abstract int[] getActionNb();
	
	public abstract String toString();
	
	
}
