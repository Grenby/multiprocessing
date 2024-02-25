package bit.multiprocessing.programming.syncTask;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Использует счетчик под капотом.
 * Как только он доходит до 0, все потоки в await начинают выполнение
 * Отличается от CountDownLatch возможностью сброса
 *
 */
@RequiredArgsConstructor
public class CyclicBarrierSyncTask implements Runnable{

    private final CyclicBarrier cyclicBarrier;
    private final Runnable delegate;

    @Override
    public void run() {
        try {
            cyclicBarrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
        delegate.run();
    }
}
