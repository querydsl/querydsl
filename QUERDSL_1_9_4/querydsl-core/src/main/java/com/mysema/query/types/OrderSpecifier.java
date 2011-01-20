/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.io.Serializable;

import net.jcip.annotations.Immutable;

import com.mysema.commons.lang.Assert;

/**
 * OrderSpecifier represents an order-by-element in a Query instance
 *
 * @param <A> - Java type of the target expression
 * @author tiwe
 * @version $Id$
 */
@SuppressWarnings({"unchecked"})
@Immutable
public class OrderSpecifier<A extends Comparable> implements Serializable {

    private static final long serialVersionUID = 3427652988262514678L;

    private final Order order;

    private final Expr<A> target;

    public OrderSpecifier(Order order, Expr<A> target) {
        this.order = Assert.notNull(order,"order");
        this.target = Assert.notNull(target,"target");
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
    
    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }else if (o instanceof OrderSpecifier){
            OrderSpecifier<?> os = (OrderSpecifier)o;
            return os.order.equals(order) && os.target.equals(target);
        }else{
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        return target.hashCode();
    }

}
