package bit.multiprocessing.programming;

import bit.multiprocessing.programming.locks.JavaSimpleLock;
import bit.multiprocessing.programming.locks.SimpleLock;
import bit.multiprocessing.programming.locks.SimpleReadWriteLock;

import java.util.concurrent.locks.ReadWriteLock;

public class SavingsAccount1 {

    private final SimpleReadWriteLock lock =  new SimpleReadWriteLock();
    private int balance = 0;
    
    public void deposit(int k){
        lock.getWriteLock().lock();
        balance+=k;
        lock.unlock();
    }

    public void withdraw(int k){
        lock.lock();
        (balance)
        
    }

}
