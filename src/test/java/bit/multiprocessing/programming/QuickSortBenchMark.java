package bit.multiprocessing.programming;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 10, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class QuickSortBenchMark {

    private final int L = 100_000_000;
    private final Quicksort quicksort = new QuicksortSeq();
    private int[] array;

    @Setup
    public void setup() {
        array = new int[L];
        Random random = new Random();
        for (int i = 0; i < L; i++) {
            array[i] = random.nextInt();
        }
    }


    @Benchmark
    public int[] quickSortBenchMark() {
        val arr = array;
        quicksort.quickSort(arr);
        return arr;
    }

}
