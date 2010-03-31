/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import static com.mysema.query.Constants.survey;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.types.path.PEntity;

public abstract class UpdateBaseTest extends AbstractBaseTest{
    
    protected SQLUpdateClause update(PEntity<?> e){
        return new SQLUpdateClause(Connections.getConnection(), dialect, e);
    }
    
    protected SQLDeleteClause delete(PEntity<?> e){
        return new SQLDeleteClause(Connections.getConnection(), dialect, e);
    }
    
    private void reset() throws SQLException{
        delete(survey).where(survey.name.isNotNull()).execute();
        Connections.getStatement().execute("insert into survey values (1, 'Hello World')");   
    }
    
    @Before
    public void setUp() throws SQLException{
        reset();
    }
    
    @After
    public void tearDown() throws SQLException{
        reset();
    }
    
    @Test
    public void test() throws SQLException{        
        
        // original state
        long count = query().from(survey).count();
        assertEquals(0, query().from(survey).where(survey.name.eq("S")).count());
        
        // update call with 0 update count
        assertEquals(0, update(survey).where(survey.name.eq("XXX")).set(survey.name, "S").execute());
        assertEquals(0, query().from(survey).where(survey.name.eq("S")).count());
        
        // update call with full update count
        assertEquals(count, update(survey).set(survey.name, "S").execute());
        assertEquals(count, query().from(survey).where(survey.name.eq("S")).count());
        
        
    }
    

}
