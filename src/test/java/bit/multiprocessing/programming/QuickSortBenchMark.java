package bit.multiprocessing.programming;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 5,time = 10)
@Measurement(iterations = 20,time = 10)
@Fork(1)
public class QuickSortBenchMark {

    private final QuicksortSeq quicksort = new QuicksortSeq();
    private final QuicksortMultiThread quicksortMultiThread = new QuicksortMultiThread(4);

    private int[] array;

    @Setup
    public void setup() {
        int l = 100_000_000;
        array = new int[l];
        Random random = new Random();
        for (int i = 0; i < l; i++) {
            array[i] = random.nextInt();
        }
    }


    @Benchmark
    public int[] quickSortPoolBenchMark() {
        val arr = array;
        Utils.shuffleArray(arr);
        quicksortMultiThread.quickSort(arr);
        return arr;
    }

    @Benchmark
    public int[] quickSortSeqBenchMark() {
        val arr = array;
        Utils.shuffleArray(arr);
        quicksort.quickSort(arr);
        return arr;
    }

    @Benchmark
    public int[] shuffle() {
        val arr = array;
        Utils.shuffleArray(arr);
        return arr;
    }


}
