package com.querydsl.core.types;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TemplateExpressionImplTest {

    @Test
    public void Equals() {
        Expression<?> expr1 = TemplateExpressionImpl.create(String.class, "abc", "abc");
        Expression<?> expr2 = TemplateExpressionImpl.create(String.class, "abc", "def");
        assertFalse(expr1.equals(expr2));
    }

}
