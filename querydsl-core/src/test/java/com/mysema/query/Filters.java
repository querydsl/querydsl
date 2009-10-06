package com.mysema.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

/**
 * @author tiwe
 *
 */
public class Filters {
    
    private final Projections projections;
    
    public Filters(Projections projections){
        this.projections = projections;
    }

    Collection<EBoolean> booleanFilters(EBoolean expr, EBoolean other){
        return Arrays.asList(
            expr.and(other),
            expr.or(other),
            expr.not().and(other.not()),
            expr.not(),
            other.not()
        );
    };

    <A> Collection<EBoolean> collection(ECollection<A> expr, ECollection<A> other, A knownElement){
        return Arrays.<EBoolean>asList(
          expr.contains(knownElement),
          expr.isEmpty(),
          expr.isNotEmpty(),
          expr.size().gt(0)
        );
    }

    private <A extends Comparable<A>> Collection<EBoolean> comparable(EComparable<A> expr, EComparable<A> other, A knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();
        rv.addAll(exprFilters(expr, other, knownValue));
        rv.addAll(Arrays.<EBoolean>asList(
            expr.gt(other),
            expr.gt(knownValue),
            expr.goe(other),
            expr.goe(knownValue),
            expr.lt(other),
            expr.lt(knownValue),
            expr.loe(other),
            expr.loe(knownValue)
        ));
        return rv;
    }

    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<EBoolean> date(EDate<A> expr, EDate<A> other, A knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();
        rv.addAll(comparable(expr, other, knownValue));
        rv.addAll(Arrays.<EBoolean>asList(
          expr.getDayOfMonth().eq(other.getDayOfMonth()),
          expr.getMonth().eq(other.getMonth()),
          expr.getYear().eq(other.getYear())
        ));        
        return rv;
    }
    

    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<EBoolean> dateTime(EDateTime<A> expr, EDateTime<A> other, A knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();
        rv.addAll(comparable(expr, other, knownValue));
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


    private <A> Collection<EBoolean> exprFilters(Expr<A> expr, Expr<A> other, A knownValue){
        return Arrays.<EBoolean>asList(
            expr.eq(other),
            expr.eq(knownValue),
            
            expr.ne(other),
            expr.ne(knownValue)
        );
    }

    <A> Collection<EBoolean> list(EList<A> expr, EList<A> other, A knownElement){
        List<EBoolean> rv = new ArrayList<EBoolean>();
        rv.addAll(collection(expr, other, knownElement));
        rv.add(expr.get(0).eq(knownElement));
        return rv;
    }


    <K,V> Collection<EBoolean> map(EMap<K,V> expr, EMap<K,V> other, K knownKey, V knownValue) {
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

    @SuppressWarnings("unchecked")
    <A extends Number & Comparable<A>> Collection<EBoolean> numeric(ENumber<A> expr, ENumber<A> other, A knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();        
        for (ENumber<?> num : projections.numeric(expr, other, knownValue)){
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
        for (EString eq : projections.string(expr, other, knownValue)){
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
            expr.endsWith(knownValue.substring(2)),       
            
            expr.equalsIgnoreCase(other),
            expr.equalsIgnoreCase(knownValue),
            
            expr.in(Arrays.asList(knownValue)),
            
            expr.indexOf(other).gt(0),
            expr.indexOf("X", 1).gt(0),
            expr.indexOf(knownValue).gt(0),
            
//            expr.lastIndexOf(other).gt(0),
//            expr.lastIndexOf(knownValue).gt(0),
            
            expr.in("A","B","C"),
                        
            expr.isEmpty(),
            expr.isNotEmpty(),
            
            expr.length().gt(0),            
                        
            expr.like(knownValue.substring(0,1)+"%"),
            expr.like("%"+knownValue.substring(1)),
            expr.like("%"+knownValue.substring(1,2)+"%"),            
            
            expr.matches(knownValue.substring(0,1)+".*"),
            expr.matches(".*"+knownValue.substring(1)),
            expr.matches(".*"+knownValue.substring(1,2)+".*"),
            
            expr.notIn("A","B","C"),
            
            expr.notBetween("A", "Z"),
            expr.notBetween(other, other)
            
            
        ));
        return rv;
    }

    
    
    @SuppressWarnings("unchecked")
    <A extends Comparable> Collection<EBoolean> time(ETime<A> expr, ETime<A> other, A knownValue){
        List<EBoolean> rv = new ArrayList<EBoolean>();
        rv.addAll(comparable(expr, other, knownValue));
        rv.addAll(Arrays.<EBoolean>asList(
          expr.getHours().eq(other.getHours()),
          expr.getMinutes().eq(other.getMinutes()),
          expr.getSeconds().eq(other.getSeconds())
        ));        
        return rv;
    }
    

}
