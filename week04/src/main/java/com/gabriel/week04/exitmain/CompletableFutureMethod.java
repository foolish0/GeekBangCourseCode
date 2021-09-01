package com.gabriel.week04.exitmain;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureMethod {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Inner02 inner = new Inner02();
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> inner.sum(10));
        System.out.println(future.get());
    }
}

class Inner02{
    int result;

    public Integer sum(int n) {
        return result = (1+n)*n/2;
    }
}
