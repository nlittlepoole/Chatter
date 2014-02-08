package com.example.chatter;

import java.util.Queue;
import java.util.Date;

public class Message {
	private String message;
	private String alias;
	private String channel;
	public static Queue<Message> feed;
	
	public Message(String input){
		message=input;
		feed.add(this);
	}
	
	public void addMessage(){
		feed.add(this);
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
