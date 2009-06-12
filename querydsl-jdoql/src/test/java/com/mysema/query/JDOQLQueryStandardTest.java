/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.jdoql.AbstractJDOTest;
import com.mysema.query.jdoql.testdomain.Product;
import com.mysema.query.jdoql.testdomain.QProduct;
import com.mysema.query.jdoql.testdomain.QStore;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;

public class JDOQLQueryStandardTest extends AbstractJDOTest implements StandardTest{
    
    @BeforeClass
    public static void doPersist() {
        // Persistence of a Product and a Book.
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            for (int i = 0; i < 10; i++) {
                pm.makePersistent(new Product("C" + i, "F" + i, i * 200.00, 2));
                pm.makePersistent(new Product("B" + i, "E" + i, i * 200.00, 4));
                pm.makePersistent(new Product("A" + i, "D" + i, i * 200.00, 6));
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
    
    private QProduct product = QProduct.product;
    
    private QProduct otherProduct = new QProduct("otherProduct");
    
    private QStore store = QStore.store;
    
    private QStore otherStore = new QStore("otherStore");
    
    @Test 
    public void booleanFilters() {
        for (EBoolean f : StandardTestData.booleanFilters(product.name.isNull(), otherProduct.price.lt(10.00))){
            System.out.println(f);
            query().from(product, otherProduct).where(f).list(product, otherProduct);
        }
    }
    
    @Test
    public void collectionFilters() {
        Product p = query().from(product).limit(1).uniqueResult(product);
        for (EBoolean f : StandardTestData.collectionFilters(store.products, otherStore.products, p)){
            System.out.println(f);
            query().from(store, otherStore).where(f).list(store.name, otherStore.name);
        }        
    }
    
    @Test
    @Ignore
    public void collectionProjections() {
        //The expression ContainerSizeExpression "(SELECT COUNT(*) FROM STORE_PRODUCTS THIS_PRODUCTS WHERE THIS_PRODUCTS.STORE_ID_OID = THIS.STORE_ID)" is not supported in results.
        Product p = query().from(product).limit(1).uniqueResult(product);
        for (Expr<?> pr : StandardTestData.collectionProjections(store.products, otherStore.products, p)){
            System.out.println(pr);
            query().from(store, otherStore).list(pr, store.name, otherStore.name);
        }
    }
    
    @Test
    @Ignore
    public void dateProjections() {
        // TODO Auto-generated method stub
        
    }
    
    @Test
    public void dateTimeFilters() {
        for (EBoolean f : StandardTestData.dateTimeFilters(product.publicationDate, otherProduct.publicationDate, new Date())){
            System.out.println(f);
            query().from(product, otherProduct).where(f).list(product.name, otherProduct.name);
        }           
    }
    
    @Test
    @Ignore
    public void dateTimeProjections() {
        // valid expressions are not allowed in projections
        for (Expr<?> pr : StandardTestData.dateTimeProjections(product.publicationDate, otherProduct.publicationDate, new Date())){
            System.out.println(pr);
            query().from(product, otherProduct).list(pr, product.name, otherProduct.name);
        }          
    }
    
    @Test
    public void listFilters() {
//        Product p = query().from(product).limit(1).uniqueResult(product);
//        for (EBoolean f : StandardTestData.listFilters(store.products, otherStore.products, p)){
//            System.out.println(f);
//            query().from(store, otherStore).where(f).list(store.name, otherStore.name);
//        }        
    }
    
    @Test
    @Ignore
    public void listProjections() {
        // java.util.List#get(int) is not supported
//        Product p = query().from(product).limit(1).uniqueResult(product);
//        for (Expr<?> pr : StandardTestData.listProjections(store.products, otherStore.products, p)){
//            System.out.println(pr);
//            query().from(store, otherStore).list(pr, store.name, otherStore.name);
//        }
    }
    
    @Test
    public void mapFilters() {
        Product p = query().from(product).limit(1).uniqueResult(product);
        for (EBoolean f : StandardTestData.mapFilters(store.productsByName, otherStore.productsByName, "A0", p)){
            System.out.println(f);
            query().from(store, otherStore).where(f).list(store.name, otherStore.name);
        }        
    }

    @Test
    @Ignore
    public void mapProjections() {
        // valid expressions are not allowed in projections
        Product p = query().from(product).limit(1).uniqueResult(product);
        for (Expr<?> pr : StandardTestData.mapProjections(store.productsByName, otherStore.productsByName, "A0", p)){
            System.out.println(pr);
            query().from(store, otherStore).list(pr, store.name, otherStore.name);
        }        
    }

    @Test
    @Ignore
    public void numericCasts(){
        // FIXME : numeric casts fail for some reason
        for (ENumber<?> num : StandardTestData.numericCasts(product.price, otherProduct.price, 200.0)){
            System.out.println(num);
            query().from(product).from(otherProduct).list(num, product.price, otherProduct.price);
        }
    }

    @Test
    public void numericFilters(){
        for (EBoolean f : StandardTestData.numericFilters(product.amount, otherProduct.amount, 2)){
            System.out.println(f);
            query().from(product, otherProduct).where(f).list(product.name, otherProduct.name);
        }
    }

    @Test    
    public void numericMatchingFilters(){        
        for (EBoolean f : StandardTestData.numericMatchingFilters(product.amount, otherProduct.amount, 2)){
            System.out.println(f);
            assertTrue(!query().from(product, otherProduct).where(f).list(product.name, otherProduct.name).isEmpty());
        }
    }

    @Test
    @Ignore
    public void numericProjections(){
        // valid expressions are not allowed in projections
        for (ENumber<?> num : StandardTestData.numericProjections(product.price, otherProduct.price, 200.0)){
            System.out.println(num);
            query().from(product).from(otherProduct).list(num, product.price, otherProduct.price);
        }
    }
    
    @Test
    public void stringFilters(){
        for (EBoolean f : StandardTestData.stringFilters(product.name, otherProduct.name, "C5")){
            System.out.println(f);
            query().from(product, otherProduct).where(f).list(product.name, otherProduct.name);
        }
    }

    @Test
    public void stringMatchingFilters(){
        for (EBoolean f : StandardTestData.stringMatchingFilters(product.name, otherProduct.name, "C5")){
            System.out.println(f);
            assertTrue(!query().from(product, otherProduct).where(f).list(product.name, otherProduct.name).isEmpty());
        }
    }
    
    @Test
    @Ignore
    public void stringProjections(){   
        // valid expressions are not allowed in projections
        for (EString str : StandardTestData.stringProjections(product.name, otherProduct.name, "C5")){
            System.out.println(str);
            query().from(product, otherProduct).list(str, product.name, otherProduct.name);
        }
    }

    @Test
    @Ignore
    public void timeProjections() {
        // TODO Auto-generated method stub
        
    }
    
}