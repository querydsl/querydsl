package com.querydsl.sql;

import java.util.Arrays;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import com.querydsl.core.Target;
import com.querydsl.core.testutil.EmptyStatement;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.testutil.IncludeIn;

public class TargetRule implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        Target target = Connections.getTarget();
        boolean run = target == null || isExecuted(description, target);
        return run ? base : EmptyStatement.DEFAULT;
    }

    private boolean isExecuted(Description description, Target target) {
        ExcludeIn ex = description.getAnnotation(ExcludeIn.class);
        // excluded in given targets
        if (ex != null && Arrays.asList(ex.value()).contains(target)) {
            return false;
        }
        // included only in given targets
        IncludeIn in = description.getAnnotation(IncludeIn.class);
        if (in != null && !Arrays.asList(in.value()).contains(target)) {
            return false;
        }
        return true;
    }

}
