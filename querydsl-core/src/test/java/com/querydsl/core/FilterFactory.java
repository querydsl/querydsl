/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import java.util.*;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.MapExpression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;

/**
 * @author tiwe
 *
 */
public class FilterFactory {

    private final ProjectionsFactory projections;

    private final Module module;

    private final Target target;

    public FilterFactory(ProjectionsFactory projections, Module module, Target target) {
        this.projections = projections;
        this.module = module;
        this.target = target;
    }

    public Collection<Predicate> booleanFilters(BooleanExpression expr, BooleanExpression other) {
        HashSet<Predicate> rv = new HashSet<Predicate>();
        rv.add(expr.and(other));
        rv.add(expr.or(other));
        rv.add(expr.not().and(other.not()));
        rv.add(expr.not());
        rv.add(other.not());
        return ImmutableList.copyOf(rv);
    }

    public <A> Collection<Predicate> collection(CollectionExpressionBase<?,A> expr,
            CollectionExpression<?,A> other, A knownElement) {
        HashSet<Predicate> rv = new HashSet<Predicate>();
        rv.add(expr.contains(knownElement));
        rv.add(expr.isEmpty());
        rv.add(expr.isNotEmpty());
        if (!module.equals(Module.RDFBEAN)) {
            rv.add(expr.size().gt(0));
        }
        return ImmutableList.copyOf(rv);
    }

    public <A> Collection<Predicate> array(ArrayExpression<A[], A> expr, ArrayExpression<A[], A> other,
            A knownElement) {
        HashSet<Predicate> rv = new HashSet<Predicate>();
        if (!module.equals(Module.RDFBEAN)) {
            rv.add(expr.size().gt(0));
        }
        rv.add(expr.get(0).eq(knownElement));
        return ImmutableList.copyOf(rv);
    }

    private <A extends Comparable<A>> Collection<Predicate> comparable(ComparableExpression<A> expr,
            ComparableExpression<A> other, A knownValue) {
        List<Predicate> rv = new ArrayList<Predicate>();
        rv.addAll(exprFilters(expr, other, knownValue));
        rv.add(expr.gt(other));
        rv.add(expr.gt(knownValue));
        rv.add(expr.goe(other));
        rv.add(expr.goe(knownValue));
        rv.add(expr.lt(other));
        rv.add(expr.lt(knownValue));
        rv.add(expr.loe(other));
        rv.add(expr.loe(knownValue));

//        rv.add(expr.in(IntervalImpl.create(knownValue, null)));
//        rv.add(expr.in(IntervalImpl.create(null, knownValue)));

        return ImmutableList.copyOf(rv);

    }

    private <A extends Comparable<A>> Collection<Predicate> dateOrTime(TemporalExpression<A> expr,
            TemporalExpression<A> other, A knownValue) {
        List<Predicate> rv = new ArrayList<Predicate>();
        rv.add(expr.after(other));
        rv.add(expr.after(knownValue));
        rv.add(expr.before(other));
        rv.add(expr.before(knownValue));
        return ImmutableList.copyOf(rv);
    }

    @SuppressWarnings("unchecked")
    public <A extends Comparable> Collection<Predicate> date(DateExpression<A> expr,
            DateExpression<A> other, A knownValue) {
        List<Predicate> rv = new ArrayList<Predicate>();
        rv.addAll(comparable(expr, other, knownValue));
        rv.addAll(dateOrTime(expr, other, knownValue));
        rv.add(expr.dayOfMonth().eq(other.dayOfMonth()));
        rv.add(expr.month().eq(other.month()));
        rv.add(expr.year().eq(other.year()));
        rv.add(expr.yearMonth().eq(other.yearMonth()));
        if (module.equals(Module.SQL) || module.equals(Module.COLLECTIONS)) {
            if (target != Target.DERBY) {
                rv.add(expr.yearWeek().eq(other.yearWeek()));
            }
        }
        return ImmutableList.copyOf(rv);
    }

