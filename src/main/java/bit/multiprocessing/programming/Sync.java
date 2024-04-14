package bit.multiprocessing.programming;

import lombok.SneakyThrows;

public class Sync {

    private final Object sync = new Object();
    private int progressPing = 0;
    private int progressPong = 0;
    private String last = "ping";

    public static void main(String[] args) {
        new Sync().main();
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
                synchronized (sync) {
                    while (!last.equals("ping")) {
                        sync.wait();
                    }
                    System.out.println("pong " + (progressPong++));
                    last = "pong";
                    sync.notifyAll();
                }
            } catch (InterruptedException e) {
                return;
            } catch (Exception ignored) {

            }
            Thread.sleep(1000);
        }
    }

    @SneakyThrows
    public void ping() {
        while (true) {
            try {
                synchronized (sync) {
                    while (!last.equals("pong")) {
                        sync.wait();
                    }
                    System.out.println("ping " + (progressPing++));
                    last = "ping";
                    sync.notifyAll();
                }
            } catch (InterruptedException e) {
                return;
            } catch (Exception ignored) {

            }
            Thread.sleep(1000);
        }
    }

}
