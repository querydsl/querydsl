/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import static com.mysema.query.Constants.survey;
import static com.mysema.query.Constants.survey2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
    public void Insert(){
//        create table survey (id int,name varchar(30))

        // with columns
        assertEquals(1, insert(survey)
            .columns(survey.id, survey.name)
            .values(3, "Hello").execute());

        // without columns
        assertEquals(1, insert(survey)
            .values(4, "Hello").execute());
        
        // with subquery
        int count = (int)query().from(survey).count();
        assertEquals(count, insert(survey)
            .columns(survey.id, survey.name)
            .select(sq().from(survey2).list(survey2.id.add(20), survey2.name))
            .execute());

        // with subquery, without columns
        count = (int)query().from(survey).count();
        assertEquals(count, insert(survey)
            .select(sq().from(survey2).list(survey2.id.add(10), survey2.name))
            .execute());
    }
    
    @Test
    public void Insert_Batch(){
        SQLInsertClause insert = insert(survey)
            .set(survey.id, 5)
            .set(survey.name, "55")
            .addBatch();
        
        insert.set(survey.id, 6)
            .set(survey.name, "66")
            .addBatch();
     
        assertEquals(2, insert.execute());
        
        assertEquals(1l, query().from(survey).where(survey.name.eq("55")).count());
        assertEquals(1l, query().from(survey).where(survey.name.eq("66")).count());
    }
    
    @Test
    public void Like_with_Escape(){
        SQLInsertClause insert = insert(survey);
        insert.set(survey.id, 5).set(survey.name, "aaa").addBatch();
        insert.set(survey.id, 6).set(survey.name, "a_").addBatch();    
        insert.set(survey.id, 7).set(survey.name, "a%").addBatch();
        assertEquals(3, insert.execute());
        
        assertEquals(3l, query().from(survey).where(survey.name.like("a%")).count());
        assertEquals(2l, query().from(survey).where(survey.name.like("a_")).count());
        
        assertEquals(1l, query().from(survey).where(survey.name.startsWith("a_")).count());
        assertEquals(1l, query().from(survey).where(survey.name.startsWith("a%")).count());
    }
    
    @Test
    public void InsertBatch_with_Subquery(){
        SQLInsertClause insert = insert(survey)
            .columns(survey.id, survey.name)
            .select(sq().from(survey2).list(survey2.id.add(20), survey2.name))
            .addBatch();
        
        insert(survey)
            .columns(survey.id, survey.name)
            .select(sq().from(survey2).list(survey2.id.add(40), survey2.name))
            .addBatch();
        
        insert.execute();
//        assertEquals(1, insert.execute());
    }

    @Test
    @ExcludeIn({Target.HSQLDB, Target.DERBY, Target.POSTGRES})
    public void Insert_With_Keys() throws SQLException{
        ResultSet rs = insert(survey).set(survey.name, "Hello World").executeWithKeys();
        assertTrue(rs.next());
        assertTrue(rs.getObject(1) != null);
        rs.close();
    }
    
    @Test
    @ExcludeIn({Target.HSQLDB, Target.DERBY, Target.POSTGRES})
    public void Insert_With_Keys_Projected() throws SQLException{
        assertNotNull(insert(survey).set(survey.name, "Hello you").executeWithKey(survey.id));
    }
    
    @Test
    public void Insert_Null(){
        // with columns
        assertEquals(1, insert(survey)
            .columns(survey.id, survey.name)
            .values(3, null).execute());

        // without columns
        assertEquals(1, insert(survey)
            .values(4, null).execute());

        // with set
        assertEquals(1, insert(survey)
            .set(survey.id, 5)
            .set(survey.name, (String)null)
            .execute());
    }

    @Test
    public void Insert_Alternative_Syntax(){
        // with columns
        assertEquals(1, insert(survey)
            .set(survey.id, 3)
            .set(survey.name, "Hello")
            .execute());
    }

    @Test
    public void Complex1(){
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
