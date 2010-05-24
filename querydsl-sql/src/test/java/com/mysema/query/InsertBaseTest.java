/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import static com.mysema.query.Connections.getConnection;
import static com.mysema.query.Constants.survey;
import static com.mysema.query.Constants.survey2;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.domain.QEmployee;
import com.mysema.query.sql.domain.QSurvey;
import com.mysema.query.types.path.PEntity;

public abstract class InsertBaseTest extends AbstractBaseTest{

    protected SQLInsertClause insert(PEntity<?> e){
        return new SQLInsertClause(getConnection(), dialect, e);
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
            .select(sq().from(survey2).list(survey2.id.add(1), survey2.name))
            .execute();
        
        // with subquery, without columns
        insert(survey)
            .select(sq().from(survey2).list(survey2.id.add(10), survey2.name))
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
