package com.gabriel.week04.exitmain;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicTest {

    public static void main(String[] args) {
        AtomicInteger result = new AtomicInteger(0);
        ExecutorService service = Executors.newFixedThreadPool(10);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 30, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());

        for (int i = 0; i < 10000; i++) {
            threadPoolExecutor.execute(() -> {
                result.set(result.addAndGet(1));
            });
        }

        threadPoolExecutor.shutdown();

        System.out.println(result);
    }
}
