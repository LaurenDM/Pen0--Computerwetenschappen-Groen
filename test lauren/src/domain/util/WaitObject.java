package domain.util;

public class WaitObject {
	private boolean hasReallyBeenNotified=false;
	public void setNotifiedTrue(){
		hasReallyBeenNotified=true;
	};
	public boolean hasReallyBeenNotified(){
		return hasReallyBeenNotified;
	}
	public synchronized void  customWait() {
		while(!hasReallyBeenNotified()&&!Thread.interrupted())
			try {
				wait();
			} catch (InterruptedException e) {
				return;
			}
		}
}
