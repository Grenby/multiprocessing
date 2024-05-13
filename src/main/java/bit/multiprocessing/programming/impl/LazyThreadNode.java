package bit.multiprocessing.programming.impl;

import bit.multiprocessing.programming.Message;
import bit.multiprocessing.programming.ThreadNode;
import lombok.SneakyThrows;

import java.util.LinkedList;

public class LazyThreadNode extends ThreadNode {

    private final LinkedList<Message> messages = new LinkedList<>();

    public LazyThreadNode(int number) {
        super(number);
    }


    @Override
    public void doSend(Message message) {
        synchronized (messages){
            messages.add(message);
            messages.notifyAll();
        }
    }

    @SneakyThrows
    @Override
    public Message doGet() {
        Message message;
        synchronized (messages){
        while (messages.isEmpty()) {
            messages.wait();
        }
        message = messages.poll();
        }
        return message;
    }

    @SneakyThrows
    public Message doGetPeek() {
        Message message;
        synchronized (messages){
            while (messages.isEmpty()) {
                messages.wait();
            }
            message = messages.peek();
        }
        return message;
    }

}
