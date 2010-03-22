/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.mysema.query.types.EArray;
import com.mysema.query.types.EBoolean;
import com.mysema.query.types.ECollection;
import com.mysema.query.types.EComparable;
import com.mysema.query.types.EDate;
import com.mysema.query.types.EDateTime;
import com.mysema.query.types.EList;
import com.mysema.query.types.EMap;
import com.mysema.query.types.ENumber;
import com.mysema.query.types.EString;
import com.mysema.query.types.ETime;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Path;

/**
 * @author tiwe
 *
 */
public class Filters {
    
    private final Projections projections;
    
    private final Module module;
    
    private final Target target;

    public Filters(Projections projections, Module module, Target target) {
        this.projections = projections;
        this.module = module;
        this.target = target;
    }

    Collection<EBoolean> booleanFilters(EBoolean expr, EBoolean other){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.add(expr.and(other));
        rv.add(expr.or(other));
        rv.add(expr.not().and(other.not()));
        rv.add(expr.not());
        rv.add(other.not());
        return rv;
    }

    <A> Collection<EBoolean> collection(ECollection<?,A> expr, ECollection<?,A> other, A knownElement){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.add(expr.contains(knownElement));
        rv.add(expr.isEmpty());
        rv.add(expr.isNotEmpty());
        if (!module.equals(Module.RDFBEAN)){
            rv.add(expr.size().gt(0));    
        }        
        return rv;
    }
    
    <A> Collection<EBoolean> array(EArray<A> expr, EArray<A> other, A knownElement){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();        
        if (!module.equals(Module.RDFBEAN)){
            rv.add(expr.size().gt(0));    
        }
        rv.add(expr.get(0).eq(knownElement));
        return rv;
    }

    private <A extends Comparable<A>> Collection<EBoolean> comparable(EComparable<A> expr, EComparable<A> other, A knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();
        rv.addAll(exprFilters(expr, other, knownValue));
        rv.add(expr.gt(other));
        rv.add(expr.gt(knownValue));
        rv.add(expr.goe(other));
        rv.add(expr.goe(knownValue));
        rv.add(expr.lt(other));
        rv.add(expr.lt(knownValue));
        rv.add(expr.loe(other));
        rv.add(expr.loe(knownValue));
        return rv;

    }

    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<EBoolean> date(EDate<A> expr, EDate<A> other, A knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();
        rv.addAll(comparable(expr, other, knownValue));
        rv.add(expr.dayOfMonth().eq(other.dayOfMonth()));
        rv.add(expr.month().eq(other.month()));
        rv.add(expr.year().eq(other.year()));    
        return rv;
    }
    

    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<EBoolean> dateTime(EDateTime<A> expr, EDateTime<A> other, A knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();
        rv.addAll(comparable(expr, other, knownValue));
        rv.add(expr.dayOfMonth().eq(1));
        rv.add(expr.dayOfMonth().eq(other.dayOfMonth()));
          
        rv.add(expr.month().eq(1));
        rv.add(expr.month().eq(other.month()));
          
        rv.add(expr.year().eq(2000));
        rv.add(expr.year().eq(other.year()));
          
        rv.add(expr.yearMonth().eq(other.yearMonth()));
        
        rv.add(expr.hour().eq(1));
        rv.add(expr.hour().eq(other.hour()));
          
        rv.add(expr.minute().eq(1));
        rv.add(expr.minute().eq(other.minute()));
          
        rv.add(expr.second().eq(1));
        rv.add(expr.second().eq(other.second()));
        return rv;
    }


    private <A> Collection<EBoolean> exprFilters(Expr<A> expr, Expr<A> other, A knownValue){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.add(expr.eq(other));
        rv.add(expr.eq(knownValue));
            
        rv.add(expr.ne(other));
        rv.add(expr.ne(knownValue));
        return rv;
    }

    <A> Collection<EBoolean> list(EList<A> expr, EList<A> other, A knownElement){
        List<EBoolean> rv = new ArrayList<EBoolean>();
        rv.addAll(collection(expr, other, knownElement));
        rv.add(expr.get(0).eq(knownElement));
        return rv;
    }


    <K,V> Collection<EBoolean> map(EMap<K,V> expr, EMap<K,V> other, K knownKey, V knownValue) {
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.add(expr.containsKey(knownKey));
        rv.add(expr.containsValue(knownValue));
        rv.add(expr.get(knownKey).eq(knownValue));
        rv.add(expr.get(knownKey).ne(knownValue));
        rv.add(expr.isEmpty());
        rv.add(expr.isNotEmpty());
        if (!module.equals(Module.RDFBEAN)){
            rv.add(expr.size().gt(0));    
        }        
        return rv;
    }

