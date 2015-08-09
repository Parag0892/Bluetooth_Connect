package com.example.bluetooth_connect;
import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
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
				
				Toast.makeText(getApplicationContext(), "Clicked ", Toast.LENGTH_LONG).show() ;
				
				
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
									
									Toast.makeText(getApplicationContext(), " which >>" + which, Toast.LENGTH_LONG) ; 
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
							});

					
				}
			}		
		}) ; 
		
	}

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
				pairup.performClick() ; 
			
			}
		}
	}
	
	

}
