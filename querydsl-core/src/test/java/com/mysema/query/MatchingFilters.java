package com.mysema.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

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

/**
 * @author tiwe
 *
 */
public class MatchingFilters {

    <A> Collection<EBoolean> collection(ECollection<A> expr,  ECollection<A> other, A knownElement, A missingElement){
        return Arrays.<EBoolean>asList(
          expr.contains(knownElement),
          expr.contains(missingElement).not(),
          
          expr.isEmpty().not(),
          
          expr.isNotEmpty()          
        );
    }
    
    @SuppressWarnings("unchecked")
    private <A extends Comparable> Collection<EBoolean> comparable(EComparable<A> expr,  Expr<A> other){
        return Arrays.asList(
            expr.eq(other),
            
            expr.goe(other),
            
            expr.loe(other),
            
            expr.ne(other).not()
        );
    }
    
    Collection<EBoolean> date(EDate<java.sql.Date> expr, EDate<java.sql.Date> other){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.addAll(comparable(expr, other));
        rv.addAll(Arrays.asList(
                expr.getDayOfMonth().eq(other.getDayOfMonth()),
                expr.getMonth().eq(other.getMonth()),
                expr.getYear().eq(other.getYear())
            ));
        return rv;
    }
    
    Collection<EBoolean> date(EDate<java.sql.Date> expr, EDate<java.sql.Date> other, java.sql.Date knownValue){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.addAll(date(expr, other));
        rv.addAll(date(expr, EDate.create(knownValue)));
        return rv;
    }

    Collection<EBoolean> dateTime(EDateTime<java.util.Date> expr, EDateTime<java.util.Date> other){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.addAll(comparable(expr, other));
        rv.addAll(Arrays.asList(
                expr.getSeconds().eq(other.getSeconds()),
                expr.getMinutes().eq(other.getMinutes()),
                expr.getHours().eq(other.getHours()),
                expr.getDayOfMonth().eq(other.getDayOfMonth()),
                expr.getMonth().eq(other.getMonth()),
                expr.getYear().eq(other.getYear())
            ));
        return rv;
    }
    
    Collection<EBoolean> dateTime(EDateTime<java.util.Date> expr, EDateTime<java.util.Date> other, java.util.Date knownValue){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.addAll(dateTime(expr, other));
        rv.addAll(dateTime(expr, EDateTime.create(knownValue)));
        return rv;
    }

    <A> Collection<EBoolean> list(EList<A> expr, EList<A> other, A knownElement, A missingElement){
        return collection(expr, other, knownElement, missingElement);
    }

    <K,V> Collection<EBoolean> map(EMap<K,V> expr, EMap<K,V> other,  K knownKey, V knownValue, K missingKey, V missingValue) {
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

    <A extends Number & Comparable<A>> Collection<EBoolean> numeric( ENumber<A> expr, ENumber<A> other, A knownValue){
        return Arrays.<EBoolean>asList(
            expr.eq(other),
            expr.eq(knownValue),
            
            expr.goe(other),
            expr.goe(knownValue),            
            
            expr.gt(knownValue.intValue()-1),            
            
            expr.loe(other),
            expr.loe(knownValue),            
            
            expr.lt(knownValue.intValue()+1),
            
            expr.ne(other).not(),
            expr.ne(knownValue).not()
        );
    }

    Collection<EBoolean> string(EString expr, EString other){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.addAll(comparable(expr, other));
        rv.addAll(Arrays.<EBoolean>asList(
            expr.charAt(0).eq(other.charAt(0)),
            expr.charAt(1).eq(other.charAt(1)),
            
            expr.contains(other),
            expr.contains(other.substring(0,1)),
            expr.contains(other.substring(0,2)),
            expr.contains(other.substring(1,2)),
            expr.contains(other.substring(1)),
            expr.contains(other.substring(2)),
            
            expr.endsWith(other),
            expr.endsWith(other,false),
            expr.endsWith(other.substring(1)),
            expr.endsWith(other.substring(2)),
            expr.endsWith(other.substring(1),false),
            expr.endsWith(other.substring(2),false),
            
            expr.eq(other),
            expr.equalsIgnoreCase(other),
            
            expr.indexOf(other).eq(0),
            expr.indexOf(other.substring(1)).eq(1), 
            expr.indexOf(other.substring(2)).eq(2), 
            
            expr.isEmpty().not(),
            expr.isNotEmpty(),
            
            expr.length().eq(other.length()),
            
            expr.like(other),
            expr.like(other.substring(0,1).append("%")),
            expr.like(other.substring(0,1).append("%").append(other.substring(2))),
            expr.like(other.substring(1).prepend("%")),
            expr.like(other.substring(1,2).append("%").prepend("%")),
            
            expr.lower().eq(other.lower()),
            
            expr.matches(other.substring(0,1).append(".*")),
            expr.matches(other.substring(0,1).append(".").append(other.substring(2))),
            expr.matches(other.substring(1).prepend(".*")),
            expr.matches(other.substring(1,2).prepend(".*").append(".*")),
            
            expr.ne(other),
            
            expr.startsWith(other),
            expr.startsWith(other,false),            
            expr.startsWith(other.substring(0,1)),
            expr.startsWith(other.substring(0,1),false),
            expr.startsWith(other.substring(0,2)),                        
            expr.startsWith(other.substring(0,2),false),
            
            expr.substring(0,1).eq(other.substring(0,1)),
            expr.substring(1).eq(other.substring(1)),
                                                             
            expr.trim().eq(other.trim()),
            
            expr.upper().eq(other.upper())
            
        ));
        return rv;
    }

    Collection<EBoolean> string(EString expr, EString other,  String knownValue){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.addAll(string(expr, other));
        rv.addAll(string(expr, EString.create(knownValue)));
        return rv;
    }

    Collection<EBoolean> time(ETime<java.sql.Time> expr,  ETime<java.sql.Time> other){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.addAll(comparable(expr, other));
        rv.addAll(Arrays.asList(
                expr.getSeconds().eq(other.getSeconds()),
                expr.getMinutes().eq(other.getMinutes()),
                expr.getHours().eq(other.getHours())
            ));
        return rv;
    }
    
    Collection<EBoolean> time(ETime<java.sql.Time> expr,  ETime<java.sql.Time> other, java.sql.Time knownValue){
        HashSet<EBoolean> rv = new HashSet<EBoolean>();
        rv.addAll(time(expr, other));
        rv.addAll(time(expr, ETime.create(knownValue)));
        return rv;
    }
}
