package bit.multiprocessing.programming;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Warmup(iterations = 10, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 20, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class StaticVolatileReadWriteTest {

    @State(Scope.Benchmark)
    public static class TestClass {
        static volatile boolean active = false;

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            TestClass.active = active;
        }
    }


    @Param({"0", "2", "3", "4", "5", "6", "7", "8", "9",
            "10","15",
            "20","25",
            "30","35","40"})
    private int tokens = 0;

    @Benchmark
    @Group("TestClassRead")
    @GroupThreads(2)
    public boolean read(final TestClass testClass) {
        Blackhole.consumeCPU(tokens);
        return testClass.isActive();
    }


    @Benchmark
    @Group("TestClassWrite")
    @GroupThreads(2)
    public void write(final TestClass testClass) {
        Blackhole.consumeCPU(tokens);
        testClass.setActive(false);
    }

}