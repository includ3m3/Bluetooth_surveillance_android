package com.example.bluetooth_raw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Bluetooth extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);
		this.setTitle("ScanMe");
		TextView view = new TextView(this);      
	    view.setText("Bluetooth Service test");
	    
	   
		setContentView(R.layout.activity_bluetooth);
		
		Button button = (Button)findViewById(R.id.start);
        button.setOnClickListener(mStartService);
        button = (Button) findViewById(R.id.finish);
        button.setOnClickListener(mStopService);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bluetooth, menu);
		return true;
	}
	
	private OnClickListener mStartService = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			 Intent start = new Intent("com.example.bluetooth_raw.Bluetooth_service");
             Bluetooth.this.startService(start);
		}
	};
	private OnClickListener mStopService = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent stop = new Intent("com.example.bluetooth_raw.Bluetooth_service");
			Bluetooth.this.stopService(stop);
			
		}
	};
	

}
