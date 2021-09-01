package com.gabriel.week04.exitmain;

public class SynchronizedMethod {
    public static void main(String[] args) throws InterruptedException {
        Inner04 inner = new Inner04();
        Thread t = new Thread(() -> inner.sum(10));
        t.start();
        System.out.println(inner.getResult());
    }
}

class Inner04 {
    int result;

    public synchronized void sum(int n) {
        result = (1+n)*n/2;
        notifyAll();
    }

    public synchronized int getResult() throws InterruptedException {
        if (result == 0) {
            wait();
        }
        return result;
    }
}
