package bit.multiprocessing.programming.impl;

import bit.multiprocessing.programming.Message;
import bit.multiprocessing.programming.ThreadNode;
import lombok.SneakyThrows;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * Используем очередь основанную на Lock
 */
public class BlockedQueueThreadNode extends ThreadNode {

    private final LinkedBlockingDeque<Message> messages = new LinkedBlockingDeque<>();

    public BlockedQueueThreadNode(int number) {
        super(number);
    }

    @Override
    public void doSend(Message message) {
        messages.add(message);
    }

    @SneakyThrows
    @Override
    public Message doGet() {
        Message message = messages.poll();
        while (message == null){
            Thread.onSpinWait();
            message = messages.poll();
        }
        return message;
    }
}
