/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import static com.mysema.query.Connections.getConnection;
import static com.mysema.query.Constants.survey;
import static com.mysema.query.Constants.survey2;

import org.junit.Test;

import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.types.path.PEntity;

public abstract class InsertBaseTest extends AbstractBaseTest{

    protected SQLInsertClause insert(PEntity<?> e){
        return new SQLInsertClause(getConnection(), dialect, e);
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
            .select(s().from(survey2).list(survey2.id.add(1), survey2.name))
            .execute();
        
        // with subquery, without columns
        insert(survey)
            .select(s().from(survey2).list(survey2.id.add(10), survey2.name))
            .execute();
    }
}
