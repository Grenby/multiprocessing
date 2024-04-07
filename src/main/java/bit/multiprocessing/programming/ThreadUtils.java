package bit.multiprocessing.programming;

public class ThreadUtils {

    public static int getNumber(){
        return Integer.parseInt(Thread.currentThread().getName());
    }

}
