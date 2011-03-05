package com.mysema.query.sql;

import static org.junit.Assert.*;

import java.sql.Connection;

import javax.inject.Provider;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.domain.QSurvey;

public class SQLQueryFactoryTest {
    
    private SQLQueryFactory queryFactory;
    
    @Before
    public void setUp(){
        Provider<Connection> provider = new Provider<Connection>(){
            @Override
            public Connection get() {
                return EasyMock.createNiceMock(Connection.class);
            }            
        };
        queryFactory = new SQLQueryFactory(SQLTemplates.DEFAULT, provider);
    }
    
    @Test
    public void Query(){
        assertNotNull(queryFactory.query());
    }
    
    @Test
    public void From(){
        assertNotNull(queryFactory.from(QSurvey.survey));
    }
    
    @Test
    public void Delete(){
        assertNotNull(queryFactory.delete(QSurvey.survey));
    }
    
    @Test
    public void Update(){
        assertNotNull(queryFactory.update(QSurvey.survey));
    }
    
    @Test
    public void Merge(){
        assertNotNull(queryFactory.merge(QSurvey.survey));
    }

}
