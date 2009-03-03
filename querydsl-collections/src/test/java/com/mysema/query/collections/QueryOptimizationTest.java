/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.collections.comparators.JoinExpressionComparator;
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
            JoinExpressionComparator comp = new JoinExpressionComparator(where, exprToIt.keySet());
            Collections.sort(joins, comp);
            total += (System.currentTimeMillis() - start);
        }
        System.out.println("Comparison took " + (total / testIterations)+" ms" );
    }
    

//    @Test
//    public void testElementOrder(){
//        int size = 100000;
//        int iterations = 40;
//        
//        List<Cat> cats1 = new ArrayList<Cat>(size);
//        for (int i= 0; i < size; i++){
//            cats1.add(new Cat("Bob" + i));
//        }
//        List<Cat> cats2 = new ArrayList<Cat>(size);
//        for (int i=0; i < size; i++){
//            cats2.add(new Cat("Kate" + i));
//        }
//        
//        EBoolean where = cat.name.eq(otherCat.name).and(otherCat.name.eq("Kate5"));
//        FilteringMultiIterator it;
//        
//        long total = 0;
//        for (int i = 0; i < iterations; i++){
//            it = new FilteringMultiIterator(new JavaOps(), where);
//            it.add(cat, cats1);
//            it.add(otherCat, cats2);
//            it.init();
//            long start = System.currentTimeMillis();
//            while (it.hasNext()){
//                it.next();
//            }   
//            total += System.currentTimeMillis() - start;
//        }        
//        System.out.println("cat, otherCat : " + (total / iterations) + " ms");
//        
//        // 2
//        total = 0;
//        for (int i = 0; i < iterations; i++){
//            it = new FilteringMultiIterator(new JavaOps(), where);
//            it.add(otherCat, cats2);
//            it.add(cat, cats1);        
//            it.init();
//            long start = System.currentTimeMillis();
//            while (it.hasNext()){
//                it.next();
//            }
//            total += System.currentTimeMillis() - start;
//        }        
//        System.out.println("otherCat, cat : " + (total / iterations) + " ms");
//    }
    
    

}
