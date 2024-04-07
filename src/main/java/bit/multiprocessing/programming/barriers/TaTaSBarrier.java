package bit.multiprocessing.programming.barriers;

import bit.multiprocessing.programming.locks.SimpleLock;
import bit.multiprocessing.programming.locks.TaTaSLock;

public class TaTaSBarrier implements Barrier {

    private final int num;
    private int val = 0;
    private final SimpleLock lock = new TaTaSLock();

    public TaTaSBarrier(int num) {
        this.num = num;

    }

    @Override
    public void barrier() {
        lock.lock();
        val++;
        lock.unlock();
        while (val!=num){
            Thread.onSpinWait();
        }
    }
}
