package com.mysema.query;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.notification.RunNotifier;

/**
 * ResourceCheckTestRunner provides
 *
 * @author tiwe
 * @version $Id$
 */
public class ResourceCheckTestRunner extends JUnit4ClassRunner{

    private boolean run = false;
    
    public ResourceCheckTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
        ResourceCheck rc = klass.getAnnotation(ResourceCheck.class);
        run = klass.getResourceAsStream(rc.value()) != null;
    }

    @Override
    public void run(final RunNotifier notifier) {
        if (run) super.run(notifier);
    }
}
