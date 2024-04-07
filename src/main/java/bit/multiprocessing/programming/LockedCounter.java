package bit.multiprocessing.programming;

import bit.multiprocessing.programming.locks.SimpleLock;
import bit.multiprocessing.programming.locks.TaTaSLock;

public class LockedCounter {

    private int value = 0;
    private final SimpleLock lock = new TaTaSLock();

    public void increment(){
        lock.lock();
        value++;
        lock.unlock();
    }

    public int get(){
        return value;
    }

}
