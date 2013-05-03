package domain.maze;
public enum Direction {
    LEFT(-1),FORWARD(0),RIGHT(1),BACKWARD(-2);
    private int offset;
    private Direction(int offset){
    	this.offset = offset;
    }
    public int getOffset(){
    	return this.offset;
    }
}