package com.example.chatter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Message {
	private String message;
	private String Alias;
	private String channel;
	public static Queue<String> feed=new LinkedList<String>();
	
	public Message(String input){
		message=input;
		feed.add(input);
	}
	
	public void addMessage(){
		
	}
	
	private void pushMessage(){
		
	}
	public String getMessage(){
		return message;
	}
	

}
