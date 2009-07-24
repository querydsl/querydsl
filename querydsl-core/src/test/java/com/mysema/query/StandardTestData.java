/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.mysema.query.functions.MathFunctions;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ECollection;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.EDate;
import com.mysema.query.types.expr.EDateTime;
import com.mysema.query.types.expr.EList;
import com.mysema.query.types.expr.EMap;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.ETime;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.Path;

class StandardTestData {
    
    Collection<EBoolean> booleanFilters(EBoolean expr, EBoolean other){
        return Arrays.asList(
            expr.and(other),
            expr.or(other),
            expr.not().and(other.not()),
            expr.not(),
            other.not()
        );
    };
    
    <A> Collection<EBoolean> collectionFilters(ECollection<A> expr, ECollection<A> other, A knownElement){
        return Arrays.<EBoolean>asList(
          expr.contains(knownElement),
          expr.isEmpty(),
          expr.isNotEmpty(),
          expr.size().gt(0)
        );
    }
    
    <A> Collection<EBoolean> collectionMatchingFilters(ECollection<A> expr, ECollection<A> other, A knownElement, A missingElement){
        return Arrays.<EBoolean>asList(
          expr.contains(knownElement),
          expr.contains(missingElement).not(),
          expr.isEmpty().not(),
          expr.isNotEmpty()          
        );
    }
    
    <A> Collection<Expr<?>> collectionProjections(ECollection<A> expr, ECollection<A> other, A knownElement){
        return Arrays.<Expr<?>>asList(
          expr.size()
        );
    }
    
    private <A extends Comparable<A>> Collection<EBoolean> comparableFilters(EComparable<A> expr, EComparable<A> other, A knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();
        rv.addAll(exprFilters(expr, other, knownValue));
        rv.addAll(Arrays.<EBoolean>asList(
            expr.after(other),
            expr.after(knownValue),
            expr.aoe(other),
            expr.aoe(knownValue),
            expr.before(other),
            expr.before(knownValue),
            expr.boe(other),
            expr.boe(knownValue)
        ));
        return rv;
    }
    
    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<EBoolean> dateFilters(EDate<A> expr, EDate<A> other, A knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();
        rv.addAll(comparableFilters(expr, other, knownValue));
        rv.addAll(Arrays.<EBoolean>asList(
          expr.getDayOfMonth().eq(other.getDayOfMonth()),
          expr.getMonth().eq(other.getMonth()),
          expr.getYear().eq(other.getYear())
        ));        
        return rv;
    }
    
    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<EBoolean> dateMatchingFilters(EDate<A> expr, EDate<A> other, A knownValue){
        return Collections.emptyList();
    }
    
    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<Expr<?>> dateProjections(EDate<A> expr, EDate<A> other, A knownValue){
        return Arrays.<Expr<?>>asList(
          expr.getDayOfMonth(),
          expr.getMonth(),
          expr.getYear()
        );
    }
    
    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<EBoolean> timeFilters(ETime<A> expr, ETime<A> other, A knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();
        rv.addAll(comparableFilters(expr, other, knownValue));
        rv.addAll(Arrays.<EBoolean>asList(
          expr.getHours().eq(other.getHours()),
          expr.getMinutes().eq(other.getMinutes()),
          expr.getSeconds().eq(other.getSeconds())
        ));        
        return rv;
    }
    
    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<EBoolean> timeMatchingFilters(ETime<A> expr, ETime<A> other, A knownValue){
        return Collections.emptyList();
    }
    
    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<Expr<?>> timeProjections(ETime<A> expr, ETime<A> other, A knownValue){
        return Arrays.<Expr<?>>asList(
          expr.getHours(),
          expr.getMinutes(),
          expr.getSeconds()
        );
    }
    
    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<EBoolean> dateTimeFilters(EDateTime<A> expr, EDateTime<A> other, A knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();
        rv.addAll(comparableFilters(expr, other, knownValue));
        rv.addAll(Arrays.<EBoolean>asList(
          expr.getDayOfMonth().eq(1),
          expr.getDayOfMonth().eq(other.getDayOfMonth()),
          expr.getMonth().eq(1),
          expr.getMonth().eq(other.getMonth()),
          expr.getYear().eq(2000),
          expr.getYear().eq(other.getYear()),
          expr.getHours().eq(1),
          expr.getHours().eq(other.getHours()),
          expr.getMinutes().eq(1),
          expr.getMinutes().eq(other.getMinutes()),
          expr.getSeconds().eq(1),
          expr.getSeconds().eq(other.getSeconds())
        ));
        return rv;
    }
    
    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<EBoolean> dateTimeMatchingFilters(EDateTime<A> expr, EDateTime<A> other, A knownValue){
        return Collections.emptyList();
    }
    
    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<Expr<?>> dateTimeProjections(EDateTime<A> expr, EDateTime<A> other, A knownValue){
        return Arrays.<Expr<?>>asList(
          expr.getDayOfMonth(),
          expr.getMonth(),
          expr.getYear(),
          expr.getHours(),
          expr.getMinutes(),
          expr.getSeconds()
        );
    }
    
