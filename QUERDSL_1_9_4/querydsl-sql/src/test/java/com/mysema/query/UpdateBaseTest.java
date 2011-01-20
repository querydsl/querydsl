/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import static com.mysema.query.Constants.survey;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.types.Path;

public abstract class UpdateBaseTest extends AbstractBaseTest{

    protected void reset() throws SQLException{
        delete(survey).execute();
        insert(survey).values(1, "Hello World").execute();
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

    @Test
    public void test2() throws SQLException{
        List<Path<?>> paths = Collections.<Path<?>>singletonList(survey.name);
        List<?> values = Collections.singletonList("S");

        // original state
        long count = query().from(survey).count();
        assertEquals(0, query().from(survey).where(survey.name.eq("S")).count());

        // update call with 0 update count
        assertEquals(0, update(survey).where(survey.name.eq("XXX")).set(paths, values).execute());
        assertEquals(0, query().from(survey).where(survey.name.eq("S")).count());

        // update call with full update count
        assertEquals(count, update(survey).set(paths, values).execute());
        assertEquals(count, query().from(survey).where(survey.name.eq("S")).count());

    }

    @Test
    public void setNull(){
        List<Path<?>> paths = Collections.<Path<?>>singletonList(survey.name);
        List<?> values = Collections.singletonList(null);
        long count = query().from(survey).count();
        assertEquals(count, update(survey).set(paths, values).execute());
    }

    @Test
    public void setNull2(){
        long count = query().from(survey).count();
        assertEquals(count, update(survey).set(survey.name, null).execute());
    }
    
    @Test
    public void batch() throws SQLException{
        insert(survey).values(2, "A").execute();
        insert(survey).values(3, "B").execute();
        
        SQLUpdateClause update = update(survey);
        update.set(survey.name, "AA").where(survey.name.eq("A")).addBatch();
        update.set(survey.name, "BB").where(survey.name.eq("B")).addBatch();
        assertEquals(2, update.execute());        
    }

}
