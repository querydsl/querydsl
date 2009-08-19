/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import static org.junit.Assert.assertEquals;

import com.mysema.query.types.expr.Expr;

public abstract class AbstractQueryTest implements Constants{
    
    private HQLSerializer visitor = new HQLSerializer(new HQLTemplates());
    
    protected void toString(String expected, Expr<?> expr) {
        assertEquals(expected, visitor.handle(expr).toString());
        // visitor.clear();
        visitor = new HQLSerializer(new HQLTemplates());
    }

}
