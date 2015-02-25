package com.backup.callloggenerator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.Toast;

public class ConnectTask  extends AsyncTask<String, Void, MainActivity>{
private Context context;
private String ip_editText;
private String port_editText;
private Socket socket;
private DataOutputStream dataoutputstream;
private DataInputStream datainputstream;
String serverData;
private boolean mRun = true;
public int QUIT_TYPE_DEFAULT				=	0;

	
	    public ConnectTask(Context context){
	    	this.context=(MainActivity)context;
	    	
	    }


@Override
protected MainActivity doInBackground(String... param) {
	// TODO Auto-generated method stub
	
	
	ip_editText = param[0];
	port_editText = param[1];
	try {
		socket = new Socket(ip_editText,Integer.parseInt(port_editText));
	
	SocketInformation.getInstance().setSocket(socket);
	dataoutputstream = new DataOutputStream(socket.getOutputStream());
	SocketInformation.getInstance().setDataoutputstream(dataoutputstream);
	datainputstream  = new DataInputStream(socket.getInputStream());
	SocketInformation.getInstance().setDatainputstream(datainputstream);
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	SendMessageToServer("hello");	
	
	while(mRun){
		try {
			datainputstream  = new DataInputStream(socket.getInputStream());
			serverData= datainputstream.readLine();
			if(serverData != null){
				mRun = false;
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}	
	}
	
	return null;
}

@Override
	protected void onPostExecute(MainActivity result) {
	
	if(serverData.startsWith("welcome")){
		Intent intent = new Intent(context,CallLogs.class);
		context.startActivity(intent);
		//getCallDetails();
		
	}
	else{
		Toast.makeText(context, "not able to connect server", Toast.LENGTH_SHORT).show();
	}
	}

private void SendMessageToServer(String Message)
{
	try {
		if(dataoutputstream !=null){
		dataoutputstream.writeBytes(Message+"\r\n");	
		}else{
			dataoutputstream=SocketInformation.getInstance().getDataoutputstream();
			dataoutputstream.writeBytes(Message+"\r\n");
		}
		
	}catch(IOException _IoExc) {
		//QuitConnection(QUIT_TYPE_DEFAULT);
	}			
}

private void getCallDetails() {

	StringBuffer sb = new StringBuffer();
	//Cursor manage= 
	Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, null,null, null);
	int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER ); 
	int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
	int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
	int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
	sb.append( "Call Details :");
	while ( managedCursor.moveToNext() ) {
	String phNumber = managedCursor.getString( number );
	String callType = managedCursor.getString( type );
	String callDate = managedCursor.getString( date );
	Date callDayTime = new Date(Long.valueOf(callDate));
	String callDuration = managedCursor.getString( duration );
	String dir = null;
	int dircode = Integer.parseInt( callType );
	switch( dircode ) {
	case CallLog.Calls.OUTGOING_TYPE:
	dir = "OUTGOING";
	break;

	case CallLog.Calls.INCOMING_TYPE:
	dir = "INCOMING";
	break;

	case CallLog.Calls.MISSED_TYPE:
	dir = "MISSED";
	break;
	}
	sb.append( "\nPhone Number:--- "+phNumber +" \nCall Type:--- "+dir+" \nCall Date:--- "+callDayTime+" \nCall duration in sec :--- "+callDuration );
	sb.append("\n----------------------------------");
	}
	managedCursor.close();
	
	SendMessageToServer(sb.toString());
	Toast.makeText(context, sb ,Toast.LENGTH_SHORT).show();
	
}

}