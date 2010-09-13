/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdoql;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;

import com.mysema.query.jdoql.dml.JDOQLDeleteClause;
import com.mysema.query.jdoql.testdomain.Product;
import com.mysema.query.jdoql.testdomain.Store;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.expr.BooleanExpression;

public abstract class AbstractJDOTest {

    private static final JDOQLTemplates templates = new JDOQLTemplates();
    
    protected static PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");

    protected PersistenceManager pm;

    protected Transaction tx;

    protected JDOQLQuery query() {
        return new JDOQLQueryImpl(pm, templates, false);
    }

    protected JDOQLQuery detachedQuery() {
        return new JDOQLQueryImpl(pm, templates, true);
    }

    protected JDOQLSubQuery sub(){
        return new JDOQLSubQuery();
    }

    protected <T> List<T> query(EntityPath<T> source, BooleanExpression condition) {
        return query().from(source).where(condition).list(source.asExpr());
    }

    protected JDOQLDeleteClause delete(EntityPath<?> entity) {
        return new JDOQLDeleteClause(pm, entity, templates);
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
