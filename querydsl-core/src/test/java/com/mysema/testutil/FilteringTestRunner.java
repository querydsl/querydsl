/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.testutil;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * FilteringTestRunner provides
 *
 * @author tiwe
 * @version $Id$
 */
public class FilteringTestRunner extends BlockJUnit4ClassRunner {

    private boolean run = true;

    public FilteringTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
        ResourceCheck rc = klass.getAnnotation(ResourceCheck.class);
        if (rc != null) {
            run = klass.getResourceAsStream(rc.value()) != null;
        }
    }

    @Override
    public void run(final RunNotifier notifier) {
        if (run){
            super.run(notifier);
        }
    }

}
