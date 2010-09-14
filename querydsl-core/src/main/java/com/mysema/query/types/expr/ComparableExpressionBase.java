/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import javax.annotation.Nullable;

import com.mysema.query.types.Ops;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;

/**
 * ComparableExpressionBase represents comparable expressions
 *
 * @author tiwe
 *
 * @param <D> Java type
 * @see java.lang.Comparable
 */
@SuppressWarnings({"unchecked"})
public abstract class ComparableExpressionBase<D extends Comparable> extends SimpleExpression<D> {

    private static final long serialVersionUID = 1460921109546656911L;

    @Nullable
    private volatile OrderSpecifier<D> asc, desc;

    @Nullable
    private volatile StringExpression stringCast;

    public ComparableExpressionBase(Class<? extends D> type) {
        super(type);
    }

    /**
     * Get an OrderSpecifier for ascending order of this expression
     *
     * @return
     */
    public final OrderSpecifier<D> asc() {
        if (asc == null){
            asc = new OrderSpecifier<D>(Order.ASC, this);
        }
        return asc;
    }

    /**
     * Create a cast expression to the given numeric type
     *
     * @param <A>
     * @param type
     * @return
     */
    public <A extends Number & Comparable<? super A>> NumberExpression<A> castToNum(Class<A> type) {
        return NumberOperation.create(type, Ops.NUMCAST, this, SimpleConstant.create(type));
    }

    /**
     * Get an OrderSpecifier for descending order of this expression
     *
     * @return
     */
    public final OrderSpecifier<D> desc() {
        if (desc == null){
            desc = new OrderSpecifier<D>(Order.DESC, this);
        }
        return desc;
    }

    /**
     * Get a cast to String expression
     *
     * @see     java.lang.Object#toString()
     * @return
     */
    public StringExpression stringValue() {
        if (stringCast == null){
            stringCast = StringOperation.create(Ops.STRING_CAST, this);
        }
        return stringCast;
    }

}
