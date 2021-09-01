package com.gabriel.week04.exitmain;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskMethod {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Inner01 inner = new Inner01();
        FutureTask<Integer> futureTask = new FutureTask<>(() -> inner.sum(10));
        Thread t = new Thread(futureTask);
        t.start();
        System.out.println(futureTask.get());
    }
}

class Inner01{
    volatile Integer result;
    public Integer sum(int n) {
        return result = (1+n)*n/2;
    }
}
