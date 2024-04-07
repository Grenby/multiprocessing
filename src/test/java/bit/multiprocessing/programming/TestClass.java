package bit.multiprocessing.programming;

import bit.multiprocessing.programming.barriers.Barrier;
import bit.multiprocessing.programming.barriers.TaTaSBarrier;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 3)
@Fork(1)
public class TestClass {

    private static final int N = 6;

    @State(Scope.Benchmark)
    public static class SpinWaitCounter{
        TaTaSBarrier barrier = new TaTaSBarrier(N);
    }

    @Benchmark
    @Group("IncrementAndSpinWait")
    @GroupThreads(N)
    public void incrementAndSpin(final SpinWaitCounter counter){
        counter.barrier.barrier();
    }



    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(TestClass.class.getName())
                .build();
        new Runner(opt).run();
    }




}
