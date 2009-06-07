/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.hql.HQLQuery;
import com.mysema.query.hql.domain.Cat;
import com.mysema.query.hql.domain.QCat;
import com.mysema.query.hql.hibernate.HQLQueryImpl;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;

public abstract class AbstractStandardTest implements StandardTest{
    
    private Session session;
      
    private static QCat cat = new QCat("cat");
    
    private static QCat otherCat = new QCat("otherCat");
    
    private List<Cat> savedCats = new ArrayList<Cat>();
    
    private HQLQuery query(){
        return new HQLQueryImpl(session);
    }
    
    @Before
    public void setUp(){        
        for (Cat cat : Arrays.asList(
                new Cat("Bob", 1),
                new Cat("Ruth", 2),
                new Cat("Felix", 3),
                new Cat("Allen", 4),
                new Cat("Mary", 5))){
            session.save(cat);
            savedCats.add(cat);
        }
    }    
    
    @Test
    public void booleanFilters(){
        for (EBoolean f : StandardTestData.booleanFilters(cat.name.isNull(), otherCat.kittens.isEmpty())){
            System.out.println(f);
            query().from(cat, otherCat).where(f).list(cat.name);
        }
    }
    
    @Test
    public void numericFilters(){
        for (EBoolean f : StandardTestData.numericFilters(cat.id, otherCat.id, 1)){
            System.out.println(f);
            query().from(cat, otherCat).where(f).list(cat.name);
        }
    }
    
    @Test
    public void numericMatchingFilters(){
        for (EBoolean f : StandardTestData.numericMatchingFilters(cat.id, otherCat.id, 1)){
            System.out.println(f);
            assertTrue(f + " failed", !query().from(cat, otherCat).where(f).list(cat.name).isEmpty());
        }
    }
    
    @Test
    public void numericProjections(){
        for (ENumber<?> num : StandardTestData.numericProjections(cat.id, otherCat.id, 1)){
            System.out.println(num);
            query().from(cat, otherCat).list(num);
        }
    }
    
    @Test
    public void numericCasts(){
        for (ENumber<?> num : StandardTestData.numericCasts(cat.id, otherCat.id, 1)){
            System.out.println(num);
            query().from(cat, otherCat).list(num);
        }
    }
   
    @Test
    public void stringFilters(){
        for (EBoolean f : StandardTestData.stringFilters(cat.name, otherCat.name, "Bob")){
            System.out.println(f);
            query().from(cat, otherCat).where(f).list(cat.name);
        }
    }
    
    @Test
    public void stringMatchingFilters(){
        for (EBoolean f : StandardTestData.stringMatchingFilters(cat.name, otherCat.name, "Bob")){
            System.out.println(f);
            assertTrue(f + " failed", !query().from(cat, otherCat).where(f).list(cat.name).isEmpty());
        }
    }
    
    @Test
    public void stringProjections(){               
        for (EString str : StandardTestData.stringProjections(cat.name, otherCat.name, "Bob")){
            System.out.println(str);
            query().from(cat, otherCat).list(str);
        }
    }
    
    @Test
    public void listProjections() {
        for (Expr<?> p : StandardTestData.listProjections(cat.kittens, otherCat.kittens, savedCats.get(0))){
            System.out.println(p);
            query().from(cat, otherCat).list(cat, otherCat, p);
        }        
    }
    
    @Test
    public void listFilters() {
        for (EBoolean f : StandardTestData.listFilters(cat.kittens, otherCat.kittens, savedCats.get(0))){
            System.out.println(f);
            query().from(cat, otherCat).where(f).list(cat.name);
        }        
    }

    @Test
    @Ignore
    public void mapProjections() {
        // TODO Auto-generated method stub        
    }

    @Test
    @Ignore
    public void mapFilters() {
        // TODO Auto-generated method stub
        
    }
    
    public void setSession(Session session) {
        this.session = session;
    }

}
