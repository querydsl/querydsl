/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
