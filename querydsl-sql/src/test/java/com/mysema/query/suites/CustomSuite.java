package com.mysema.query.suites;

import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

public class CustomSuite extends Suite {

    public CustomSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(builder, klass, klass.getClasses());
    }

}
