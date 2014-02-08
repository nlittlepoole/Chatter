package com.example.chatter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static BluetoothAdapter mBluetoothAdapter;
    private static Vector<BluetoothDevice> devices=new Vector<BluetoothDevice>();


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
        
        if(mBluetoothAdapter ==null){
        	Context context = getApplicationContext();
        	CharSequence text = "No bluetooth";
        	int duration = Toast.LENGTH_SHORT;
        	Toast toast = Toast.makeText(this, text, duration);
        	toast.show();
        }
        if(!mBluetoothAdapter.isEnabled()){
        	Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }  
    	Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
    	discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
    	startActivity(discoverableIntent);
    	mBluetoothAdapter.startDiscovery();

     }
	/** Called when the user clicks the Connect button */
	public void connect(View view) {
	    // Do something in response to button
    	for(int i=0; i<devices.size();i++){
	    	if(devices.get(i)!=null){
	        	Thread mConnectedThread = new ConnectThread(devices.get(i));
	            mConnectedThread.start();
	    	}
    	}
	}
	/** Called when the user clicks the Accept button */
	public void accept(View view){
    	Thread mConnectedThread = new AcceptThread();
        mConnectedThread.start();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	// Create a BroadcastReceiver for ACTION_FOUND
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
    	            // Get the BluetoothDevice object from the Intent
    	            context = getApplicationContext();
    	            CharSequence text = device.getName() + "\n" + device.getAddress() + "\n" + device.getBluetoothClass();
    	            int duration = Toast.LENGTH_SHORT;

    	            Toast toast = Toast.makeText(context, text, duration);
    	            toast.show();
    	        	System.out.println(device.getName() + "\n" + device.getAddress() + "\n" + device.getBluetoothClass());
    	        	devices.add(device);
            // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            	
	        }
	    }
	};
	public class AcceptThread extends Thread {
		   private final BluetoothServerSocket mmServerSocket;

		   public AcceptThread() {
		       // Use a temporary object that is later assigned to mmServerSocket,
		       // because mmServerSocket is final
		       BluetoothServerSocket tmp = null;
		       try {
		           // MY_UUID is the app's UUID string, also used by the client code
		    	   UUID temp=UUID.fromString("f79d717e-d685-4677-8eee-cc4529abbabd");
		           tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("Stratus", temp);
		       } catch (IOException e) { }
		       mmServerSocket = tmp;
		   }

		   public void run() {
		       BluetoothSocket socket = null;
		       // Keep listening until exception occurs or a socket is returned
		       while (true) {
		           try {
		               socket = mmServerSocket.accept();
		           } catch (IOException e) {
		               break;
		           }
		           // If a connection was accepted
		           if (socket != null) {
		               // Do work to manage the connection (in a separate thread)
		               //manageConnectedSocket(socket);
		        	   System.out.println("Success!!!!!");
		               try {
						mmServerSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		               break;
		           }
		       }
		   }

		   /** Will cancel the listening socket, and cause the thread to finish */
		   public void cancel() {
		       try {
		           mmServerSocket.close();
		       } catch (IOException e) { }
		   }
		}
	private class ConnectThread extends Thread {
	    private final BluetoothSocket mmSocket;
	    private final BluetoothDevice mmDevice;
	 
	    public ConnectThread(BluetoothDevice device) {
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	        BluetoothSocket tmp = null;
	        mmDevice = device;
	 
	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	        	UUID temp=UUID.fromString("f79d717e-d685-4677-8eee-cc4529abbabd");
	            tmp = device.createInsecureRfcommSocketToServiceRecord(temp);
	        } catch (IOException e) { }
	        mmSocket = tmp;
	    }
	 
	    public void run() {
	        // Cancel discovery because it will slow down the connection
	        mBluetoothAdapter.cancelDiscovery();
	 
	        try {
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	            mmSocket.connect();
	        } catch (IOException connectException) {
	            // Unable to connect; close the socket and get out
	            try {
	                mmSocket.close();
	            } catch (IOException closeException) { }
	            return;
	        }
	 
	        // Do work to manage the connection (in a separate thread)
	        //manageConnectedSocket(mmSocket);
	    }
	 
	    /** Will cancel an in-progress connection, and close the socket */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}
	private class ConnectedThread extends Thread {
	    private final BluetoothSocket mmSocket;
	    private final InputStream mmInStream;
	    private final OutputStream mmOutStream;
	 
	    public ConnectedThread(BluetoothSocket socket) {
	        mmSocket = socket;
	        InputStream tmpIn = null;
	        OutputStream tmpOut = null;
	 
	        // Get the input and output streams, using temp objects because
	        // member streams are final
	        try {
	            tmpIn = socket.getInputStream();
	            tmpOut = socket.getOutputStream();
	        } catch (IOException e) { }
	 
	        mmInStream = tmpIn;
	        mmOutStream = tmpOut;
	    }
	 
	    public void run() {
	        byte[] buffer = new byte[1024];  // buffer store for the stream
	        int bytes; // bytes returned from read()
	 
	        // Keep listening to the InputStream until an exception occurs
	        while (true) {
	            try {
	                // Read from the InputStream
	                bytes = mmInStream.read(buffer);
	                // Send the obtained bytes to the UI activity
	                //mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
	                  //      .sendToTarget();
	            } catch (IOException e) {
	                break;
	            }
	        }
	    }
	 
	    /* Call this from the main activity to send data to the remote device */
	    public void write(byte[] bytes) {
	        try {
	            mmOutStream.write(bytes);
	        } catch (IOException e) { }
	    }
	 
	    /* Call this from the main activity to shutdown the connection */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}
 
}
