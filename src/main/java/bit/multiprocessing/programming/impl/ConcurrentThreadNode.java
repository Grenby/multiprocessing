package bit.multiprocessing.programming.impl;

import bit.multiprocessing.programming.Message;
import bit.multiprocessing.programming.ThreadNode;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentThreadNode extends ThreadNode {

    private final ConcurrentLinkedQueue<Message> queue = new ConcurrentLinkedQueue<>();

    public ConcurrentThreadNode(int number) {
        super(number);
    }

    @Override
    protected void doSend(Message message) {
        queue.add(message);
    }

    @Override
    protected Message doGet() {
        Message message = queue.poll();
        while (message == null){
            Thread.onSpinWait();
            message = queue.poll();
        }
        return message;
    }
}
