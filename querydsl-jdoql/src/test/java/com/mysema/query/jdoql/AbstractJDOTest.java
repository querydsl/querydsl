package com.mysema.query.jdoql;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;

import com.mysema.query.jdoql.testdomain.Product;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;

public abstract class AbstractJDOTest {

    protected static PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");

    protected PersistenceManager pm;

    protected Transaction tx;

    protected JDOQLQuery query() {
        return new JDOQLQueryImpl(pm);
    }

    protected <T> List<T> query(PEntity<T> source, EBoolean condition) {
        return new JDOQLQueryImpl(pm).from(source).where(condition)
                .list(source);
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
            System.out.println("Deleting all products from persistence");
            Query q = pm.newQuery(Product.class);
            long numberInstancesDeleted = q.deletePersistentAll();
            System.out.println("Deleted " + numberInstancesDeleted
                    + " products");

            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }

}
