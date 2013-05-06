package bluetooth;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.bluetooth.BluetoothStateException;

import domain.robots.BTRobotPilot;
import domain.util.WaitObject;

import exceptions.ConnectErrorException;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;

public class BTCommPC  implements SpecialReplyCode {

	private static final int MAXONEREPLYLENGTH = 10;
	private NXTComm _nxtComm;
	private NXTInfo _nxtInfo;
	private DataOutputStream _dos;
	private DataInputStream _dis;
	private boolean _opened;
	private boolean _closed;
	private boolean sendingIsBlocked;
	//We set this to null to say that there is no buffered command.
	//	private long lastWaitingTicketNumber=0;
	//	private long firstValidTicket=1;
	private Queue<WaitObject> waitObjectQueue= new LinkedList<WaitObject>();
	private WaitObject toBeWokenObject=null;
	private BTRobotPilot robot;
	private List<WaitObject> replyWaitList= new ArrayList<WaitObject>();

	public boolean sendingIsPossible() {
		return !sendingIsBlocked;
	}

	public BTCommPC(BTRobotPilot robot){
		this.robot = robot;
	}
	public synchronized void blockSending(){
		sendingIsBlocked=true;
	};

	public synchronized void unblockSending(){
		sendingIsBlocked=false;
	};

	public boolean open(String deviceName, String BTaddress){

		//nxtComm = null;
		_opened = false;

		try {
			_nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			NXTConnector conn = new NXTConnector();
			NXTInfo[] nxtInfo = conn.search(deviceName, BTaddress,
					NXTCommFactory.BLUETOOTH);
			_nxtInfo = nxtInfo[0];
		} catch (NXTCommException e) {
			throw new ConnectErrorException();
		}
		try {
			_opened = _nxtComm.open(_nxtInfo);
		} catch (NXTCommException e) {
			throw new ConnectErrorException();
		} finally {
			if (!_opened) {
				throw new ConnectErrorException();
			}
		}

		InputStream is = _nxtComm.getInputStream();
		OutputStream os = _nxtComm.getOutputStream();

		_dos = new DataOutputStream(os);
		_dis = new DataInputStream(is);

		return _opened;
	}

	public boolean close() {

		_closed = false;
		try {
			_dis.close();
			_dos.close();
			_nxtComm.close();
		} catch (IOException ioe) {
			System.out.println("something went wrong");
			return _closed;
		}

		_closed = true;
		return _closed;
	}

	public DataOutputStream getOutputStream() {
		return _dos;
	}

	public DataInputStream getInputStream() {
		return _dis;
	}

	public boolean isOpened() {
		return _opened;
	}

	public boolean isClosed() {
		return _closed;
	}

	public int[] sendCommand(int command) {
		return sendCommand(command, 0, null);
	}

	public int[] sendCommand(int command, double argument) {
		return sendCommand(command, (int) (100 * argument), null);
	}

	public int[] sendCommand(int command, double argument,
			WaitObject replyWaiter) {
		return sendCommand(command, (int) (100 * argument), replyWaiter);
	}

