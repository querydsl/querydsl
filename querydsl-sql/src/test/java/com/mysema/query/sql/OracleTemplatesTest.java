/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.sql.AbstractSQLQuery.UnionBuilder;
import com.mysema.query.types.Expression;
import com.mysema.query.types.custom.SimpleTemplate;
import com.mysema.query.types.path.NumberPath;



public class OracleTemplatesTest extends AbstractSQLTemplatesTest{

    @Override
    protected SQLTemplates createTemplates() {
        return new OracleTemplates();
    }    
    
    @SuppressWarnings("unchecked")
    @Test
    public void union(){        
        Expression<Integer> one = SimpleTemplate.create(Integer.class,"1");
        Expression<Integer> two = SimpleTemplate.create(Integer.class,"2");
        Expression<Integer> three = SimpleTemplate.create(Integer.class,"3");
        NumberPath<Integer> col1 = new NumberPath<Integer>(Integer.class,"col1");
        UnionBuilder union = query.union(
            sq().unique(one.as(col1)),
            sq().unique(two),
            sq().unique(three));
        assertEquals(
                "(select 1 col1 from dual) " +
                "union " +
                "(select 2 from dual) " +
                "union " +
                "(select 3 from dual)", union.toString());
    }


}
