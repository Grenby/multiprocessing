package bit.multiprocessing.programming.locks;

public interface SimpleLock {

    void lock() throws InterruptedException;

    void unlock();

}
