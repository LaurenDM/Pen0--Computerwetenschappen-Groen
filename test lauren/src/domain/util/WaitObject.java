package domain.util;

public class WaitObject {
	private boolean hasReallyBeenNotified=false;
	public void setNotifiedTrue(){
		hasReallyBeenNotified=true;
	};
	public boolean hasReallyBeenNotified(){
		return hasReallyBeenNotified;
	}
}
