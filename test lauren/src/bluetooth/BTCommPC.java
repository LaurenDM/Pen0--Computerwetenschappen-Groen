package bluetooth;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.SynchronousQueue;

import javax.bluetooth.BluetoothStateException;

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
	private long lastWaitingTicketNumber=0;
	private long firstValidTicket=1;
	
	public boolean sendingIsPossible() {
		return !sendingIsBlocked;
	}

	public BTCommPC(){
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
	
	public int[] sendCommand(int[] command) {
		//TODO kijken of we deze code nog willen, is eerder voor debug
		//We testen even of alle tickets al gebruikt zijn.
		if(!sendingIsBlocked&&firstValidTicket<=lastWaitingTicketNumber+1){
			try {
				// We wachten hier 10 milliseconden om de wachtende
				// send-commands de tijd te geven om toch uit te voeren indien
				// ze een ticket hebben dat aan de beurt zou komen.
				wait(10);
				//Nu kijken we nogeens of we nogaltijd in deze foute staat zitten.
				if(!sendingIsBlocked&&firstValidTicket<=lastWaitingTicketNumber){
					increaseFirstValidTicket();
					System.out.println("Something has gone wrong, we forcibly used increaseFirstValidTicket()");
				}
			} catch (InterruptedException e) {
				return null;
			}
			
		}

		
		if (!sendingIsPossible()) {
			long waitingTicketNumber = takeWaitingTicket();
			try {
				while (!itIsThisThreadsTurn(waitingTicketNumber)
					|| !sendingIsPossible()) {
					{
						wait();
					}
					increaseFirstValidTicket();
				}
			} catch (InterruptedException e) {
				//This is not a problem, because in this case the command doesn't have to be sent anymore.
				increaseFirstValidTicket();
				return null;
			}
		}
			blockSending();
			boolean commandIsSentForReal = false;
			while (!commandIsSentForReal&&!Thread.interrupted()) {
				try {
					commandIsSentForReal = false;
					sendCommandForReal(command);
					commandIsSentForReal = true;
				} catch (BluetoothStateException e) {
					// In this case the while loop will try to send the command
					// again.
				}
			}
			unblockSending();
			notifyAll();
			return _reply;
	}

	private synchronized void increaseFirstValidTicket() {
		firstValidTicket++;
	}

	private synchronized long takeWaitingTicket() {
		lastWaitingTicketNumber++;
		return lastWaitingTicketNumber;
	}

	private boolean itIsThisThreadsTurn(long waitingTicketNumber) {
		return waitingTicketNumber==firstValidTicket;
	}

	private void sendCommandForReal(int[] command)
			throws BluetoothStateException {
		if(_dos == null){ 
			System.out.println("dos is null");
		}
		
		for(int k = 0; k<2; k++){
			try{
				_dos.writeInt(command[k]);
				_dos.flush();
			}catch(IOException ioe){
				throw new BluetoothStateException(" sending the command failed because of an IO exception");
			}
		}
			
		for(int k = 0; k<4; k++){
			try{
				_reply[k] = _dis.readInt();
			}catch(IOException ioe){
				throw new BluetoothStateException(" sending the command failed because of an IO exception");
			}
		}
	}


	public int[] sendCommand(int command) {
		return sendCommand(new int[]{command,0});
	}
	
}
