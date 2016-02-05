package com.skbr;

public class CreateThread {
	
	

	public static void main(String[] args) {

		new Thread().start();
		new Thread( new Runnable() {
			@Override
			public void run() {
				System.out.println("Hello World from Runnable");
				
			}}).start();
		new Thread() {
			@Override
			public void run() {
				System.out.println("Hello World from Thread");
				
			}
		}.start();

		new Thread(() -> System.out.println("Hello World from Lamba")).start();
	}

}
