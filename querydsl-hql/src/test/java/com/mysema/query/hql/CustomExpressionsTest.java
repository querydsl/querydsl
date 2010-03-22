/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.Arrays;

import org.junit.Test;

import com.mysema.query.types.Expr;
import com.mysema.query.types.TemplateFactory;
import com.mysema.query.types.custom.CString;

public class CustomExpressionsTest extends AbstractQueryTest{
    
    public static class MyCustomExpr extends CString {

        private static final long serialVersionUID = 1L;

        public MyCustomExpr(Expr<?>... args) {
            super(new TemplateFactory().create("myCustom({0},{1})"), Arrays.asList(args));
        }
    }
    
    @Test
    public void testCustomExpressions() {
        assertToString("myCustom(cust,cat)", new MyCustomExpr(cust, cat));
    }

}
