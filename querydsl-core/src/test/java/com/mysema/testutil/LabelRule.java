package com.mysema.testutil;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.mysema.query.Target;

public class LabelRule implements MethodRule{

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        Label label = method.getMethod().getDeclaringClass().getAnnotation(Label.class);
        boolean run = label != null && isIgnored(method.getMethod(), label.value());
        return run ? base : EmptyStatement.DEFAULT;
    }
    
    private boolean isIgnored(Method method, Target label) {
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
