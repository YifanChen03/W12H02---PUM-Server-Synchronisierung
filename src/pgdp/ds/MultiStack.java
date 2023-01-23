package pgdp.ds;

import java.util.concurrent.Semaphore;

public class MultiStack {

	private final Stack stacks;

	private static RW lock = new RW();

	public MultiStack() {
		stacks = new Stack(1);
	}

	public void push(int val) {
		try {
			lock.startWrite();
			stacks.push(val);
			lock.endWrite();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int pop() {
		try {
			lock.startWrite();
			if (stacks.isEmpty()) {
				lock.endWrite();
				return Integer.MIN_VALUE;
			}
			int temp = stacks.pop();
			lock.endWrite();
			return temp;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Integer.MIN_VALUE;
	}

	public int top() {
		try {
			lock.startRead();
			if (stacks.isEmpty()) {
				return Integer.MIN_VALUE;
			}
			lock.endRead();
			return stacks.top();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Integer.MIN_VALUE;
	}

	public int size() {
		try {
			lock.startRead();
			if (stacks.isEmpty()) {
				return 0;
			}
			lock.endRead();
			return stacks.size();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Integer.MIN_VALUE;
	}

	public int search(int element) {
		try {
			lock.startRead();
			if (stacks.isEmpty()) {
				return -1;
			}
			lock.endRead();
			return stacks.search(element);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return Integer.MIN_VALUE;
	}

	//aus der Zentralübung
	public static class RW {
		private int countReaders = 0;

		public synchronized void startRead() throws InterruptedException {
			while (countReaders < 0) {
				wait();
			}
			countReaders++;
		}

		public synchronized void endRead() {
			countReaders--;
			if (countReaders == 0) {
				notify();
			}
		}

		public synchronized void startWrite() throws InterruptedException {
			while (countReaders != 0) {
				wait();
			}
			countReaders = -1;
		}

		public synchronized void endWrite() {
			countReaders = 0;
			notifyAll();
		}
	}

	@Override
	public String toString() {
		return stacks.toString();
	}
}
