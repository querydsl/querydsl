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
package com.querydsl.jdo;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;

import com.querydsl.jdo.dml.JDODeleteClause;
import com.querydsl.jdo.test.domain.Book;
import com.querydsl.jdo.test.domain.Product;
import com.querydsl.jdo.test.domain.Store;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;

public abstract class AbstractJDOTest {

    private static final JDOQLTemplates templates = new JDOQLTemplates();
    
    protected static final PersistenceManagerFactory pmf = 
            JDOHelper.getPersistenceManagerFactory("datanucleus.properties");

    protected PersistenceManager pm;

    protected Transaction tx;

    protected JDOQuery query() {
        return new JDOQuery(pm, templates, false);
    }

    protected JDOQuery detachedQuery() {
        return new JDOQuery(pm, templates, true);
    }

    protected JDOSubQuery sub() {
        return new JDOSubQuery();
    }

    protected <T> List<T> query(EntityPath<T> source, Predicate condition) {
        return query().from(source).where(condition).list(source);
    }

    protected JDODeleteClause delete(EntityPath<?> entity) {
        return new JDODeleteClause(pm, entity, templates);
    }

    @Before
    public void setUp() {
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        tx.begin();
    }

    @After
    public void tearDown() {
        if (tx.isActive()) {
            tx.rollback();
        }
        pm.close();
    }

    @AfterClass
    public static void doCleanUp() {
        // Clean out the database
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            pm.newQuery(Store.class).deletePersistentAll();
            pm.newQuery(Book.class).deletePersistentAll();
            pm.newQuery(Product.class).deletePersistentAll();
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }

}
