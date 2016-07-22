package com.querydsl.core.testutil;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;

public final class ThreadSafety {

    private ThreadSafety() { }

    public static void check(Runnable... runnables) {
        Collection<Callable<Object>> callables = Lists.newArrayListWithCapacity(runnables.length);
        ExecutorService executor = MoreExecutors.getExitingScheduledExecutorService(
                new ScheduledThreadPoolExecutor(runnables.length));

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
            // all need to complete successfully
            Futures.getUnchecked(each);
        }
    }
}
