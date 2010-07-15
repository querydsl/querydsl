/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.sql.AbstractSQLQuery.UnionBuilder;
import com.mysema.query.types.path.PSimple;


public class SQLServerTemplatesTest extends AbstractSQLTemplatesTest{
    
    @Override
    @Test
    public void noFrom(){
        query.getMetadata().addProjection(new PSimple<Integer>(Integer.class,"1"));
        assertEquals("select 1", query.toString());
    }

    @Override
    protected SQLTemplates createTemplates() {
        return new SQLServerTemplates();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void union(){        
        PSimple<Integer> one = new PSimple<Integer>(Integer.class,"1");
        PSimple<Integer> two = new PSimple<Integer>(Integer.class,"2");
        PSimple<Integer> three = new PSimple<Integer>(Integer.class,"3");
        PSimple<Integer> col1 = new PSimple<Integer>(Integer.class,"col1");
        UnionBuilder union = query.union(
            sq().unique(one.as(col1)),
            sq().unique(two),
            sq().unique(three));
        assertEquals(
                "(select 1 as col1) " +
                "union " +
                "(select 2) " +
                "union " +
                "(select 3)", union.toString());
    }
    
}
