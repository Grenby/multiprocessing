package bit.multiprocessing.programming;

import bit.multiprocessing.programming.locks.SimpleReadWriteLock;
import bit.multiprocessing.programming.locks.TaTaSLock;

public class LockedCounter {

    private int value = 0;
    private final SimpleReadWriteLock lock = new TaTaSLock();

    public void increment(){
        lock.lock();
        value++;
        lock.unlock();
    }

    public int get(){
        return value;
    }

}
