package com.skbr;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierExample {
	
	private static class Worker extends Thread {

		private CyclicBarrier cyclicBarrier;
		
		public Worker(CyclicBarrier cyclicBarrier) {
			this.cyclicBarrier = cyclicBarrier;
		}

		@Override
		public void run() {
			
			try {
				System.out.println("Waiting in thread " + Thread.currentThread().getName());
				cyclicBarrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
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
		
		final int numberOfThreadsToSynchronize = 5;
		final int numberOfThreadsToCreate = 10;

		/*
		 * The action that is executed when the barrier breaks. 
		 */
		Runnable barrierBrokenAction = () -> System.out.println("Barrier broken action");
		
		/*
		 * The count determines the number of threads that need to arrive (i.e block) 
		 * at the barrier for the barrier to break. In this example the barrier will break
		 * when 5 threads arrive at the barrier. In case more threads arrive, they just pass 
		 * through
		 */
		CyclicBarrier cyclicBarrier = new CyclicBarrier(numberOfThreadsToSynchronize, barrierBrokenAction);
		
		List<Worker> workers = new ArrayList<>();
		for(int i = 0; i < numberOfThreadsToCreate; ++i) {
			Worker worker = new Worker(cyclicBarrier);
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
