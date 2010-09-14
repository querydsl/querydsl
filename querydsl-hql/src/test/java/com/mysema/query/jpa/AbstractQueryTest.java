/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import static org.junit.Assert.assertEquals;

import com.mysema.query.jpa.JPQLSerializer;
import com.mysema.query.jpa.JPQLSubQuery;
import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.types.Expression;

public abstract class AbstractQueryTest implements Constants{

    protected QueryHelper query() {
        return new QueryHelper();
    }

    protected JPQLSubQuery sub(){
        return new JPQLSubQuery();
    }

    protected static void assertToString(String expected, Expression<?> expr) {
        JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
        assertEquals(expected, serializer.handle(expr).toString().replace("\n", " "));
    }

}
