package pgdp.ds;

//aus der Zentralübung
public class RW {
    private int countReaders;

    public RW() {
        countReaders = 0;
    }

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
