package pgdp.ds;

public class MultiStack {

	private final Stack stacks;

	private final RW lock;

	public MultiStack() {
		stacks = new Stack(1);
		lock = new RW();
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

	public static void main(String[] args) {
		MultiStack ms = new MultiStack();
		Thread pusher = new Thread(
				() -> {
					for (int i = 0; i < 10; i++) {
						ms.push(i);
					}
					System.out.println("finished push1");
				}
		);

		Thread topper = new Thread(
				() -> {
					System.out.println("top: " + ms.top());
				}
		);

		Thread popper = new Thread(
				() -> {
					System.out.println("pop: " + ms.pop());
				}
		);

		Thread sizer = new Thread(
				() -> {
					System.out.println("size: " + ms.size());
				}
		);

		Thread pusher2 = new Thread(
				() -> {
					for (int i = 10; i < 20; i++) {
						ms.push(i);
					}
					System.out.println("finished push2");
				}
		);

		Thread searcher = new Thread(
				() -> {
					System.out.println("search: " + ms.search(12));
				}
		);

		searcher.start();
		pusher2.start();
		pusher.start();
		topper.start();
		popper.start();
		sizer.start();
	}
}


