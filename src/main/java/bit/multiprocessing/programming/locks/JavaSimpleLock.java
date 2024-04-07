package bit.multiprocessing.programming.locks;

import lombok.SneakyThrows;

public class JavaSimpleLock implements SimpleLock{

    private final Object lock = new Object();
    private boolean locked = false;

    @SneakyThrows
    @Override
    public void lock() {
        synchronized (lock){
            while (locked){
                lock.wait();
            }
            locked = true;
        }
    }

    @Override
    public void unlock() {
        synchronized (lock){
            locked=false;
            lock.notify();
        }
    }
}
