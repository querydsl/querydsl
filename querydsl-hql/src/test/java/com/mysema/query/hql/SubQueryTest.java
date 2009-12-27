/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SubQueryTest extends AbstractQueryTest{
    
    protected HQLSubQuery sub(){
        return new HQLSubQuery();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void wrongUsage(){
        sub().exists();
    }
    
    @Test
    public void serialization(){
        HQLSubQuery query = sub();
        
        query.from(cat);
        assertEquals("from Cat cat", query.toString());
        
        query.from(fatcat);
        assertEquals("from Cat cat, Cat fatcat", query.toString());        
    }
    
    @Test
    public void joins(){
        assertEquals("from Cat cat\n  inner join cat.mate", sub().from(cat).innerJoin(cat.mate).toString());
        assertEquals("from Cat cat\n  left join cat.mate", sub().from(cat).leftJoin(cat.mate).toString());
        assertEquals("from Cat cat\n  full join cat.mate", sub().from(cat).fullJoin(cat.mate).toString());
        assertEquals("from Cat cat\n  join cat.mate", sub().from(cat).join(cat.mate).toString());
    }
    
    @Test
    public void uniqueProjection(){
        assertToString("(select cat from Cat cat)", sub().from(cat).unique(cat));
    }
    
    @Test
    public void listProjection(){
        assertToString("(select cat from Cat cat)", sub().from(cat).list(cat));
    }
    
    @Test
    public void exists(){        
        assertToString("exists (select 1 from Cat cat)",                        sub().from(cat).exists());
        assertToString("exists (select 1 from Cat cat where cat.weight < :a1)", sub().from(cat).where(cat.weight.lt(1)).exists());
        assertToString("exists (select 1 from Cat cat where cat.weight < :a1)", sub().from(cat).where(cat.weight.lt(1)).unique(cat).exists());
    }
    
}
