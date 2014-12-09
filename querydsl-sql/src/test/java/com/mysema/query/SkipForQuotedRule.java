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
            boolean skip = method.getMethod().isAnnotationPresent(SkipForQuoted.class);
            return skip ? EmptyStatement.DEFAULT : base;
        } else {
            return base;
        }
    }

}
