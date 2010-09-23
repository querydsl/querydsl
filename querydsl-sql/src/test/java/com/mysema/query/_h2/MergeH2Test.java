/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._h2;

import static com.mysema.query.Constants.survey;
import static com.mysema.query.Constants.survey2;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysema.query.Connections;
import com.mysema.query.MergeBaseTest;
import com.mysema.query.Target;
import com.mysema.query.sql.H2Templates;
import com.mysema.query.sql.dml.SQLMergeClause;
import com.mysema.testutil.Label;

@Label(Target.H2)
public class MergeH2Test extends MergeBaseTest{

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initH2();
    }

    @Before
    public void setUp() throws SQLException {
        templates = new H2Templates().newLineToSingleSpace();
        super.setUp();
    }
    
    @Test
    public void mergeBatch(){
        SQLMergeClause merge = merge(survey)
            .keys(survey.id)
            .set(survey.id, 5)
            .set(survey.name, "5")
            .addBatch();
        
        merge
            .keys(survey.id)
            .set(survey.id, 6)
            .set(survey.name, "6")
            .addBatch();
     
        assertEquals(2, merge.execute());
        
        assertEquals(1l, query().from(survey).where(survey.name.eq("5")).count());
        assertEquals(1l, query().from(survey).where(survey.name.eq("6")).count());
    }
    
    @Test
    public void mergeBatch_with_subquery(){
        SQLMergeClause merge = merge(survey)
            .keys(survey.id)
            .columns(survey.id, survey.name)
            .select(sq().from(survey2).list(survey2.id.add(20), survey2.name))
            .addBatch();
        
        merge(survey)
            .keys(survey.id)
            .columns(survey.id, survey.name)
            .select(sq().from(survey2).list(survey2.id.add(40), survey2.name))
            .addBatch();
        
        merge.execute();
//        assertEquals(1, insert.execute());
    }
}
