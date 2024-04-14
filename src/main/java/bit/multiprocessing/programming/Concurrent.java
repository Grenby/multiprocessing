package bit.multiprocessing.programming;

import lombok.SneakyThrows;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Concurrent {

    private final Lock lock = new ReentrantLock();
    private int progressPing = 0;
    private int progressPong = 0;
    private String last = "ping";

    public static void main(String[] args) {
        new Concurrent().main();
    }

    @SneakyThrows
    public void main() {
        Thread ping = new Thread(this::ping);
        Thread pong = new Thread(this::pong);

        ping.start();
        pong.start();
        pong.join();
        ping.join();
    }

    @SneakyThrows
    public void pong() {
        while (true) {
            try {
                while (!last.equals("ping")) {
                    Thread.onSpinWait();
                }
                lock.lock();
                System.out.println("pong " + (progressPong++));
                last = "pong";
            } catch (Exception ignored) {
            } finally {
                lock.unlock();
            }

            Thread.sleep(1000);
        }
    }

    @SneakyThrows
    public void ping() {
        while (true) {
            try {
                while (!last.equals("pong")) {
                    Thread.onSpinWait();
                }
                lock.lock();
                System.out.println("ping " + (progressPing++));
                last = "ping";

            } catch (Exception ignored) {
            } finally {
                lock.unlock();
            }

            Thread.sleep(1000);

        }
    }

}
