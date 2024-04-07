package bit.multiprocessing.programming.accounts;

import bit.multiprocessing.programming.locks.SimpleLock;
import bit.multiprocessing.programming.locks.TaTaSLock;

public class SavingsAccount2 {

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

}
