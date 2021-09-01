package com.gabriel.week04.exitmain;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockMethod {
    public static void main(String[] args) throws InterruptedException {
        Inner05 inner = new Inner05();
        new Thread(() -> inner.sum(10)).start();
        System.out.println(inner.getResult());
    }
}

class Inner05 {
    volatile int result;
    ReentrantLock lock = new ReentrantLock();

    public void sum(int n) {
        lock.lock();
        try {
            result = (1+n)*n/2;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public int getResult() throws InterruptedException {
        while (true) {
            if (lock.tryLock(30, TimeUnit.MILLISECONDS)) {
                return result;
            }
        }
    }
}
