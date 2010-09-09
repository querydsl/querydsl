/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.sql.AbstractSQLQuery.UnionBuilder;
import com.mysema.query.types.Expr;
import com.mysema.query.types.custom.CSimple;
import com.mysema.query.types.path.PNumber;



public class OracleTemplatesTest extends AbstractSQLTemplatesTest{

    @Override
    protected SQLTemplates createTemplates() {
        return new OracleTemplates();
    }    
    
    @SuppressWarnings("unchecked")
    @Test
    public void union(){        
        Expr<Integer> one = CSimple.create(Integer.class,"1");
        Expr<Integer> two = CSimple.create(Integer.class,"2");
        Expr<Integer> three = CSimple.create(Integer.class,"3");
        PNumber<Integer> col1 = new PNumber<Integer>(Integer.class,"col1");
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
