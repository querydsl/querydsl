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
import java.util.Collections;
import java.util.HashSet;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.Constant;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.MapExpression;
import com.querydsl.core.types.expr.ArrayExpression;
import com.querydsl.core.types.expr.CaseBuilder;
import com.querydsl.core.types.expr.CollectionExpressionBase;
import com.querydsl.core.types.expr.DateExpression;
import com.querydsl.core.types.expr.DateTimeExpression;
import com.querydsl.core.types.expr.ListExpression;
import com.querydsl.core.types.expr.MapExpressionBase;
import com.querydsl.core.types.expr.NumberExpression;
import com.querydsl.core.types.expr.SimpleExpression;
import com.querydsl.core.types.expr.StringExpression;
import com.querydsl.core.types.expr.TimeExpression;
import com.querydsl.core.types.path.ListPath;

/**
 * @author tiwe
 *
 */
public class ProjectionsFactory {

    private final Module module;

    private final Target target;

    public ProjectionsFactory(Module module, Target target) {
        this.module = module;
        this.target = target;
    }

    public <A> Collection<Expression<?>> array(ArrayExpression<A[], A> expr, ArrayExpression<A[], A> other, A knownElement) {
        HashSet<Expression<?>> rv = new HashSet<Expression<?>>();
        if (!module.equals(Module.RDFBEAN)) {
            rv.add(expr.size());
        }
        return ImmutableList.copyOf(rv);
    }

    public <A> Collection<Expression<?>> collection(CollectionExpressionBase<?,A> expr, CollectionExpression<?,A> other, A knownElement) {
        HashSet<Expression<?>> rv = new HashSet<Expression<?>>();
        if (!module.equals(Module.RDFBEAN)) {
            rv.add(expr.size());
        }
        return ImmutableList.copyOf(rv);
    }

    @SuppressWarnings("unchecked")
    public <A extends Comparable> Collection<Expression<?>> date(DateExpression<A> expr, DateExpression<A> other, A knownValue) {
        HashSet<Expression<?>> rv = new HashSet<Expression<?>>();
        rv.add(expr.dayOfMonth());
        rv.add(expr.month());
        rv.add(expr.year());
        rv.add(expr.yearMonth());

        if (module != Module.COLLECTIONS && module != Module.RDFBEAN) {
            rv.add(expr.min());
            rv.add(expr.max());
        }

        return ImmutableList.copyOf(rv);
    }

    @SuppressWarnings("unchecked")
    public <A extends Comparable> Collection<Expression<?>> dateTime(DateTimeExpression<A> expr, DateTimeExpression<A> other, A knownValue) {
        HashSet<Expression<?>> rv = new HashSet<Expression<?>>();
        rv.add(expr.dayOfMonth());
        rv.add(expr.month());
        rv.add(expr.year());
        rv.add(expr.yearMonth());
        rv.add(expr.hour());
        rv.add(expr.minute());
        rv.add(expr.second());

        if (module != Module.COLLECTIONS && module != Module.RDFBEAN) {
            rv.add(expr.min());
            rv.add(expr.max());
        }

        return ImmutableList.copyOf(rv);
    }

    public <A,Q extends SimpleExpression<A>> Collection<Expression<?>> list(ListPath<A,Q> expr, ListExpression<A,Q> other, A knownElement) {
        HashSet<Expression<?>> rv = new HashSet<Expression<?>>();
        rv.add(expr.get(0));
        if (!module.equals(Module.RDFBEAN)) {
            rv.add(expr.size());
        }
        return ImmutableList.copyOf(rv);
    }

    public <K,V> Collection<Expression<?>> map(MapExpressionBase<K,V,?> expr, MapExpression<K,V> other, K knownKey, V knownValue) {
        HashSet<Expression<?>> rv = new HashSet<Expression<?>>();
        rv.add(expr.get(knownKey));
        if (!module.equals(Module.RDFBEAN)) {
            rv.add(expr.size());
        }
        return ImmutableList.copyOf(rv);
    }

