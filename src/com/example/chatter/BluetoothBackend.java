package com.example.chatter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
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

public class BluetoothBackend extends Activity {

	private static final int REQUEST_ENABLE_BT = 1;
	private static BluetoothAdapter mBluetoothAdapter;
	private static Vector<BluetoothDevice> devices=new Vector<BluetoothDevice>();
	private ArrayList<String> mDeviceAddresses = new ArrayList<String>();
	private ArrayList<ConnectedThread> mConnThreads = new ArrayList<ConnectedThread>();
	private ArrayList<BluetoothSocket> mSockets = new ArrayList<BluetoothSocket>();
	private ArrayList<UUID> listeners= new ArrayList<UUID>();
	private ArrayList<UUID> connectors= new ArrayList<UUID>();
	private ArrayList<Object> connections=new ArrayList<Object>();
	private static boolean connect=false;
	public static Profile user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String alias = intent.getStringExtra("ALIAS");
		String channel = intent.getStringExtra("CHANNEL");
		user=new Profile(alias,channel);
		setContentView(R.layout.activity_main);
		mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();        
		ArrayList<UUID> mUuids = new ArrayList<UUID>();
		// 8 randomly-generated UUIDs. These must match on both server and client.
		mUuids.add(UUID.fromString("b7746a40-c758-4868-aa19-7ac6b3475dfc"));
		mUuids.add(UUID.fromString("2d64189d-5a2c-4511-a074-77f199fd0834"));
		mUuids.add(UUID.fromString("e442e09a-51f3-4a7b-91cb-f638491d1412"));
		mUuids.add(UUID.fromString("a81d6504-4536-49ee-a475-7d96d09439e4"));
		mUuids.add(UUID.fromString("aa91eab1-d8ad-448e-abdb-95ebba4a9b55"));
		mUuids.add(UUID.fromString("4d34da73-d0a4-4f40-ac38-917e0a9dee97"));
		mUuids.add(UUID.fromString("5e14d4df-9c8a-4db7-81e4-c937564c86e0"));
		mUuids.add(UUID.fromString("4df56fe0-9079-11e3-baa8-0800200c9a66"));
		while(listeners.size()<4){
			int random=(int) (Math.random() * mUuids.size()-1);
			listeners.add(mUuids.get(random));
			mUuids.remove(random);
		}
		connectors=mUuids;

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
		push();
		/*while(true){
			if(Message.feed.peek()!=null){
				Context context = getApplicationContext();
				int duration = Toast.LENGTH_SHORT;
	            Toast toast = Toast.makeText(context, (CharSequence) Message.feed.poll(), duration);
	            toast.show();
	            break;
			}
		}*/
	}

	public void push(){
		new Thread(new Runnable() {
			public void run() {
				while(true){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(!Profile.toSend.isEmpty()){
						for( int i=0; i<connections.size();i++){
							((ConnectedThread) connections.get(i)).write(Profile.toSend.poll().pushMessage().getBytes());
						}
					}
				}
			}
		}).start();
	}

	/** Called when the user clicks the Connect button */
	public void connect(View view) {
		// Do something in response to button
		for(int i=0; i<devices.size();i++){
			for(UUID uuid:connectors){
				if(devices.get(i)!=null){
					if(connect){
						Thread mConnectedThread = new ConnectThread(devices.get(i),uuid);
						mConnectedThread.start();			            
					}
				}
				connect=true;
			}
		}
	}
	/** Called when the user clicks the Accept button */
	public void accept(View view){
		for(UUID uuid:listeners){
			Thread mConnectedThread = new AcceptThread(uuid);
			mConnectedThread.start();
		}
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
		private UUID uuid;

		public AcceptThread(UUID input) {
			// Use a temporary object that is later assigned to mmServerSocket,
			// because mmServerSocket is final
			BluetoothServerSocket tmp = null;
			uuid=input;
			mmServerSocket = tmp;
		}

		public void run() {
			try {
				BluetoothServerSocket serverSocket = null;
				BluetoothSocket socket = null;
				// MY_UUID is the app's UUID string, also used by the client code
				serverSocket = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("Stratus", uuid);
				socket = serverSocket.accept();
				if (socket != null) {
					// Do work to manage the connection (in a separate thread)
					//manageConnectedSocket(socket);
					System.out.println("Success!!!!!");
					connections.add(new ConnectedThread(socket));
					((Thread) connections.get(connections.size()-1)).start();
					/* try {
							mmServerSocket.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			               break; */
				}

			} catch (IOException e) { }
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

		public ConnectThread(BluetoothDevice device, UUID uuid) {
			// Use a temporary object that is later assigned to mmSocket,
			// because mmSocket is final
			BluetoothSocket tmp = null;
			mmDevice = device;

			// Get a BluetoothSocket to connect with the given BluetoothDevice
			try {
				// MY_UUID is the app's UUID string, also used by the server code
				tmp = device.createInsecureRfcommSocketToServiceRecord(uuid);
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
			connect=false;
			Message test=new Message("Fuck Java splitting",user);
			connections.add(new ConnectedThread(mmSocket));
			((Thread) connections.get(connections.size()-1)).start();
			((ConnectedThread) connections.get(connections.size()-1)).write(test.pushMessage().getBytes());

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
	public class ConnectedThread extends Thread {
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
					String input =new String( buffer, Charset.forName("UTF-8") );
					input=input.substring(0, bytes);
					Message message=new Message(input);
					if(!user.inAliases(message.getAlias()))
							Message.queueMessage(message);
							
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
