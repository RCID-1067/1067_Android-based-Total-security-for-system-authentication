package com.backup.callloggenerator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class SocketInformation {
	private static SocketInformation instance;
	private Socket socket;
	private DataOutputStream dataoutputstream;
	private DataInputStream datainputstream;
	
	private SocketInformation(){}
	
	public static SocketInformation getInstance(){
		if(instance == null) {
	         instance = new SocketInformation();
	      }
		return instance;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public DataOutputStream getDataoutputstream() {
		return dataoutputstream;
	}

	public void setDataoutputstream(DataOutputStream dataoutputstream) {
		this.dataoutputstream = dataoutputstream;
	}

	public DataInputStream getDatainputstream() {
		return datainputstream;
	}

	public void setDatainputstream(DataInputStream datainputstream) {
		this.datainputstream = datainputstream;
	}

}