    public <A extends Number & Comparable<A>> Collection<NumberExpression<?>> numeric(NumberExpression<A> expr, NumberExpression<A> other, A knownValue, boolean forFilter) {
        HashSet<NumberExpression<?>> rv = new HashSet<NumberExpression<?>>();
        rv.addAll(numeric(expr, other, forFilter));
        rv.addAll(numeric(expr, NumberConstant.create(knownValue), forFilter));
        return ImmutableList.copyOf(rv);
    }

    @SuppressWarnings("unchecked")
    private <A extends Number & Comparable<A>> Collection<NumberExpression<?>> numeric(NumberExpression<A> expr, NumberExpression<?> other, boolean forFilter) {
        HashSet<NumberExpression<?>> rv = new HashSet<NumberExpression<?>>();
        rv.add(expr.abs());
        rv.add(expr.add(other));
        rv.add(expr.divide(other));     
        
        if (target != Target.HSQLDB) {
            rv.add(expr.negate());    
        }        
        
        rv.add(expr.multiply(other));
        rv.add(expr.sqrt());
        rv.add(expr.subtract(other));

        if (!forFilter && module != Module.COLLECTIONS && module != Module.RDFBEAN) {
            rv.add(expr.min());
            rv.add(expr.max());
            rv.add(expr.avg());
            rv.add(expr.count());
            rv.add(expr.countDistinct());
        }

        if (!(other instanceof Constant || module == Module.JDO || module == Module.RDFBEAN)) {
            CaseBuilder cases = new CaseBuilder();
            rv.add(NumberConstant.create(1).add(cases
                .when(expr.gt(10)).then(expr)
                .when(expr.between(0, 10)).then((NumberExpression<A>)other)
                .otherwise((NumberExpression<A>)other)));

            rv.add(expr
                    .when((NumberExpression<A>)other).then(expr)
                    .otherwise((NumberExpression<A>)other));
        }
        
        return ImmutableList.copyOf(rv);
    }

    public <A extends Number & Comparable<A>> Collection<NumberExpression<?>> numericCasts(NumberExpression<A> expr, NumberExpression<A> other, A knownValue) {
        if (!target.equals(Target.MYSQL)) {
            HashSet<NumberExpression<?>> rv = new HashSet<NumberExpression<?>>();
            rv.add(expr.byteValue());
            rv.add(expr.doubleValue());
            rv.add(expr.floatValue());
            rv.add(expr.intValue());
            rv.add(expr.longValue());
            rv.add(expr.shortValue());
            return ImmutableList.copyOf(rv);
        } else {
            return Collections.emptySet();
        }
    }

    public Collection<SimpleExpression<String>> string(StringExpression expr, StringExpression other, String knownValue) {
        HashSet<SimpleExpression<String>> rv = new HashSet<SimpleExpression<String>>();
        rv.addAll(stringProjections(expr, other));
        rv.addAll(stringProjections(expr, StringConstant.create(knownValue)));
        return rv;
    }

    @SuppressWarnings("unchecked")
    public Collection<SimpleExpression<String>> stringProjections(StringExpression expr, StringExpression other) {
        HashSet<SimpleExpression<String>> rv = new HashSet<SimpleExpression<String>>();

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

        if (!(other instanceof Constant || module == Module.JDO || module == Module.RDFBEAN)) {
            CaseBuilder cases = new CaseBuilder();
            rv.add(cases.when(expr.eq("A")).then(other)
                        .when(expr.eq("B")).then(expr)
                        .otherwise(other));

            rv.add(expr.when("A").then(other)
                       .when("B").then(expr)
                       .otherwise(other));
        }

        rv.add(expr.trim());

        rv.add(expr.upper());
        
        if (module != Module.JDO) {
            rv.add(expr.nullif("xxx"));    
        }        
        
        return ImmutableList.copyOf(rv);
    }

    @SuppressWarnings("unchecked")
    public <A extends Comparable> Collection<Expression<?>> time(TimeExpression<A> expr, TimeExpression<A> other, A knownValue) {
        HashSet<Expression<?>> rv = new HashSet<Expression<?>>();
        rv.add(expr.hour());
        rv.add(expr.minute());
        rv.add(expr.second());
        return ImmutableList.copyOf(rv);
    }

}
