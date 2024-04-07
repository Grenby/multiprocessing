package bit.multiprocessing.programming.barriers;

import bit.multiprocessing.programming.ThreadUtils;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class NBarrier implements Barrier{

    private final AtomicIntegerArray arr;

    public NBarrier(int num){
        arr = new AtomicIntegerArray(num);

    }
    @Override
    public void barrier() {
        int number = ThreadUtils.getNumber();
        final int l = arr.length()-1;
        if (number==0){
            do0();
        }else if (number==arr.length()-1){
            doN1();
        }else {
            doI(number);
        }
    }

    private void do0(){
        arr.set(0 , 1);
        while (arr.get(1)!=2){
            Thread.onSpinWait();
        }
    }

    private void doN1(){
        while (arr.get(arr.length()-1)==0){
            Thread.onSpinWait();
        }
        arr.set(arr.length()-1,2);
    }

    private void doI(final int i){
        while (arr.get(i-1)==0){
            Thread.onSpinWait();
        }
        arr.set(i,1);
        while (arr.get(i+1)!=2){
            Thread.onSpinWait();
        }
    }

}
