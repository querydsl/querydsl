package com.mysema.query.types.query;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.types.PathImpl;

public class ListSubQueryTest {

    @Test
    public void As(){
        ListSubQuery<Date> subQuery = new ListSubQuery<Date>(Date.class,new DefaultQueryMetadata()); 
        assertNotNull(subQuery.as(new PathImpl<Date>(Date.class,"a")));
        assertNotNull(subQuery.as(new PathImpl<Date>(Date.class,"a")));
    }
    
}
