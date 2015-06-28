package com.querydsl.example.sql.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

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
