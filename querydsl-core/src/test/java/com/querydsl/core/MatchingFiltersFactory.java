/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core;

import java.util.Collection;
import java.util.HashSet;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.*;
import com.querydsl.core.types.expr.*;
import com.querydsl.core.types.path.ListPath;

/**
 * @author tiwe
 *
 */
public class MatchingFiltersFactory {

    private final Module module;

    private final Target target;

    public MatchingFiltersFactory(Module module, Target target) {
        this.module = module;
        this.target = target;
    }

    public <A> Collection<Predicate> array(ArrayExpression<A[], A> expr,  
            ArrayExpression<A[], A> other, A knownElement, A missingElement) {
        HashSet<Predicate> rv = new HashSet<Predicate>();
//        rv.add(expr.isEmpty().not());
        if (!module.equals(Module.RDFBEAN)) {
            rv.add(expr.size().gt(0));
        }
        return ImmutableList.copyOf(rv);
    }

    public <A> Collection<Predicate> collection(CollectionExpressionBase<?,A> expr,  
            CollectionExpression<?,A> other, A knownElement, A missingElement) {
        HashSet<Predicate> rv = new HashSet<Predicate>();
        if (!module.equals(Module.RDFBEAN)) {
            rv.add(expr.contains(knownElement));
            rv.add(expr.contains(missingElement).not());
        }
        rv.add(expr.isEmpty().not());
        rv.add(expr.isNotEmpty());
        return ImmutableList.copyOf(rv);
    }

    @SuppressWarnings("unchecked")
    private <A extends Comparable> Collection<Predicate> comparable(ComparableExpression<A> expr,  
            Expression<A> other) {
        HashSet<Predicate> rv = new HashSet<Predicate>();
        rv.add(expr.eq(other));
        rv.add(expr.goe(other));
        rv.add(expr.loe(other));
        rv.add(expr.ne(other).not());
        return ImmutableList.copyOf(rv);
    }

    public Collection<Predicate> date(DateExpression<java.sql.Date> expr, 
            DateExpression<java.sql.Date> other) {
        HashSet<Predicate> rv = new HashSet<Predicate>();
        rv.addAll(comparable(expr, other));
        rv.add(expr.dayOfMonth().eq(other.dayOfMonth()));

        if (!target.equals(Target.DERBY) && !module.equals(Module.JDO) && !target.equals(Target.ORACLE)
                && !target.equals(Target.FIREBIRD)
                && (!target.equals(Target.POSTGRES) || !module.equals(Module.JPA))) {
            rv.add(expr.dayOfWeek().eq(other.dayOfWeek ()));
            rv.add(expr.dayOfYear().eq(other.dayOfYear()));
            
            if (!target.equals(Target.SQLSERVER) && !target.equals(Target.MYSQL) 
                    && !target.equals(Target.CUBRID)
                    && !target.equals(Target.POSTGRES) && !target.equals(Target.HSQLDB)) {
                rv.add(expr.week().eq(other.week()));
            }
        }

        rv.add(expr.month().eq(other.month()));
        rv.add(expr.year().eq(other.year()));
        rv.add(expr.yearMonth().eq(other.yearMonth()));
        return ImmutableList.copyOf(rv);
    }

    public Collection<Predicate> date(DateExpression<java.sql.Date> expr, 
            DateExpression<java.sql.Date> other, java.sql.Date knownValue) {
        HashSet<Predicate> rv = new HashSet<Predicate>();
        rv.addAll(date(expr, other));
        rv.addAll(date(expr, DateConstant.create(knownValue)));
        return ImmutableList.copyOf(rv);
    }

    public Collection<Predicate> dateTime(DateTimeExpression<java.util.Date> expr, 
            DateTimeExpression<java.util.Date> other) {
        HashSet<Predicate> rv = new HashSet<Predicate>();
        rv.addAll(comparable(expr, other));
        rv.add(expr.milliSecond().eq(other.milliSecond()));
        rv.add(expr.second().eq(other.second()));
        rv.add(expr.minute().eq(other.minute()));
        rv.add(expr.hour().eq(other.hour()));
        rv.add(expr.dayOfMonth().eq(other.dayOfMonth()));

        if (!target.equals(Target.DERBY) && !module.equals(Module.JDO) && !target.equals(Target.ORACLE)
                && (!target.equals(Target.POSTGRES) || !module.equals(Module.JPA))) {
            rv.add(expr.dayOfWeek().eq(other.dayOfWeek ()));
            rv.add(expr.dayOfYear().eq(other.dayOfYear()));
            
            if (!target.equals(Target.SQLSERVER) && !target.equals(Target.MYSQL) 
                    && !target.equals(Target.CUBRID)
                    && !target.equals(Target.POSTGRES) && !target.equals(Target.HSQLDB)) {
                rv.add(expr.week().eq(other.week()));
            }
        }

        rv.add(expr.month().eq(other.month()));
        rv.add(expr.year().eq(other.year()));
        rv.add(expr.yearMonth().eq(other.yearMonth()));
        return ImmutableList.copyOf(rv);
    }

