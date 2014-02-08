package com.example.chatter;

import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity {
	String title = null;
    TextView text;

    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        text = (TextView) findViewById(R.id.textView1);



        userID = getIntent().getStringExtra("userID");
         i = getIntent().getIntExtra("uid", 0);


        text.setText(title);

}
