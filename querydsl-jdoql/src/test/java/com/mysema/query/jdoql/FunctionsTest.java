package com.mysema.query.jdoql;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.junit.BeforeClass;

import com.mysema.query.jdoql.testdomain.Product;

public class FunctionsTest extends AbstractJDOTest{

    @BeforeClass
    public static void doPersist() {
        // Persistence of a Product and a Book.
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            for (int i = 0; i < 10; i++) {
                pm.makePersistent(new Product("C" + i, "F" + i, i * 200.00));
                pm.makePersistent(new Product("B" + i, "E" + i, i * 200.00));
                pm.makePersistent(new Product("A" + i, "D" + i, i * 200.00));
            }
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
        System.out.println("");

    }
    
}
