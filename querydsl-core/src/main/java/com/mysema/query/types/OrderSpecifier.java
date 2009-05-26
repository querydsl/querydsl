/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.expr.Expr;

/**
 * OrderSpecifier represents an order by element in a Query instance
 * 
 * @param <A>
 *            *
 * @author tiwe
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public class OrderSpecifier<A extends Comparable> {
    private final Order order;
    private final Expr<A> target;

    public OrderSpecifier(Order order, Expr<A> target) {
        this.order = Assert.notNull(order);
        this.target = Assert.notNull(target);
    }

    public Order getOrder() {
        return order;
    }

    public boolean isAscending() {
        return order == Order.ASC;
    }

    public Expr<A> getTarget() {
        return target;
    }

    public String toString() {
        return target + " " + order;
    }

}