    private <A> Collection<EBoolean> exprFilters(Expr<A> expr, Expr<A> other, A knownValue){
        return Arrays.<EBoolean>asList(
            expr.eq(other),
            expr.eq(knownValue),
            expr.ne(other),
            expr.ne(knownValue)
        );
    }
    
    <A> Collection<EBoolean> listFilters(EList<A> expr, EList<A> other, A knownElement){
        List<EBoolean> rv = new ArrayList<EBoolean>();
        rv.addAll(collectionFilters(expr, other, knownElement));
        rv.add(expr.get(0).eq(knownElement));
        return rv;
    }
    
    <A> Collection<EBoolean> listMatchingFilters(EList<A> expr, EList<A> other, A knownElement, A missingElement){
        return collectionMatchingFilters(expr, other, knownElement, missingElement);
    }
    
    <A> Collection<Expr<?>> listProjections(EList<A> expr, EList<A> other, A knownElement){
        return Arrays.<Expr<?>>asList(
          expr.get(0),
          expr.size()
        );
    }
    
    <K,V> Collection<EBoolean> mapFilters(EMap<K,V> expr, EMap<K,V> other, K knownKey, V knownValue) {
        return Arrays.<EBoolean>asList(
          expr.containsKey(knownKey),
          expr.containsValue(knownValue),
          expr.get(knownKey).eq(knownValue),
          expr.get(knownKey).ne(knownValue),
          expr.isEmpty(),
          expr.isNotEmpty(),
          expr.size().gt(0)
        );
    }
        
    <K,V> Collection<EBoolean> mapMatchingFilters(EMap<K,V> expr, EMap<K,V> other, K knownKey, V knownValue, K missingKey, V missingValue) {
        return Arrays.<EBoolean>asList(
          expr.containsKey(knownKey),
          expr.containsKey(missingKey).not(),
          expr.containsValue(knownValue),
          expr.containsValue(missingValue).not(),
          expr.get(knownKey).eq(knownValue),
          expr.isEmpty().not(),
          expr.isNotEmpty()
        );
    }
        
