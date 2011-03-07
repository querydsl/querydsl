package com.mysema.query.jpa;

import static org.junit.Assert.assertNotNull;

import javax.inject.Provider;
import javax.persistence.EntityManager;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.jpa.impl.JPAQueryFactory;
import com.mysema.query.sql.domain.QSurvey;

public class JPAQueryFactoryTest {
    
    private JPAQueryFactory queryFactory;
    
    @Before
    public void setUp(){
        Provider<EntityManager> provider = new Provider<EntityManager>(){
            @Override
            public EntityManager get() {
                return EasyMock.createNiceMock(EntityManager.class);
            }            
        };
        queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, provider);
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
