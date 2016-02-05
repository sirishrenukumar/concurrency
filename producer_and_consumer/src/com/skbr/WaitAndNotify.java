package com.skbr;

import java.util.LinkedList;
import java.util.Queue;

public class WaitAndNotify {

	private static final int NUMBER_OF_ITEMS = 2;
	
	static class Producer extends Thread {
		
		private Queue<Integer> queue;

		public Producer(Queue<Integer> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			
			for(int i = 0; i < NUMBER_OF_ITEMS ; ++i) {
				synchronized (queue) {
					
					while(queue.size() == 5) {
						try {
							queue.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					System.out.println("Produced : " + i);
					queue.offer(i);
					queue.notify();
				}
			}
		}
	}
	
	static class Consumer extends Thread {
		private Queue<Integer> queue;

		public Consumer(Queue<Integer> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			
			for(int i = 0 ; i < NUMBER_OF_ITEMS ; ++i) {
				synchronized (queue) {
					
					while(queue.isEmpty()) {
						try {
							queue.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					System.out.println("Consumed : " + queue.poll());
					queue.notify();
				}
			}
		}
	}
	public static void main(String[] args) throws InterruptedException {

		Queue<Integer> queue = new LinkedList<>();
		
		Producer producer = new Producer(queue);
		Consumer consumer = new Consumer(queue);
		
		producer.start();
		consumer.start();
		
		producer.join();
		consumer.join();
		
	}

}