    @SuppressWarnings("unchecked")
    public <A extends Comparable> Collection<Predicate> dateTime(DateTimeExpression<A> expr,
            DateTimeExpression<A> other, A knownValue) {
        List<Predicate> rv = new ArrayList<Predicate>();
        rv.addAll(comparable(expr, other, knownValue));
        rv.addAll(dateOrTime(expr, other, knownValue));
        rv.add(expr.dayOfMonth().eq(1));
        rv.add(expr.dayOfMonth().eq(other.dayOfMonth()));

        rv.add(expr.month().eq(1));
        rv.add(expr.month().eq(other.month()));

        rv.add(expr.year().eq(2000));
        rv.add(expr.year().eq(other.year()));

        rv.add(expr.yearMonth().eq(other.yearMonth()));

        if (module.equals(Module.SQL) || module.equals(Module.COLLECTIONS)) {
            if (target != Target.DERBY) {
                rv.add(expr.yearWeek().eq(other.yearWeek()));
            }
        }

        rv.add(expr.hour().eq(1));
        rv.add(expr.hour().eq(other.hour()));

        rv.add(expr.minute().eq(1));
        rv.add(expr.minute().eq(other.minute()));

        rv.add(expr.second().eq(1));
        rv.add(expr.second().eq(other.second()));
        return ImmutableList.copyOf(rv);
    }

    private <A> Collection<BooleanExpression> exprFilters(SimpleExpression<A> expr,
            SimpleExpression<A> other, A knownValue) {
        HashSet<BooleanExpression> rv = new HashSet<BooleanExpression>();
        rv.add(expr.eq(other));
        rv.add(expr.eq(knownValue));

        rv.add(expr.ne(other));
        rv.add(expr.ne(knownValue));
        return ImmutableList.copyOf(rv);
    }

    public <A, Q extends SimpleExpression<A>> Collection<Predicate> list(ListPath<A, Q> expr,
            ListExpression<A, Q> other, A knownElement) {
        List<Predicate> rv = new ArrayList<Predicate>();
        rv.addAll(collection(expr, other, knownElement));
        rv.add(expr.get(0).eq(knownElement));
        return ImmutableList.copyOf(rv);
    }

    public <K,V> Collection<Predicate> map(MapExpressionBase<K,V,?> expr,
            MapExpression<K,V> other, K knownKey, V knownValue) {
        HashSet<Predicate> rv = new HashSet<Predicate>();
        rv.add(expr.containsKey(knownKey));
        rv.add(expr.containsValue(knownValue));
        rv.add(expr.get(knownKey).eq(knownValue));
        rv.add(expr.get(knownKey).ne(knownValue));
        rv.add(expr.isEmpty());
        rv.add(expr.isNotEmpty());
        if (!module.equals(Module.RDFBEAN)) {
            rv.add(expr.size().gt(0));
        }
        return ImmutableList.copyOf(rv);
    }

