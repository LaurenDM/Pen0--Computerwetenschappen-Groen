package domain.maze.barcodes;





import domain.robots.Robot;







public interface Action {

	
	public abstract void run(Robot robot);

	public abstract String toString();

	
	
}
