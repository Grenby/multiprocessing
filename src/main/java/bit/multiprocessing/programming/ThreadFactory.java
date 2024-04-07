package bit.multiprocessing.programming;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadFactory {

    public Thread getNumberedThread(int number, Runnable task){
        log.debug("was created Thread with number {}", number);
        return new Thread(task, Integer.toString(number));
    }

}