    <K,V> Collection<Expr<?>> mapProjections(EMap<K,V> expr, EMap<K,V> other, K knownKey, V knownValue) {
        return Arrays.<Expr<?>>asList(
          expr.get(knownKey),
          expr.size()
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
        
    @SuppressWarnings("unchecked")
    <A extends Number & Comparable<A>> Collection<EBoolean> numericFilters(ENumber<A> expr, ENumber<A> other, A knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();        
        for (ENumber<?> num : numericProjections(expr, other, knownValue)){
            rv.add(num.lt(expr));
        }        
        rv.addAll(Arrays.asList(
            expr.ne(other),
            expr.ne(knownValue),
            expr.goe(other),
            expr.goe(knownValue),
            expr.gt(other),
            expr.gt(knownValue),            
            expr.loe(other),
            expr.loe(knownValue),
            expr.lt(other),
            expr.lt(knownValue)
            
        ));       
        if (expr.getType().equals(Integer.class)){
            ENumber<Integer> eint = (ENumber)expr;
            rv.add(eint.between(1, 2));
            rv.add(eint.notBetween(1, 2));            
        }else if (expr.getType().equals(Double.class)){
            ENumber<Double> edouble = (ENumber)expr;
            rv.add(edouble.between(1.0, 2.0));
            rv.add(edouble.notBetween(1.0, 2.0));
        }
        
        return rv;
    }
    
    <A extends Number & Comparable<A>> Collection<EBoolean> numericMatchingFilters(ENumber<A> expr, ENumber<A> other, A knownValue){
        return Arrays.<EBoolean>asList(
            expr.eq(knownValue),            
            expr.goe(knownValue),
            expr.gt(knownValue.intValue()-1),
            expr.loe(knownValue),
            expr.lt(knownValue.intValue()+1)
        );
    }
    
    <A extends Number & Comparable<A>> Collection<ENumber<?>> numericProjections(ENumber<A> expr, ENumber<A> other, A knownValue){
        return Arrays.<ENumber<?>>asList(
          MathFunctions.abs(expr),
          expr.add(other),
          expr.div(other),
          expr.mult(other),
          MathFunctions.sqrt(expr),
          expr.sub(other)
        );
    }
    
    <A> Collection<EBoolean> pathFilters(Path<A> expr, Path<A> other, A knownValue){
        return Arrays.<EBoolean>asList(
             expr.isNull(),
             expr.isNotNull()
        );
    }
    
    @SuppressWarnings("unchecked")
    Collection<EBoolean> stringFilters(EString expr, EString other, String knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();
        if (expr instanceof Path && other instanceof Path){
            rv.addAll(pathFilters((Path<String>)expr, (Path<String>)other, knownValue));
        }
        rv.addAll(comparableFilters(expr, other, knownValue));        
        for (EString eq : stringProjections(expr, other, knownValue)){
            rv.add(eq.eq(other));
        }
        rv.addAll(Arrays.<EBoolean>asList(            
            expr.between("A", "Z"),
            expr.charAt(0).eq(knownValue.charAt(0)),
            expr.notBetween("A", "Z"),
            expr.contains(other),
            expr.contains(knownValue.substring(0,1)),
            expr.contains(knownValue.substring(1,2)),
            expr.endsWith(other),
            expr.endsWith(knownValue.substring(1)),            
            expr.equalsIgnoreCase(other),
            expr.equalsIgnoreCase(knownValue),
            expr.in(Arrays.asList(knownValue)),
            expr.indexOf(other).gt(0),
            expr.indexOf("X", 1).gt(0),
            expr.indexOf(knownValue).gt(0),
            expr.in("A","B","C"),
            expr.notIn("A","B","C"),
//            expr.like(knownValue.substring(0,1)+"%"),
//            expr.like("%"+knownValue.substring(1)),
//            expr.like("%"+knownValue.substring(1,2)+"%"),            
            expr.matches(knownValue.substring(0,1)+".*"),
            expr.matches(".*"+knownValue.substring(1)),
            expr.matches(".*"+knownValue.substring(1,2)+".*"),
            expr.length().gt(0),            
            expr.notBetween("A", "Z"),
            expr.notBetween(other, other),
            expr.isEmpty(),
            expr.isNotEmpty()
        ));
        return rv;
    }
    
    Collection<EBoolean> stringMatchingFilters(EString expr, EString other, String knownValue){
        return Arrays.<EBoolean>asList(
            expr.eq(other),
            expr.eq(knownValue),
            expr.ne(other),
            expr.ne(knownValue),
            expr.equalsIgnoreCase(other),
            expr.equalsIgnoreCase(knownValue),
            expr.lower().eq(other.lower()),
            expr.upper().eq(other.upper()),
            expr.lower().eq(knownValue.toLowerCase()),
            expr.charAt(0).eq(other.charAt(0)),
            expr.endsWith(other),
            expr.endsWith(knownValue),
            expr.endsWith(other,false),
            expr.endsWith(knownValue,false),
            expr.startsWith(other),
            expr.startsWith(knownValue),
            expr.startsWith(other,false),
            expr.startsWith(knownValue,false),
            expr.contains(other),
            expr.contains(knownValue),
            expr.contains(knownValue.substring(0,1)),
            expr.contains(knownValue.substring(1,2)),
            other.startsWith(expr),
            other.endsWith(expr),
            other.contains(expr),
            expr.substring(0,1).eq(other.substring(0,1)),
            expr.substring(1).eq(other.substring(1)),
            expr.substring(0,1).eq(knownValue.substring(0,1)),
            expr.substring(1).eq(knownValue.substring(1)),
//            expr.like(knownValue),
//            other.like(knownValue),
//            expr.like(knownValue.substring(0,1)+"%"),
//            expr.like("%"+knownValue.substring(1)),
//            expr.like("%"+knownValue.substring(1,2)+"%"),
            expr.matches(knownValue.substring(0,1)+".*"),
            expr.matches(".*"+knownValue.substring(1)),
            expr.matches(".*"+knownValue.substring(1,2)+".*")
        );
    }
    
    Collection<EString> stringProjections(EString expr, EString other, String knownValue){
        return Arrays.<EString>asList(
          expr.add("Hello"),
          expr.add(other),
          expr.concat("Hello"),
          expr.concat(other),
          expr.lower(),
          expr.upper(),
          expr.stringValue(),
          expr.substring(1),
          expr.substring(0, 1),
          expr.trim() 
        );
    }
        
}
