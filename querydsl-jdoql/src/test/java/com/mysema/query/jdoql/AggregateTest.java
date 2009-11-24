package com.mysema.query.jdoql;

import static org.junit.Assert.assertEquals;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mysema.query.jdoql.testdomain.Product;
import com.mysema.query.jdoql.testdomain.QProduct;

public class AggregateTest extends AbstractJDOTest{
    
    private final QProduct product = QProduct.product;
    
    @Test
    public void test() {        
        double min = 200.00, avg = 400.00, max = 600.00;
        
        // uniqueResult
        assertEquals(Double.valueOf(min), query().from(product).uniqueResult(product.price.min()));
        assertEquals(Double.valueOf(avg), query().from(product).uniqueResult(product.price.avg()));
        assertEquals(Double.valueOf(max), query().from(product).uniqueResult(product.price.max()));
        
        // list
        assertEquals(Double.valueOf(min), query().from(product).list(product.price.min()).get(0));
        assertEquals(Double.valueOf(avg), query().from(product).list(product.price.avg()).get(0));
        assertEquals(Double.valueOf(max), query().from(product).list(product.price.max()).get(0));
    }
    
    @BeforeClass
    public static void doPersist() {
        // Persistence of a Product and a Book.
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
