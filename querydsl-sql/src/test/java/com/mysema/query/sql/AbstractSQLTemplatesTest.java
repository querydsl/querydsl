package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.path.PSimple;

public abstract class AbstractSQLTemplatesTest {
    
    protected SQLQueryImpl query = new SQLQueryImpl(createTemplates().newLineToSingleSpace());
    
    protected abstract SQLTemplates createTemplates();
    
    @Test
    public void noFrom(){
        query.getMetadata().addProjection(new PSimple<Integer>(Integer.class,"1"));
        assertEquals("select 1 from dual", query.toString());
    }
    

}
