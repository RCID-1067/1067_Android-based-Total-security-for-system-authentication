package com.backup.callloggenerator;

public class ServerDetailsBean {
	private String ServerIP;
	private int ServerPort;
	private static ServerDetailsBean instance= new ServerDetailsBean(); 
	
	private ServerDetailsBean(){}
	
	public static ServerDetailsBean getInstance(){
		return instance;
	 }
	public String getServerIP() {
		return ServerIP;
	}
	public void setServerIP(String serverIP) {
		ServerIP = serverIP;
	}
	public int getServerPort() {
		return ServerPort;
	}
	public void setServerPort(int serverPort) {
		ServerPort = serverPort;
	}
	

}
