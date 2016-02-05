package com.skbr;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueueExample {

	private static class BlockingQueue<T> {

		private Queue<T> queue;
		private int size;

		public BlockingQueue(int size) {
			queue = new LinkedList<T>();
			this.size = size;
		}

		public void put(T item) {

			synchronized (queue) {
				while (queue.size() == size) {
					
					System.out.println("Producer waiting");
					try {
						queue.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				queue.add(item);
				queue.notifyAll();
			}
		}

		public T take() {

			synchronized (queue) {

				while (queue.isEmpty()) {
					try {
						System.out.println("Consumer waiting");
						queue.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				T item = queue.remove();
				queue.notifyAll();
				return item;
			}
		}
	}

	private static class Producer extends Thread {

		private BlockingQueue<Integer> blockingQueue;
		
		public Producer(BlockingQueue<Integer> blockingQueue) {
			this.blockingQueue = blockingQueue;
		}

		@Override
		public void run() {

			int i = 0;

			while (true) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("Producing " + i);
				blockingQueue.put(i);
				++i;
			}
		}

	}
	private static class Consumer extends Thread {

		private BlockingQueue<Integer> blockingQueue;

		public Consumer(BlockingQueue<Integer> blockingQueue) {
			this.blockingQueue = blockingQueue;
		}


		@Override
		public void run() {

			while (true) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int i = blockingQueue.take();
				System.out.println("Consuming " + i);
			}
		}

	}

	public static void main(String[] args) {
		BlockingQueue<Integer> blockingQueue = new BlockingQueue<>(1);
		new Producer(blockingQueue).start();
		new Consumer(blockingQueue).start();
	}
}
