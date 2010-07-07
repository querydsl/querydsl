package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.sql.AbstractSQLQuery.UnionBuilder;
import com.mysema.query.types.path.PSimple;

public abstract class AbstractSQLTemplatesTest {
    
    protected SQLQueryImpl query = new SQLQueryImpl(createTemplates().newLineToSingleSpace());
    
    protected abstract SQLTemplates createTemplates();
    
    @Test
    public void noFrom(){
        query.getMetadata().addProjection(new PSimple<Integer>(Integer.class,"1"));
        assertEquals("select 1 from dual", query.toString());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void union(){        
        PSimple<Integer> one = new PSimple<Integer>(Integer.class,"1");
        PSimple<Integer> two = new PSimple<Integer>(Integer.class,"2");
        PSimple<Integer> three = new PSimple<Integer>(Integer.class,"3");
        PSimple<Integer> col1 = new PSimple<Integer>(Integer.class,"col1");
        UnionBuilder union = query.union(
            sq().unique(one.as(col1)),
            sq().unique(two),
            sq().unique(three));
        assertEquals(
                "(select 1 as col1 from dual) " +
        	"union " +
        	"(select 2 from dual) " +
        	"union " +
        	"(select 3 from dual)", union.toString());
    }
    
    protected SQLSubQuery sq(){
        return new SQLSubQuery();
    }
    
}
