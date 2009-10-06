/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mysema.query.jdoql.AbstractJDOTest;
import com.mysema.query.jdoql.testdomain.Product;
import com.mysema.query.jdoql.testdomain.QProduct;
import com.mysema.query.jdoql.testdomain.QStore;
import com.mysema.query.jdoql.testdomain.Store;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

public class JDOQLQueryStandardTest extends AbstractJDOTest {
    
    private static Date publicationDate = new Date();
    
//    private static java.sql.Date date = new java.sql.Date(publicationDate.getTime());
    
//    private static java.sql.Time time = new java.sql.Time(publicationDate.getTime());
    
    @BeforeClass
    public static void doPersist() {
        // Persistence of a Product and a Book.
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            for (int i = 0; i < 10; i++) {
                // Product instances
                pm.makePersistent(new Product("C" + i, "F" + i, i * 200.00, 2, publicationDate));
                pm.makePersistent(new Product("B" + i, "E" + i, i * 200.00, 4, publicationDate));
                pm.makePersistent(new Product("A" + i, "D" + i, i * 200.00, 6, publicationDate));
                
                // Product of Store
                Product product = new Product("A","A",100.0,1, publicationDate);
                pm.makePersistent(product);
                
                // Store instances
                Store store = new Store();
                store.getProducts().add(product);
                store.getProductsByName().put("A", product);
                pm.makePersistent(store);
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
    
    private StandardTest standardTest = new StandardTest(){
        @Override
        public int executeFilter(EBoolean f) {
            return query().from(store, product, otherProduct).where(f).list(store, product, otherProduct).size();
        }
        @Override
        public int executeProjection(Expr<?> pr) {
            return query().from(store, product, otherProduct).list(pr, store, product, otherProduct).size();
        }        
    };
    
    private QProduct product = QProduct.product;
    
    private QProduct otherProduct = new QProduct("otherProduct");
    
    private QStore store = QStore.store;
    
    private QStore otherStore = new QStore("otherStore");
    
    @Test
    public void test(){
        Product p = query().from(product).where(product.name.eq("A")).limit(1).uniqueResult(product);
        Product p2 = query().from(product).where(product.name.startsWith("C")).limit(1).uniqueResult(product);
        standardTest.noProjections();
                
        standardTest.booleanTests(product.name.isNull(), otherProduct.price.lt(10.00));
        standardTest.collectionTests(store.products, otherStore.products, p, p2);
//        standardTest.dateTests(product.dateField, otherProduct.dateField, date);
//        standardTest.dateTimeTests(product.publicationDate, otherProduct.publicationDate, publicationDate);
        // NO list support in JDOQL
//        testData.listTests(store.products, otherStore.products, p);
        standardTest.mapTests(store.productsByName, otherStore.productsByName, "A", p, "X", p2);
        standardTest.numericCasts(product.price, otherProduct.price, 200.0);
        standardTest.numericTests(product.amount, otherProduct.amount, 2);
//        standardTest.stringTests(product.name, otherProduct.name, "C5");
        // timeTests too slow and causes OutOfMemoryError
//        standardTest.timeTests(product.timeField, otherProduct.timeField, time);
        
        standardTest.report();        
    }
    

    
}