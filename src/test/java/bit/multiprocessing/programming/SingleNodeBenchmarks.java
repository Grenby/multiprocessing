package bit.multiprocessing.programming;


import bit.multiprocessing.programming.impl.LazyThreadNode;
import lombok.SneakyThrows;
import org.apache.commons.math3.analysis.function.Sin;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
@Warmup(iterations = 10, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 20, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class SingleNodeBenchmarks {

//    @Param({"0", "2", "3", "4", "5", "6", "7", "8", "9",
//            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
//            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
//            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39","40"})
    private int tokens = 0;

    private final Message message = () -> 0;

    LazyThreadNode lazyThreadNode = new LazyThreadNode(0);

    @Setup
    public void set(){
        lazyThreadNode.sendMessage(()->1);
    }

    @Benchmark
    public int lazySend() {
        Blackhole.consumeCPU(tokens);
        lazyThreadNode.doSend(message);
        return 42;
    }

    @Benchmark
    public int lazyGet() {
        Blackhole.consumeCPU(tokens);
        lazyThreadNode.doGetPeek();
        return 42;
    }

    @SneakyThrows
    public static void main(String[] args)  {
        Options options = new OptionsBuilder()
                .include(SingleNodeBenchmarks.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

}
