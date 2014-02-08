package com.example.chatter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Date;

public class Message {
	private String message;
	private String alias;
	private String channel;
	private Date time;
	
	public Message(String input){
		String[] in = input.split("^");
		alias = in[0];
		channel = in[1];
		message = in[2];
		time = new Date(in[3]);
	}
	
	public String pushMessage(){
		Date current = new Date();
		String toSend = alias + "^" + channel + "^" + message 
				+ "^" + current.getTime();
		return toSend;
	}
	
	public void clearMessage(){
		message = null;
	}
	
	public String getMessage(){
		return message;
	}
	

}
