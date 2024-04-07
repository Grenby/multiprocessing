package bit.multiprocessing.programming.locks;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

public class SimpleReadWriteLock {

    private final Object sync = new Object();
    @Getter
    private final WriteLock writeLock;
    @Getter
    private final ReadLock readLock;

    public SimpleReadWriteLock() {
        writeLock = new WriteLock(sync);
        readLock = new ReadLock(sync);

        synchronized (sync) {
            writeLock.read = readLock;
            readLock.readLock = writeLock;
        }
    }

    @RequiredArgsConstructor
    private static class ReadLock implements SimpleLock {

        private final Object sync;
        WriteLock readLock;
        private int locked = 0;

        @SneakyThrows
        @Override
        public void lock() {
            synchronized (sync) {
                while (readLock.locked) {
                    sync.wait();
                }
                locked += 1;
            }
        }

        @Override
        public void unlock() {
            synchronized (sync) {
                locked -= 1;
                // нотифай имеет смысл только для write
                if (locked == 0) {
                    sync.notify();
                }
            }
        }
    }

    @RequiredArgsConstructor
    private static class WriteLock implements SimpleLock {

        private final Object sync;
        ReadLock read;
        boolean locked = false;

        @Override
        public void lock() throws InterruptedException {
            synchronized (sync) {
                while (read.locked != 0 || locked) {
                    sync.wait();
                }
                locked = true;
            }
        }

        @Override
        public void unlock() {
            synchronized (sync) {
                locked = false;
                sync.notify();
            }
        }
    }
}
