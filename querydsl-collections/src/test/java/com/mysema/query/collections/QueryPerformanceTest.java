/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.collections.Domain.Cat;
import com.mysema.query.collections.support.JoinExpressionComparator;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;

/**
 * QueryPerformanceTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class QueryPerformanceTest extends AbstractQueryTest{
    
    private long testIterations = 50;
    
    private long results;
    
    private List<String> resultLog = new ArrayList<String>(30);
    
    private List<EBoolean> conditions = Arrays.asList(
            cat.ne(otherCat),
            cat.name.ne(otherCat.name).and(otherCat.name.eq("Kate5")),
            cat.bodyWeight.eq(0).and(otherCat.name.eq("Kate5")),
            cat.name.ne(otherCat.name).and(otherCat.name.like("Kate5%")),
            cat.name.like("Bob5%").and(otherCat.name.like("Kate5%"))         
    );
        
    @Test
    @Ignore
    public void longTest(){
        Map<Expr<?>,Iterable<?>> exprToIt = new HashMap<Expr<?>,Iterable<?>>();
        exprToIt.put(cat, Collections.emptySet());
        exprToIt.put(otherCat, Collections.emptySet());
        
        List<JoinExpression<?>> joins = Arrays.<JoinExpression<?>>asList(
                new JoinExpression<Object>(JoinType.DEFAULT, cat), 
                new JoinExpression<Object>(JoinType.DEFAULT, otherCat));
        
        for (int i = 0; i < conditions.size(); i++){            
            if (new JoinExpressionComparator(conditions.get(i)).comparePrioritiesOnly(joins.get(0), joins.get(1)) > 0){
                System.out.println("#" + (i+1) + " inverted");
            }else{
                System.out.println("#" + (i+1) + " order preserved");
            }
        }

        System.out.println();
         
        runTest(100);
        runTest(500);        
        runTest(1000);       
//        runTest(5000);        
//        runTest(10000);
//        runTest(50000);        
        
        for (String line : resultLog){
            System.out.println(line);
        }
    }
    
    private void runTest(int size){               
        List<Cat> cats1 = new ArrayList<Cat>(size);
        for (int i= 0; i < size; i++){
            cats1.add(new Cat("Bob" + i));
        }
        List<Cat> cats2 = new ArrayList<Cat>(size);
        for (int i=0; i < size; i++){
            cats2.add(new Cat("Kate" + i));
        }
        resultLog.add(size + " * " + size + " items");
        
        // test each condition
        for (EBoolean condition : conditions){
            long level1 = 0, level2 = 0, level3 = 0;
            ColQuery query;
            for (long j=0; j < testIterations; j++){            
                // without wrapped iterators
                query = new ColQuery();
                query.setWrapIterators(false);                       
                level1 += query(query, condition, cats1, cats2);
//                if (size == 100 && j == 0){
//                    System.out.println(results + " results");
//                }
                
                // without reordering
                query = new ColQuery();
                query.setSortSources(false);            
                level2 += query(query, condition, cats1, cats2);
                
//                if (size == 100 && j == 0){
//                    System.out.println(results + " results");
//                }
                
                // with reordering and iterator wrapping
                query = new ColQuery();            
                level3 += query(query, condition, cats1, cats2);
//                if (size == 100 && j == 0){
//                    System.out.println(results + " results");
//                }
                
            }
            StringBuilder builder = new StringBuilder();
            builder.append(" #").append(conditions.indexOf(condition)+1).append("          ");
            builder.append(StringUtils.leftPad(String.valueOf(level1 / testIterations), 10)).append(" ms");
            builder.append(StringUtils.leftPad(String.valueOf(level2 / testIterations), 10)).append(" ms");
            builder.append(StringUtils.leftPad(String.valueOf(level3 / testIterations), 10)).append(" ms");            
            resultLog.add(builder.toString());    
        }
        resultLog.add("");
        System.out.println("finished for " + size);
    }
    
    private long query(ColQuery query, EBoolean condition, List<Cat> cats1, List<Cat> cats2){        
        long start = System.currentTimeMillis();
        Iterator<Cat> it = query.from(cat, cats1).from(otherCat, cats2).where(condition).iterate(cat).iterator();
        results = 0;
        while (it.hasNext()){
            results++;
            it.next();
        }
        return System.currentTimeMillis() - start;
    }
    
    public static void main(String[] args){
        new QueryPerformanceTest().longTest();
    }

}
