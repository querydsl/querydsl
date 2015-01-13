package com.querydsl.jpa;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.querydsl.core.Target;
import com.querydsl.core.testutil.EmptyStatement;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.testutil.IncludeIn;

/**
 * @author tiwe
 *
 */
public class TargetRule implements MethodRule {

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object obj) {
        //Target target = Connections.getTarget();
        Target target = Mode.target.get();
        boolean run = target == null || isExecuted(method.getMethod(), target);
        return run ? base : EmptyStatement.DEFAULT;
    }
    
    private boolean isExecuted(Method method, Target target) {
        ExcludeIn ex = method.getAnnotation(ExcludeIn.class);
        if (ex == null) {
            ex = method.getDeclaringClass().getAnnotation(ExcludeIn.class);
        }
        // excluded in given targets
        if (ex != null && Arrays.asList(ex.value()).contains(target)) {
            return false;
        }
        // included only in given targets
        IncludeIn in = method.getAnnotation(IncludeIn.class);
        if (in == null) {
            in = method.getDeclaringClass().getAnnotation(IncludeIn.class);
        }
        if (in != null && !Arrays.asList(in.value()).contains(target)) {
            return false;
        }
        return true;
    }

}
