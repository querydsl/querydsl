package com.mysema.testutil;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class ResourceCheckRule implements MethodRule{

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        Class<?> testClass = method.getMethod().getDeclaringClass();
        ResourceCheck rc = testClass.getAnnotation(ResourceCheck.class);
        boolean run = true;
        if (rc != null) {
            run = testClass.getResourceAsStream(rc.value()) != null;
        }
        return run ? base : EmptyStatement.DEFAULT;
    }

}
