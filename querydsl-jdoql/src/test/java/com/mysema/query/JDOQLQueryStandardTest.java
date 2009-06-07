/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
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
    
    @Test 
    public void booleanFilters() {
        for (EBoolean f : StandardTestData.booleanFilters(product.name.isNull(), otherProduct.price.lt(10.00))){
            System.out.println(f);
            query().from(product, otherProduct).where(f).list(product, otherProduct);
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
        // some valid numeric expressions are not supported in projections
        for (ENumber<?> num : StandardTestData.numericProjections(product.price, otherProduct.price, 200.0)){
            System.out.println(num);
            query().from(product).from(otherProduct).list(num, product.price, otherProduct.price);
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
        // commented out since lots of valid String expressions aren't allowed in projections
        for (EString str : StandardTestData.stringProjections(product.name, otherProduct.name, "C5")){
            System.out.println(str);
            query().from(product, otherProduct).list(str, product.name, otherProduct.name);
        }
    }

    @Test
    public void listProjections() {
        // TODO Auto-generated method stub       
    }

    @Test
    public void mapProjections() {
        // TODO Auto-generated method stub        
    }

    @Test
    public void listFilters() {
        // TODO Auto-generated method stub        
    }

    @Test
    public void mapFilters() {
        // TODO Auto-generated method stub        
    }

}