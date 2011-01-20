/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.DerbyTemplates;
import com.mysema.query.sql.SQLQueryImpl;
import com.mysema.query.sql.domain.QSurvey;

public class QueryMutabilityTest{

    private static final QSurvey survey = new QSurvey("survey");

    private Connection connection;

    @Before
    public void setUp() throws Exception{
        Connections.initDerby();
        connection = Connections.getConnection();
    }

    @After
    public void tearDown() throws SQLException{
        Connections.close();
    }

    @Test
    public void test() throws IOException, SecurityException,
            IllegalArgumentException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        SQLQueryImpl query = new SQLQueryImpl(connection, new DerbyTemplates());
        query.from(survey);
        new QueryMutability(query).test(survey.id, survey.name);
    }

    @Test
    public void testClone(){
        SQLQueryImpl query = new SQLQueryImpl(new DerbyTemplates()).from(survey);
        SQLQueryImpl query2 = query.clone(connection);
        assertEquals(query.getMetadata().getJoins(), query2.getMetadata().getJoins());
        assertEquals(query.getMetadata().getWhere(), query2.getMetadata().getWhere());
        query2.list(survey.id);
    }

}
