package com.mysema.query;

import java.net.URL;
import java.net.URLClassLoader;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CycleClassInitDependencyTest {

    private static ClassLoader loader;

    @BeforeClass
    public static void overrideClassLoader() {
        loader = Thread.currentThread().getContextClassLoader();
        ClassLoader cl = null;
        if (loader instanceof URLClassLoader) {
            URL[] urls = ((URLClassLoader) loader).getURLs();
            cl = URLClassLoader.newInstance(urls, null/*no delegation*/);
        } else {
            throw new IllegalStateException("Unknown classloader type");
        }
        Thread.currentThread().setContextClassLoader(cl);
    }

    @AfterClass
    public static void resetClassLoader() {
        Thread.currentThread().setContextClassLoader(loader);
    }

    @Test(timeout = 2000)
    public void test() throws InterruptedException {

        // each thread wants to load one part of the dependency circle
        Thread t1 = new Thread(new LoadClassRunnable("com.mysema.query.types.OperatorImpl"));
        Thread t2 = new Thread(new LoadClassRunnable("com.mysema.query.types.Ops"));
        t1.start();
        t2.start();

        t1.join();

    }

    private static class LoadClassRunnable implements Runnable {

        private final String classToLoad;

        public LoadClassRunnable(String classToLoad) {
            this.classToLoad = classToLoad;
        }

        @Override
        public void run() {
            try {
                Class.forName(classToLoad, true, Thread.currentThread().getContextClassLoader());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
