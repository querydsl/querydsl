package com.querydsl.core.testutil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public final class ThreadSafety {

    private ThreadSafety() { }

    public static void check(Runnable... runnables) {
        Collection<Callable<Object>> callables = new ArrayList<>(runnables.length);
        ExecutorService executor = Executors.newFixedThreadPool(runnables.length);

        for (Runnable runnable : runnables) {
            callables.add(Executors.callable(runnable));
        }
        List<Future<Object>> result;
        try {
            result = executor.invokeAll(callables);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        for (Future<Object> each : result) {
            try {
                // all need to complete successfully
                each.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
