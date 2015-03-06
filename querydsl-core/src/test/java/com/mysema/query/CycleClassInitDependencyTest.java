package com.mysema.query;

import org.junit.Test;

import com.mysema.query.types.Ops;

public class CycleClassInitDependencyTest {

    @Test(timeout = 2000)
    public void test() throws InterruptedException {

        // If class is loaded before hand it will work for example:
        // System.out.println(Ops.DateTimeOps.DATE);

        // each thread wants to load one part of the dependency circle
        Thread t1 = new Thread(new LoadClassRunnable("com.mysema.query.types.OperatorImpl"));
        Thread t2 = new Thread(new LoadClassRunnable("com.mysema.query.types.Ops"));
        t1.start();
        t2.start();

        // trying to do anything depending on Ops class for instance:
        Ops.ADD.getId();

    }

    private static class LoadClassRunnable implements Runnable {

        private final String classToLoad;

        public LoadClassRunnable(String classToLoad) {
            this.classToLoad = classToLoad;
        }

        @Override
        public void run() {
            try {
                Class.forName(classToLoad);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
