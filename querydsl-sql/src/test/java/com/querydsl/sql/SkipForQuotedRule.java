package com.querydsl.sql;

import com.querydsl.core.testutil.EmptyStatement;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class SkipForQuotedRule implements MethodRule {

    private final Configuration configuration;

    public SkipForQuotedRule(Configuration conf) {
        this.configuration = conf;
    }

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        SQLTemplates templates = configuration.getTemplates();
        if (templates.isUseQuotes() || templates.isPrintSchema() || configuration.getUseLiterals()) {
            boolean skip = method.getMethod().isAnnotationPresent(SkipForQuoted.class);
            return skip ? EmptyStatement.DEFAULT : base;
        } else {
            return base;
        }
    }

}
