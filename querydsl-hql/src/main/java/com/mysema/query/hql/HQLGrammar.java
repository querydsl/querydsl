/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import com.mysema.query.alias.GrammarWithAlias;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ECollection;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.OComparable;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.OSimple;
import com.mysema.query.types.operation.Ops;

/**
 * HqlGrammar extends the Query DSL base grammar to provide HQL specific syntax
 * elements.
 * 
 * @author tiwe
 * @version $Id$
 */
public class HQLGrammar extends GrammarWithAlias {

    public static <D> Expr<D> all(ECollection<D> col) {
        return new OSimple<Object,D>(col.getElementType(), Ops.QuantOps.ALL, (Expr<?>)col);
    }

    public static <D> Expr<D> any(ECollection<D> col) {
        return new OSimple<Object,D>(col.getElementType(), Ops.QuantOps.ANY, (Expr<?>)col);
    }

    public static <A extends Comparable<? super A>> EComparable<A> avg(ECollection<A> col) {
        return new OComparable<Number,A>(col.getElementType(), Ops.QuantOps.AVG_IN_COL, (Expr<?>)col);
    }

    public static EBoolean exists(ECollection<?> col) {
        return new OBoolean(Ops.QuantOps.EXISTS, (Expr<?>)col);
    }

    public static <A extends Comparable<? super A>> EComparable<A> max(ECollection<A> left) {
        return new OComparable<Number,A>(left.getElementType(), Ops.QuantOps.MAX_IN_COL, (Expr<?>)left);
    }

    public static <A extends Comparable<? super A>> EComparable<A> min(ECollection<A> left) {
        return new OComparable<Number,A>(left.getElementType(), Ops.QuantOps.MIN_IN_COL, (Expr<?>)left);
    }

    public static EBoolean notExists(ECollection<?> col) {
        return new OBoolean(Ops.QuantOps.NOTEXISTS, (Expr<?>)col);
    }

    public static <D> Expr<D> some(ECollection<D> col) {
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
        return ONumber.create((Class<D>) type, Ops.SUM_AGG, left);
    }

    public static <D extends Number & Comparable<? super D>> ENumber<Long> sumAsLong(Expr<D> left) {
        return sum(left).longValue();
    }

    public static <D extends Number & Comparable<? super D>> ENumber<Double> sumAsDouble(Expr<D> left) {
        return sum(left).doubleValue();
    }

}
