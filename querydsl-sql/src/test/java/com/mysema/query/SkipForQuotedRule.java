package com.mysema.query;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.mysema.query.sql.SQLTemplates;
import com.mysema.testutil.EmptyStatement;

public class SkipForQuotedRule implements MethodRule {

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        SQLTemplates templates = Connections.getTemplates();
        if (templates.isUseQuotes() || templates.isPrintSchema()) {
            boolean run = method.getMethod().getAnnotation(SkipForQuoted.class) == null;
            return run ? base :  EmptyStatement.DEFAULT;
        } else {
            return base;
        }
    }

}
