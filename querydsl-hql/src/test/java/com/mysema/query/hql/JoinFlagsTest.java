package com.mysema.query.hql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class JoinFlagsTest implements Constants{
    
    protected QueryHelper query() {
        return new QueryHelper();
    }

    @Test
    public void fetchAll(){
        QueryHelper query1 = query().from(cat).fetchAll().where(cat.name.isNotNull());
        assertEquals("from Cat cat fetch all properties\n" +
                "where cat.name is not null", query1.toString());
        
        QueryHelper query2 = query().from(cat).fetchAll().from(cat1).fetchAll(); 
        assertEquals("from Cat cat fetch all properties, Cat cat1 fetch all properties", query2.toString());
    }
    
    @Test
    public void fetch(){
        // TODO
    }
    
}
