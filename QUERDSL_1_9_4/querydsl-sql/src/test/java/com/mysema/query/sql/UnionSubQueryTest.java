/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.Expr;
import com.mysema.query.types.path.PSimple;

public class UnionSubQueryTest {
    
    private SQLTemplates templates = new H2Templates().newLineToSingleSpace();
    
    private SQLSerializer serializer = new SQLSerializer(templates);    
    
    @Test
    public void unionSubQuery(){
        PSimple<Integer> one = new PSimple<Integer>(Integer.class,"1");
        PSimple<Integer> two = new PSimple<Integer>(Integer.class,"2");
        PSimple<Integer> three = new PSimple<Integer>(Integer.class,"3");
        PSimple<Integer> col1 = new PSimple<Integer>(Integer.class,"col1");
        Expr<?> union = sq().union(
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
