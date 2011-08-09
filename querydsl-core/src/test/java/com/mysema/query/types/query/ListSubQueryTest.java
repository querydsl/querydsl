package com.mysema.query.types.query;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinType;
import com.mysema.query.types.PathImpl;

public class ListSubQueryTest {

    @Test
    public void As(){
        ListSubQuery<Date> subQuery = new ListSubQuery<Date>(Date.class,new DefaultQueryMetadata()); 
        assertNotNull(subQuery.as(new PathImpl<Date>(Date.class,"a")));
        assertNotNull(subQuery.as(new PathImpl<Date>(Date.class,"a")));
    }
 
    @Test
    public void Count(){
        DefaultQueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, new PathImpl<Object>(Object.class, "path"));
        ListSubQuery<Date> subQuery = new ListSubQuery<Date>(Date.class, md);
        assertNotNull(subQuery.count().toString());
    }
    
    @Test
    public void Count_Distinct(){
        DefaultQueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, new PathImpl<Object>(Object.class, "path"));
        ListSubQuery<Date> subQuery = new ListSubQuery<Date>(Date.class, md);
        assertNotNull(subQuery.count().toString());
    }
    
}
