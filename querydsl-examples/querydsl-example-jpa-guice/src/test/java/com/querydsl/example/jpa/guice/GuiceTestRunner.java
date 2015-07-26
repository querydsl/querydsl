package com.querydsl.example.jpa.guice;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.querydsl.example.jpa.guice.ServiceModule;

public class GuiceTestRunner extends BlockJUnit4ClassRunner {

    private static final Injector injector = Guice
            .createInjector(new ServiceModule());

    public GuiceTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Object createTest() throws Exception {
        return injector.getInstance(getTestClass().getJavaClass());
    }
}
