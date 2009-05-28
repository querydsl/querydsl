/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.Date;

import com.mysema.query.alias.GrammarWithAlias;
import com.mysema.query.hql.HQLPatterns.OpQuant;
import com.mysema.query.types.CollectionType;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.OComparable;
import com.mysema.query.types.operation.OSimple;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PCollection;
import com.mysema.query.types.path.PEntity;

/**
 * HqlGrammar extends the Query DSL base grammar to provide HQL specific syntax
 * elements.
 * 
 * @author tiwe
 * @version $Id$
 */
public class HQLGrammar extends GrammarWithAlias {

    public static <D> Expr<D> all(CollectionType<D> col) {
        return new OSimple<Object,D>(col.getElementType(), OpQuant.ALL, (Expr<?>)col);
    }

    public static <D> Expr<D> any(CollectionType<D> col) {
        return new OSimple<Object,D>(col.getElementType(), OpQuant.ANY, (Expr<?>)col);
    }

    public static <A extends Comparable<? super A>> EComparable<A> avg(PCollection<A> col) {
        return new OComparable<Number,A>(col.getElementType(), OpQuant.AVG_IN_COL, (Expr<?>)col);
    }

    public static EComparable<Date> current_date() {
        return operationFactory.createComparable(Date.class,Ops.DateTimeOps.CURRENT_DATE);
    }

    public static EComparable<Date> current_time() {
        return operationFactory.createComparable(Date.class,Ops.DateTimeOps.CURRENT_TIME);
    }

    public static EComparable<Date> current_timestamp() {
        return operationFactory.createComparable(Date.class,Ops.DateTimeOps.CURRENT_TIMESTAMP);
    }

    public static EComparable<Date> day(Expr<Date> date) {
        return operationFactory.createComparable(Date.class,Ops.DateTimeOps.DAY, date);
    }

    public static EBoolean exists(CollectionType<?> col) {
        return new OBoolean(OpQuant.EXISTS, (Expr<?>)col);
    }

    public static <A> SubQuery<HQLJoinMeta, A> from(PEntity<A> select) {
        return new SubQuery<HQLJoinMeta, A>(select).from(select);
    }

    public static EComparable<Date> hour(Expr<Date> date) {
        return operationFactory.createComparable(Date.class, Ops.DateTimeOps.HOUR, date);
    }

    public static <A extends Comparable<? super A>> EComparable<A> max(PCollection<A> left) {
        return new OComparable<Number,A>(left.getElementType(), OpQuant.MAX_IN_COL, (Expr<?>)left);
    }

    public static <A extends Comparable<? super A>> EComparable<A> min(PCollection<A> left) {
        return new OComparable<Number,A>(left.getElementType(), OpQuant.MIN_IN_COL, (Expr<?>)left);
    }

    public static EComparable<Date> minute(Expr<Date> date) {
        return operationFactory.createComparable(Date.class,Ops.DateTimeOps.MINUTE, date);
    }

    public static EComparable<Date> month(Expr<Date> date) {
        return operationFactory.createComparable(Date.class,Ops.DateTimeOps.MONTH, date);
    }

    public static <A> Expr<A> newInstance(Class<A> a, Expr<?>... args) {
        return new EConstructor<A>(a, args);
    }

    public static EBoolean notExists(CollectionType<?> col) {
        return new OBoolean(OpQuant.NOTEXISTS, (Expr<?>)col);
    }

    public static EComparable<Date> second(Expr<Date> date) {
        return operationFactory.createComparable(Date.class,Ops.DateTimeOps.SECOND, date);
    }

    public static <A> SubQuery<HQLJoinMeta, A> select(Expr<A> select) {
        return new SubQuery<HQLJoinMeta, A>(select);
    }

    public static <D> Expr<D> some(CollectionType<D> col) {
        return any(col);
    }

    /**
     * SUM returns Long when applied to state-fields of integral types (other
     * than BigInteger); Double when applied to state-fields of floating point
     * types; BigInteger when applied to state-fields of type BigInteger; and
     * BigDecimal when applied to state-fields of type BigDecimal.
     */
    @SuppressWarnings("unchecked")
    public static <D extends Number & Comparable<? super D>> ENumber<?> sum(Expr<D> left) {
        Class<?> type = left.getType();
        if (type.equals(Byte.class) || type.equals(Integer.class) || type.equals(Short.class)){
            type = Long.class;
        }else if (type.equals(Float.class)){
            type = Double.class;
        }            
        return operationFactory.createNumber((Class<D>) type, Ops.SUM_AGG, left);
    }

    public static <D extends Number & Comparable<? super D>> ENumber<Long> sumAsLong(Expr<D> left) {
        return sum(left).longValue();
    }

    public static <D extends Number & Comparable<? super D>> ENumber<Double> sumAsDouble(Expr<D> left) {
        return sum(left).doubleValue();
    }

    public static EComparable<Date> sysdate() {
        return operationFactory.createComparable(Date.class,Ops.DateTimeOps.SYSDATE);
    }

    public static EComparable<Date> year(Expr<Date> date) {
        return operationFactory.createComparable(Date.class,Ops.DateTimeOps.YEAR, date);
    }

}
