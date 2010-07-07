package com.mysema.query.sql;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.types.path.PSimple;


public class SQLServerTemplatesTest extends AbstractSQLTemplatesTest{
    
    @Override
    @Test
    public void noFrom(){
        query.getMetadata().addProjection(new PSimple<Integer>(Integer.class,"1"));
        assertEquals("select 1", query.toString());
    }

    @Override
    protected SQLTemplates createTemplates() {
        return new SQLServerTemplates();
    }

}
