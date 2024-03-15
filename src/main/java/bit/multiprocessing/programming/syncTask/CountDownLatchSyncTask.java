package bit.multiprocessing.programming.syncTask;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.CountDownLatch;

/**
 * Использует счетчик под капотом.
 * Как только он доходит до 0, все потоки в await начинают выполнение
 * Из плюсов прост в использовании, из минусов одноразовый - на каждый этап синхронизации нужно создавать новый
 * счетчик
 */
@RequiredArgsConstructor
public class CountDownLatchSyncTask implements Runnable{

    private final CountDownLatch countDownLatch;
    private final Runnable delegate;

    @Override
    public void run() {
        try {
            countDownLatch.countDown();
            countDownLatch.await();
            delegate.run();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
