/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mysema.commons.lang.Pair;
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

    private static final QCat cat = QCat.cat;
    
    private static final QCat otherCat = new QCat("otherCat");
        
    private final Date birthDate;    
    
    private final java.sql.Date date;
    
    private Projections projections = new Projections(Module.HQL, getTarget()){
        <A> Collection<Expr<?>> list(EList<A> expr, EList<A> other, A knownElement){
            // NOTE : expr.get(0) is only supported in the where clause
            return Collections.<Expr<?>>singleton(expr.size());
        }          
    };
    
    private final List<Cat> savedCats = new ArrayList<Cat>();
    
    private QueryExecution standardTest = new QueryExecution(
            projections, new Filters(projections, Module.HQL, getTarget()), new MatchingFilters(Module.HQL, getTarget())){
        
        @Override
        protected Pair<Projectable, List<Expr<?>>> createQuery() {
            return Pair.of(
                (Projectable)query().from(cat, otherCat),
                Collections.<Expr<?>>emptyList());
        }
        @Override
        protected Pair<Projectable, List<Expr<?>>> createQuery(EBoolean filter) {
            return Pair.of(
                (Projectable)query().from(cat, otherCat).where(filter),
                Collections.<Expr<?>>singletonList(cat.name));
        }              
    };


    private final java.sql.Time time;
    
    {
        Calendar cal = Calendar.getInstance();
        cal.set(2000, 1, 2, 3, 4);
        cal.set(Calendar.MILLISECOND, 0);
        birthDate = cal.getTime();
        date = new java.sql.Date(cal.getTimeInMillis());
        time = new java.sql.Time(cal.getTimeInMillis());
    }
    
    protected HQLQuery catQuery(){
        return query().from(cat);
    }
    
    protected abstract Target getTarget();
    
    protected abstract HQLQuery query();
        
    protected abstract void save(Object entity);

    @Before
    public void setUp(){
        Cat prev = null;
        for (Cat cat : Arrays.asList(
                new Cat("Bob123", 1, 1.0),
                new Cat("Ruth123", 2, 2.0),
                new Cat("Felix123", 3, 3.0),
                new Cat("Allen123", 4, 4.0),
                new Cat("Mary123", 5, 5.0))){
            if (prev != null){
                cat.addKitten(prev);
            }
            cat.setBirthdate(birthDate);
            cat.setDateField(date);
            cat.setTimeField(time);
            save(cat);
            savedCats.add(cat);
            prev = cat;
        }
        
        Cat cat = new Cat("Some",6, 6.0);
        cat.setBirthdate(birthDate);
        save(cat);
        savedCats.add(cat);
    }
    
    @Test
    public void test(){
        Cat kitten = savedCats.get(0);        
        Cat noKitten = savedCats.get(savedCats.size()-1);
        
        standardTest.runArrayTests(cat.kittensArray, otherCat.kittensArray, kitten, noKitten);
        standardTest.runBooleanTests(cat.name.isNull(), otherCat.kittens.isEmpty());
        standardTest.runCollectionTests(cat.kittens, otherCat.kittens, kitten, noKitten);
        standardTest.runDateTests(cat.dateField, otherCat.dateField, date);
        standardTest.runDateTimeTests(cat.birthdate, otherCat.birthdate, birthDate);
        standardTest.runListTests(cat.kittens, otherCat.kittens, kitten, noKitten);
//        standardTest.mapTests(cat.kittensByName, otherCat.kittensByName, "Kitty", kitten);
        
        // int
        standardTest.runNumericCasts(cat.id, otherCat.id, 1);
        standardTest.runNumericTests(cat.id, otherCat.id, 1);
        
        // double
        standardTest.runNumericCasts(cat.bodyWeight, otherCat.bodyWeight, 1.0);
        standardTest.runNumericTests(cat.bodyWeight, otherCat.bodyWeight, 1.0);
        
        standardTest.runStringTests(cat.name, otherCat.name, kitten.getName());
        standardTest.runTimeTests(cat.timeField, otherCat.timeField, time);
        
        standardTest.report();        
    }
    
    @Test
    public void testAggregates(){
        // uniqueResult
        assertEquals(Integer.valueOf(1), catQuery().uniqueResult(cat.id.min()));
        assertEquals(Integer.valueOf(6), catQuery().uniqueResult(cat.id.max()));
        
        // list
        assertEquals(Integer.valueOf(1), catQuery().list(cat.id.min()).get(0));
        assertEquals(Integer.valueOf(6), catQuery().list(cat.id.max()).get(0));
    }
    
    @Test
    public void testDistinctResults(){
        System.out.println("-- list results");
        SearchResults<Date> res = catQuery().limit(2).listResults(cat.birthdate);
        assertEquals(2, res.getResults().size());
        assertEquals(6l, res.getTotal());
        System.out.println();
        
        System.out.println("-- list distinct results"); 
        res = catQuery().limit(2).listDistinctResults(cat.birthdate);
        assertEquals(1, res.getResults().size());
        assertEquals(1l, res.getTotal());        
        System.out.println();
        
        System.out.println("-- list distinct");
        assertEquals(1, catQuery().listDistinct(cat.birthdate).size());
    }
    
    @Test
    public void testStringOperations(){
        // startsWith
        assertEquals(1, catQuery().where(cat.name.startsWith("R")).count());
        assertEquals(0, catQuery().where(cat.name.startsWith("X")).count());
        assertEquals(1, catQuery().where(cat.name.startsWith("r",false)).count());
        
        // endsWith
        assertEquals(1, catQuery().where(cat.name.endsWith("h123")).count());                    
        assertEquals(0, catQuery().where(cat.name.endsWith("X")).count());
        assertEquals(1, catQuery().where(cat.name.endsWith("H123",false)).count());
        
        // contains
        assertEquals(1, catQuery().where(cat.name.contains("eli")).count());
                
        // indexOf
        assertEquals(Integer.valueOf(0), catQuery().where(cat.name.eq("Bob123")).uniqueResult(cat.name.indexOf("B")));
        assertEquals(Integer.valueOf(1), catQuery().where(cat.name.eq("Bob123")).uniqueResult(cat.name.indexOf("o")));
        
        // case-sensitivity        
        if (!getTarget().equals(Target.MYSQL)){ // NOTE : locate in MYSQL in case-insensitive
            assertEquals(0, catQuery().where(cat.name.startsWith("r")).count());
            assertEquals(0, catQuery().where(cat.name.endsWith("H123")).count());
            assertEquals(Integer.valueOf(2), catQuery().where(cat.name.eq("Bob123")).uniqueResult(cat.name.indexOf("b")));
        }
                
    }
        

}
