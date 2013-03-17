package domain.util;

public class WaitObject {
	private boolean hasReallyBeenNotified=false;
	private boolean notifyIsBlocked;
	public void setNotifiedTrue(){
		if(notifyIsBlocked)
			return;
		else{
		hasReallyBeenNotified=true;
		}
	};
	public boolean hasReallyBeenNotified(){
		return hasReallyBeenNotified;
	}

	public synchronized void customNotifyAll() {
		if(notifyIsBlocked){
			return;
		}
		setNotifiedTrue();
		this.notifyAll();
	}

	public synchronized void customWait() {
		while (!hasReallyBeenNotified() && !Thread.interrupted())
			try {
				this.wait();
			} catch (InterruptedException e) {
				return;
			}
	}
	public void blockNotify(){
		notifyIsBlocked=true;
	}
	public  void unBlockNotify(){
		notifyIsBlocked=false;

	}
 
}
