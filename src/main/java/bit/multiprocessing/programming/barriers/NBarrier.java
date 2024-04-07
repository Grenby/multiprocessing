package bit.multiprocessing.programming.barriers;

import bit.multiprocessing.programming.ThreadUtils;

public class NBarrier implements Barrier{

    private final int[] arr;

    public NBarrier(int num){
        arr = new int[num];
    }

    @Override
    public void barrier() {
        int number = ThreadUtils.getNumber();
        if (number == 0){
            arr[number] = 1;
            while (arr[number+1]!=2){
                Thread.onSpinWait();
            }
        }else if(number==arr.length-1) {
            while (arr[number-1]==0){
                Thread.onSpinWait();
            }
            arr[number]=2;
        }else {
            while (arr[number-1]==0){
                Thread.onSpinWait();
            }
            arr[number]=1;
            while (arr[number+1]!=2){
                Thread.onSpinWait();
            }
        }
    }
}
