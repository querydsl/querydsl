/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.expr.Expr;

/**
 * OrderSpecifier represents an order-by-element in a Query instance
 * 
 * @param <A> - Java type of the target expression
 * @author tiwe
 * @version $Id$
 */
@SuppressWarnings("unchecked")
@Immutable
public class OrderSpecifier<A extends Comparable> {
    
    private final Order order;
    
    private final Expr<A> target;

    public OrderSpecifier(Order order, Expr<A> target) {
        this.order = Assert.notNull(order,"order is null");
        this.target = Assert.notNull(target,"target is null");
    }

    /**
     * Get the order of this specifier
     * 
     * @return
     */
    public Order getOrder() {
        return order;
    }

    /**
     * Get whether the order is ascending or not
     * 
     * @return
     */
    public boolean isAscending() {
        return order == Order.ASC;
    }

    /**
     * Get the target expression of this OrderSpecifier
     * 
     * @return
     */
    public Expr<A> getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return target + " " + order;
    }

}