	private int[] sendCommand(int command, int argument, WaitObject replyWaiter) {
		try {
			// TODO kijken of we deze code nog willen, is eerder voor debug
			// We testen even of alle tickets al gebruikt zijn.
			if (sendingIsPossible() && !waitObjectQueue.isEmpty()) {
				try {
					System.out
							.println("Something has gone wrong, we are going to wait a little");
					// We wachten hier 10 milliseconden om de wachtende
					// send-commands de tijd te geven om toch uit te voeren
					// indien
					// ze een ticket hebben dat aan de beurt zou komen.
					Thread.sleep(10);
					// Nu kijken we nogeens of we nogaltijd in deze foute staat
					// zitten.
					if (sendingIsPossible() && !waitObjectQueue.isEmpty()) {
						takeNextWaiter();
						System.out
								.println("Something has gone wrong, we forcibly denied a send request and alarmed a waitingObject");
					}
				} catch (InterruptedException e) {
					System.out.println("interrupted exception:"+ e.getMessage());
				}

			}//TODO tot hier mss wegdoen

			if (!sendingIsPossible()) {
				if (command == CMD.GETPOSE || command == CMD.GETSENSORVALUES) {
					return null;
				}

				WaitObject thisThreadsWaitObject = new WaitObject();
				synchronized (waitObjectQueue) {
					waitObjectQueue.add(thisThreadsWaitObject);
				}
				try {
					while (!thisThreadsWaitObject.hasReallyBeenNotified()) {
						{
							synchronized (thisThreadsWaitObject) {
								thisThreadsWaitObject.wait();
							}
						}
					}
					toBeWokenObject = null;
				} catch (InterruptedException e) {
					// This is not a problem, because in this case the command
					// doesn't have to be sent anymore.
					synchronized (waitObjectQueue) {
						waitObjectQueue.remove(thisThreadsWaitObject);
					}
					System.out.println("There was an interrupt code 125i(random code Xp)");
				}
			}

			blockSending();
			int[] reply = null;
			// We try to send the command two times, if this would fail then
			// bluetooth connection is failing
			try {
				reply = sendCommandForReal(command, argument);
			} catch (BluetoothStateException e) {
				try {
					reply = sendCommandForReal(command, argument);
				} catch (BluetoothStateException e2) {
					throw new BluetoothStateException();
				}
			}
			
			int[] returnReply = new int[0];
			int k = 2;
			int numberOfSpecialReplies = 0;
			if (reply != null && reply.length > 0 && reply[0] > 0) {
				numberOfSpecialReplies = reply[0] - 1;

				// reply[1] should contain the first ReplyLenght, and only the
				// first reply is an answer to this sendCommand call, the others
				// are special replies
				returnReply = new int[reply[1]];
				for (int i = 0; i < returnReply.length; i++) {
					returnReply[i] = reply[k++];
				}
			}
			Thread isMoveUpdater = null;
			cleanReplyWaitList();
			List<WaitObject> blockedWaiters = null;
			if (numberOfSpecialReplies > 0) {
				isMoveUpdater=new Thread(new Runnable() {
					
					@Override
					public void run() {
						while(!Thread.interrupted()){
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								System.out.println("There was an interrupt exception, code 987e");
							}
							//TODO isMoving aanmaken in robot
							robot.getOrientation();
						}
					}
				});
				isMoveUpdater.start();
				blockedWaiters = blockReplyWaiters();
			}
			if (!waitObjectQueue.isEmpty()) {
				takeNextWaiter();
			} else {
				unblockSending();
			}
			for (int i = 0; i < numberOfSpecialReplies; i++) {
				int SpecialReplyCode = reply[k++];
				int[] specialArgs = new int[reply[k++]];
				for (int j = 0; j < specialArgs.length; j++) {
					specialArgs[j] = reply[k++];
				}
				switchOnSpecialReply(SpecialReplyCode, specialArgs);

			}
			if (numberOfSpecialReplies > 0) {
				unBlockReplyWaiters(blockedWaiters);
			}

			if (replyWaiter != null) {
				replyWaitList.add(replyWaiter);
				replyWaiter.customWait();
			}
			if(isMoveUpdater!=null){
			isMoveUpdater.interrupt();}
			return returnReply;
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getClass()+" "+e.getMessage());
			System.out.println();
			throw new RuntimeException(
					"something is wrong with your code or the robot is disconnected");
		}

	}

	private void cleanReplyWaitList() {
		if(replyWaitList.isEmpty())
			return;
		synchronized (replyWaitList) {
		for(int i=0;i<replyWaitList.size();i++){
			if (replyWaitList.get(i).hasReallyBeenNotified()) {
				replyWaitList.remove(i);
			}
		}
	}
	}

	private void unBlockReplyWaiters(List<WaitObject> blockedList) {
		if (blockedList == null || blockedList.isEmpty()) {
			return;
		}
		synchronized (replyWaitList) {
			for (WaitObject blockedWaiter : blockedList) {
				blockedWaiter.unBlockNotify();
				replyWaitList.remove(blockedWaiter);
			}
		}

	}

	private List<WaitObject> blockReplyWaiters() {
		List<WaitObject> blockedList = new ArrayList<WaitObject>();
		synchronized (replyWaitList) {
			for (int i=0;i< replyWaitList.size(); i++) {
				WaitObject waitObject= replyWaitList.get(i);
				waitObject.blockNotify();
				blockedList.add(waitObject);				
			}
		}
		return blockedList;
	}

	private void takeNextWaiter() {
		synchronized (waitObjectQueue) {
			synchronized (waitObjectQueue.peek()) {
				if (toBeWokenObject != null) {
					System.out.println(" the toBeWokenObject in not null!");
				}
				toBeWokenObject = waitObjectQueue.poll();
				toBeWokenObject.setNotifiedTrue();
				toBeWokenObject.notifyAll();

			}
		}
	}

	private synchronized int[] sendCommandForReal(int command, int argument)
			throws BluetoothStateException {


		if(_dos == null){ 
			System.out.println("dos is null");
		}
		try {
			_dos.writeByte(command);
			_dos.flush();
			_dos.writeInt(argument);
			_dos.flush();
		} catch (IOException ioe) {
//			System.out.println("BTCOMMPC2 we will throw BluetoothStateException");
			throw new BluetoothStateException(
					" sending the command failed because of an IO exception");
		}
		// We first read how many replies this reply holds, this is bigger dan 1
		// if there are special replies.

		int numberOfReplies;
		int[] reply=null;
		try {
			numberOfReplies = _dis.readByte();
			if (numberOfReplies > 0) {
			reply = new int[numberOfReplies*MAXONEREPLYLENGTH];
			reply[0]=numberOfReplies;
				// Now we read the first reply, this is always the reply that
				// should be returned by this method and is therefore put in the
				// reply array.
				int firstReplyLength = _dis.readByte();
				reply[1]=firstReplyLength;
				int k = 2;
				for (int i=0; i < firstReplyLength; i++) {
					reply[k++] = _dis.readInt();
				}
				
				
				int numberOfSpecialReplies = numberOfReplies - 1;
				// Now we read the special replies, if there are any.
				for (int i = 0; i < numberOfSpecialReplies; i++) {
					byte specialReplyCode=_dis.readByte();
					reply[k++]=specialReplyCode;
					int specialReplyLength=getSpecialReplyLength(specialReplyCode);
					reply[k++]=specialReplyLength;
					for(int j=0;j<specialReplyLength;j++){
						reply[k++]=_dis.readInt();
					}
				}

			}
		} catch (IOException e) {
			System.out.println("BTCOMMPC1: we will throw BluetoothStateException");
			throw new BluetoothStateException(
					" sending the command failed because of an IO exception");
		}
		return reply;
	}

	private void switchOnSpecialReply(int specialReplyCode, int[] specialInfo){

		switch (specialReplyCode) {
		case ADDBARCODE:
			addBarcode(specialInfo);
			break;
		default:
			throw new RuntimeException(
					"A case was not implemented or you forgot the break or return");

		}

	}





	@Override
	public void addBarcode(int[] barcodeInfo) {
		robot.makeBarcode(barcodeInfo);		
	}

	@Override
	public int getSpecialReplyLength(byte SpecialReply) {
		switch (SpecialReply) {
		case ADDBARCODE:
			return ADDBARCODELENGHT;
		default:
			throw new RuntimeException(
					"A case was not implemented or you forgot the break or return");
		}
	}

}
