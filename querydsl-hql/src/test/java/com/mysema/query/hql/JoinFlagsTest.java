/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class JoinFlagsTest extends AbstractQueryTest{
    
    @Test
    public void fetchAll(){
        QueryHelper query1 = query().from(cat).fetchAll().where(cat.name.isNotNull());
        assertEquals("from Cat cat fetch all properties\nwhere cat.name is not null", query1.toString());
        
        QueryHelper query2 = query().from(cat).fetchAll().from(cat1).fetchAll(); 
        assertEquals("from Cat cat fetch all properties, Cat cat1 fetch all properties", query2.toString());
    }
    
    @Test
    public void fetch(){
        QueryHelper query = query().from(cat).innerJoin(cat.mate, cat1).fetch();
        assertEquals("from Cat cat\n  inner join fetch cat.mate as cat1", query.toString());
    }
    
}
