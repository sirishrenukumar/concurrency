package com.skbr;

public class SimpleDeadlock {
	
	static class SimpleThread extends Thread {
		
		private SimpleLock lock1;
		private SimpleLock lock2;
		
		public SimpleThread(SimpleLock lock1, SimpleLock lock2) {

//			if(lock1.getName().compareTo(lock2.getName()) <=0) {
//				this.lock1 = lock1;
//				this.lock2 = lock2;
//			} else 
//			{
//				this.lock1 = lock2;
//				this.lock2 = lock1;
//			}
			this.lock1 = lock1;
			this.lock2 = lock2;
			
		}
		

		@Override
		public void run() {
			
			synchronized(lock1) {
				System.out.printf("%s : Acquiring %s \n", Thread.currentThread().getName(), lock1);
				synchronized (lock2) {
					System.out.printf("%s : Acquiring %s\n", Thread.currentThread().getName(), lock2);
				}
				System.out.printf("%s : Releasing %s \n", Thread.currentThread().getName(), lock2);
			}
			System.out.printf("%s : Releasing %s \n", Thread.currentThread().getName(), lock1);
		}
	}
	static class SimpleLock {
		private String name;

		public SimpleLock(String name) {
			super();
			this.name = name;
		}

		@Override
		public String toString() {
			return "SimpleLock [name=" + name + "]";
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
	public static void main(String []a) {
		

		SimpleLock lock1 = new SimpleLock("Lock1");
		SimpleLock lock2 = new SimpleLock("Lock2");
		
		new SimpleThread(lock1, lock2).start();
		new SimpleThread(lock2, lock1).start();
		
		
	}

}
