/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.Collection;
import java.util.HashSet;

import com.mysema.query.types.expr.EArray;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ECollection;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.EDate;
import com.mysema.query.types.expr.EDateConst;
import com.mysema.query.types.expr.EDateTime;
import com.mysema.query.types.expr.EDateTimeConst;
import com.mysema.query.types.expr.EList;
import com.mysema.query.types.expr.EMap;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.ENumberConst;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.EStringConst;
import com.mysema.query.types.expr.ETime;
import com.mysema.query.types.expr.ETimeConst;
import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 *
 */
public class MatchingFilters {

    private final Module module;
    
    private final Target target;

    public MatchingFilters(Module module, Target target) {
        this.module = module;
        this.target = target;
    }

    <A> Collection<EBoolean> array(EArray<A> expr,  EArray<A> other, A knownElement, A missingElement){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();          
//        rv.add(expr.isEmpty().not());          
        rv.add(expr.size().gt(0));
        return rv;
    }
    
    <A> Collection<EBoolean> collection(ECollection<?,A> expr,  ECollection<?,A> other, A knownElement, A missingElement){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.add(expr.contains(knownElement));
        rv.add(expr.contains(missingElement).not());          
        rv.add(expr.isEmpty().not());          
        rv.add(expr.isNotEmpty());
        return rv;
    }
    
    @SuppressWarnings("unchecked")
    private <A extends Comparable> Collection<EBoolean> comparable(EComparable<A> expr,  Expr<A> other){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.add(expr.eq(other));            
        rv.add(expr.goe(other));            
        rv.add(expr.loe(other));            
        rv.add(expr.ne(other).not());
        return rv;
    }
    
    Collection<EBoolean> date(EDate<java.sql.Date> expr, EDate<java.sql.Date> other){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.addAll(comparable(expr, other));
        rv.add(expr.getDayOfMonth().eq(other.getDayOfMonth()));
        
        if (!target.equals(Target.DERBY) && !module.equals(Module.JDOQL)){
            rv.add(expr.getDayOfWeek().eq(other.getDayOfWeek ()));
            rv.add(expr.getDayOfYear().eq(other.getDayOfYear()));
            rv.add(expr.getWeek().eq(other.getWeek()));
        }        
        
        rv.add(expr.getYearMonth().eq(other.getYearMonth()));
        rv.add(expr.getMonth().eq(other.getMonth()));
        rv.add(expr.getYear().eq(other.getYear()));
        return rv;
    }
    
    Collection<EBoolean> date(EDate<java.sql.Date> expr, EDate<java.sql.Date> other, java.sql.Date knownValue){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.addAll(date(expr, other));
        rv.addAll(date(expr, EDateConst.create(knownValue)));
        return rv;
    }

    Collection<EBoolean> dateTime(EDateTime<java.util.Date> expr, EDateTime<java.util.Date> other){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.addAll(comparable(expr, other));
        rv.add(expr.getMilliSecond().eq(other.getMilliSecond()));
        rv.add(expr.getSecond().eq(other.getSecond()));     
        rv.add(expr.getMinute().eq(other.getMinute()));
        rv.add(expr.getHour().eq(other.getHour()));
        rv.add(expr.getDayOfMonth().eq(other.getDayOfMonth()));
        
        if (!target.equals(Target.DERBY) && !module.equals(Module.JDOQL)){
            rv.add(expr.getDayOfWeek().eq(other.getDayOfWeek ()));
            rv.add(expr.getDayOfYear().eq(other.getDayOfYear()));
            rv.add(expr.getWeek().eq(other.getWeek()));
        }          
        
        rv.add(expr.getMonth().eq(other.getMonth()));
        rv.add(expr.getYear().eq(other.getYear()));
        return rv;
    }
    
    Collection<EBoolean> dateTime(EDateTime<java.util.Date> expr, EDateTime<java.util.Date> other, java.util.Date knownValue){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.addAll(dateTime(expr, other));
        rv.addAll(dateTime(expr, EDateTimeConst.create(knownValue)));
        return rv;
    }

    <A> Collection<EBoolean> list(EList<A> expr, EList<A> other, A knownElement, A missingElement){
        return collection(expr, other, knownElement, missingElement);
    }

    <K,V> Collection<EBoolean> map(EMap<K,V> expr, EMap<K,V> other,  K knownKey, V knownValue, K missingKey, V missingValue) {
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.add(expr.containsKey(knownKey));
        rv.add(expr.containsKey(missingKey).not());          
        rv.add(expr.containsValue(knownValue));
        rv.add(expr.containsValue(missingValue).not());          
        rv.add(expr.get(knownKey).eq(knownValue));          
        rv.add(expr.isEmpty().not());          
        rv.add(expr.isNotEmpty());
        return rv;
    }

    <A extends Number & Comparable<A>> Collection<EBoolean> numeric( ENumber<A> expr, ENumber<A> other, A knownValue){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.addAll(numeric(expr, other));
        rv.addAll(numeric(expr, ENumberConst.create(knownValue)));
        return rv;
    }
    
