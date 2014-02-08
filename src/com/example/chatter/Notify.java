package com.example.chatter;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;

public class Notify extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notify);

		 NotificationCompat.Builder mBuilder =
		            new NotificationCompat.Builder(this)
		            .setSmallIcon(R.drawable.notification_icon)
		            .setContentTitle("My notification")
		            .setContentText("Hello World!");
		    // Creates an explicit intent for an Activity in your app
		    Intent resultIntent = new Intent(this, ResultActivity.class);

		    resultIntent.putExtra("userID", user.ID);

		    // The stack builder object will contain an artificial back stack for the
		    // started Activity.
		    // This ensures that navigating backward from the Activity leads out of
		    // your application to the Home screen.
		    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		    // Adds the back stack for the Intent (but not the Intent itself)
		    stackBuilder.addParentStack(ResultActivity.class);
		    // Adds the Intent that starts the Activity to the top of the stack
		    stackBuilder.addNextIntent(resultIntent);
		    PendingIntent resultPendingIntent =
		            stackBuilder.getPendingIntent(
		                0,
		                PendingIntent.FLAG_UPDATE_CURRENT
		            );
		    mBuilder.setContentIntent(resultPendingIntent);
		    NotificationManager mNotificationManager =
		        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notify, menu);
		return true;

	}

}
