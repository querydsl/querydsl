/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdo;

import static org.junit.Assert.assertEquals;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mysema.query.jdo.test.domain.Product;
import com.mysema.query.jdo.test.domain.QProduct;

public class GroupByTest extends AbstractJDOTest {

    private QProduct product = QProduct.product;

    @Test
    public void Distinct() {
        assertEquals(3, query().from(product).listDistinct(product.description).size());
        assertEquals(3, query().from(product).listDistinct(product.price).size());
    }

    @Test
    public void GroupBy() {
        assertEquals(3, query().from(product).groupBy(product.description).list(product.description).size());
        assertEquals(3, query().from(product).groupBy(product.price).list(product.price).size());
    }

    @BeforeClass
    public static void doPersist() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            for (int i = 0; i < 10; i++) {
                pm.makePersistent(new Product("C" + i, "F", 200.00, 2));
                pm.makePersistent(new Product("B" + i, "E", 400.00, 4));
                pm.makePersistent(new Product("A" + i, "D", 600.00, 6));
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
