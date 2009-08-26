/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.mysema.query.HibernateConfig;
import com.mysema.query.HibernateTestRunner;
import com.mysema.query.hql.hibernate.HibernateQuery;

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
                    HibernateQuery.setConstants(query, getConstants());
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

    public void setSession(Session session) {
        this.session = session;
    }

}
