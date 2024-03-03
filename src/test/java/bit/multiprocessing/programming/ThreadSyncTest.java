package bit.multiprocessing.programming;

import bit.multiprocessing.programming.syncTask.CountDownLatchSyncTask;
import bit.multiprocessing.programming.syncTask.CyclicBarrierSyncTask;
import bit.multiprocessing.programming.syncTask.PhaserSyncTask;
import bit.multiprocessing.programming.syncTask.SimpleSyncTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
public class ThreadSyncTest {
    final static int COUNT = 1000;
    static Map<String,List<Long>> results;

    ThreadFactory factory = new ThreadFactory();

    @BeforeAll
    static void init(){
        results = new TreeMap<>();
    }

    @BeforeEach
    void sleep() throws InterruptedException {
        Thread.sleep(10);
    }

    @RepeatedTest(value = COUNT)
    public void gapTest(RepetitionInfo repetitionInfo) throws InterruptedException {
        Task task1 = new Task();
        Task task2 = new Task();

        Thread t1 = factory.getNumberedThread(0, task1);
        Thread t2 = factory.getNumberedThread(1, task2);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        addResult("gapTest", task1, task2, repetitionInfo);
    }

    /**
     * Тест на синхронизацию через фазер.
     * Геп может возникнуть если первый поток зайдет и уснет, а второй зайдет, кинет notify и продолжит выполнение
     */
    @RepeatedTest(value = COUNT)
    public void phaserSyncTest(RepetitionInfo repetitionInfo) throws InterruptedException {
        Phaser phaser = new Phaser(2);
        Task task1 = new Task();
        Task task2 = new Task();

        Thread t1 = factory.getNumberedThread(0, new PhaserSyncTask(phaser, task1));
        Thread t2 = factory.getNumberedThread(1, new PhaserSyncTask(phaser, task2));

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        addResult("phaserSyncTest", task1, task2, repetitionInfo);
    }

    /**
     * Тест на синхронизацию через фазер.
     * Чтобы уменьшить геп, можно дать обоим потокам заснуть, и после
     * пробудить их по команде.
     */
    @RepeatedTest(value = COUNT)
    public void phaserWithCommandSyncTest(RepetitionInfo repetitionInfo) throws InterruptedException {
        Phaser phaser = new Phaser(3);// 2 воркера + 1 main для синхронизации
        Task task1 = new Task();
        Task task2 = new Task();

        Thread t1 = factory.getNumberedThread(0, new PhaserSyncTask(phaser, task1));
        Thread t2 = factory.getNumberedThread(1, new PhaserSyncTask(phaser, task2));

        t1.start();
        t2.start();

        Thread.sleep(10);
        //ждем пока воркеры заснут и будим их
        phaser.arriveAndAwaitAdvance(); // ждем пока у начала 1й фазы
        phaser.arriveAndAwaitAdvance(); // ждем пока у начала 2й фазы (све потоки закончат выполнение)
        phaser.arriveAndDeregister(); // завершаем выполнение текущего потока у 3й фазы

        addResult("phaserSyncSleepTest", task1, task2, repetitionInfo);
    }

    /**
     * Тест на метод синхронизации с пар.
     * Так как в данном методе потоки не засыпают,
     * он может давать большую точность
     */
    @RepeatedTest(value = COUNT)
    public void simpleSyncTest(RepetitionInfo repetitionInfo) throws InterruptedException {
        Task task1 = new Task();
        Task task2 = new Task();

        SimpleSyncTask.SyncSource syncSource = new SimpleSyncTask.SyncSource();

        Thread t1 = factory.getNumberedThread(0, new SimpleSyncTask(task1,syncSource));
        Thread t2 = factory.getNumberedThread(1, new SimpleSyncTask(task2,syncSource));

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        addResult("simpleSyncTest", task1, task2, repetitionInfo);
    }


    /**
     * Тест на синхронизацию через CountDownLatch
     */
    @RepeatedTest(value = COUNT)
    public void countDownLatchSyncTest(RepetitionInfo repetitionInfo) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Task task1 = new Task();
        Task task2 = new Task();

