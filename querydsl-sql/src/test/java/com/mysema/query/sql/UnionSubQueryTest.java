/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.Expression;
import com.mysema.query.types.path.SimplePath;

public class UnionSubQueryTest {
    
    private SQLTemplates templates = new H2Templates(){{
        newLineToSingleSpace();
    }};
    
    private SQLSerializer serializer = new SQLSerializer(templates);    
    
    @Test
    public void UnionSubQuery(){
        SimplePath<Integer> one = new SimplePath<Integer>(Integer.class,"1");
        SimplePath<Integer> two = new SimplePath<Integer>(Integer.class,"2");
        SimplePath<Integer> three = new SimplePath<Integer>(Integer.class,"3");
        SimplePath<Integer> col1 = new SimplePath<Integer>(Integer.class,"col1");
        Expression<?> union = sq().union(
            sq().unique(one.as(col1)),
            sq().unique(two),
            sq().unique(three));
        
        serializer.handle(union);
        assertEquals(
                "(select 1 as col1 from dual)\n" +
                "union\n" +
                "(select 2 from dual)\n" +
                "union\n" +
                "(select 3 from dual)", serializer.toString());
    }
    
    protected SQLSubQuery sq(){
        return new SQLSubQuery();
    }

}
