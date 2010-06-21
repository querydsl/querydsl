/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql;

import com.mysema.query.types.Expr;
import com.mysema.query.types.Ops;
import com.mysema.query.types.expr.ECollection;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.OComparable;
import com.mysema.query.types.expr.ONumber;
import com.mysema.query.types.expr.OSimple;

/**
 * HQLGrammar provides factory methods for HQL specific operations
 * elements.
 *
 * @author tiwe
 * @version $Id$
 */
public final class HQLGrammar {

    private HQLGrammar(){}

    /**
     * @param <D>
     * @param col
     * @return
     */
    public static <D> Expr<D> all(ECollection<?,D> col) {
        return OSimple.create(col.getElementType(), Ops.QuantOps.ALL, (Expr<?>)col);
    }

    /**
     * @param <D>
     * @param col
     * @return
     */
    public static <D> Expr<D> any(ECollection<?,D> col) {
        return OSimple.create(col.getElementType(), Ops.QuantOps.ANY, (Expr<?>)col);
    }

    /**
     * @param <A>
     * @param col
     * @return
     */
    public static <A extends Comparable<? super A>> EComparable<A> avg(ECollection<?,A> col) {
        return OComparable.create(col.getElementType(), Ops.QuantOps.AVG_IN_COL, (Expr<?>)col);
    }

    /**
     * @param <A>
     * @param left
     * @return
     */
    public static <A extends Comparable<? super A>> EComparable<A> max(ECollection<?,A> left) {
        return OComparable.create(left.getElementType(), Ops.QuantOps.MAX_IN_COL, (Expr<?>)left);
    }

    /**
     * @param <A>
     * @param left
     * @return
     */
    public static <A extends Comparable<? super A>> EComparable<A> min(ECollection<?,A> left) {
        return OComparable.create(left.getElementType(), Ops.QuantOps.MIN_IN_COL, (Expr<?>)left);
    }

    /**
     * @param <D>
     * @param col
     * @return
     */
    public static <D> Expr<D> some(ECollection<?,D> col) {
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
        return ONumber.create((Class<D>) type, Ops.AggOps.SUM_AGG, left);
    }

    /**
     * @param <D>
     * @param left
     * @return
     */
    public static <D extends Number & Comparable<? super D>> ENumber<Long> sumAsLong(Expr<D> left) {
        return sum(left).longValue();
    }

    /**
     * @param <D>
     * @param left
     * @return
     */
    public static <D extends Number & Comparable<? super D>> ENumber<Double> sumAsDouble(Expr<D> left) {
        return sum(left).doubleValue();
    }

}