    @SuppressWarnings("unchecked")
    <A extends Number & Comparable<A>> Collection<EBoolean> numeric(ENumber<A> expr, ENumber<A> other, A knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();        
        for (ENumber<?> num : projections.numeric(expr, other, knownValue, true)){
            rv.add(num.lt(expr));
        }        
        rv.add(expr.ne(other));
        rv.add(expr.ne(knownValue));
        rv.add(expr.goe(other));
        rv.add(expr.goe(knownValue));
        rv.add(expr.gt(other));
        rv.add(expr.gt(knownValue));            
        rv.add(expr.loe(other));
        rv.add(expr.loe(knownValue));
        rv.add(expr.lt(other));
        rv.add(expr.lt(knownValue));

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
    
    <A> Collection<EBoolean> pathFilters(Path<A> expr, Path<A> other, A knownValue){
        return Arrays.<EBoolean>asList(
             expr.isNull(),
             expr.isNotNull()
        );
    }

    @SuppressWarnings("unchecked")
    Collection<EBoolean> string(EString expr, EString other, String knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();
        if (expr instanceof Path && other instanceof Path){
            rv.addAll(pathFilters((Path<String>)expr, (Path<String>)other, knownValue));
        }
        rv.addAll(comparable(expr, other, knownValue));        
        for (Expr<String> eq : projections.string(expr, other, knownValue)){
            rv.add(eq.eq(other));
        }            
        rv.add(expr.between("A", "Z"));
            
        rv.add(expr.charAt(0).eq(knownValue.charAt(0)));
            
        rv.add(expr.notBetween("A", "Z"));
            
        rv.add(expr.contains(other));
        rv.add(expr.contains(knownValue.substring(0,1)));
        rv.add(expr.contains(knownValue.substring(1,2)));
        
        rv.add(expr.contains(other, false));
        rv.add(expr.contains(knownValue.substring(0,1), false));
        rv.add(expr.contains(knownValue.substring(1,2), false));
            
        rv.add(expr.endsWith(other));
        rv.add(expr.endsWith(knownValue.substring(1)));
        rv.add(expr.endsWith(knownValue.substring(2)));      
            
        rv.add(expr.equalsIgnoreCase(other));
        rv.add(expr.equalsIgnoreCase(knownValue));
            
        rv.add(expr.in(Arrays.asList(knownValue)));
            
        rv.add(expr.indexOf(other).gt(0));
        rv.add(expr.indexOf("X", 1).gt(0));
        rv.add(expr.indexOf(knownValue).gt(0));
            
//        if (!module.equals(Module.HQL) && !module.equals(Module.JDOQL) && !module.equals(Module.SQL)){
//            rv.add(expr.lastIndexOf(other).gt(0));
//            rv.add(expr.lastIndexOf(knownValue).gt(0));    
//        }        
            
        rv.add(expr.in("A","B","C"));
                        
        rv.add(expr.isEmpty());
        rv.add(expr.isNotEmpty());
            
        rv.add(expr.length().gt(0));            
                        
        rv.add(expr.like(knownValue.substring(0,1)+"%"));
        rv.add(expr.like("%"+knownValue.substring(1)));
        rv.add(expr.like("%"+knownValue.substring(1,2)+"%"));            
            
        if (!target.equals(Target.DERBY) 
         && !target.equals(Target.HSQLDB)
         && !target.equals(Target.SQLSERVER)){
            rv.add(expr.matches(knownValue.substring(0,1)+".*"));
            rv.add(expr.matches(".*"+knownValue.substring(1)));
            rv.add(expr.matches(".*"+knownValue.substring(1,2)+".*"));    
        }        
            
        rv.add(expr.notIn("A","B","C"));
            
        rv.add(expr.notBetween("A", "Z"));
        rv.add(expr.notBetween(other, other));
            
        return rv;
    }    
    
    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<EBoolean> time(ETime<A> expr, ETime<A> other, A knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();
        rv.addAll(comparable(expr, other, knownValue));
        rv.add(expr.hour().eq(other.hour()));
        rv.add(expr.minute().eq(other.minute()));
        rv.add(expr.second().eq(other.second()));
        return rv;
    }
    

}
