package com.gabriel.week04.exitmain;

public class JoinMethod {
    public static void main(String[] args) throws InterruptedException {
        Inner03 inner = new Inner03();
        Thread t = new Thread(() -> inner.sum(10));
        t.start();
        t.join();
        System.out.println(inner.result);
    }
}

class Inner03{
    int result;

    public Integer sum(int n) {
        return result = (1+n)*n/2;
    }
}