    @SuppressWarnings("unchecked")
    public <A extends Number & Comparable<A>> Collection<Predicate> numeric(NumberExpression<A> expr,
            NumberExpression<A> other, A knownValue) {
        List<Predicate> rv = new ArrayList<Predicate>();
        for (NumberExpression<?> num : projections.numeric(expr, other, knownValue, true)) {
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

        rv.add(expr.in(1,2,3));
        rv.add(expr.in(1L,2L,3L));

        if (expr.getType().equals(Integer.class)) {
            NumberExpression<Integer> eint = (NumberExpression) expr;
            rv.add(eint.between(1, 2));
            rv.add(eint.notBetween(1, 2));
            rv.add(eint.mod(5).eq(0));
        } else if (expr.getType().equals(Double.class)) {
            NumberExpression<Double> edouble = (NumberExpression) expr;
            rv.add(edouble.between(1.0, 2.0));
            rv.add(edouble.notBetween(1.0, 2.0));
        } else if (expr.getType().equals(Long.class)) {
            NumberExpression<Long> elong = (NumberExpression) expr;
            rv.add(elong.mod(5L).eq(0L));
        }

//        rv.add(expr.in(IntervalImpl.create(0, 100)));

        return ImmutableList.copyOf(rv);
    }

    public <A> Collection<Predicate> pathFilters(SimpleExpression<A> expr,
            SimpleExpression<A> other, A knownValue) {
        return Arrays.<Predicate>asList(
             expr.isNull(),
             expr.isNotNull()
        );
    }

    @SuppressWarnings("unchecked")
    public Collection<Predicate> string(StringExpression expr, StringExpression other,
            String knownValue) {
        List<Predicate> rv = new ArrayList<Predicate>();
        if (expr instanceof Path && other instanceof Path) {
            rv.addAll(pathFilters(expr, other, knownValue));
        }
        rv.addAll(comparable(expr, other, knownValue));
        for (SimpleExpression<String> eq : projections.string(expr, other, knownValue)) {
            rv.add(eq.eq(other));
        }
        rv.add(expr.between("A", "Z"));

        rv.add(expr.charAt(0).eq(knownValue.charAt(0)));

        rv.add(expr.notBetween("A", "Z"));

        rv.add(expr.contains(other));
        rv.add(expr.contains(knownValue.substring(0,1)));
        rv.add(expr.contains(knownValue.substring(1,2)));

        rv.add(expr.containsIgnoreCase(other));
        rv.add(expr.containsIgnoreCase(knownValue.substring(0,1)));
        rv.add(expr.containsIgnoreCase(knownValue.substring(1,2)));

        rv.add(expr.endsWith(other));
        rv.add(expr.endsWith(knownValue.substring(1)));
        rv.add(expr.endsWith(knownValue.substring(2)));

        rv.add(expr.equalsIgnoreCase(other));
        rv.add(expr.equalsIgnoreCase(knownValue));

        rv.add(expr.in(Arrays.asList(knownValue)));

        rv.add(expr.indexOf(other).gt(0));
        rv.add(expr.indexOf("X", 1).gt(0));
        rv.add(expr.indexOf(knownValue).gt(0));

        rv.add(expr.locate(other).gt(1));
        if (!target.equals(Target.FIREBIRD)) { // FIXME
            rv.add(expr.locate("X", 2).gt(1));
        }
        rv.add(expr.locate(knownValue).gt(1));

//        if (!module.equals(Module.HQL) && !module.equals(Module.JDOQL) && !module.equals(Module.SQL)) {
//            rv.add(expr.lastIndexOf(other).gt(0));
//            rv.add(expr.lastIndexOf(knownValue).gt(0));
//        }

        rv.add(expr.in("A","B","C"));

        rv.add(expr.isEmpty());
        rv.add(expr.isNotEmpty());

        rv.add(expr.length().gt(0));

        rv.add(expr.like(knownValue.substring(0,1) + "%"));
        rv.add(expr.like("%" + knownValue.substring(1)));
        rv.add(expr.like("%" + knownValue.substring(1,2) + "%"));

        rv.add(expr.like(knownValue.substring(0,1) + "%", '!'));
        rv.add(expr.like("%" + knownValue.substring(1), '!'));
        rv.add(expr.like("%" + knownValue.substring(1,2) + "%", '!'));

        rv.add(expr.likeIgnoreCase(knownValue.substring(0, 1) + "%"));
        rv.add(expr.likeIgnoreCase("%" + knownValue.substring(1)));
        rv.add(expr.likeIgnoreCase("%" + knownValue.substring(1, 2) + "%"));

        rv.add(expr.likeIgnoreCase(knownValue.substring(0, 1) + "%", '!'));
        rv.add(expr.likeIgnoreCase("%" + knownValue.substring(1), '!'));
        rv.add(expr.likeIgnoreCase("%" + knownValue.substring(1, 2) + "%", '!'));

        rv.add(expr.notLike(knownValue.substring(0,1) + "%"));
        rv.add(expr.notLike("%" + knownValue.substring(1)));
        rv.add(expr.notLike("%" + knownValue.substring(1,2) + "%"));

        if (!target.equals(Target.DERBY)
                && !target.equals(Target.DB2)
                && !target.equals(Target.FIREBIRD)
                && !target.equals(Target.HSQLDB)
                && !target.equals(Target.H2)
                && !target.equals(Target.SQLITE)
                && !target.equals(Target.SQLSERVER)) {
            rv.add(expr.matches(knownValue.substring(0, 1) + ".*"));
            rv.add(expr.matches(".*" + knownValue.substring(1)));
            rv.add(expr.matches(".*" + knownValue.substring(1, 2) + ".*"));
        }

        rv.add(expr.notIn("A","B","C"));

        rv.add(expr.notBetween("A", "Z"));
        rv.add(expr.notBetween(other, other));

        if (!target.equals(Target.DERBY) && !module.equals(Module.JDO)) {
            // https://issues.apache.org/jira/browse/DERBY-4389
            rv.add(new Coalesce<String>(String.class, expr, other).getValue().eq("xxx"));
        }

//        rv.add(expr.in(IntervalImpl.create("A", "Z")));

        return ImmutableList.copyOf(rv);
    }

    @SuppressWarnings("unchecked")
    public <A extends Comparable> Collection<Predicate> time(TimeExpression<A> expr,
            TimeExpression<A> other, A knownValue) {
        List<Predicate> rv = new ArrayList<Predicate>();
        rv.addAll(comparable(expr, other, knownValue));
        rv.addAll(dateOrTime(expr, other, knownValue));
        rv.add(expr.hour().eq(other.hour()));
        rv.add(expr.minute().eq(other.minute()));
        rv.add(expr.second().eq(other.second()));
        return ImmutableList.copyOf(rv);
    }

}
