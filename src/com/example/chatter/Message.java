package com.example.chatter;

import java.util.Queue;

public class Message {
	private String message;
	private String Alias;
	private String channel;
	public static Queue<Message> feed;
	
	public Message(String input){
		message=input;
		feed.add(this);
	}
	
	public void addMessage(){
		
	}
	
	private void pushMessage(){
		
	}
	public String getMessage(){
		return message;
	}
	

}
