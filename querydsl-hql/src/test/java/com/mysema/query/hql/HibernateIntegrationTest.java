/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import static org.junit.Assert.assertEquals;

import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.mysema.query.HibernateConfig;
import com.mysema.query.HibernateTestRunner;
import com.mysema.query.hql.domain.Cat;
import com.mysema.query.hql.domain.QCat;
import com.mysema.query.hql.hibernate.HibernateDeleteClause;
import com.mysema.query.hql.hibernate.HibernateQuery;
import com.mysema.query.hql.hibernate.HibernateUpdateClause;
import com.mysema.query.hql.hibernate.HibernateUtil;
import com.mysema.query.types.path.PEntity;

/**
 * HibernatePersistenceTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
@RunWith(HibernateTestRunner.class)
@HibernateConfig("hsqldb.properties")
public class HibernateIntegrationTest extends ParserTest {

    private Session session;

    protected TestQuery query() {
        return new TestQuery() {
            public void parse() throws RecognitionException,
                    TokenStreamException {
                try {
                    System.out.println("query : " + toString().replace('\n', ' '));

                    // create Query and execute it
                    Query query = session.createQuery(toString());
                    HibernateUtil.setConstants(query, getConstants());
                    try {
                        query.list();
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                } finally {
                    System.out.println();
                }
            }
        };
    }

    @Override
    @Test
    public void testGroupBy() throws Exception {
        // NOTE : commented out, because HQLSDB doesn't support these queries
    }

    @Test
    public void testOrderBy() throws Exception {
        // NOTE : commented out, because HQLSDB doesn't support these queries
    }

    @Test
    public void testDocoExamples910() throws Exception {
        // NOTE : commented out, because HQLSDB doesn't support these queries
    }

    private HibernateDeleteClause delete(PEntity<?> entity){
        return new HibernateDeleteClause(session, entity);
    }
    
    private HibernateUpdateClause update(PEntity<?> entity){
        return new HibernateUpdateClause(session, entity);
    }
    
    @Test
    public void testScroll(){
        session.save(new Cat("Bob",10));
        session.save(new Cat("Steve",11));
        
        HibernateQuery query = new HibernateQuery(session);
        ScrollableResults results = query.from(QCat.cat).scroll(ScrollMode.SCROLL_INSENSITIVE, QCat.cat);
        while (results.next()){
            System.out.println(results.get(0));
        }
        results.close();
    }
    
    @Test
    public void testUpdate(){
        session.save(new Cat("Bob",10));
        session.save(new Cat("Steve",11));
        
        QCat cat = QCat.cat;
        long amount = update(cat).where(cat.name.eq("Bob"))
            .set(cat.name, "Bobby")
            .set(cat.alive, false)
            .execute();
        assertEquals(1, amount);
            
        assertEquals(0l, query().from(cat).where(cat.name.eq("Bob")).count());
    }
    
    @Test
    public void testDelete(){
        session.save(new Cat("Bob",10));
        session.save(new Cat("Steve",11));
        
        QCat cat = QCat.cat;
        long amount = delete(cat).where(cat.name.eq("Bob"))            
            .execute();
        assertEquals(1, amount);
            
        assertEquals(0l, query().from(cat).where(cat.name.eq("Bob")).count());
    }
    
    public void setSession(Session session) {
        this.session = session;
    }

}
