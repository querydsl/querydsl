/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Test;
import org.junit.runner.RunWith;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.mysema.query.JPAConfig;
import com.mysema.query.JPATestRunner;
import com.mysema.query.hql.jpa.JPAUtil;

/**
 * HibernatePersistenceTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
@RunWith(JPATestRunner.class)
@JPAConfig("hsqldb")
public class JPAIntegrationTest extends ParserTest {

    private EntityManager entityManager;

    protected TestQuery query() {
        return new TestQuery() {
            public void parse() throws RecognitionException, TokenStreamException {
                try {
                    System.out.println("query : " + toString().replace('\n', ' '));

                    // create Query and execute it
                    Query query = entityManager.createQuery(toString());
                    JPAUtil.setConstants(query, getConstants());
                    try {
                        query.getResultList();
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

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
