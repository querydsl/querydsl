/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

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
 * The Class Projections.
 */
/**
 * @author tiwe
 *
 */
public class Projections {

    <A> Collection<Expr<?>> collection(ECollection<A> expr, ECollection<A> other, A knownElement){
        return Arrays.<Expr<?>>asList(
          expr.size()
        );
    }
    
    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<Expr<?>> date(EDate<A> expr, EDate<A> other, A knownValue){
        return Arrays.<Expr<?>>asList(
          expr.getDayOfMonth(),
          expr.getMonth(),
          expr.getYear()
        );
    } 

    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<Expr<?>> dateTime(EDateTime<A> expr, EDateTime<A> other, A knownValue){
        return Arrays.<Expr<?>>asList(
          expr.getDayOfMonth(),
          expr.getMonth(),
          expr.getYear(),
          expr.getHours(),
          expr.getMinutes(),
          expr.getSeconds()
        );
    }

    <A> Collection<Expr<?>> list(EList<A> expr, EList<A> other, A knownElement){
        return Arrays.<Expr<?>>asList(
          expr.get(0),
          expr.size()
        );
    }

    <K,V> Collection<Expr<?>> map(EMap<K,V> expr, EMap<K,V> other, K knownKey, V knownValue) {
        return Arrays.<Expr<?>>asList(
          expr.get(knownKey),
          expr.size()
        );
    }

    <A extends Number & Comparable<A>> Collection<ENumber<?>> numeric(ENumber<A> expr, ENumber<A> other, A knownValue){
        return Arrays.<ENumber<?>>asList(
          expr.abs(),
          expr.add(other),
          expr.div(other),
          expr.mult(other),
          expr.sqrt(),
          expr.sub(other)
        );
    }
    
    <A extends Number & Comparable<A>> Collection<ENumber<?>> numericCasts(ENumber<A> expr, ENumber<A> other, A knownValue){
        return Arrays.<ENumber<?>>asList(
          expr.byteValue(),
          expr.doubleValue(),
          expr.floatValue(),
          expr.intValue(), 
          expr.longValue(),
          expr.shortValue()      
        );
    }

    Collection<EString> string(EString expr, EString other, String knownValue){
        HashSet<EString> rv = new HashSet<EString>();
        rv.addAll(stringProjections(expr, other));
        rv.addAll(stringProjections(expr, EString.create(knownValue)));
        return rv;
    }
    
    Collection<EString> stringProjections(EString expr, EString other){
        return Arrays.<EString>asList(
            expr.append("Hello"),
            expr.append(other),
            
            expr.concat(other),
            expr.concat("Hello"),
            
            expr.lower(),
            
            expr.prepend("Hello"),
            expr.prepend(other),
            
            expr.stringValue(),
            
            expr.substring(1),
            expr.substring(0, 1),
            
            expr.trim(),
            
            expr.upper()                        
         );
    }
        
    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<Expr<?>> time(ETime<A> expr, ETime<A> other, A knownValue){
        return Arrays.<Expr<?>>asList(
          expr.getHours(),
          expr.getMinutes(),
          expr.getSeconds()
        );
    };
        
}
