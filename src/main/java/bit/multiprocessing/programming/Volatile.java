package bit.multiprocessing.programming;

import lombok.SneakyThrows;

public class Volatile {

    private int progressPing = 0;
    private int progressPong = 0;

    /**
     * Дедлоков нет, так как у переменной только два значения, при каждом из которых один из
     * потоков может продолжить выполнение
     */
    private volatile boolean shouldPing = false;

    public static void main(String[] args) {
        new Volatile().main();
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
            while (!shouldPing) {
                Thread.onSpinWait();
            }
            System.out.println("pong " + (progressPong++));
            shouldPing = false;
            Thread.sleep(1000);
        }
    }

    @SneakyThrows
    public void ping() {
        while (true) {
            while (shouldPing) {
                Thread.onSpinWait();
            }
            System.out.println("ping " + (progressPing++));
            shouldPing = true;
            Thread.sleep(1000);
        }
    }

}
