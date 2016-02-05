package com.skbr;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UsingCondtions {

	static class Buffer {
		private char item;
		private boolean available;
		private Lock lock;
		private Condition notFull;
		private Condition notEmpty;

		public Buffer() {
			lock = new ReentrantLock();
			notFull = lock.newCondition();
			notEmpty = lock.newCondition();
		}

		private char getItem() {

			lock.lock();
			
			while (!available) {
				try {
					notEmpty.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			char result = item;
			available = false;
			notFull.signalAll();

			lock.unlock();
			return result;
		}

		private void setItem(char item) {

			lock.lock();
			
			while (available) {
				try {
					notFull.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			this.item = item;
			available = true;
			notEmpty.signalAll();
			
			lock.unlock();
		}
	}

	public static void main(String[] args) {

		ExecutorService executorService = Executors.newFixedThreadPool(3);

		final Buffer buffer = new Buffer();

		Runnable producer = () -> {
			for(char c = 'a' ; c <= 'z'; ++c) {
				buffer.setItem(c);
				System.out.println("Produced " + c);
			}

		};
		Runnable consumer1 = () -> {
			while (true) {
				System.out.println("Consumed " + buffer.getItem() + " from " + Thread.currentThread().getName());
			}
		};
		Runnable consumer2 = () -> {
			while (true) {
				System.out.println("Consumed " + buffer.getItem() + " from " + Thread.currentThread().getName());
			}
		};

		executorService.execute(producer);
		executorService.execute(consumer1);
		executorService.execute(consumer2);

		try {
			executorService.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		executorService.shutdown();
	}

}
