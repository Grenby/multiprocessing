package bit.multiprocessing.programming.syncTask;

import bit.multiprocessing.programming.TwoThreadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Конструкция, аналогичная тем, что были рассмотрены на парах
 */
@Slf4j
@RequiredArgsConstructor
public class SimpleSyncTask implements Runnable{

    public static class SyncSource{
        public volatile boolean val0 = true;
        public volatile boolean val1 = true;
    }

    private final Runnable delegate;
    private final SyncSource syncSource;

    /**
     * Корректность следует из корректности Мьютекса.
     * Большое преимущество данного метода - потоки не засыпают,
     * Минус, сложно реализовать для большего числа потоков
     */
    @Override
    public void run() {
        int other = TwoThreadUtils.other();
        if (other == 0) {
            syncSource.val0 = false;
            while (syncSource.val1){
                Thread.onSpinWait();
            }
        }
        else {
            syncSource.val1 = false;
            while (syncSource.val0){
                Thread.onSpinWait();
            }
        }
        delegate.run();
    }
}