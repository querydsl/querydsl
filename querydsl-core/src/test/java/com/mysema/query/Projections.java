/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import com.mysema.query.types.CaseBuilder;
import com.mysema.query.types.expr.Constant;
import com.mysema.query.types.expr.ECollection;
import com.mysema.query.types.expr.EDate;
import com.mysema.query.types.expr.EDateTime;
import com.mysema.query.types.expr.EList;
import com.mysema.query.types.expr.EMap;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.ENumberConst;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.EStringConst;
import com.mysema.query.types.expr.ETime;
import com.mysema.query.types.expr.Expr;

/**
 * The Class Projections.
 */
/**
 * @author tiwe
 *
 */
public class Projections {
    
    private final Module module;
    
    private final Target target;

    public Projections(Module module, Target target) {
        this.module = module;
        this.target = target;
    }

    <A> Collection<Expr<?>> collection(ECollection<A> expr, ECollection<A> other, A knownElement){
        HashSet<Expr<?>> rv = new HashSet<Expr<?>>();
        rv.add(expr.size());
        return rv;
    }
    
    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<Expr<?>> date(EDate<A> expr, EDate<A> other, A knownValue){
        HashSet<Expr<?>> rv = new HashSet<Expr<?>>();
        rv.add(expr.getDayOfMonth());
        rv.add(expr.getMonth());
        rv.add(expr.getYear());
        return rv;
    } 

    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<Expr<?>> dateTime(EDateTime<A> expr, EDateTime<A> other, A knownValue){
        HashSet<Expr<?>> rv = new HashSet<Expr<?>>();
        rv.add(expr.getDayOfMonth());
        rv.add(expr.getMonth());
        rv.add(expr.getYear());
        rv.add(expr.getHour());
        rv.add(expr.getMinute());
        rv.add(expr.getSecond());
        return rv;
    }

    <A> Collection<Expr<?>> list(EList<A> expr, EList<A> other, A knownElement){
        HashSet<Expr<?>> rv = new HashSet<Expr<?>>();
        rv.add(expr.get(0));
        rv.add(expr.size());
        return rv;
    }

    <K,V> Collection<Expr<?>> map(EMap<K,V> expr, EMap<K,V> other, K knownKey, V knownValue) {
        HashSet<Expr<?>> rv = new HashSet<Expr<?>>();
        rv.add(expr.get(knownKey));
        rv.add(expr.size());
        return rv;
    }

    <A extends Number & Comparable<A>> Collection<ENumber<?>> numeric(ENumber<A> expr, ENumber<A> other, A knownValue){
        HashSet<ENumber<?>> rv = new HashSet<ENumber<?>>();
        rv.addAll(numeric(expr, other));
        rv.addAll(numeric(expr, ENumberConst.create(knownValue)));
        return rv;
    }
    
    @SuppressWarnings("unchecked")
    private <A extends Number & Comparable<A>> Collection<ENumber<?>> numeric(ENumber<A> expr, ENumber<?> other){
        HashSet<ENumber<?>> rv = new HashSet<ENumber<?>>();
        rv.add(expr.abs());
        rv.add(expr.add(other));
        rv.add(expr.divide(other));
        rv.add(expr.multiply(other));
        rv.add(expr.sqrt());
        rv.add(expr.subtract(other));
        
        if (!(other instanceof Constant || module == Module.JDOQL)){
            CaseBuilder cases = new CaseBuilder();
            rv.add(ENumberConst.create(1).add(cases
                .when(expr.gt(10)).then(expr)
                .when(expr.between(0, 10)).then((ENumber)other)
                .otherwise((ENumber)other)));    
        }
        
        
        return rv;
    }
    
    <A extends Number & Comparable<A>> Collection<ENumber<?>> numericCasts(ENumber<A> expr, ENumber<A> other, A knownValue){
        if (!target.equals(Target.MYSQL)){
            HashSet<ENumber<?>> rv = new HashSet<ENumber<?>>();
            rv.add(expr.byteValue());
            rv.add(expr.doubleValue());
            rv.add(expr.floatValue());
            rv.add(expr.intValue());
            rv.add(expr.longValue());
            rv.add(expr.shortValue());     
            return rv;    
        }else{
            return Collections.emptySet();
        }        
    }

    Collection<Expr<String>> string(EString expr, EString other, String knownValue){
        HashSet<Expr<String>> rv = new HashSet<Expr<String>>();
        rv.addAll(stringProjections(expr, other));
        rv.addAll(stringProjections(expr, EStringConst.create(knownValue)));
        return rv;
    }
    
    @SuppressWarnings("unchecked")
    Collection<Expr<String>> stringProjections(EString expr, EString other){
        HashSet<Expr<String>> rv = new HashSet<Expr<String>>();
        rv.add(expr.append("Hello"));
        rv.add(expr.append(other));
            
        rv.add(expr.concat(other));
        rv.add(expr.concat("Hello"));
           
        rv.add(expr.lower());
            
        rv.add(expr.prepend("Hello"));
        rv.add(expr.prepend(other));
            
        rv.add(expr.stringValue());
            
        rv.add(expr.substring(1));
        rv.add(expr.substring(0, 1));
                
        if (!(other instanceof Constant || module == Module.JDOQL)){
            CaseBuilder cases = new CaseBuilder();
            rv.add(cases.when(expr.eq("A")).then(other)
                        .when(expr.eq("B")).then(expr)
                        .otherwise(other));    
        }         
          
        rv.add(expr.trim());
            
        rv.add(expr.upper());                       
        return rv;
    }
        
    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<Expr<?>> time(ETime<A> expr, ETime<A> other, A knownValue){
        HashSet<Expr<?>> rv = new HashSet<Expr<?>>();
        rv.add(expr.getHour());
        rv.add(expr.getMinute());
        rv.add(expr.getSecond());
        return rv;
    }
        
}
