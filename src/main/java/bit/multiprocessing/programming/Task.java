package bit.multiprocessing.programming;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class Task implements Runnable {

    @Getter
    private long time;


    @Override
    public void run() {
        time = System.nanoTime();
        log.debug("task was run from thread {}", Thread.currentThread().getName());
    }
}
