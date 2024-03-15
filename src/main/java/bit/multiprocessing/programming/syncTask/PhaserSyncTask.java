package bit.multiprocessing.programming.syncTask;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Phaser;

/**
 * Использует счетчик с фазами.
 * Имеет более гибкий АПИ по сравнению с CountDownLatch и CyclicBarrier
 * По сути тот же CyclicBarrier, с разграничением на фазы выполнения
 *  <a href="https://habr.com/ru/articles/277669/">...</a>
 * Статья с гифками про все синхронизаторы
 *
 * Из минусов - относительно сложен в применении, но дает большую гибкость с многофазовой синхронизацией
 */
@Slf4j
@RequiredArgsConstructor
public class PhaserSyncTask implements Runnable{

    private final Phaser phaser;
    private final Runnable delegate;

    @Override
    public void run() {
        phaser.arriveAndAwaitAdvance(); // прибытие к 1й фазе и ожидание остальных
        delegate.run();
        phaser.arriveAndDeregister(); // прибытие ко 2й фазе и удаление
    }
}
