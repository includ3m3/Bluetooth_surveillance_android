package com.example.bluetooth_raw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

public class Bluetooth_service extends Service{
	boolean isRunning = true;
	BluetoothAdapter btAdapter;
	List<NameValuePair> nameValues;
	IntentFilter filter;
	BroadcastReceiver receiver;
	private final static int INVERVAL = 1000 * 60 * 1; //1 mins
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isRunning = false;
		
		if(btAdapter != null && btAdapter.isDiscovering()){
			btAdapter.cancelDiscovery();
        	btAdapter.disable();
        }
        unregisterReceiver(receiver);
        btAdapter.disable();
		stopSelf();
		
		show_noti("Stopping Service",getApplicationContext());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);
		show_noti("Starting Service",getApplicationContext());
		isRunning = true;
		init();
		Thread backgroundThread = new Thread(new BackgroundThread());
        backgroundThread.start();
        
		return START_STICKY;
	}
	
	private void init(){
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		if(btAdapter == null){
			show_noti("No bluetooth",getApplicationContext());
			isRunning = false;
			stopSelf();
		}else{
			if(!btAdapter.isEnabled()){
				try{
					btAdapter.enable();
				}catch(Exception e){
					Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
					isRunning=false;
				}
				
			}
			while(!btAdapter.isEnabled()){
			}
			nameValues = new ArrayList<NameValuePair>();
			receiver = new BroadcastReceiver() {
				
				@Override
				public void onReceive(Context context, Intent intent) {
					String action = intent.getAction();
					if(action.equals(BluetoothDevice.ACTION_FOUND)){
						show_noti("Found one",getApplicationContext());
						BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
						nameValues.add(new BasicNameValuePair(device.getName(), device.getAddress()));		
						genNote("bluetoothDevices",nameValues.toString());
						nameValues.clear();
					}
					
				}
			};
			registerReceiver(receiver, filter);
			
			
		}
	}
	
	
	private class BackgroundThread implements Runnable {
		int counter = 0;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				while(isRunning){
					System.out.println(""+counter++);
					startDiscovery();
					show_noti("Sleeping...",getApplicationContext());
					System.out.println("Sleeping");
					btAdapter.disable();
					Thread.sleep(INVERVAL);
				}
				System.out.println("Background thread is finished");
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}
	private void startDiscovery(){
		if(!btAdapter.isEnabled()){
			btAdapter.enable();
		}
		while (!btAdapter.isEnabled()) {               
		}
		if(btAdapter.isEnabled()){
			if(btAdapter.isDiscovering()){
				btAdapter.cancelDiscovery();
			}
			btAdapter.startDiscovery();
			
			
			show_noti("Scanning in progress",getApplicationContext());
		}else{
			show_noti("Give sometime to enable bluetooth",getApplicationContext());
		}
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	private void show_noti(String any,Context context){
		Toast.makeText(context, any, Toast.LENGTH_LONG).show();
	}
	
	private void genNote(String sFileName, String sBody){
	    try
	    {
	        File root = new File(Environment.getExternalStorageDirectory(), "bluetooth_test");
	        if (!root.exists()) {
	            root.mkdirs();
	        }
	        File file = new File(root, sFileName);
	        FileWriter writer = new FileWriter(file,true);
	        writer.append(sBody);
	        writer.flush();
	        writer.close();
	        System.out.println("Saved");
	    }catch(IOException e){
	         e.printStackTrace();
	    }
	   }  
}



