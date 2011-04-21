/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Test;
import org.junit.runner.RunWith;

import antlr.RecognitionException;
import antlr.TokenStreamException;

import com.mysema.query.jpa.impl.JPAUtil;
import com.mysema.testutil.JPAConfig;
import com.mysema.testutil.JPATestRunner;

/**
 * HibernatePersistenceTest provides.
 *
 * @author tiwe
 * @version $Id$
 */
@RunWith(JPATestRunner.class)
@JPAConfig("hsqldb")
public class JPAIntegrationTest extends ParsingTest {

    private EntityManager entityManager;

    @Override
    protected QueryHelper query() {
        return new QueryHelper() {
            @Override
            public void parse() throws RecognitionException, TokenStreamException {
                try {
                    System.out.println("query : " + toString().replace('\n', ' '));

                    // create Query and execute it
                    Query query = entityManager.createQuery(toString());
                    JPAUtil.setConstants(query, getConstants(),getMetadata().getParams());
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
    public void GroupBy() throws Exception {
        // NOTE : commented out, because HQLSDB doesn't support these queries
    }
    
    @Override
    @Test
    public void GroupBy_2() throws Exception {
        // NOTE : commented out, because HQLSDB doesn't support these queries
    }

    @Override
    @Test
    public void OrderBy() throws Exception {
        // NOTE : commented out, because HQLSDB doesn't support these queries
    }

    @Override
    @Test
    public void DocoExamples910() throws Exception {
        // NOTE : commented out, because HQLSDB doesn't support these queries
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
