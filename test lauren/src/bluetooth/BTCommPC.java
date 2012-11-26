package bluetooth;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
	private int[] _reply = new int[8];
	
	
	public BTCommPC(){
	}
	
	
	public boolean open(String deviceName, String BTaddress){
		
		//nxtComm = null;
		_opened = false;
		
		try {
			_nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			NXTConnector conn = new NXTConnector();
			NXTInfo[] nxtInfo= conn.search(deviceName, BTaddress, NXTCommFactory.BLUETOOTH);
			_nxtInfo=nxtInfo[0];
		} catch (NXTCommException e) {
			System.out.println("something went wrong");
			return _opened;
		}
				
		
		try {
			_opened = _nxtComm.open(_nxtInfo); 
		} catch (NXTCommException e) {
			System.out.println("something went wrong");
			return _opened;
		} finally {
			if (!_opened) {
				System.out.println("something went wrong");
			return _opened;
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
	
	public int[] sendCommand(int[] command){
		if(_dos == null){ 
			System.out.println("dos is null");
		}
		
		for(int k = 0; k<3; k++){
			try{
				_dos.writeInt(command[k]);
				_dos.flush();
			}catch(IOException ioe){
				break;
			}
		}
			
		for(int k = 0; k<8; k++){
			try{
				_reply[k] = _dis.readInt();
			}catch(IOException ioe){
				break;
			}
		}
			
		return _reply;	
	}


	public int[] sendCommand(int command) {
		return sendCommand(new int[]{command,0});
	}
	
}
