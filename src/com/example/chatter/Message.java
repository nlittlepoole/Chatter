package com.example.chatter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Date;

public class Message {
	private String message;
	private String alias;
	private String channel;
	public static Queue<String> feed=new LinkedList<String>();
	
	public Message(String input){
		message=input;
		feed.add(input);
	}
	
	public void addMessage(){
		//feed.add(this);
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
