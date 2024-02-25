package bit.multiprocessing.programming;

public class TwoThreadUtils {

    public static int getNumber(){
        return Integer.parseInt(Thread.currentThread().getName());
    }

    public static int other(){
        return 1 - Integer.parseInt(Thread.currentThread().getName());
    }

}
