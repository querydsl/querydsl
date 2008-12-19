/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static com.mysema.query.collections.MiniApi.str;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
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
    
    List<Cat> cats = Arrays.asList(c1, c2, c3, c4);
    
    QCat cat = new QCat("cat");    
    QCat mate = new QCat("mate");
    QCat offspr = new QCat("offspr");
    QCat otherCat = new QCat("otherCat");
    
    TestQuery last;
         
    @Test
    public void testAlias(){
        query().from(cat, c1, c2).from(otherCat, c2, c3)
            .where(cat.name.eq(otherCat.name)).select(cat.name); 
        
    }
    
    @Test
    public void testMiniApiUsage(){        
        for (Cat c : MiniApi.select(cats, cat.name.eq("Kitty"))){
            System.out.println(c.getName());
        }
        MiniApi.select(cats, cat.kittens.size().gt(0)).iterator();
        MiniApi.select(cats, cat.mate.isnotnull()).iterator();
        MiniApi.select(cats, cat.alive.and(cat.birthdate.isnotnull())).iterator();       
        MiniApi.select(cats, cat.bodyWeight.lt(cat.weight)).iterator();
        MiniApi.select(cats, cat.color.isnull().or(cat.eyecolor.eq(cat.color))).iterator();
        MiniApi.select(cats, cat.bodyWeight.between(1, 2)).iterator();
        
        // from where order
        MiniApi.select(cats, cat.name.eq("Kitty"), cat.name.asc()).iterator();
    }
    
    @Test
    public void testCSVIteration(){
        // 1st level
        List<String> lines = Arrays.asList("1;10;100","2;20;200","3;30;300");
        for (String[] row : query().from(str, lines).iterate(str.split(";"))){
            for (String col : row){
                System.out.println(col);
            }
        }
    }
    
    @Test
    public void testSimpleCases(){
        // select all cat names
        query().from(cat,cats).select(cat.name);
        assertTrue(last.res.size() == 4);
        
        // select all kittens
        query().from(cat,cats).select(cat.kittens);
        assertTrue(last.res.size() == 4);
        
        // select cats with kittens
        query().from(cat,cats).where(cat.kittens.size().gt(0)).select(cat.name);
        assertTrue(last.res.size() == 4);
                
        // select cats named Kitty
        query().from(cat,cats).where(cat.name.eq("Kitty")).select(cat.name);
        assertTrue(last.res.size() == 1);
        
        // select cats named Kitt%
        query().from(cat,cats).where(cat.name.like("Kitt%")).select(cat.name);
        assertTrue(last.res.size() == 1);        
    }
    
    @Test
    public void testPrimitives(){
        // select cats with kittens
        query().from(cat,cats).where(cat.kittens.size().ne(0)).select(cat.name);
        assertTrue(last.res.size() == 4);
        
        // select cats without kittens
        query().from(cat,cats).where(cat.kittens.size().eq(0)).select(cat.name);
        assertTrue(last.res.size() == 0);
    }
    
    @Test
    public void testArrayProjection(){
        // select pairs of cats with different names
        query().from(cat,cats).from(otherCat,cats)
            .where(cat.name.ne(otherCat.name)).select(cat.name, otherCat.name);
        assertTrue(last.res.size() == 4 * 3);
    }
    
    @Test
    @Ignore
    public void testOrder(){
        // TODO
        query().from(cat,cats).orderBy(cat.name.asc()).select(cat.name);
        assertArrayEquals(new Object[]{"Alex","Bob","Francis","Kitty"}, last.res.toArray());
    }
    
    @Test
    public void testVarious(){

        query().from(cat,cats).select(cat.mate);
        
        query().from(cat,cats).select(cat.kittens);
        
        query().from(cat,cats).where(cat.name.like("fri%")).select(cat.name);
        
//        select(mother, offspr, mate.name).from(mother)
//            .innerJoin(mother.mate.as(mate)).leftJoin(mother.kittens.as(offspr)).parse();

//        select(new QFamily(mother, mate, offspr))
//            .from(mother).innerJoin(mother.mate.as(mate))
//            .leftJoin(mother.kittens.as(offspr)).parse();
    }
    
    private TestQuery query(){
        last = new TestQuery();
        return last;
    }
    
    private class TestQuery extends ColQuery<TestQuery>{
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
