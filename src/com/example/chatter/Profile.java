package com.example.chatter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.SynchronousQueue;

public class Profile {

	private Set<String> acceptedChannels;
	private Set<String> rejectedChannels;
	private Set<String> usedAliases;
	private String alias;
	private String channel;
	public static Queue<Message> toSend;
	
	public Profile(String alias,String channel){
		this.channel=channel==null || channel.equals("")?"The Danger Zone":channel;
		this.alias = alias==null || alias.equals("")? "Bill Bellamy":alias;
		usedAliases = new HashSet<String>();
		usedAliases.add(alias);
		acceptedChannels = new HashSet<String>();
		rejectedChannels = new HashSet<String>();
		toSend = new  ConcurrentLinkedQueue<Message>();
	}
	public String getChannel(){
		return channel;
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
	
	public boolean channelStatus(String channel){
		if(!this.inAccChan(channel) && !this.inRejChan(channel))
			return true;
		else if(this.inRejChan(channel))
			return false;
		else
			return true;
	}
		
	private boolean inAccChan(String channel){
		return acceptedChannels.contains(channel);
	}
	
	private boolean inRejChan(String channel){
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
