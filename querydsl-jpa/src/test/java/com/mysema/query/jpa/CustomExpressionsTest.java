/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import java.util.Arrays;

import org.junit.Test;

import com.mysema.query.types.Expression;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.template.StringTemplate;

public class CustomExpressionsTest extends AbstractQueryTest{

    public static class MyCustomExpr extends StringTemplate {

        private static final long serialVersionUID = 1L;

        public MyCustomExpr(Expression<?>... args) {
            super(new TemplateFactory().create("myCustom({0},{1})"), Arrays.asList(args));
        }
    }

    @Test
    public void testCustomExpressions() {
        assertToString("myCustom(cust,cat)", new MyCustomExpr(cust, cat));
    }

}
