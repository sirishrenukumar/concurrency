package com.skbr;

public class BoundedBlockingQueueExample {

	private static class BoundedBlockingQueue {
		
		private int size;
		private int count;
		private int array[];
		private int head;
		private int tail;
		
		public BoundedBlockingQueue(int size) {
			this.size = size;
			this.array = new int[size];
			this.head = 0;
			this.tail = 0;
		}
		
		boolean isFull() {
			return count == size;
		}
		boolean isEmpty() {
			return count == 0;
		}
		
		void put(int item) {
			
			synchronized (this) {
				while(isFull()) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				array[tail] = item;
				tail = (tail + 1) % size;
				++count;
				this.notifyAll();
			}
		}
		
		int get() {
			int result;
			synchronized (this) {
				
				while(isEmpty()) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				result = array[head];
				head = (head + 1) % size;
				--count;
				this.notifyAll();
			}
			return result;
		}
		
	}
	public static void main(String[] args) {
		
		final BoundedBlockingQueue blockingQueue = new BoundedBlockingQueue(2);

		new Thread(new Runnable() {
			public void run() {
				for(int i = 0; i < 10; ++i) {
					blockingQueue.put(i);
					System.out.println("Produced " + i + " from " + Thread.currentThread().getName());
				}

			}
		},"Producer1").start();
		
		new Thread(new Runnable() {
			public void run() {
				for(int i = 10; i < 20; ++i) {
					blockingQueue.put(i);
					System.out.println("Produced " + i + " from " + Thread.currentThread().getName());
				}

			}
		},"Producer2").start();

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					System.out.println("Consumed " + blockingQueue.get() + " from " + Thread.currentThread().getName());
				}
				
			}
		},"Consumer1").start();
		

	}

}
