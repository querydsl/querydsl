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
import com.mysema.query.lucene.session.LuceneTransaction;
import com.mysema.query.lucene.session.NoSessionBoundException;
import com.mysema.query.lucene.session.QDocument;
import com.mysema.query.lucene.session.SessionReadOnlyException;

public class LuceneTransactionalHandlerTest {

    private LuceneSessionFactory sessionFactory;
    
    private LuceneTransactionHandler handler = new LuceneTransactionHandler();

    private TxTest txTest;
    
    private QDocument doc = new QDocument("test");
    
    //private StringPath title = doc.title;

    @Before
    public void before() {

        sessionFactory = new LuceneSessionFactoryImpl(new RAMDirectory());

        AspectJProxyFactory factory = new AspectJProxyFactory(new TxTestImpl(sessionFactory));
        factory.addAspect(handler);

        txTest = factory.getProxy();
    }

    @Test
    public void testEmpty() {
        txTest.empty();
        assertEquals(1, txTest.count());
    }

    @Test(expected = NoSessionBoundException.class)
    public void testNoAnnotation() {
        txTest.noAnnotation();
    }

    @Test
    public void testAnnotation() {
        txTest.annotation();
        assertEquals(1, txTest.count());
    }
    
    @Test(expected = SessionReadOnlyException.class)
    public void testReadOnly() {
        txTest.readOnly();
    }
    
    @Test
    public void testWriting() {
        txTest.writing();
        
        LuceneQuery q = sessionFactory.openSession(true).createQuery();
        assertEquals(4, q.where(doc.title.like("*")).count());
    }
    
    @Test
    public void testMultifactories() {
        
        LuceneSessionFactory sf1 = new LuceneSessionFactoryImpl(new RAMDirectory());
        LuceneSessionFactory sf2 = new LuceneSessionFactoryImpl(new RAMDirectory());
        LuceneSessionFactory sf3 = new LuceneSessionFactoryImpl(new RAMDirectory());
        
        AspectJProxyFactory factory = new AspectJProxyFactory(new TxTestImpl(sf1,sf2,sf3));
        factory.addAspect(handler);
        txTest = factory.getProxy();
        
        txTest.multiFactories();
        
        LuceneQuery q = sf1.openSession(true).createQuery();
        assertEquals(1, q.where(doc.title.eq("sf1")).count());
        
        q = sf2.openSession(true).createQuery();
        assertEquals(1, q.where(doc.title.eq("sf2")).count());
        
        q = sf3.openSession(true).createQuery();
        assertEquals(1, q.where(doc.title.eq("sf3")).count());
    }
    
    

    private static interface TxTest {
        void empty();

        int count();

        void noAnnotation();

        void annotation();

        void readOnly();

        void writing();
        
        void multiFactories();
    }

    private static class TxTestImpl implements TxTest {

        private int count = 0;

        private LuceneSessionFactory[] factories;
        
        TxTestImpl(LuceneSessionFactory ... factories) {
            this.factories = factories;
        }

        @Override
        @LuceneTransaction
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
        @LuceneTransaction
        public void annotation() {
            count++;
            factories[0].getCurrentSession();
        }

        @Override
        @LuceneTransaction(readOnly=true)
        public void readOnly() {
           LuceneSession session =  factories[0].getCurrentSession();
           session.beginAppend().addDocument(getDocument());
        }

        @Override
        @LuceneTransaction
        public void writing() {
            LuceneSession session =  factories[0].getCurrentSession();
            createDocuments(session);
        }

        @Override
        @LuceneTransaction
        public void multiFactories() {
           LuceneSession s1 = factories[0].getCurrentSession();
           LuceneSession s2 = factories[1].getCurrentSession();
           LuceneSession s3 = factories[2].getCurrentSession();
           
           s1.beginOverwrite().addDocument(createDocument("sf1","","",0,0));
           s2.beginOverwrite().addDocument(createDocument("sf2","","",0,0));
           s3.beginOverwrite().addDocument(createDocument("sf3","","",0,0));
        }
    }
}
