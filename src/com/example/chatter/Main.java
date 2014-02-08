package com.example.chatter;

import java.util.UUID;

import com.example.chatter.BluetoothBackend.AcceptThread;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
	}
	public void login(View view){
		Intent intent = new Intent(this, BluetoothBackend.class);
		EditText editText = (EditText) findViewById(R.id.alias);
		String alias = editText.getText().toString();
		editText = (EditText) findViewById(R.id.channel);
		String channel = editText.getText().toString();
		intent.putExtra("ALIAS",alias);
		intent.putExtra("CHANNEL", channel);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
