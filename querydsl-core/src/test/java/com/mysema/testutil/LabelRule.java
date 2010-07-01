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
        Label label = target.getClass().getAnnotation(Label.class);
        boolean run = true;
        if (label != null){
            run = isExecuted(method.getMethod(), label.value()); 
        }
        return run ? base : EmptyStatement.DEFAULT;
    }
    
    private boolean isExecuted(Method method, Target target) {
        ExcludeIn ex = method.getAnnotation(ExcludeIn.class);
        // excluded in given targets
        if (ex != null && Arrays.asList(ex.value()).contains(target)){
            return false;
        }
        // included only in given targets
        IncludeIn in = method.getAnnotation(IncludeIn.class);
        if (in != null && !Arrays.asList(in.value()).contains(target)){
            return false;
        }
        return true;
    }

}
