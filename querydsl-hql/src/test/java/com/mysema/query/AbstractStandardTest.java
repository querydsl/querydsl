/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.hql.HQLQuery;
import com.mysema.query.hql.domain.Cat;
import com.mysema.query.hql.domain.QCat;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EList;
import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 *
 */
public abstract class AbstractStandardTest {
      
    private static QCat cat = new QCat("cat");
    
    private static QCat otherCat = new QCat("otherCat");
    
    private List<Cat> savedCats = new ArrayList<Cat>();
    
    private StandardTest standardTest = new StandardTest(new StandardTestData(){        
        <A> Collection<Expr<?>> listProjections(EList<A> expr, EList<A> other, A knownElement){
            // NOTE : expr.get(0) is only supported in the where clause
            return Collections.<Expr<?>>singleton(expr.size());
        }        
    }){
        @Override
        public int executeFilter(EBoolean f){
            return query().from(cat, otherCat).where(f).list(cat.name).size();
        }
        @Override
        public int executeProjection(Expr<?> pr){
            return query().from(cat, otherCat).list(pr).size();
        }              
    };
    
    protected abstract HQLQuery query();
    
    protected HQLQuery catQuery(){
        return query().from(cat);
    }
        
    protected abstract void save(Object entity);

    @Before
    public void setUp(){
        Cat prev = null;
        for (Cat cat : Arrays.asList(
                new Cat("Bob", 1),
                new Cat("Ruth", 2),
                new Cat("Felix", 3),
                new Cat("Allen", 4),
                new Cat("Mary", 5))){
            if (prev != null){
                cat.getKittens().add(prev);
            }
            save(cat);
            savedCats.add(cat);
            prev = cat;
        }
        
        Cat cat = new Cat("Some",6);
        save(cat);
        savedCats.add(cat);
    }
    
    @Test
    public void test(){
        Cat kitten = savedCats.get(0);        
        Cat noKitten = savedCats.get(savedCats.size()-1);
        
        standardTest.booleanTests(cat.name.isNull(), otherCat.kittens.isEmpty());
        standardTest.collectionTests(cat.kittens, otherCat.kittens, kitten, noKitten);
//        standardTest.dateTests(null, null, null);
        standardTest.dateTimeTests(cat.birthdate, otherCat.birthdate, new Date());
        standardTest.listTests(cat.kittens, otherCat.kittens, kitten, noKitten);
//        standardTest.mapTests(cat.kittensByName, otherCat.kittensByName, "Kitty", kitten);
        standardTest.numericCasts(cat.id, otherCat.id, 1);
        standardTest.numericTests(cat.id, otherCat.id, 1);
        standardTest.stringTests(cat.name, otherCat.name, "Bob");
//        standardTest.timeTests(null, null, null);
        
        standardTest.report();        
    }
    
    @Test
    public void testStringOperations(){
        // startsWith
        assertEquals(1, catQuery().where(cat.name.startsWith("R")).count());
        assertEquals(0, catQuery().where(cat.name.startsWith("r")).count());
        assertEquals(1, catQuery().where(cat.name.startsWith("r",false)).count());
        
        // endsWith
        assertEquals(1, catQuery().where(cat.name.endsWith("h")).count());
        assertEquals(0, catQuery().where(cat.name.endsWith("H")).count());
        assertEquals(1, catQuery().where(cat.name.endsWith("H",false)).count());
        
        // contains
        assertEquals(1, catQuery().where(cat.name.contains("eli")).count());
                
        // indexOf
        assertEquals(Integer.valueOf(0), catQuery().where(cat.name.eq("Bob")).uniqueResult(cat.name.indexOf("B")));
        assertEquals(Integer.valueOf(1), catQuery().where(cat.name.eq("Bob")).uniqueResult(cat.name.indexOf("o")));
        assertEquals(Integer.valueOf(2), catQuery().where(cat.name.eq("Bob")).uniqueResult(cat.name.indexOf("b")));
        
    }
        

}
