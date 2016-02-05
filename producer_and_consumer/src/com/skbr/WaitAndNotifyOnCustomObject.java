package com.skbr;

public class WaitAndNotifyOnCustomObject {
	
	
	private static class Bucket {
		
		private boolean isFull;
		private int value;
		public boolean isFull() {
			return isFull;
		}
		public void setFull(boolean isFull) {
			this.isFull = isFull;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
	}
	
	private static class Producer extends Thread {
		private Bucket bucket;

		public Producer(Bucket bucket) {
			this.bucket = bucket;
		}

		@Override
		public void run() {
			int value = 0;
			while(true) {
				synchronized (bucket) {
					while(bucket.isFull()) {
						try {
							bucket.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					bucket.setValue(value++);
					bucket.setFull(true);
					System.out.println("Produced : " + value);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					bucket.notifyAll();
				}
			}
		}
	}
	
	private static class Consumer extends Thread {
		private Bucket bucket;

		public Consumer(Bucket bucket) {
			super();
			this.bucket = bucket;
		}

		@Override
		public void run() {
			
			while(true) {
				
				synchronized (bucket) {
					
					while(!bucket.isFull()) {
						try {
							bucket.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Consumed : " + bucket.getValue());
					bucket.setFull(false);
					bucket.notifyAll();
				}
			}
		}
	}
	
	

	public static void main(String[] args) {

		Bucket bucket = new Bucket();
		new Producer(bucket).start();
		new Consumer(bucket).start();
	}

}
