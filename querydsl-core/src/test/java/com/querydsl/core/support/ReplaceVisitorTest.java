package com.querydsl.core.support;

import static org.junit.Assert.assertEquals;

import org.jetbrains.annotations.Nullable;

import org.junit.Test;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;

public class ReplaceVisitorTest {

    private static final ReplaceVisitor<Void> visitor = new ReplaceVisitor<Void>() {
        public Expression<?> visit(Path<?> expr, @Nullable Void context) {
            if (expr.getMetadata().isRoot()) {
                return ExpressionUtils.path(expr.getType(), expr.getMetadata().getName() + "_");
            } else {
                return super.visit(expr, context);
            }
        }
    };

    @Test
    public void operation() {
        Expression<String> str = Expressions.stringPath(ExpressionUtils.path(Object.class, "customer"), "name");
        Expression<String> str2 = Expressions.stringPath("str");
        Expression<String> concat = Expressions.stringOperation(Ops.CONCAT, str, str2);
        assertEquals("customer.name + str", concat.toString());
        assertEquals("customer_.name + str_", concat.accept(visitor, null).toString());
    }

    @Test
    public void templateExpression() {
        Expression<String> str = Expressions.stringPath(ExpressionUtils.path(Object.class, "customer"), "name");
        Expression<String> str2 = Expressions.stringPath("str");
        Expression<String> concat = Expressions.stringTemplate("{0} + {1}", str, str2);
        assertEquals("customer.name + str", concat.toString());
        assertEquals("customer_.name + str_", concat.accept(visitor, null).toString());
    }
}
