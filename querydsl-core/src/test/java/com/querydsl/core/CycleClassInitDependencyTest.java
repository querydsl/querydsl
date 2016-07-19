package com.querydsl.core;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.reflections.util.ClasspathHelper;

import com.querydsl.core.testutil.ThreadSafety;

public class CycleClassInitDependencyTest {

    private static ClassLoader loader;

    @BeforeClass
    public static void overrideClassLoader() {
        loader = Thread.currentThread().getContextClassLoader();
        Collection<URL> urls = ClasspathHelper.forClassLoader();
        ClassLoader cl = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]), null/*no delegation*/);
        Thread.currentThread().setContextClassLoader(cl);
    }

    @AfterClass
    public static void resetClassLoader() {
        Thread.currentThread().setContextClassLoader(loader);
    }

    @Test(timeout = 2000)
    public void test() {

        // each thread wants to load one part of the dependency circle
        ThreadSafety.check(
                new LoadClassRunnable("com.querydsl.core.types.dsl.NumberExpression"),
                new LoadClassRunnable("com.querydsl.core.types.dsl.Expressions"));
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
