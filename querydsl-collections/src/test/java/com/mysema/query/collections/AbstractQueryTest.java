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

import com.mysema.query.collections.Domain.Cat;
import com.mysema.query.collections.Domain.QCat;
import com.mysema.query.collections.support.SimpleIndexSupport;
import com.mysema.query.collections.support.SimpleIteratorSource;
import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;

/**
 * AbstractQueryTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class AbstractQueryTest {
    protected Cat c1 = new Cat("Kitty");
    protected Cat c2 = new Cat("Bob");
    protected Cat c3 = new Cat("Alex");
    protected Cat c4 = new Cat("Francis");
    
    protected QCat cat = new QCat("cat");
    
    protected List<Cat> cats = Arrays.asList(c1, c2, c3, c4);
    
    protected List<Integer> ints = new ArrayList<Integer>();
    
    protected List<Integer> myInts = new ArrayList<Integer>();
    
    protected QCat mate = new QCat("mate");
    protected QCat offspr = new QCat("offspr");
    protected QCat otherCat = new QCat("otherCat");
    
    protected List<Cat> cats(int size){
        List<Cat> cats = new ArrayList<Cat>(size);
        for (int i= 0; i < size / 2; i++){
            cats.add(new Cat("Kate" + (i+1)));
            cats.add(new Cat("Bob"+ (i+1)));
        }
        return cats;
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

    class ColQueryWithoutIndexing extends ColQuery{
        @Override
        protected QueryIndexSupport createIndexSupport(Map<Expr<?>, Iterable<?>> exprToIt, JavaOps ops, List<Expr<?>> sources){
            return new SimpleIndexSupport(new SimpleIteratorSource(exprToIt), ops, sources);
        }
    }
}
