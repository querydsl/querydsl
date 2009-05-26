/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.internal.runners.TestClass;
import org.junit.internal.runners.TestMethod;
import org.junit.runner.notification.RunNotifier;

/**
 * FilteringTestRunner provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class FilteringTestRunner extends JUnit4ClassRunner {

    private boolean run = true;

    private String label;

    private TestClass fTestClass;

    public FilteringTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
        fTestClass = new TestClass(klass);
        ResourceCheck rc = klass.getAnnotation(ResourceCheck.class);
        if (rc != null) {
            run = klass.getResourceAsStream(rc.value()) != null;
        }
        Label labelAnn = klass.getAnnotation(Label.class);
        if (labelAnn != null) {
            label = labelAnn.value();
        }
    }

    @Override
    public void run(final RunNotifier notifier) {
        if (run)
            super.run(notifier);
    }

    protected TestMethod wrapMethod(Method method) {
        boolean ignore = isIgnored(method);
        if (ignore) {
            return new TestMethod(method, fTestClass) {
                public boolean isIgnored() {
                    return true;
                }
            };
        } else {
            return super.wrapMethod(method);
        }
    }

    private boolean isIgnored(Method method) {
        boolean ignore = false;
        if (label != null) {
            ExcludeIn ex = method.getAnnotation(ExcludeIn.class);
            IncludeIn in = method.getAnnotation(IncludeIn.class);
            ignore |= ex != null && Arrays.asList(ex.value()).contains(label);
            ignore |= in != null && !Arrays.asList(in.value()).contains(label);
        }
        return ignore;
    }
}
