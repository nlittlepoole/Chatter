package com.example.chatter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Date;

public class Message {
	private String message;
	private String alias;
	private String channel;
	private String time;
	private String key;
	public static Queue<Message> feed=new LinkedList<Message>(); 
	
	public Message(String input){
		String[] in = input.split("&&");
		alias = in[0];
		channel = in[1];
		message = in[2];
		key=input;
	}
	public Message(String input, Profile user){
		String[] in = input.split("&&");
		alias = user.getAlias();
		channel =user.getChannel();
		message = input;
		key=this.pushMessage();
	}
	
	public String pushMessage(){
		Date current = new Date();
		String toSend = alias + "&&" + channel + "&&" + message 
				+ "&&" ;
		return toSend;
	}
	
	public void clearMessage(){
		message = null;
	}
	
	public String getMessage(){
		return message;
	}
	public String getAlias(){
		return alias;
	}
	public String getChannel(){
		return channel;
	}
	public String getKey(){
		return key;
	}
	public static void queueMessage(Message input){
		if(Message.isUnique(input)){
			feed.add(input);
			Profile.toSend.add(input);
		}
	}
	private static boolean isUnique(Message input){
		int x=0;
		for(Message message:feed){
			if(input.getKey().equals(message.getKey()))
				return false;
			if(x>15)
				break;
			x++;
		}
		return true;
	}
	

}
