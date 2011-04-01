package com.mysema.query.jdo;

import static org.junit.Assert.assertNotNull;

import javax.inject.Provider;
import javax.jdo.PersistenceManager;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.jdo.test.domain.QProduct;

public class JDOQueryFactoryTest {

    private JDOQueryFactory queryFactory;

    @Before
    public void setUp(){
        Provider<PersistenceManager> provider = new Provider<PersistenceManager>(){
            @Override
            public PersistenceManager get() {
                return EasyMock.createNiceMock(PersistenceManager.class);
            }
        };
        queryFactory = new JDOQueryFactory(provider);
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
    public void From(){
        assertNotNull(queryFactory.from(QProduct.product));
    }

    @Test
    public void Delete(){
        assertNotNull(queryFactory.delete(QProduct.product));
    }

}
