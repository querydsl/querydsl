/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mysema.commons.lang.Pair;
import com.mysema.query.jdoql.AbstractJDOTest;
import com.mysema.query.jdoql.JDOQLQuery;
import com.mysema.query.jdoql.testdomain.Product;
import com.mysema.query.jdoql.testdomain.QProduct;
import com.mysema.query.jdoql.testdomain.QStore;
import com.mysema.query.jdoql.testdomain.Store;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

public class JDOQLQueryStandardTest extends AbstractJDOTest {
    
    private static final Date publicationDate;
    
    private static final java.sql.Date date;
    
    private static final java.sql.Time time;
    
    static{
        Calendar cal = Calendar.getInstance();
        cal.set(2000, 1, 2, 3, 4);
        cal.set(Calendar.MILLISECOND, 0);
        publicationDate = cal.getTime();
        date = new java.sql.Date(cal.getTimeInMillis());
        time = new java.sql.Time(cal.getTimeInMillis());
    }
    
    private static String productName = "ABCD";
    private static String otherName = "ABC0";
    
    
    @BeforeClass
    public static void doPersist() {
        // Persistence of a Product and a Book.
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            for (int i = 0; i < 10; i++) {
                // Product instances
                pm.makePersistent(new Product("ABC" + i, "F" + i, i * 200.00, 2, publicationDate));
                pm.makePersistent(new Product("DEF" + i, "E" + i, i * 200.00, 4, publicationDate));
                pm.makePersistent(new Product("GHI" + i, "D" + i, i * 200.00, 6, publicationDate));
                
                // Product of Store
                Product product = new Product(productName,"A",100.0,1, publicationDate);
                pm.makePersistent(product);
                
                // Store instances
                Store store = new Store();
                store.getProducts().add(product);
                store.getProductsByName().put(productName, product);
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
    
    private QueryExecution standardTest = new QueryExecution(Module.JDOQL, Target.HSQLDB){        
        @Override
        protected Pair<Projectable, List<Expr<?>>> createQuery() {
            return Pair.of(
                (Projectable)query().from(store, product, otherProduct),
                Arrays.<Expr<?>>asList(store, product, otherProduct));
        }
        @Override
        protected Pair<Projectable, List<Expr<?>>> createQuery(EBoolean filter) {
            return Pair.of(
                (Projectable)query().from(store, product, otherProduct).where(filter),
                Arrays.<Expr<?>>asList(store, product, otherProduct));                
        }        
    };
    
    private QProduct product = QProduct.product;
    
    private QProduct otherProduct = new QProduct("otherProduct");
    
    private QStore store = QStore.store;
    
    private QStore otherStore = new QStore("otherStore");
    
    @Test
    public void test(){        
        Product p = query().from(product).where(product.name.eq(productName)).limit(1).uniqueResult(product);
        Product p2 = query().from(product).where(product.name.startsWith(otherName)).limit(1).uniqueResult(product);
        standardTest.noProjections();
        standardTest.noCounts();
                
        standardTest.runBooleanTests(product.name.isNull(), otherProduct.price.lt(10.00));
        standardTest.runCollectionTests(store.products, otherStore.products, p, p2);
        standardTest.runDateTests(product.dateField, otherProduct.dateField, date);
        standardTest.runDateTimeTests(product.publicationDate, otherProduct.publicationDate, publicationDate);
        // NO list support in JDOQL
//        testData.listTests(store.products, otherStore.products, p);
        standardTest.runMapTests(store.productsByName, otherStore.productsByName, productName, p, "X", p2);
        standardTest.runNumericCasts(product.price, otherProduct.price, 200.0);
        standardTest.runNumericTests(product.amount, otherProduct.amount, 2);
        standardTest.runStringTests(product.name, otherProduct.name, productName);
        standardTest.runTimeTests(product.timeField, otherProduct.timeField, time);
        
        standardTest.report();        
    }
    

    
}