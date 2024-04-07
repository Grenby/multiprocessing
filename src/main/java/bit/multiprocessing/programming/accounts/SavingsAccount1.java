package bit.multiprocessing.programming.accounts;

import bit.multiprocessing.programming.locks.SimpleLock;
import bit.multiprocessing.programming.locks.TaTaSLock;

public class SavingsAccount1 {

    private final SimpleLock lock = new TaTaSLock();
    private int balance = 0;
    
    public void deposit(int k){
        try {
            lock.lock();
            balance += k;
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(int k) {
        while (true) {
            try {
                lock.lock();
                if (balance > k) {
                    balance -= k;
                    return;
                }
            } finally {
                lock.unlock();
            }
            Thread.onSpinWait();
        }
    }

}
