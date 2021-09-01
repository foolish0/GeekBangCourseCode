package com.gabriel.week04.exitmain;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchMethod {
    public static void main(String[] args) throws InterruptedException {
        Inner06 inner = new Inner06();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                inner.sum(10);
                countDownLatch.countDown();
            }
        });
        t.start();
        countDownLatch.await();

        System.out.println(inner.getResult());
    }

}

class Inner06 {
    int result;
    public void sum(int n) {
        result = (1+n)*n/2;
    }

    public  int getResult() {
        return result;
    }
}