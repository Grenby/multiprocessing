package bit.multiprocessing.programming.locks;

import java.util.concurrent.atomic.AtomicBoolean;

public class TaTaSLock implements SimpleReadWriteLock {

    private final AtomicBoolean lock = new AtomicBoolean(false);

    @Override
    public void lock() {
        while (true){
            while (lock.get()){
                Thread.onSpinWait();
            }
            if (!lock.getAndSet(true)) return;
        }
    }

    @Override
    public void unlock() {
        lock.set(false);
    }
}
