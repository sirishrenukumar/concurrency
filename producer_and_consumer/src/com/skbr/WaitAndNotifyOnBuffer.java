package com.skbr;

import java.util.concurrent.TimeUnit;

public class WaitAndNotifyOnBuffer {
	
	private static final int NUMBER_OF_ITEMS = 6;
	
	private static class BlockingBuffer {
		
		private int[] array;
		private int size;
		private int count;
		
		public BlockingBuffer(int size) {
			this.size = size;
			this.array = new int[size];
		}
		
		public synchronized void put(int item) throws InterruptedException {
			while(count == size)
				wait();
			
			array[count++] = item;
			
			System.out.println("Produced :" + item);
			notifyAll();
		}
		public synchronized int take() throws InterruptedException {
			while(count == 0)
				wait();
			
			int item = array[--count];
			
			System.out.println("Consumed :" + item);
			notifyAll();
			return item;
		}
	}
	
	private static class Producer extends Thread {
		
		private BlockingBuffer blockingBuffer;
		
		public Producer(BlockingBuffer blockingBuffer) {
			super();
			this.blockingBuffer = blockingBuffer;
		}

		@Override
		public void run() {
			
			for(int i = 0; i < NUMBER_OF_ITEMS; ++i) {
				
				try {
					blockingBuffer.put(i);
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static class Consumer extends Thread {
		private BlockingBuffer blockingBuffer;

		public Consumer(BlockingBuffer blockingBuffer) {
			super();
			this.blockingBuffer = blockingBuffer;
		}

		@Override
		public void run() {
			for(int i = 0; i < NUMBER_OF_ITEMS; ++i) {
				
				try {
					blockingBuffer.take();
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}

	public static void main(String[] args) throws InterruptedException {
		
		BlockingBuffer blockingBuffer = new BlockingBuffer(2);
		Producer producer = new Producer(blockingBuffer);
		Consumer consumer = new Consumer(blockingBuffer);
		
		producer.start();
		consumer.start();
		
		producer.join();
		consumer.join();
		
		

	}

}
