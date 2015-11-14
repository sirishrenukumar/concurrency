package com.skbr;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class SemaphoreExample {
	
	private static class Worker extends Thread {

		private Semaphore semaphore;
		
		public Worker(Semaphore semaphore) {
			this.semaphore = semaphore;
		}

		@Override
		public void run() {
			
			try {
				System.out.println("Waiting in thread " + Thread.currentThread().getName());
				semaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			try {
				System.out.println("Starting processing in " + Thread.currentThread().getName());
				Thread.sleep(1000);
				System.out.println("End of processing in " + Thread.currentThread().getName());
				semaphore.release();
				System.out.println("Releasing permit from " + Thread.currentThread().getName());
				System.out.println();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {

		final int permits = 2;
		
		/*
		 * The number of permits available for the threads to acquire. In this example
		 * there are 5 permits that ensure that only 5 threads can proceed concurrently.
		 * After a thread completes, it returns the permit which can then be reused
		 */
		Semaphore semaphore = new Semaphore(permits);
		
		List<Worker> workers = new ArrayList<>();
		for(int i = 0; i < 5; ++i) {
			Worker worker = new Worker(semaphore);
			workers.add(worker);
			worker.start();
		}

		/*
		 * Wait for all threads to start and then block
		 */
		Thread.sleep(3000);
		
		for(Worker worker : workers)
			worker.join();
		
	}

}
