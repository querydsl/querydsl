package com.mysema.query.jpa;

import static org.junit.Assert.assertNotNull;

import javax.inject.Provider;

import org.easymock.EasyMock;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.jpa.hibernate.HibernateQueryFactory;
import com.mysema.query.sql.domain.QSurvey;

public class HibernateQueryFactoryTest {
    
    private HibernateQueryFactory queryFactory;
    
    @Before
    public void setUp(){
        Provider<Session> provider = new Provider<Session>(){
            @Override
            public Session get() {
                return EasyMock.createNiceMock(Session.class);
            }            
        };
        queryFactory = new HibernateQueryFactory(JPQLTemplates.DEFAULT, provider);
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
    
}
