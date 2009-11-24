/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ECollection;
import com.mysema.query.types.expr.EDate;
import com.mysema.query.types.expr.EDateTime;
import com.mysema.query.types.expr.EList;
import com.mysema.query.types.expr.EMap;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.ETime;
import com.mysema.query.types.expr.Expr;

/**
 * The Class StandardTest.
 * 
 * @author tiwe
 */
public abstract class StandardTest {
    
    private final List<String> errors = new ArrayList<String>();
    
    private final List<String> failures = new ArrayList<String>();
    
    private final MatchingFilters matchers;
    
    private final Projections projections;
    
    private final Filters filters;
    
    private boolean runFilters = true, runProjections = true;
    
    private int total;
    
    public StandardTest(Module module, Target target){
        projections = new Projections(module, target);
        filters = new Filters(projections, module, target);
        matchers = new MatchingFilters(module, target);
    }
    
    public StandardTest(Projections p, Filters f, MatchingFilters m){
        projections = p;
        filters = f;
        matchers = m;
    }
    

    private void execute(Collection<? extends Expr<?>> projections){
        if (this.runProjections){
            for (Expr<?> pr : projections){
                total++;
                try{
                    System.err.println(pr);
                    executeProjection(pr);
                    System.err.println();
                }catch(Throwable t){
                    t.printStackTrace();
                    errors.add(pr + " failed : \n" + 
                            t.getClass().getName() + " : " +
                            t.getMessage() + "\n");
                }            
            }    
        }        
    }

    private void execute(Collection<EBoolean> filters, boolean matching){
        if (this.runFilters){
            for (EBoolean f : filters){
                total++;
                try{
                    System.err.println(f);
                    int results = executeFilter(f);
                    System.err.println();
                    if (matching && results == 0){
                        failures.add(f + " failed");
                    }    
                }catch(Throwable t){
                    t.printStackTrace();
                    errors.add(f + " failed : \n" +
                            t.getClass().getName() + " : " +
                            t.getMessage() +"\n");
                }            
            }    
        }        
    }

    public abstract int executeFilter(EBoolean f);

    public abstract int executeProjection(Expr<?> pr);


    public void report() {
        if (!failures.isEmpty() || !errors.isEmpty()){
            // System.err logging
            System.err.println(failures.size() + " failures");
            for (String f : failures){
                System.err.println(f);
            }
            System.err.println();
            System.err.println(errors.size() + " errors");
            for (String e : errors){
                System.err.println(e);
            }
            
            // construct String for Assert.fail()
            StringBuffer buffer = new StringBuffer("Failed with ");
            if (!failures.isEmpty()){
                buffer.append(failures.size()).append(" failure(s) ");
                if (!errors.isEmpty()){
                    buffer.append("and ");
                }
            }
            if (!errors.isEmpty()){
                buffer.append(errors.size()).append(" error(s) ");
            }
            buffer.append("of ").append(total).append(" tests");            
            Assert.fail(buffer.toString());
        }else{
            System.out.println("Success with " + total + " tests");
        }        
    }

    public StandardTest noFilters() {
        runFilters = false;        
        return this;
    }

    public StandardTest noProjections() {
        runProjections = false;
        return this;
    }
        
    public void booleanTests(EBoolean expr, EBoolean other){
        execute(filters.booleanFilters(expr, other), false);
    }

    public <A> void collectionTests(ECollection<?,A> expr, ECollection<?,A> other, A knownElement, A missingElement){
        execute(matchers.collection(expr, other, knownElement, missingElement), true);
        execute(filters.collection(expr, other, knownElement), false);
        execute(projections.collection(expr, other, knownElement));
    }

    public void dateTests(EDate<java.sql.Date> expr, EDate<java.sql.Date> other, java.sql.Date knownValue){
        execute(matchers.date(expr, other, knownValue), true);
        execute(filters.date(expr, other, knownValue), false);
        execute(projections.date(expr, other, knownValue));
    }

    public void dateTimeTests(EDateTime<java.util.Date> expr, EDateTime<java.util.Date> other, java.util.Date knownValue){
        execute(matchers.dateTime(expr, other, knownValue), true);
        execute(filters.dateTime(expr, other, knownValue), false);
        execute(projections.dateTime(expr, other, knownValue));   
    }

    public <A> void listTests(EList<A> expr, EList<A> other, A knownElement, A missingElement){
        execute(matchers.list(expr, other, knownElement, missingElement), true);
        execute(filters.list(expr, other, knownElement), false);
        execute(projections.list(expr, other, knownElement));
    }

    public <K,V> void mapTests(EMap<K,V> expr, EMap<K,V> other, K knownKey, V knownValue, K missingKey, V missingValue) {
        execute(matchers.map(expr, other, knownKey, knownValue, missingKey, missingValue), true);
        execute(filters.map(expr, other, knownKey, knownValue), false);
        execute(projections.map(expr, other, knownKey, knownValue));
    }

    public <A extends Number & Comparable<A>> void numericCasts(ENumber<A> expr, ENumber<A> other, A knownValue){
        execute(projections.numericCasts(expr, other, knownValue));
    }

    public <A extends Number & Comparable<A>> void numericTests(ENumber<A> expr, ENumber<A> other, A knownValue){
        execute(matchers.numeric(expr, other, knownValue), true);
        execute(filters.numeric(expr, other, knownValue), false);
        execute(projections.numeric(expr, other, knownValue));
    }

    public void stringTests(EString expr, EString other, String knownValue){
        execute(matchers.string(expr, other, knownValue), true);
        execute(filters.string(expr, other, knownValue), false);
        execute(projections.string(expr, other, knownValue));
    }       
    
    public void timeTests(ETime<java.sql.Time> expr, ETime<java.sql.Time> other, java.sql.Time knownValue){
        execute(matchers.time(expr, other, knownValue), true);
        execute(filters.time(expr, other, knownValue), false);
        execute(projections.time(expr, other, knownValue));
    }  

}
