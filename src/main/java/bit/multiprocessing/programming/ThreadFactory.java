package bit.multiprocessing.programming;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Фабрика потоков, для создания нумерованных потоков.
 */
@Slf4j
public class ThreadFactory {

    private final AtomicInteger count = new AtomicInteger(0);

    /**
     * Метод для создания потока с указанным именем
     */
    public static Thread getNumberedThread(int number, Runnable task) {
        log.debug("was created Thread with number {}", number);
        return new Thread(task, Integer.toString(number));
    }

    /**
     * Метод для создания потоков с последовательными номерами
     */
    public Thread nextThread(Runnable task) {
        return getNumberedThread(count.incrementAndGet(), task);
    }

}
