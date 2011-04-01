package com.mysema.query.sql.mysql;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;

import javax.inject.Provider;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.domain.QSurvey;

public class MySQLQueryFactoryTest {

    private MySQLQueryFactory queryFactory;

    @Before
    public void setUp(){
        Provider<Connection> provider = new Provider<Connection>(){
            @Override
            public Connection get() {
                return EasyMock.createNiceMock(Connection.class);
            }
        };
        queryFactory = new MySQLQueryFactory(SQLTemplates.DEFAULT, provider);
    }

    @Test
    public void Query(){
        assertNotNull(queryFactory.query());
    }

    @Test
    public void SubQuery(){
        assertNotNull(queryFactory.subQuery());
    }

    @Test
    public void SubQuery_From(){
        assertNotNull(queryFactory.subQuery(QSurvey.survey));
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
    public void Insert(){
        assertNotNull(queryFactory.insert(QSurvey.survey));
    }

    @Test
    public void InsertIgnore(){
        assertNotNull(queryFactory.insertIgnore(QSurvey.survey));
    }

    @Test
    public void Replace(){
        assertNotNull(queryFactory.replace(QSurvey.survey));
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
