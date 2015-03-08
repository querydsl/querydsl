package com.mysema.query;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.reflections.util.ClasspathHelper;

import com.mysema.query.types.Ops;

public class CycleClassInitDependencyTest {

    private static ClassLoader loader;

    @BeforeClass
    public static void overrideClassLoader() {
        loader = Thread.currentThread().getContextClassLoader();
        Collection<URL> urls = ClasspathHelper.forClassLoader();
        ClassLoader cl = new URLClassLoader(urls.toArray(new URL[0]));
        Thread.currentThread().setContextClassLoader(cl);
    }

    @AfterClass
    public static void resetClassLoader() {
        Thread.currentThread().setContextClassLoader(loader);
    }

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