    public Collection<Predicate> dateTime(DateTimeExpression<java.util.Date> expr, 
            DateTimeExpression<java.util.Date> other, java.util.Date knownValue) {
        HashSet<Predicate> rv = new HashSet<Predicate>();
        rv.addAll(dateTime(expr, other));
        rv.addAll(dateTime(expr, DateTimeConstant.create(knownValue)));
        return ImmutableList.copyOf(rv);
    }

    public <A,Q extends SimpleExpression<A>> Collection<Predicate> list(ListPath<A,Q> expr, 
            ListExpression<A,Q> other, A knownElement, A missingElement) {
        return collection(expr, other, knownElement, missingElement);
    }

    public <K,V> Collection<Predicate> map(MapExpressionBase<K,V,?> expr, 
            MapExpression<K,V> other,  K knownKey, V knownValue, K missingKey, V missingValue) {
        HashSet<Predicate> rv = new HashSet<Predicate>();
        rv.add(expr.containsKey(knownKey));
        rv.add(expr.containsKey(missingKey).not());
        rv.add(expr.containsValue(knownValue));
        rv.add(expr.containsValue(missingValue).not());
        rv.add(expr.get(knownKey).eq(knownValue));
        rv.add(expr.isEmpty().not());
        rv.add(expr.isNotEmpty());
        return ImmutableList.copyOf(rv);
    }

    public <A extends Number & Comparable<A>> Collection<Predicate> numeric( 
            NumberExpression<A> expr, NumberExpression<A> other, A knownValue) {
        HashSet<Predicate> rv = new HashSet<Predicate>();
        rv.addAll(numeric(expr, other));
        rv.addAll(numeric(expr, NumberConstant.create(knownValue)));
        return ImmutableList.copyOf(rv);
    }

    public <A extends Number & Comparable<A>> Collection<Predicate> numeric( 
            NumberExpression<A> expr, NumberExpression<A> other) {
        HashSet<Predicate> rv = new HashSet<Predicate>();
        rv.add(expr.eq(other));
        rv.add(expr.goe(other));
        rv.add(expr.gt(other.subtract(1)));
        rv.add(expr.gt(other.subtract(2)));
        rv.add(expr.loe(other));
        rv.add(expr.lt(other.add(1)));
        rv.add(expr.lt(other.add(2)));
        rv.add(expr.ne(other).not());
        return ImmutableList.copyOf(rv);
    }

