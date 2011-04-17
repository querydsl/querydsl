/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.oracle;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.OracleTemplates;
import com.mysema.query.sql.domain.QSurvey;

public class OracleQueryTest {
    
    private OracleQuery query;
    
    private QSurvey survey = new QSurvey("survey");
    
    @Before
    public void setUp(){
        query = new OracleQuery(null, new OracleTemplates(){{
            newLineToSingleSpace();
        }});
        query.from(survey);
        query.orderBy(survey.name.asc());
    }    

    @Test
    public void ConnectByPrior() {
        query.connectByPrior(survey.name.isNull());
        assertEquals("from SURVEY survey connect by prior survey.NAME is null order by survey.NAME asc", toString(query));
    }

    @Test
    public void ConnectBy() {
        query.connectByPrior(survey.name.isNull());
        assertEquals("from SURVEY survey connect by prior survey.NAME is null order by survey.NAME asc", toString(query));
    }

    @Test
    public void ConnectByNocyclePrior() {
        query.connectByNocyclePrior(survey.name.isNull());
        assertEquals("from SURVEY survey connect by nocycle prior survey.NAME is null order by survey.NAME asc", toString(query));
    }

    @Test
    public void StartWith() {
        query.startWith(survey.name.isNull());
        assertEquals("from SURVEY survey start with survey.NAME is null order by survey.NAME asc", toString(query));
    }

    @Test
    public void OrderSiblingsBy() {
        query.orderSiblingsBy(survey.name);
        assertEquals("from SURVEY survey order siblings by survey.NAME order by survey.NAME asc", toString(query));
    }

    private String toString(OracleQuery query){
        return query.toString().replace('\n', ' ');
    }
}
