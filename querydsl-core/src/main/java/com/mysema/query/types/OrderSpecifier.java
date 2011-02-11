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
 * @param <T> related expression type
 * @author tiwe
 */
@SuppressWarnings({"unchecked"})
@Immutable
public class OrderSpecifier<T extends Comparable> implements Serializable {

    private static final long serialVersionUID = 3427652988262514678L;

    private final Order order;

    private final Expression<T> target;

    public OrderSpecifier(Order order, Expression<T> target) {
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
    public Expression<T> getTarget() {
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