    <A extends Number & Comparable<A>> Collection<EBoolean> numeric( ENumber<A> expr, ENumber<A> other){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.add(expr.eq(other));
        rv.add(expr.goe(other));
        rv.add(expr.gt(other.subtract(1)));                           
        rv.add(expr.gt(other.subtract(2)));
        rv.add(expr.loe(other));                
        rv.add(expr.lt(other.add(1)));                
        rv.add(expr.lt(other.add(2)));
        rv.add(expr.ne(other).not());
        return rv;
    }

    Collection<EBoolean> string(EString expr, EString other){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.addAll(comparable(expr, other));
        
        rv.add(expr.charAt(0).eq(other.charAt(0)));
        rv.add(expr.charAt(1).eq(other.charAt(1)));
            
        rv.add(expr.contains(other));
        rv.add(expr.contains(other.substring(0,1)));
        rv.add(expr.contains(other.substring(0,2)));
        rv.add(expr.contains(other.substring(1,2)));
        rv.add(expr.contains(other.substring(1)));
        rv.add(expr.contains(other.substring(2)));
        
        rv.add(expr.contains(other, false));
        rv.add(expr.contains(other.lower(), false));
        rv.add(expr.contains(other.upper(), false));
        rv.add(expr.contains(other.substring(0,1),false));
        rv.add(expr.contains(other.substring(0,2).lower(),false));
        rv.add(expr.contains(other.substring(1,2).upper(),false));
        rv.add(expr.contains(other.substring(1).lower(),false));
        rv.add(expr.contains(other.substring(2).upper(),false));
            
        rv.add(expr.endsWith(other));
        rv.add(expr.endsWith(other,false));
        rv.add(expr.endsWith(other.substring(1)));
        rv.add(expr.endsWith(other.substring(2)));
        rv.add(expr.endsWith(other.substring(1),false));
        rv.add(expr.endsWith(other.substring(2),false));
            
        rv.add(expr.eq(other));
        rv.add(expr.equalsIgnoreCase(other));
            
        rv.add(expr.indexOf(other).eq(0));
        
        if (!target.equals(Target.DERBY)){
            rv.add(expr.indexOf(other.substring(1)).eq(1)); 
            rv.add(expr.indexOf(other.substring(2)).eq(2));     
        }        
            
        rv.add(expr.isEmpty().not());
        rv.add(expr.isNotEmpty());
        
//        if (!module.equals(Module.HQL) && !module.equals(Module.JDOQL) && !module.equals(Module.SQL)){
//            rv.add(expr.lastIndexOf(other).eq(0));    
//        }   
        
        rv.add(expr.length().eq(other.length()));
            
        rv.add(expr.like(other));
        rv.add(expr.like(other.substring(0,1).append("%")));
        rv.add(expr.like(other.substring(0,1).append("%").append(other.substring(2))));
        rv.add(expr.like(other.substring(1).prepend("%")));
        rv.add(expr.like(other.substring(1,2).append("%").prepend("%")));
            
        rv.add(expr.lower().eq(other.lower()));
        
        if (!module.equals(Module.SQL) || (!target.equals(Target.HSQLDB) && !target.equals(Target.DERBY))){
            rv.add(expr.matches(other.substring(0,1).append(".*")));
            rv.add(expr.matches(other.substring(0,1).append(".").append(other.substring(2))));
            rv.add(expr.matches(other.substring(1).prepend(".*")));
            rv.add(expr.matches(other.substring(1,2).prepend(".*").append(".*")));    
        }        
            
        rv.add(expr.ne(other));
            
        rv.add(expr.startsWith(other));
        rv.add(expr.startsWith(other,false));            
        rv.add(expr.startsWith(other.substring(0,1)));
        rv.add(expr.startsWith(other.substring(0,1),false));
        rv.add(expr.startsWith(other.substring(0,2)));                        
        rv.add(expr.startsWith(other.substring(0,2),false));
            
        rv.add(expr.substring(0,1).eq(other.substring(0,1)));
        rv.add(expr.substring(1).eq(other.substring(1)));
                                                             
        rv.add(expr.trim().eq(other.trim()));
            
        rv.add(expr.upper().eq(other.upper()));
        return rv;
    }

    Collection<EBoolean> string(EString expr, EString other,  String knownValue){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.addAll(string(expr, other));
        rv.addAll(string(expr, EStringConst.create(knownValue)));
        return rv;
    }

    Collection<EBoolean> time(ETime<java.sql.Time> expr,  ETime<java.sql.Time> other){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.addAll(comparable(expr, other));
        rv.add(expr.getMilliSecond().eq(other.getMilliSecond()));
        rv.add(expr.getSecond().eq(other.getSecond()));
        rv.add(expr.getMinute().eq(other.getMinute()));
        rv.add(expr.getHour().eq(other.getHour()));
        return rv;
    }
    
    Collection<EBoolean> time(ETime<java.sql.Time> expr,  ETime<java.sql.Time> other, java.sql.Time knownValue){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.addAll(time(expr, other));
        rv.addAll(time(expr, ETimeConst.create(knownValue)));
        return rv;
    }
}
