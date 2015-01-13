package com.querydsl.core.support;

import com.querydsl.core.types.*;
import com.querydsl.core.types.path.StringPath;
import org.junit.Test;

import javax.annotation.Nullable;

import static org.junit.Assert.assertEquals;

public class ReplaceVisitorTest {

    private static final ReplaceVisitor visitor = new ReplaceVisitor() {
        public Expression<?> visit(Path<?> expr, @Nullable Void context) {
            if (expr.getMetadata().isRoot()) {
                return new PathImpl(expr.getType(), expr.getMetadata().getName() + "_");
            } else {
                return super.visit(expr, context);
            }
        }
    };

    @Test
    public void Operation() {
        Expression<String> str = new StringPath(new PathImpl(Object.class, "customer"), "name");
        Expression<String> str2 = new StringPath("str");
        Expression<String> concat = Expressions.stringOperation(Ops.CONCAT, str, str2);
        assertEquals("customer.name + str", concat.toString());
        assertEquals("customer_.name + str_", concat.accept(visitor, null).toString());
    }

    @Test
    public void TemplateExpression() {
        Expression<String> str = new StringPath(new PathImpl(Object.class, "customer"), "name");
        Expression<String> str2 = new StringPath("str");
        Expression<String> concat = Expressions.stringTemplate("{0} + {1}", str, str2);
        assertEquals("customer.name + str", concat.toString());
        assertEquals("customer_.name + str_", concat.accept(visitor, null).toString());
    }
}
