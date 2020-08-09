package com.jdc.views.test;

import java.time.LocalTime;

public class StringTest {
	
	static int loop = 100_0000;
	
	public static void main(String[] args) {
		
		int start = LocalTime.now().getNano();
		
		String s = "";
		
		for(int i = 0; i < loop; i++) {
		 	s.concat("a");
		}
		
		int end = LocalTime.now().getNano();
		System.out.println("Time Taken (String): " + (end - start));
		
		start = LocalTime.now().getNano();
		
		StringBuilder builder = new StringBuilder("");
		for(int i = 0; i < loop; i++) {
		 	builder.append("a");
		}
		
		end = LocalTime.now().getNano();
		System.out.println("Time Taken (Builder): " + (end - start));
		
		start = LocalTime.now().getNano();
		
		StringBuffer buffer = new StringBuffer("");
		for(int i = 0; i < loop; i++) {
		 	buffer.append("a");
		}
		
		end = LocalTime.now().getNano();
		System.out.println("Time Taken (Buffer): " + (end - start));
	}

}















