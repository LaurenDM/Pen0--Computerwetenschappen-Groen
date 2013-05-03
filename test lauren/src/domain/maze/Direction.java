package domain.maze;
public enum Direction {
    LEFT(-1,90.0),FORWARD(0,0.0),RIGHT(1,-90.0),BACKWARD(-2,180.0);
    private int offset;
    private double angle;
    private Direction(int offset, double angle){
    	this.offset = offset;
    	this.angle = angle;
    }
    public int getOffset(){
    	return this.offset;
    }
    
    
}