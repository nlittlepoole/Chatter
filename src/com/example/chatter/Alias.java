package com.example.chatter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class Alias {

	public Vector<String> available;
	public Vector<String> used;
	private String channel;
	
	public Alias(){
		available  = new Vector<String>();
		used = new Vector<String>();
	}
	
	public void read(String fileName){
		Scanner in = null;
		try {
			in = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(in.hasNext()){
			String next = in.nextLine();
			next = next.replaceAll("![a-zA-z]", "");
			available.add(next);
		}
		System.out.println(available);
	}
	
	
	
}
