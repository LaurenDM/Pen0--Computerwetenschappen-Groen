package exceptions;

public class ConnectErrorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2405490827510302514L;
@Override
public String getMessage() {
	return "The program was unable to make a connection with the robot";
}
}