    public Collection<Predicate> string(StringExpression expr, StringExpression other) {
        HashSet<Predicate> rv = new HashSet<Predicate>();
        if (module != Module.LUCENE) {
            rv.addAll(comparable(expr, other));

            rv.add(expr.charAt(0).eq(other.charAt(0)));
            rv.add(expr.charAt(1).eq(other.charAt(1)));
        }

        rv.add(expr.contains(other));
        rv.add(expr.contains(other.substring(0,1)));
        rv.add(expr.contains(other.substring(0,2)));
        rv.add(expr.contains(other.substring(1,2)));
        rv.add(expr.contains(other.substring(1)));
        rv.add(expr.contains(other.substring(2)));

        rv.add(expr.containsIgnoreCase(other));
        rv.add(expr.containsIgnoreCase(other.lower()));
        rv.add(expr.containsIgnoreCase(other.upper()));
        rv.add(expr.containsIgnoreCase(other.substring(0,1)));
        rv.add(expr.containsIgnoreCase(other.substring(0,2).lower()));
        rv.add(expr.containsIgnoreCase(other.substring(1,2).upper()));
        rv.add(expr.containsIgnoreCase(other.substring(1).lower()));
        rv.add(expr.containsIgnoreCase(other.substring(2).upper()));

        rv.add(expr.endsWith(other));
        rv.add(expr.endsWith(other.substring(1)));
        rv.add(expr.endsWith(other.substring(2)));
        
        rv.add(expr.endsWithIgnoreCase(other));        
        rv.add(expr.endsWithIgnoreCase(other.substring(1)));
        rv.add(expr.endsWithIgnoreCase(other.substring(2)));

        rv.add(expr.eq(other));
        rv.add(expr.equalsIgnoreCase(other));

        if (module != Module.LUCENE) {
            rv.add(expr.indexOf(other).eq(0));            
            rv.add(expr.locate(other).eq(1));
        }

        if (target != Target.DERBY && module != Module.LUCENE) {
            rv.add(expr.indexOf(other.substring(1)).eq(1));
            rv.add(expr.indexOf(other.substring(2)).eq(2));
        }

        if (module != Module.LUCENE) {
            rv.add(expr.isEmpty().not());
            rv.add(expr.isNotEmpty());
        }

        if (module != Module.LUCENE) {
            rv.add(expr.length().eq(other.length()));
            rv.add(expr.like(other));
            
            if (module != Module.JDO || other instanceof Constant<?>) {
                rv.add(expr.like(other.substring(0,1).append("%")));
                rv.add(expr.like(other.substring(0,1).append("%").append(other.substring(2))));
                rv.add(expr.like(other.substring(1).prepend("%")));
                rv.add(expr.like(other.substring(1,2).append("%").prepend("%")));    
            }            
        }

        rv.add(expr.lower().eq(other.lower()));

        if (module != Module.LUCENE) {
            if (!module.equals(Module.SQL)
            || (!target.equals(Target.HSQLDB)
            && !target.equals(Target.FIREBIRD)
            && !target.equals(Target.H2)
            && !target.equals(Target.DB2)
            && !target.equals(Target.DERBY)
            && !target.equals(Target.SQLITE)
            && !target.equals(Target.SQLSERVER))) {
                
                rv.add(expr.matches(other));
                
                if (module != Module.JDO || other instanceof Constant<?>) {
                    rv.add(expr.matches(other.substring(0,1).append(".*")));
                    rv.add(expr.matches(other.substring(0,1).append(".").append(other.substring(2))));
                    rv.add(expr.matches(other.substring(1).prepend(".*")));
                    rv.add(expr.matches(other.substring(1,2).prepend(".*").append(".*")));    
                }                
            }

            rv.add(expr.ne(other));
        }

        rv.add(expr.startsWith(other));        
        rv.add(expr.startsWith(other.substring(0,1)));        
        rv.add(expr.startsWith(other.substring(0,2)));
        
        rv.add(expr.startsWithIgnoreCase(other));
        rv.add(expr.startsWithIgnoreCase(other.substring(0,1)));
        rv.add(expr.startsWithIgnoreCase(other.substring(0,2)));

        if (module != Module.LUCENE) {
            rv.add(expr.substring(0,1).eq(other.substring(0,1)));
            rv.add(expr.substring(1,2).eq(other.substring(1,2)));
            rv.add(expr.substring(1).eq(other.substring(1)));

            rv.add(expr.trim().eq(other.trim()));
        }

        rv.add(expr.upper().eq(other.upper()));
        return ImmutableList.copyOf(rv);
    }

    public Collection<Predicate> string(StringExpression expr, StringExpression other,  
            String knownValue) {
        HashSet<Predicate> rv = new HashSet<Predicate>();
        rv.addAll(string(expr, other));
        rv.addAll(string(expr, StringConstant.create(knownValue)));
        return ImmutableList.copyOf(rv);
    }

    public Collection<Predicate> time(TimeExpression<java.sql.Time> expr,  
            TimeExpression<java.sql.Time> other) {
        HashSet<Predicate> rv = new HashSet<Predicate>();
        rv.addAll(comparable(expr, other));
        rv.add(expr.milliSecond().eq(other.milliSecond()));
        rv.add(expr.second().eq(other.second()));
        rv.add(expr.minute().eq(other.minute()));
        rv.add(expr.hour().eq(other.hour()));
        return ImmutableList.copyOf(rv);
    }

    public Collection<Predicate> time(TimeExpression<java.sql.Time> expr,  
            TimeExpression<java.sql.Time> other, java.sql.Time knownValue) {
        HashSet<Predicate> rv = new HashSet<Predicate>();
        rv.addAll(time(expr, other));
        rv.addAll(time(expr, TimeConstant.create(knownValue)));
        return ImmutableList.copyOf(rv);
    }
}
