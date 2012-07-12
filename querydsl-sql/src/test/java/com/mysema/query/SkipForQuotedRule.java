package com.mysema.query;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.mysema.testutil.EmptyStatement;

public class SkipForQuotedRule implements MethodRule {

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        if (Connections.getTemplates().isUseQuotes()) {
            boolean run = method.getMethod().getAnnotation(SkipForQuoted.class) == null;
            return run ? base :  EmptyStatement.DEFAULT;
        } else {
            return base;
        }
    }

}
