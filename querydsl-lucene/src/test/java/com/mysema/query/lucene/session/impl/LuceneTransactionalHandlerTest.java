package com.mysema.query.lucene.session.impl;

import static com.mysema.query.lucene.session.QueryTestHelper.*;
import static org.junit.Assert.assertEquals;

import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import com.mysema.query.lucene.LuceneQuery;
import com.mysema.query.lucene.session.LuceneSession;
import com.mysema.query.lucene.session.LuceneSessionFactory;
import com.mysema.query.lucene.session.LuceneTransactional;
import com.mysema.query.lucene.session.SessionNotBoundException;
import com.mysema.query.lucene.session.QDocument;
import com.mysema.query.lucene.session.SessionReadOnlyException;

public class LuceneTransactionalHandlerTest {

    private LuceneSessionFactory sessionFactory;
    
    private LuceneTransactionHandler handler = new LuceneTransactionHandler();

    private TestDao testDao;
    
    private QDocument doc = new QDocument("test");

    @Before
    public void before() {

        sessionFactory = new LuceneSessionFactoryImpl(new RAMDirectory());

        AspectJProxyFactory factory = new AspectJProxyFactory(new TestDaoImpl(sessionFactory));
        factory.addAspect(handler);

        testDao = factory.getProxy();
    }

    @Test
    public void Empty() {
        testDao.empty();
        assertEquals(1, testDao.count());
    }

    @Test(expected = SessionNotBoundException.class)
    public void NoAnnotation() {
        testDao.noAnnotation();
    }

    @Test
    public void Annotation() {
        testDao.annotation();
        assertEquals(1, testDao.count());
    }
    
    @Test(expected = SessionReadOnlyException.class)
    public void ReadOnly() {
        testDao.readOnly();
    }
    
    @Test
    public void Writing() {
        testDao.writing();
        
        LuceneQuery q = sessionFactory.openSession(true).createQuery();
        assertEquals(4, q.where(doc.title.like("*")).count());
    }
    
    @Test
    public void Multifactories() {
        
        LuceneSessionFactory sf1 = new LuceneSessionFactoryImpl(new RAMDirectory());
        LuceneSessionFactory sf2 = new LuceneSessionFactoryImpl(new RAMDirectory());
        LuceneSessionFactory sf3 = new LuceneSessionFactoryImpl(new RAMDirectory());
        
        AspectJProxyFactory factory = new AspectJProxyFactory(new TestDaoImpl(sf1,sf2,sf3));
        factory.addAspect(handler);
        testDao = factory.getProxy();
        
        testDao.multiFactories();
        
        LuceneQuery q = sf1.openSession(true).createQuery();
        assertEquals(1, q.where(doc.title.eq("sf1")).count());
        
        q = sf2.openSession(true).createQuery();
        assertEquals(1, q.where(doc.title.eq("sf2")).count());
        
        q = sf3.openSession(true).createQuery();
        assertEquals(1, q.where(doc.title.eq("sf3")).count());
    }
    
    @Test
    public void NestedSession() {
        AspectJProxyFactory factory = new AspectJProxyFactory(new NestedDaoImpl(sessionFactory));
        factory.addAspect(handler);
        NestedDao nestedDao = factory.getProxy();
        
        testDao.setNested(nestedDao);
        
        testDao.nested();
        
        LuceneSession session = sessionFactory.openSession(true);
        assertEquals(1, session.createQuery().where(doc.title.eq("nested")).count());
        session.close();
        
    }
    

    private static interface TestDao {
        void empty();

        int count();

        void noAnnotation();

        void annotation();

        void readOnly();

        void writing();
        
        void multiFactories();
        
        void nested();
        
        void setNested(NestedDao nested);
    }
    
    private static class TestDaoImpl implements TestDao {

        private int count = 0;

        private LuceneSessionFactory[] factories;
        
        private NestedDao nested;
        
        TestDaoImpl(LuceneSessionFactory ... factories) {
            this.factories = factories;
        }

        @Override
        @LuceneTransactional
        public void empty() {
            count++;
        }

        @Override
        public int count() {
            return count;
        }

        @Override
        public void noAnnotation() {
            factories[0].getCurrentSession();
        }

        @Override
        @LuceneTransactional
        public void annotation() {
            count++;
            factories[0].getCurrentSession();
        }

        @Override
        @LuceneTransactional(readOnly=true)
        public void readOnly() {
           LuceneSession session =  factories[0].getCurrentSession();
           session.beginAppend().addDocument(getDocument());
        }

        @Override
        @LuceneTransactional
        public void writing() {
            LuceneSession session =  factories[0].getCurrentSession();
            createDocuments(session);
        }

        @Override
        @LuceneTransactional
        public void multiFactories() {
           LuceneSession s1 = factories[0].getCurrentSession();
           LuceneSession s2 = factories[1].getCurrentSession();
           LuceneSession s3 = factories[2].getCurrentSession();
           
           s1.beginReset().addDocument(createDocument("sf1","","",0,0));
           s2.beginReset().addDocument(createDocument("sf2","","",0,0));
           s3.beginReset().addDocument(createDocument("sf3","","",0,0));
        }

        @Override
        public void setNested(NestedDao nested) {
            this.nested = nested;
        }
        
        @Override
        @LuceneTransactional
        public void nested() {
            LuceneSession session = factories[0].getCurrentSession();
            session.beginReset().addDocument(createDocument("nested","","",0,0));
            nested.nested();
        }
    }
    
    private static interface NestedDao {
        void nested();
    }
    
    private class NestedDaoImpl implements NestedDao {

        private LuceneSessionFactory sessionFactory;
        
        public NestedDaoImpl(LuceneSessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
        }
        
        @Override
        @LuceneTransactional
        public void nested() {
            LuceneSession session = sessionFactory.getCurrentSession(); 
            LuceneQuery query = session.createQuery();
            
            assertEquals(0, query.where(doc.title.eq("nested")).count());

            // This verifies that the we are using the same session opened in
            // the caller scope
            session.flush();

            query = session.createQuery();
            assertEquals(1, query.where(doc.title.eq("nested")).count());
        }
    }
}
