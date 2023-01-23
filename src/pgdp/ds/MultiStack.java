package pgdp.ds;

import java.util.concurrent.Semaphore;

public class MultiStack {

	private final Stack stacks;

	private RW lock;

	public MultiStack() {
		stacks = new Stack(1);
		lock = new RW(0);
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

	@Override
	public String toString() {
		return stacks.toString();
	}
}
