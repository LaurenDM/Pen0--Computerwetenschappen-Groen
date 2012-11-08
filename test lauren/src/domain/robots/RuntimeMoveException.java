package domain.robots;

import java.io.PrintStream;
import java.io.PrintWriter;

public class RuntimeMoveException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void printStackTrace(){
		//do nothing
	}
	
	@Override
	public void printStackTrace(PrintStream s){
		// nothing
	}
	
	@Override
	public void printStackTrace(PrintWriter s){
		//nothing
	}

}
