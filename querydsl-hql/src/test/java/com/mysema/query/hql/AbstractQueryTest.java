/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import static org.junit.Assert.assertEquals;

import com.mysema.query.types.Expr;

public abstract class AbstractQueryTest implements Constants{
   
    protected QueryHelper query() {
        return new QueryHelper();
    }
    
    protected HQLSubQuery sub(){
        return new HQLSubQuery();
    }
    
    protected static void assertToString(String expected, Expr<?> expr) {
        HQLSerializer serializer = new HQLSerializer(HQLTemplates.DEFAULT);
        assertEquals(expected, serializer.handle(expr).toString().replace("\n", " "));
    }

}
