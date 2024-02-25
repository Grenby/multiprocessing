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
        private final boolean[] arr = new boolean[]{true, true};
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
        int me = TwoThreadUtils.getNumber();
        int other = TwoThreadUtils.other();
        syncSource.arr[other] = false;
        while (syncSource.arr[me]){
            Thread.onSpinWait();
        }
        delegate.run();
    }
}