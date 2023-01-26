package pgdp.ds;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class RingBuffer {

	private int[] mem;
	private int in;
	private int out;
	private int stored;
	private Semaphore free;
	private Semaphore occupied;

	public RingBuffer(int capacity) {
		mem = new int[capacity];
		in = 0;
		out = 0;
		stored = 0;
		free = new Semaphore(capacity);
		occupied = new Semaphore(0);
	}

	public boolean isEmpty() {
		return stored == 0;
	}

	public boolean isFull() {
		return stored == mem.length;
	}

	public void put(int val) throws InterruptedException {
		free.acquire();
		synchronized (this) {
			if (isFull()) {
				return;
			}
			mem[in++] = val;
			in %= mem.length;
			stored++;
		}
		occupied.release();
	}

	public int get() throws InterruptedException {
		occupied.acquire();
		int val = Integer.MIN_VALUE;
		synchronized (this) {
			if (isEmpty()) {
				return Integer.MIN_VALUE;
			}
			val = mem[out++];
			out %= mem.length;
			stored--;

		}
		free.release();
		return val;
	}

	@Override
	public String toString() {
		try {
			occupied.acquire();
			StringBuilder sb = new StringBuilder();
			sb.append("RingBuffer := { capacity = ").append(mem.length).append(", out = ").append(out).append(", in = ")
					.append(in).append(", stored = ").append(stored).append(", mem = ").append(Arrays.toString(mem))
					.append(", buffer = [");
			if (!isEmpty()) {
				if (in >= 0 || in < mem.length) {
					int i = out;
					do {
						sb.append(mem[i]).append(", ");
						i = (i + 1) % mem.length;
					} while (i != in);
					sb.setLength(sb.length() - 2);
				} else {
					sb.append("Error: Field 'in' is <").append(in)
							.append(">, which is out of bounds for an array of length ").append(mem.length);
				}
			}
			sb.append("] }");
			free.release();
			return sb.toString();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
}