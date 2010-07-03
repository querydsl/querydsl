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

import com.mysema.query.sql.domain.QSurvey;
import com.mysema.testutil.IncludeIn;

public abstract class MergeBaseTest extends AbstractBaseTest{

    private void reset() throws SQLException{
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
    @IncludeIn(Target.H2)
    public void merge_with_Keys_and_Values(){
        // NOTE : doesn't work with composite merge implementation
        // keys + values
        assertEquals(1, merge(survey).keys(survey.id).values(5, "Hello World").execute());
    }

    @Test
    public void merge_with_Keys_Columns_and_Values(){
        // keys + columns + values
        assertEquals(1, merge(survey).keys(survey.id)
            .set(survey.id, 5)
            .set(survey.name, "Hello World").execute());
    }

    @Test
    @IncludeIn(Target.H2)
    public void merge_with_Keys_and_SubQuery(){    
        assertEquals(1, insert(survey).set(survey.id, 6).set(survey.name, "H").execute());

        // keys + subquery
        QSurvey survey2 = new QSurvey("survey2");
        assertEquals(2, merge(survey).keys(survey.id).select(sq().from(survey2).list(survey2.id.add(1), survey2.name)).execute());
    }
}
