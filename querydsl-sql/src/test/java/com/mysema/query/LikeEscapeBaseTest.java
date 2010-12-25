package com.mysema.query;

import static com.mysema.query.Constants.survey;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.dml.SQLInsertClause;

public abstract class LikeEscapeBaseTest extends AbstractBaseTest{
    
    private void reset() throws SQLException{
        delete(survey).execute();
        SQLInsertClause insert = insert(survey);
        insert.set(survey.id, 5).set(survey.name, "aaa").addBatch();
        insert.set(survey.id, 6).set(survey.name, "a_").addBatch();    
        insert.set(survey.id, 7).set(survey.name, "a%").addBatch();
        insert.execute();
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
    public void Like_with_Escape(){        
        assertEquals(1l, query().from(survey).where(survey.name.like("a!%")).count());
        assertEquals(1l, query().from(survey).where(survey.name.like("a!_")).count());
        assertEquals(3l, query().from(survey).where(survey.name.like("a%")).count());
        assertEquals(2l, query().from(survey).where(survey.name.like("a_")).count());
        
        assertEquals(1l, query().from(survey).where(survey.name.startsWith("a_")).count());
        assertEquals(1l, query().from(survey).where(survey.name.startsWith("a%")).count());
    }
    

}
