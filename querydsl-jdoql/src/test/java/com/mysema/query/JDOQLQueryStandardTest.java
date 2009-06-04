package com.mysema.query;

import static org.junit.Assert.assertTrue;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.jdoql.AbstractJDOTest;
import com.mysema.query.jdoql.testdomain.Product;
import com.mysema.query.jdoql.testdomain.QProduct;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;

public class JDOQLQueryStandardTest extends AbstractJDOTest{
    
    private QProduct product = QProduct.product;
    
    private QProduct otherProduct = new QProduct("otherProduct");
    
    @Test
    @Ignore
    public void stringProjections(){               
        for (EString str : TestExprs.getProjectionsForString(product.name, otherProduct.name, "C5")){
            System.out.println(str);
            query().from(product, otherProduct).list(str, product.name, otherProduct.name);
        }
    }
    
    @Test
    @Ignore
    public void testNumericProjections(){
        for (ENumber<?> num : TestExprs.getProjectionsForNumber(product.price, otherProduct.price, 200.0)){
            System.out.println(num);
            query().from(product).from(otherProduct).list(num, product.price, otherProduct.price);
        }
    }
    
    @Test
    public void stringFilters(){
        for (EBoolean f : TestFilters.getFiltersForString(product.name, otherProduct.name, "C5")){
            System.out.println(f);
            query().from(product, otherProduct).where(f).list(product.name, otherProduct.name);
        }
    }
    
    @Test
    public void matchingStringFilters(){
        for (EBoolean f : TestFilters.getMatchingFilters(product.name, otherProduct.name, "C5")){
            System.out.println(f);
            assertTrue(!query().from(product, otherProduct).where(f).list(product.name, otherProduct.name).isEmpty());
        }
    }
    
    @Test
    @Ignore
    public void numericFilters(){
     // FIXME
        for (EBoolean f : TestFilters.getFiltersForNumber(product.price, otherProduct.price, 200.0)){
            System.out.println(f);
            query().from(product, otherProduct).where(f).list(product.name, otherProduct.name);
        }
    }
    
    @Test    
    public void matchingNumericFilters(){        
        for (EBoolean f : TestFilters.getMatchingFilters(product.price, otherProduct.price, 200.0)){
            System.out.println(f);
            assertTrue(!query().from(product, otherProduct).where(f).list(product.name, otherProduct.name).isEmpty());
        }
    }
    
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