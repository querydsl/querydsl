/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;

import com.mysema.query.alias.GrammarWithAlias;
import com.mysema.query.collections.Domain.Cat;
import com.mysema.query.collections.Domain.QCat;
import com.mysema.query.collections.support.SimpleIndexSupport;
import com.mysema.query.collections.support.SimpleIteratorSource;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * AbstractQueryTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class AbstractQueryTest {
    
    protected Cat c1 = new Cat("Kitty");
    
    protected Cat c2 = new Cat("Bob");
    
    protected Cat c3 = new Cat("Alex");
    
    protected Cat c4 = new Cat("Francis");
        
    protected QCat cat = new QCat("cat");
    
    protected List<Cat> cats = Arrays.asList(c1, c2, c3, c4);
    
    protected List<Integer> ints = new ArrayList<Integer>();
    
    protected TestQuery last;
    
    protected QCat mate = new QCat("mate");
    
    protected List<Integer> myInts = new ArrayList<Integer>();
    
    protected QCat offspr = new QCat("offspr");
    
    protected QCat otherCat = new QCat("otherCat");
    
    
    @Before 
    public void setUp(){
        myInts.addAll(Arrays.asList(1,2,3,4));              
        GrammarWithAlias.resetAlias();
    }
    
    protected List<EBoolean> conditionsFor1Source = Arrays.asList(
            cat.name.eq("Kate5"),
            cat.name.eq("Kate5").not(),
            cat.name.like("Kate5%"),
            cat.bodyWeight.eq(0),
            
            // and            
            cat.bodyWeight.eq(0).and(cat.name.eq("Kate5")),
            cat.bodyWeight.eq(0).not().and(cat.name.eq("Kate5")),
            cat.bodyWeight.eq(0).and(cat.name.eq("Kate5").not()),
            
            // or            
            cat.bodyWeight.eq(0).or(cat.name.eq("Kate5")),
            cat.bodyWeight.eq(0).not().or(cat.name.eq("Kate5")),
            cat.bodyWeight.eq(0).or(cat.name.eq("Kate5").not())
    );
    
    protected List<EBoolean> conditionsFor2Sources = Arrays.asList(
            cat.ne(otherCat),
            cat.eq(otherCat),                                  
            cat.name.eq(otherCat.name),
            
            // and
            cat.name.eq(otherCat.name).and(otherCat.name.eq("Kate5")),
            cat.name.eq(otherCat.name).not().and(otherCat.name.eq("Kate5")),
            cat.name.eq(otherCat.name).and(otherCat.name.eq("Kate5").not()),
            cat.name.ne(otherCat.name).and(otherCat.name.eq("Kate5")),
            cat.name.ne(otherCat.name).and(otherCat.name.like("Kate5%")),
            cat.bodyWeight.eq(0).and(otherCat.name.eq("Kate5")),
            cat.name.like("Bob5%").and(otherCat.name.like("Kate5%")),
            
            // or
            cat.name.eq(otherCat.name).or(otherCat.name.eq("Kate5")),
            cat.name.eq(otherCat.name).or(otherCat.name.eq("Kate5").not()),
            cat.name.ne(otherCat.name).or(otherCat.name.eq("Kate5")),            
            cat.name.ne(otherCat.name).or(otherCat.name.like("%ate5")),
            cat.bodyWeight.eq(0).or(otherCat.name.eq("Kate5")),
            cat.bodyWeight.eq(0).or(cat.name.eq("Kate5")),
            cat.bodyWeight.gt(0).and(otherCat.name.eq("Bob5")).or(cat.name.eq("Kate5")),
            cat.bodyWeight.gt(0).and(otherCat.name.eq(cat.name)).or(cat.name.eq("Kate5")),
            cat.bodyWeight.gt(0).or(otherCat.name.eq(cat.name)),
            cat.bodyWeight.eq(0).not().or(otherCat.name.eq("Kate5")),
            cat.bodyWeight.eq(0).or(otherCat.name.eq("Kate5").not()),
            cat.name.like("Bob5%").or(otherCat.name.like("%ate5"))      
    );
        
    protected List<Cat> cats(int size){
        List<Cat> cats = new ArrayList<Cat>(size);
        for (int i= 0; i < size / 2; i++){
            cats.add(new Cat("Kate" + (i+1)));
            cats.add(new Cat("Bob"+ (i+1)));
        }
        return cats;
    }
    
    protected TestQuery query(){
        last = new TestQuery();
        return last;
    }

    static class ColQueryWithoutIndexing extends ColQuery{
        @Override
        protected QueryIndexSupport createIndexSupport(Map<Expr<?>, Iterable<?>> exprToIt, JavaOps ops, List<Expr<?>> sources){
            return new SimpleIndexSupport(new SimpleIteratorSource(exprToIt), ops, sources);
        }
    }
    
    static class TestQuery extends AbstractColQuery<TestQuery>{
        List<Object> res = new ArrayList<Object>();
        public <RT> void sel(Expr<RT> projection){
            for (Object o : list(projection)){
                System.out.println(o);
                res.add(o);
            }
            System.out.println();
        }
        public <RT> void selelect(Expr<RT> p1, Expr<RT> p2, Expr<RT>... rest){
            for (Object[] o : list(p1, p2, rest)){
                System.out.println(Arrays.asList(o));
                res.add(o);
            }
            System.out.println();
        }
    }
}
