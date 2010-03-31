/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import static com.mysema.query.Constants.survey;
import static com.mysema.query.Target.MYSQL;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.types.path.PEntity;
import com.mysema.testutil.ExcludeIn;

public abstract class DeleteBaseTest extends AbstractBaseTest{
    
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
    @ExcludeIn(MYSQL)
    public void test() throws SQLException{
        long count = query().from(survey).count();
        assertEquals(0, delete(survey).where(survey.name.eq("XXX")).execute());
        assertEquals(count, delete(survey).execute());    
    }

}
