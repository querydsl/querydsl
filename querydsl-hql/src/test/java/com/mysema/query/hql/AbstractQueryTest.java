/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import static org.junit.Assert.assertEquals;

import com.mysema.query.types.expr.Expr;

public abstract class AbstractQueryTest implements Constants{
    
    protected static void assertToString(String expected, Expr<?> expr) {
        HQLSerializer serializer = new HQLSerializer(new HQLTemplates());
        assertEquals(expected, serializer.handle(expr).toString().replace("\n", " "));
    }

}
