package com.example.chatter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Profile {

	private Set<String> acceptedChannels;
	private Set<String> rejectedChannels;
	private Set<String> usedAliases;
	private String alias;
	public static Queue<Message> toSend;
	
	public Profile(String alias){
		this.alias = alias;
		usedAliases = new HashSet<String>();
		usedAliases.add(alias);
		acceptedChannels = new HashSet<String>();
		rejectedChannels = new HashSet<String>();
		toSend = new LinkedList<Message>();
	}
	
	public void addAccChannels(String channel){
		acceptedChannels.add(channel);
	}
	
	public void addRejChannels(String channel){
		rejectedChannels.add(channel);
	}
	
	public boolean inAliases(String alias){
		return usedAliases.contains(alias);
	}
	
	public boolean inAccChan(String channel){
		return acceptedChannels.contains(channel);
	}
	
	public boolean inRejChan(String channel){
		return rejectedChannels.contains(channel);
	}
	
	public void addMessage(Message m){
		toSend.add(m);
	}
	
	public Message dequeueMessage(){
		return toSend.remove();
	}
	
	public String getAlias(){
		return alias;
	}
	
	
}
