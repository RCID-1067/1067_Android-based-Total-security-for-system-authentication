package com.backup.callloggenerator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	
	private EditText ip_editText;
	private EditText port_editText;
	private Button connect_button;
	private Socket socket;
	private DataOutputStream dataoutputstream;
	private DataInputStream datainputstream;
	private Thread thread;
	public int QUIT_TYPE_DEFAULT				=	0;
	private String serverData;
	private static final String PATTERN = 
	        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
	        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);        
        ip_editText = (EditText)findViewById(R.id.ip_edittext);
        port_editText = (EditText)findViewById(R.id.port_edit);
        connect_button = (Button)findViewById(R.id.buttonGO);
        connect_button.setOnClickListener(this);;
     }
   
    @Override
    public void onClick(View v) {
		if(getValue()){
		String iptext = ip_editText.getText().toString();
		String portText = port_editText.getText().toString();
		if(validateipAdress(iptext) && isInteger(portText)){
			ServerDetailsBean.getInstance().setServerIP(iptext);
			ServerDetailsBean.getInstance().setServerPort(Integer.parseInt(portText));
			Toast.makeText(getApplicationContext(),
			ServerDetailsBean.getInstance().getServerIP()+" : "+ServerDetailsBean.getInstance().getServerPort(),Toast.LENGTH_SHORT).show();
			new ConnectTask(MainActivity.this).execute(iptext,portText);
			//ConnectToServer();
			//new connectTask().execute("");
			
		}
		else{
			Toast.makeText(getApplicationContext(), "Please Enter valid IPadress and port", Toast.LENGTH_SHORT).show();
			if(isInteger(portText)){
			Toast.makeText(getApplicationContext(), "port no should range from 5900 to 6000", Toast.LENGTH_SHORT).show();
			}
		}
	
		}}
	public static boolean validateipAdress(final String ip){          

	      Pattern pattern = Pattern.compile(PATTERN);
	      Matcher matcher = pattern.matcher(ip);
	      return matcher.matches();             
	}
	
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	        if(Integer.parseInt(s) >= 5900 && Integer.parseInt(s) <= 6000){
	        	return true;
	        }
	        else{
	        	return false;
	        }
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	   
	  
	}
	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private boolean getValue() {
		// TODO Auto-generated method stub

		Calendar c = Calendar.getInstance(); 
		int year = c.get(Calendar.YEAR);
		if (year > 2014){
		return false;
		}else{
			return true;
		}
	
	}
		
	

}
