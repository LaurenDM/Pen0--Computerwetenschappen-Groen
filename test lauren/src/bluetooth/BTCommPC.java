package bluetooth;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.PriorityQueue;
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

public class BTCommPC  {
	
	private NXTComm _nxtComm;
	private NXTInfo _nxtInfo;
	private DataOutputStream _dos;
	private DataInputStream _dis;
	private boolean _opened;
	private boolean _closed;
	private int[] _reply = new int[4];
	private boolean sendingIsBlocked;
	//We set this to null to say that there is no buffered command.
//	private long lastWaitingTicketNumber=0;
//	private long firstValidTicket=1;
	private Queue<WaitObject> waitObjectQueue= new LinkedList<WaitObject>();
	private WaitObject toBeWokenObject=null;
	private BTRobotPilot robot;

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
			NXTInfo[] nxtInfo= conn.search(deviceName, BTaddress, NXTCommFactory.BLUETOOTH);
			_nxtInfo=nxtInfo[0];
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
	
	public boolean close(){
		
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
	
	public DataOutputStream getOutputStream(){
		return _dos;
	}
	
	public DataInputStream getInputStream(){
		return _dis;
	}
	
	public boolean isOpened(){
		return _opened;
	}
	
	public boolean isClosed(){
		return _closed;
	}
	public int[]  sendCommand(int  command, double argument) {
		return sendCommand(command,(int) (100*argument));
	}
	private int[] sendCommand(int  command, int argument) {
		//TODO kijken of we deze code nog willen, is eerder voor debug
		//We testen even of alle tickets al gebruikt zijn.
		if(sendingIsPossible()&&!waitObjectQueue.isEmpty()){
			try {
				// We wachten hier 10 milliseconden om de wachtende
				// send-commands de tijd te geven om toch uit te voeren indien
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
				return null;
			}
			
		}

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
				return null;
			}
		}
		blockSending();
		boolean commandIsSentForReal = false;
		while (!commandIsSentForReal && !Thread.interrupted()) {
			try {
				commandIsSentForReal = false;
				sendCommandForReal(command, argument);
				commandIsSentForReal = true;
			} catch (BluetoothStateException e) {
				// In this case the while loop will try to send the command
				// again.
			}
		}
		int[] returnReply = new int[_reply.length];
		int i = 0;
		for (int oneReply : _reply) {

			returnReply[i] = oneReply;
			i++;
		}
		if (!waitObjectQueue.isEmpty()) {
			takeNextWaiter();
		} else {
			unblockSending();
		}
		return returnReply;
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


	private void sendCommandForReal(int command, int argument)
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
			throw new BluetoothStateException(
					" sending the command failed because of an IO exception");
		}
		int replyLength;
		try {
			replyLength=_dis.readByte();
			}
		catch(IOException e){
			throw new BluetoothStateException(" sending the command failed because of an IO exception");
		}
		if(replyLength == 8){
			int[] barcode = new int[4];
			for(int i =0; i<4; i++){
				barcode[i] = _reply[4+i];
			}
			robot.makeBarcode(barcode);
			replyLength = 4;
		}
		for(int k = 0; k<replyLength; k++){
			try{
				_reply[k] = _dis.readInt();
			}catch(IOException ioe){
				throw new BluetoothStateException(" sending the command failed because of an IO exception");
			}
		}
	}


	public int[] sendCommand(int command) {
		return sendCommand(command,0);
	}
	
}
