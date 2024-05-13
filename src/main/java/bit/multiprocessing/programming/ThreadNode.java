package bit.multiprocessing.programming;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ThreadNode extends Thread{

    public static volatile boolean isActive = true;

    @Setter
    private ThreadNode next;
    @Getter
    private volatile long numMessages = 0L;
    @Getter
    private volatile long delta = 0L;



    public ThreadNode(int number){
        this.setName(Integer.toString(number));
    }

    public void sendMessage(Message message){
        doSend(message);
    }

    @Override
    public void run(){
        long numMessages = 0;
        long start = System.nanoTime();
            while (isActive) {
                Message message = doGet();
                numMessages++;
                //do some work
//                log.info("поток {} получил сообщение {}", getName(), message.number());
//               Thread.sleep(1000);
                next.sendMessage(message);
            }
            long end = System.nanoTime();
            this.delta = end - start;
            this.numMessages = numMessages;
    }

    public abstract void doSend(Message message);

    public abstract Message doGet();

}
