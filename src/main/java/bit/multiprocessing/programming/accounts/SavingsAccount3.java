package bit.multiprocessing.programming.accounts;

import bit.multiprocessing.programming.locks.SimpleLock;
import bit.multiprocessing.programming.locks.TaTaSLock;

public class SavingsAccount3 {

    private final SimpleLock lock = new TaTaSLock();
    private int balance = 0;
    private int preferenceCount = 0;

    public void deposit(int k){
        try {
            lock.lock();
            balance += k;
        } finally {
            lock.unlock();
        }
    }

    public void withdraw (int k, boolean preference) {
        boolean added = false;
        while (true) {
            try {
                lock.lock();

                if (preference && !added){
                    this.preferenceCount +=1;
                    added = true;
                }

                if (balance > k && (preference || this.preferenceCount ==0)) {
                    balance -= k;
                    return;
                }

            } finally {
                this.preferenceCount--;
                lock.unlock();
            }
            Thread.onSpinWait();
        }
    }

    /**
     * Конкретно в моей реализации такой код попадет в дедлок ри попытке вызвать deposit в локе
     *
     * Если предположить, что внутри класса методы вызываются без лока (как proxy методы в Spring)
     *
     * Поток может заблокироваться только если на аккаунте reserve меньше 100 денег
     * Предположим что у reserve было меньше 100 денег, то данный поток будет ждать в локе, следовательно,
     * если BOSS thread сначала захочет положить деньги в этот аккаунт, а потом на reserve,
     * то он не сможет, так как лок не снят, и случиться дедлок
     *
     */
    public void transfer(int k, SavingsAccount3 reserve) {
        lock.lock();
        try {
            reserve.withdraw(k, false);
            deposit(k);
        } finally {
            lock.unlock();
        }
    }

}
