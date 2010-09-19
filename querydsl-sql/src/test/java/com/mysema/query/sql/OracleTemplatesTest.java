/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.sql.AbstractSQLQuery.UnionBuilder;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.template.SimpleTemplate;



public class OracleTemplatesTest extends AbstractSQLTemplatesTest{

    @Override
    protected SQLTemplates createTemplates() {
        return new OracleTemplates();
    }    
    
    @SuppressWarnings("unchecked")
    @Test
    public void union(){        
        SimpleExpression<Integer> one = SimpleTemplate.create(Integer.class,"1");
        SimpleExpression<Integer> two = SimpleTemplate.create(Integer.class,"2");
        SimpleExpression<Integer> three = SimpleTemplate.create(Integer.class,"3");
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
