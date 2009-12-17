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
    public void exists(){
        toString("exists (select cat from Cat cat)",                        sub().from(cat).exists());
        toString("exists (select cat from Cat cat where cat.weight < :a1)", sub().from(cat).where(cat.weight.lt(1)).exists());
        toString("exists (select cat from Cat cat where cat.weight < :a1)", sub().from(cat).where(cat.weight.lt(1)).unique(cat).exists());
    }
    
}
