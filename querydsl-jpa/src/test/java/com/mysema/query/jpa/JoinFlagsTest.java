/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JoinFlagsTest extends AbstractQueryTest{

    @Test
    public void FetchAll(){
        QueryHelper query1 = query().from(cat).fetchAll().where(cat.name.isNotNull());
        assertEquals("from Cat cat fetch all properties\nwhere cat.name is not null", query1.toString());
    }
    
    @Test
    public void FetchAll2(){
        QueryHelper query2 = query().from(cat).fetchAll().from(cat1).fetchAll();
        assertEquals("from Cat cat fetch all properties, Cat cat1 fetch all properties", query2.toString());
    }

    @Test
    public void Fetch(){
        QueryHelper query = query().from(cat).innerJoin(cat.mate, cat1).fetch();
        assertEquals("from Cat cat\n  inner join fetch cat.mate as cat1", query.toString());
    }

}
