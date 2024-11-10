package bit.multiprocessing.programming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

@Slf4j
public class QuicksortMultiThread{
    // say no oop
    private static final QuicksortSeq QUICKSORT = new QuicksortSeq();
    private final ForkJoinPool commonPool;

    public QuicksortMultiThread(int poolSize){
        this.commonPool = new ForkJoinPool(poolSize);
    }

    public void quickSort(int[] arr) {
        commonPool.invoke(new Action(0, arr.length-1, arr));
    }


    @RequiredArgsConstructor
    private static class Action extends RecursiveAction{

        private final int from;
        private final int to;
        private final int[] arr;
        @Override
        protected void compute() {
            if (to - from < 1000){
                QUICKSORT.quickSort(arr, from, to);
                return;
            }
            val i = QUICKSORT.partition(arr, from, to);
            val t1 = new Action(from, i - 1, arr).fork();
            val t2 = new Action(i + 1,to , arr).fork();
            t1.join();
            t2.join();
        }
    }

}
