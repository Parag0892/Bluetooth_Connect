package com.example.bluetooth_connect;
import java.util.ArrayList;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button pairup ;
	BluetoothAdapter mBluetoothAdapter = null ; 
	Set<BluetoothDevice> devicesArray = null ;
	ArrayList<String> pairdevice = new ArrayList();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		pairup = (Button)findViewById(R.id.button1) ;
		pairup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Toast.makeText(getApplicationContext(), ">>Clicked ", Toast.LENGTH_LONG).show() ;
				
				
				mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
				if (mBluetoothAdapter == null) {
					// Device does not support Bluetooth4
					Toast.makeText(getApplicationContext(),
							"No bluetooth Detected", 2000).show();
					return;
				}
				
				if (!mBluetoothAdapter.isEnabled()) {
					Intent enableBtIntent = new Intent(
							BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableBtIntent, 1);
				}
				else
				{
					System.out.println("inside else") ; 
					devicesArray = mBluetoothAdapter.getBondedDevices();
					
					
					AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
					
					builderSingle.setTitle("Select One Name:-");
					final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,
							android.R.layout.select_dialog_singlechoice);

					if (devicesArray != null ) {
						for (BluetoothDevice device : devicesArray) {
							arrayAdapter.add(device.getName() + "\n"
									+ device.getAddress());
						}
						arrayAdapter.add("Pairup a new device") ;
					}
					
					builderSingle.setNegativeButton("cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
					
					builderSingle.setAdapter(arrayAdapter,
							new DialogInterface.OnClickListener() {
								String strName;

								@Override
								public void onClick(DialogInterface dialog,int which) {
									System.out.println(mBluetoothAdapter.startDiscovery()) ;
									if (which != devicesArray.size() -1)
									{
									
										System.out.println("which >> " + which) ; 
										strName = arrayAdapter.getItem(which);
										AlertDialog.Builder builderInner = new AlertDialog.Builder(MainActivity.this);
										builderInner.setMessage(strName);
										builderInner.setTitle("Your Selected Item is");
										builderInner.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
	
															@Override
															public void onClick(DialogInterface dialog,int which) {
																String add = strName.substring(strName.length() - 17);
																Toast.makeText(getApplicationContext(),add, 2000).show();
																dialog.dismiss();
																
															}
														});
										builderInner.show();
									}
									else
									{
										System.out.println ("Pairup new devices") ; 
										if (mBluetoothAdapter.isDiscovering()) {
										    mBluetoothAdapter.cancelDiscovery();
										}
										
										System.out.println(" start>>" + mBluetoothAdapter.startDiscovery()) ; 
									}
								}
							});

					builderSingle.show() ;		
				}
			}		
		}) ; 
		
		
		BroadcastReceiver mReceiver = new BroadcastReceiver() {
		    public void onReceive(Context context, Intent intent) {
		        String action = intent.getAction();
		        // When discovery finds a device
		        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
		            // Get the BluetoothDevice object from the Intent
		            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		            // Add the name and address to an array adapter to show in a ListView
		            //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
		            System.out.println( "Device Name >> " + device.getName()) ;
		        }
		    }
		};
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter);
		
	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1)
		{
			if (resultCode == RESULT_CANCELED) {
			
				Toast.makeText(getApplicationContext(),
					"Bluetooth must be enabled to send", 2000).show();

			}

			else if (resultCode == RESULT_OK) {
				
				Toast.makeText(getApplicationContext(), "Bluetooth has enabeled",2000).show();
				pairup.callOnClick() ; 
			
			}
		}
	}
	
	

}
