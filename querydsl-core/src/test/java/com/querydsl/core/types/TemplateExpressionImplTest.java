package com.querydsl.core.types;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TemplateExpressionImplTest {

    @Test
    public void Equals() {
        Expression<?> expr1 = ExpressionUtils.template(String.class, "abc", "abc");
        Expression<?> expr2 = ExpressionUtils.template(String.class, "abc", "def");
        assertFalse(expr1.equals(expr2));
    }

}