        Thread t1 = factory.getNumberedThread(0, new CountDownLatchSyncTask(countDownLatch,task1));
        Thread t2 = factory.getNumberedThread(1, new CountDownLatchSyncTask(countDownLatch,task2));

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        addResult("countDownLatchSyncTest", task1, task2, repetitionInfo);
    }

    /**
     * Тест на синхронизацию через CountDownLatch
     * Пробуждаем воркеров через команду из майн потока.
     */
    @RepeatedTest(value = COUNT)
    public void countDownByCommandSyncTest(RepetitionInfo repetitionInfo) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);// 2 воркера + 1 main для синхронизации

        Task task1 = new Task();
        Task task2 = new Task();

        Thread t1 = factory.getNumberedThread(0, new CountDownLatchSyncTask(countDownLatch,task1));
        Thread t2 = factory.getNumberedThread(1, new CountDownLatchSyncTask(countDownLatch,task2));

        t1.start();
        t2.start();

        Thread.sleep(10);
        //ждем пока воркеры заснут и будим их
        countDownLatch.countDown();

        t1.join();
        t2.join();

        addResult("countDownByCommandSyncTest", task1, task2, repetitionInfo);
    }

    /**
     * Тест на синхронизацию через CyclicBarrier
     */
    @RepeatedTest(value = COUNT)
    public void cyclicBarrierSyncTest(RepetitionInfo repetitionInfo) throws InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

        Task task1 = new Task();
        Task task2 = new Task();

        Thread t1 = factory.getNumberedThread(0, new CyclicBarrierSyncTask(cyclicBarrier,task1));
        Thread t2 = factory.getNumberedThread(1, new CyclicBarrierSyncTask(cyclicBarrier,task2));

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        addResult("cyclicBarrierSyncTest", task1, task2, repetitionInfo);
    }

    /**
     * Тест на синхронизацию через CyclicBarrier
     * Аналогично верхним, используем главный поток для
     * одновременного пробуждение воркеров
     */
    @RepeatedTest(value = COUNT)
    public void cyclicBarrierByCommandSyncTest(RepetitionInfo repetitionInfo) throws InterruptedException, BrokenBarrierException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);// 2 воркера + 1 main для синхронизации

        Task task1 = new Task();
        Task task2 = new Task();

        Thread t1 = factory.getNumberedThread(0, new CyclicBarrierSyncTask(cyclicBarrier,task1));
        Thread t2 = factory.getNumberedThread(1, new CyclicBarrierSyncTask(cyclicBarrier,task2));

        t1.start();
        t2.start();
        //ждем пока воркеры заснут и будим их
        Thread.sleep(10);
        cyclicBarrier.await();

        t1.join();
        t2.join();

        addResult("cyclicBarrierByCommandSyncTest", task1, task2, repetitionInfo);
    }


    private void addResult(String name, Task t1, Task t2, RepetitionInfo repetitionInfo){
        assertNotEquals(t1.getTime(),0);
        assertNotEquals(t2.getTime(),0);
        if (repetitionInfo.getCurrentRepetition() >200) {
            results.computeIfAbsent(name, s -> new ArrayList<>()).add(
                    Math.abs(t1.getTime()- t2.getTime())
            );
        }
    }

    @AfterAll
    static void printRes(){

        for (String name: results.keySet()){
            List<Long> l = results.get(name);
            log.info("Results for {}\n max delta: {} mks\n min delta: {} mks\n mean delta: {} ± {} mks", name,
                    getMax(l),
                    getMin(l),
                    getMean(l),
                    err((long)(1000*getMean(l)), l)
            );
        }
    }

    static double getMax(List<Long> l){
        return l.stream().max(Long::compareTo).map(a->(double)a).orElse(0.0)/1000;
    }


    static double getMin(List<Long> l){
        return l.stream().min(Long::compareTo).map(a->(double)a).orElse(0.0)/1000;
    }


    static double getMean(List<Long> l){
        return l.stream().reduce(0L, Long::sum)/ Math.max(l.size(),1.0)/1000;
    }

    static double err(double val, List<Long> l){
        return Math.sqrt(l.stream().map(x-> (x - val)*(x - val)).reduce(0.0, Double::sum)/ Math.max(l.size() -1,1.0))/1000.0;
    }

}

/**
 * [2024-03-03 21:06:53] INFO  [ThreadSyncTest:239] [thread:main]: Results for countDownByCommandSyncTest
 *  max delta: 3935.1 mks
 *  min delta: 1.6 mks
 *  mean delta: 36.67203333333333 ± 74.2488575365428 mks
 * [2024-03-03 21:06:53] INFO  [ThreadSyncTest:239] [thread:main]: Results for countDownLatchSyncTest
 *  max delta: 3055.4 mks
 *  min delta: 0.9 mks
 *  mean delta: 11.7812 ± 55.83436897514784 mks
 * [2024-03-03 21:06:53] INFO  [ThreadSyncTest:239] [thread:main]: Results for cyclicBarrierByCommandSyncTest
 *  max delta: 384.4 mks
 *  min delta: 2.8 mks
 *  mean delta: 35.603699999999996 ± 23.675126342551557 mks
 * [2024-03-03 21:06:53] INFO  [ThreadSyncTest:239] [thread:main]: Results for cyclicBarrierSyncTest
 *  max delta: 117.6 mks
 *  min delta: 4.2 mks
 *  mean delta: 20.57916666666667 ± 16.113428049958014 mks
 * [2024-03-03 21:06:53] INFO  [ThreadSyncTest:239] [thread:main]: Results for gapTest
 *  max delta: 336.1 mks
 *  min delta: 8.0 mks
 *  mean delta: 74.1809 ± 40.97144874833587 mks
 * [2024-03-03 21:06:53] INFO  [ThreadSyncTest:239] [thread:main]: Results for phaserSyncSleepTest
 *  max delta: 255.2 mks
 *  min delta: 0.0 mks
 *  mean delta: 8.903333333333334 ± 13.85196476951887 mks
 * [2024-03-03 21:06:53] INFO  [ThreadSyncTest:239] [thread:main]: Results for phaserSyncTest
 *  max delta: 2347.7 mks
 *  min delta: 0.0 mks
 *  mean delta: 11.800166666666666 ± 57.699144422504475 mks
 * [2024-03-03 21:06:53] INFO  [ThreadSyncTest:239] [thread:main]: Results for simpleSyncTest
 *  max delta: 62.6 mks
 *  min delta: 0.0 mks
 *  mean delta: 0.15 ± 1.4232715484522076 mks
 *
 * Process finished with exit code 0
 */