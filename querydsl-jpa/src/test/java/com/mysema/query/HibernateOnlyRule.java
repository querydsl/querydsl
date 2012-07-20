package com.mysema.query;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.mysema.testutil.EmptyStatement;

/**
 * @author tiwe
 *
 */
public class HibernateOnlyRule implements MethodRule {

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        if (method.getMethod().getAnnotation(HibernateOnly.class) == null
         && method.getMethod().getDeclaringClass().getAnnotation(HibernateOnly.class) == null) {
            return base;
        } else if (Mode.mode.get().contains("-eclipselink")) {
            return EmptyStatement.DEFAULT;            
        } else {
            return base;
        }
    }

}
