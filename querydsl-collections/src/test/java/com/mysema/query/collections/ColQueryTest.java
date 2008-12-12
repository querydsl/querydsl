/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.mysema.query.collections.Domain.Cat;
import com.mysema.query.collections.Domain.QCat;
import com.mysema.query.grammar.types.Expr;


/**
 * ColQueryTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class ColQueryTest {
    Cat c1 = new Cat("Kitty");
    Cat c2 = new Cat("Bob");
    Cat c3 = new Cat("Alex");
    Cat c4 = new Cat("Francis");
    
    QCat cat = new QCat("cat");
    QCat otherCat = new QCat("otherCat");
    
    TestQuery last;
            
    @Test
    public void testSimpleCases(){
        // select all cat names
        query().from(cat, c1, c2, c3, c4).select(cat.name);
        assertTrue(last.res.size() == 4);
        
        // select all kittens
        query().from(cat, c1, c2, c3, c4).select(cat.kittens);
        assertTrue(last.res.size() == 4);
        
        // select cats with kittens
        query().from(cat, c1, c2, c3, c4).where(cat.kittens.size().gt(0)).select(cat.name);
        assertTrue(last.res.size() == 4);
                
        // select cats named Kitty
        query().from(cat, c1, c2, c3, c4).where(cat.name.eq("Kitty")).select(cat.name);
        assertTrue(last.res.size() == 1);
        
        // select cats named Kitt%
        query().from(cat, c1, c2, c3, c4).where(cat.name.like("Kitt%")).select(cat.name);
        assertTrue(last.res.size() == 1);        
    }
    
    @Test
    public void testPrimitives(){
        // select cats with kittens
        query().from(cat, c1, c2, c3, c4).where(cat.kittens.size().ne(0)).select(cat.name);
        assertTrue(last.res.size() == 4);
        
        // select cats without kittens
        query().from(cat, c1, c2, c3, c4).where(cat.kittens.size().eq(0)).select(cat.name);
        assertTrue(last.res.size() == 0);
    }
    
    @Test
    public void testArrayProjection(){
        // select pairs of cats with different names
        query().from(cat, c1, c2, c3, c4).from(otherCat, c1, c2, c3, c4)
            .where(cat.name.ne(otherCat.name)).select(cat.name, otherCat.name);
        assertTrue(last.res.size() == 4 * 3);
    }
    
    @Test
    public void testJoins(){
        // TODO
        // innerJoin
        // leftJoin
    }
    
    @Test
    public void testAggregation(){
        // TODO
        // groupBy
        // having
    }
    
    private TestQuery query(){
        last = new TestQuery();
        return last;
    }
    
    private static class TestQuery extends ColQuery<TestQuery>{
        List<Object> res = new ArrayList<Object>();
        <RT> void select(final Expr<RT> projection){
            for (Object o : iterate(projection)){
                System.out.println(o);
                res.add(o);
            }
            System.out.println();
        }
        void select(final Expr<?> p1, final Expr<?> p2, final Expr<?>... rest){
            for (Object[] o : iterate(p1, p2, rest)){
                System.out.println(Arrays.asList(o));
                res.add(o);
            }
            System.out.println();
        }
    }
    

}
