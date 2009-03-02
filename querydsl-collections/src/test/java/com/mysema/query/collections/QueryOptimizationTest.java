/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.*;

import org.junit.Test;

import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.collections.Domain.Cat;
import com.mysema.query.collections.comparators.JoinExpressionComparator;
import com.mysema.query.collections.iterators.FilteringMultiIterator;
import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;

/**
 * QueryOptimizationTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class QueryOptimizationTest extends AbstractQueryTest{
    
    private long testIterations = 40;
    
    private List<String> resultLog = new ArrayList<String>(30);
    
    private EBoolean where = cat.name.eq(otherCat.name).and(otherCat.name.eq("Kate5"));
    
    @Test
    public void testOptimizationOptions(){
        ColQuery query = new ColQuery();
        query.setWrapIterators(false);
        query.from(cat, cats).list(cat);
        
        query = new ColQuery();
        query.setSortSources(false);
        query.from(cat, cats).list(cat);
        
        query = new ColQuery();
        query.setWrapIterators(false);
        query.setSortSources(false);
        query.from(cat, cats).list(cat);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testOrderingPenalty(){
        Map<Expr<?>,Iterable<?>> exprToIt = new HashMap<Expr<?>,Iterable<?>>();
        exprToIt.put(cat, Collections.emptySet());
        exprToIt.put(otherCat, Collections.emptySet());
        
        long total = 0;
        for (long i = 0; i < testIterations; i++){
            List<JoinExpression<?>> joins = Arrays.<JoinExpression<?>>asList(new JoinExpression(JoinType.DEFAULT, cat), new JoinExpression(JoinType.DEFAULT, otherCat));
            long start = System.currentTimeMillis();
            JoinExpressionComparator comp = new JoinExpressionComparator(where, exprToIt);
            Collections.sort(joins, comp);
            total += (System.currentTimeMillis() - start);
        }
        System.out.println("Comparison took " + (total / testIterations)+" ms" );
    }
    

    @Test
    public void testElementOrder(){
        int size = 100000;
        int iterations = 40;
        
        List<Cat> cats1 = new ArrayList<Cat>(size);
        for (int i= 0; i < size; i++){
            cats1.add(new Cat("Bob" + i));
        }
        List<Cat> cats2 = new ArrayList<Cat>(size);
        for (int i=0; i < size; i++){
            cats2.add(new Cat("Kate" + i));
        }
        
        EBoolean where = cat.name.eq(otherCat.name).and(otherCat.name.eq("Kate5"));
        FilteringMultiIterator it;
        
        long total = 0;
        for (int i = 0; i < iterations; i++){
            it = new FilteringMultiIterator(new JavaOps(), where);
            it.add(cat, cats1);
            it.add(otherCat, cats2);
            it.init();
            long start = System.currentTimeMillis();
            while (it.hasNext()){
                it.next();
            }   
            total += System.currentTimeMillis() - start;
        }        
        System.out.println("cat, otherCat : " + (total / iterations) + " ms");
        
        // 2
        total = 0;
        for (int i = 0; i < iterations; i++){
            it = new FilteringMultiIterator(new JavaOps(), where);
            it.add(otherCat, cats2);
            it.add(cat, cats1);        
            it.init();
            long start = System.currentTimeMillis();
            while (it.hasNext()){
                it.next();
            }
            total += System.currentTimeMillis() - start;
        }        
        System.out.println("otherCat, cat : " + (total / iterations) + " ms");
    }
    
    public void longTest(){
        // initialization query
        ColQuery query = new ColQuery();
        query.from(cat, cats).from(otherCat, cats).where(where).list(cat);
        
        runTest(100);
        runTest(500);        
        runTest(1000);       
        runTest(5000);        
        runTest(10000);
        runTest(50000);        
        
        for (String line : resultLog){
            System.out.println(line);
        }
    }
    
    private void runTest(int size){        
        long level1 = 0, level2 = 0, level3 = 0;
        List<Cat> cats1 = new ArrayList<Cat>(size);
        for (int i= 0; i < size; i++){
            cats1.add(new Cat("Bob" + i));
        }
        List<Cat> cats2 = new ArrayList<Cat>(size);
        for (int i=0; i < size; i++){
            cats2.add(new Cat("Kate" + i));
        }
        ColQuery query;
        for (long j=0; j < testIterations; j++){            
            // without wrapped iterators
            query = new ColQuery();
            query.setWrapIterators(false);                       
            level1 += query(query, cats1, cats2);
            
            // without reordering
            query = new ColQuery();
            query.setSortSources(false);            
            level2 += query(query, cats1, cats2);
            
            // with reordering and iterator wrapping
            query = new ColQuery();            
            level3 += query(query, cats1, cats2);            
            
        }
        resultLog.add(size + " items");
        resultLog.add(" unfiltered query took              " + (level1 / testIterations) +" ms");
        resultLog.add(" filtered, but unordered query took " + (level2 / testIterations) +" ms");
        resultLog.add(" filtered and ordered query took    " + (level3 / testIterations) +" ms");
        resultLog.add("");
    }
    
    private long query(ColQuery query, List<Cat> cats1, List<Cat> cats2){
        long start = System.currentTimeMillis();
        Iterator<Cat> it = query.from(cat, cats1).from(otherCat, cats2).where(where).iterate(cat).iterator();
        while (it.hasNext()){
            it.next();
        }
        return System.currentTimeMillis() - start;
    }
    
    public static void main(String[] args){
        new QueryOptimizationTest().longTest();
    }

}
