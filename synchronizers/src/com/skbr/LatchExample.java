package com.skbr;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class LatchExample {
	
	private static class Worker extends Thread {

		private CountDownLatch countDownLatch;
		
		public Worker(CountDownLatch countDownLatch) {
			this.countDownLatch = countDownLatch;
		}

		@Override
		public void run() {
			
			try {
				System.out.println("Waiting in thread " + Thread.currentThread().getName());
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			try {
				System.out.println("Starting processing in " + Thread.currentThread().getName());
				Thread.sleep(1000);
				System.out.println("End of processing in " + Thread.currentThread().getName());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		/*
		 * The count determines the number of external events that should occur 
		 * before the threads waiting on this latch will proceed. The threads
		 * are unblocked when the latch hits 0. In this example only a single 
		 * external event has to occur before the threads are unblocked 
		 */
		CountDownLatch countDownLatch = new CountDownLatch(1);
		
		List<Worker> workers = new ArrayList<>();
		for(int i = 0; i < 10; ++i) {
			Worker worker = new Worker(countDownLatch);
			workers.add(worker);
			worker.start();
		}

		/*
		 * Wait for all threads to start and then block
		 */
		Thread.sleep(3000);
		
		/*
		 * All threads are blocked until the latch becomes 0. Commenting the below line
		 * will result in indefinite loop
		 */
		countDownLatch.countDown();

		for(Worker worker : workers)
			worker.join();
		
	}

}
