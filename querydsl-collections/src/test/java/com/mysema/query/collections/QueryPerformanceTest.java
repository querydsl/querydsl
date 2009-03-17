/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static org.junit.Assert.fail;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.collections.Domain.Cat;
import com.mysema.query.collections.eval.JavaSerializer;
import com.mysema.query.collections.support.JoinExpressionComparator;
import com.mysema.query.collections.support.SimpleIndexSupport;
import com.mysema.query.collections.support.SimpleIteratorSource;
import com.mysema.query.grammar.JavaOps;
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
            cat.bodyWeight.eq(0).not().or(otherCat.name.eq("Kate5")),
            cat.bodyWeight.eq(0).or(otherCat.name.eq("Kate5").not()),
            cat.name.like("Bob5%").or(otherCat.name.like("%ate5"))      
    );
    
    @Test
    public void testValidateResultSizes(){
        int size = 50;
        long count, expected;
        List<Cat> cats1 = cats(size);
        List<Cat> cats2 = cats(size);
        ColQuery query;
        StringBuilder res = new StringBuilder();
        for (EBoolean condition : conditions){            
            // without wrapped iterators
            query = new ColQueryWithoutIndexing();
            query.setWrapIterators(false);       
            count = query.from(cat, cats1).from(otherCat, cats2).where(condition).count();
            expected = count;
            res.append(StringUtils.leftPad(String.valueOf(count), 7));
            
            // without reordering
            query = new ColQueryWithoutIndexing();
            query.setSortSources(false);   
            count = query.from(cat, cats1).from(otherCat, cats2).where(condition).count();            
            res.append(StringUtils.leftPad(String.valueOf(count), 7));
            res.append(expected != count ? " X":"  ");
            
            // with reordering and iterator wrapping       
            query = new ColQueryWithoutIndexing();
            count = query.from(cat, cats1).from(otherCat, cats2).where(condition).count();
            res.append(StringUtils.leftPad(String.valueOf(count), 7));
            res.append(expected != count ? " X":"  ");
            
            // indexed, without reordering
            query = new ColQuery();
            query.setSortSources(false);            
            count = query.from(cat, cats1).from(otherCat, cats2).where(condition).count();
            res.append(StringUtils.leftPad(String.valueOf(count), 7));
            res.append(expected != count ? " X":"  ");
            
            // indexed, with reordering and iterator wrapping
            query = new ColQuery();            
            count = query.from(cat, cats1).from(otherCat, cats2).where(condition).count();
            res.append(StringUtils.leftPad(String.valueOf(count), 7));
            res.append(expected != count ? " X":"  ");
            
            res.append("   ");
            res.append(new JavaSerializer(JavaOps.DEFAULT).handle(condition).toString());
            res.append("\n");
        }
        System.out.println(res);
        if (res.toString().contains("X")){
            fail(res.toString().replaceAll("[^X]", "").length() + " errors occurred. See log for details.");
        }
    }
    
    @Test
    public void testQueryResults(){
        EBoolean condition = cat.bodyWeight.eq(0).and(otherCat.name.eq("Kate5"));
        List<Cat> cats1 = cats(10);
        List<Cat> cats2 = cats(10);
        
        ColQuery query = new ColQueryWithoutIndexing();
        query.setSortSources(false);                  
        for (Object[] cats :  query.from(cat, cats1).from(otherCat, cats2)
                .where(condition).iterate(cat, otherCat)){
            System.out.println(Arrays.asList(cats));
        }
    }
        
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
    
    private List<Cat> cats(int size){
        List<Cat> cats = new ArrayList<Cat>(size);
        for (int i= 0; i < size / 2; i++){
            cats.add(new Cat("Kate" + (i+1)));
            cats.add(new Cat("Bob"+ (i+1)));
        }
        return cats;
    }
    
    private void runTest(int size){               
        List<Cat> cats1 = cats(size);
        List<Cat> cats2 = cats(size);
        resultLog.add(size + " * " + size + " items");
        
        // test each condition
        for (EBoolean condition : conditions){
            long level1 = 0, level2 = 0, level3 = 0, level4 = 0;
            ColQuery query;
            for (long j=0; j < testIterations; j++){            
                // without wrapped iterators
                query = new ColQueryWithoutIndexing();
                query.setWrapIterators(false);       
                level1 += query(query, condition, cats1, cats2);
                
                // without reordering
                query = new ColQueryWithoutIndexing();
                query.setSortSources(false);            
                level2 += query(query, condition, cats1, cats2);
                                
                // with reordering and iterator wrapping         
                query = new ColQueryWithoutIndexing();
                level3 += query(query, condition, cats1, cats2);
                                                
                // indexed, with reordering and iterator wrapping
                query = new ColQuery();            
                level4 += query(query, condition, cats1, cats2);
                
            }
            StringBuilder builder = new StringBuilder();
            builder.append(" #").append(conditions.indexOf(condition)+1).append("          ");
            builder.append(StringUtils.leftPad(String.valueOf(level1 / testIterations), 10)).append(" ms");
            builder.append(StringUtils.leftPad(String.valueOf(level2 / testIterations), 10)).append(" ms");
            builder.append(StringUtils.leftPad(String.valueOf(level3 / testIterations), 10)).append(" ms");            
            builder.append(StringUtils.leftPad(String.valueOf(level4 / testIterations), 10)).append(" ms");
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
    
    private class ColQueryWithoutIndexing extends ColQuery{
        @Override
        protected QueryIndexSupport createIndexSupport(Map<Expr<?>, Iterable<?>> exprToIt, JavaOps ops, List<Expr<?>> sources){
            return new SimpleIndexSupport(new SimpleIteratorSource(exprToIt), ops, sources);
        }
    }

}
