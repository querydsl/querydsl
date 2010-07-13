/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import static com.mysema.query.Constants.survey;
import static com.mysema.query.Constants.survey2;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.domain.QEmployee;
import com.mysema.query.sql.domain.QSurvey;
import com.mysema.testutil.ExcludeIn;

public abstract class InsertBaseTest extends AbstractBaseTest{

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
    public void insert(){
//        create table survey (id int,name varchar(30))

        // with columns
        insert(survey)
            .columns(survey.id, survey.name)
            .values(3, "Hello").execute();

        // without columns
        insert(survey)
            .values(4, "Hello").execute();

        // with subquery
        insert(survey)
            .columns(survey.id, survey.name)
            .select(sq().from(survey2).list(survey2.id.add(20), survey2.name))
            .execute();

        // with subquery, without columns
        insert(survey)
            .select(sq().from(survey2).list(survey2.id.add(10), survey2.name))
            .execute();
    }

    @Test
    @ExcludeIn({Target.HSQLDB, Target.DERBY})
    public void insert_With_Keys() throws SQLException{
        ResultSet rs = insert(survey).set(survey.name, "Hello World").executeWithKeys();
        assertTrue(rs.next());
        rs.close();
    }

    @Test
    public void insertNull(){
        // with columns
        insert(survey)
            .columns(survey.id, survey.name)
            .values(3, null).execute();

        // without columns
        insert(survey)
            .values(4, null).execute();

        // with set
        insert(survey)
            .set(survey.id, 5)
            .set(survey.name, null)
            .execute();
    }

    @Test
    public void insert_Alternative_Syntax(){
        // with columns
        insert(survey)
            .set(survey.id, 3)
            .set(survey.name, "Hello")
            .execute();
    }

    @Test
    public void complex1(){
        // related to #584795
        QSurvey survey = new QSurvey("survey");
        QEmployee emp1 = new QEmployee("emp1");
        QEmployee emp2 = new QEmployee("emp2");
        SQLInsertClause insert = insert(survey);
        insert.columns(survey.id, survey.name);
        insert.select(new SQLSubQuery().from(survey)
          .innerJoin(emp1)
           .on(survey.id.eq(emp1.id))
          .innerJoin(emp2)
           .on(emp1.superiorId.eq(emp2.superiorId), emp1.firstname.eq(emp2.firstname))
          .list(survey.id, emp2.firstname));
        insert.execute();
    }
}
