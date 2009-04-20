/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import com.mysema.query.grammar.types.Expr;
import com.mysema.query.util.Assert;

/**
 * OrderSpecifier represents an order by element in a Query instance
 * 
 * @param <A>  * 
 * @author tiwe
 * @version $Id$
 */
public class OrderSpecifier<A extends Comparable<?>> {
    private final Order order;
    private final  Expr<A> target;
    
    public OrderSpecifier(Order order, Expr<A> target){
        this.order = Assert.notNull(order);
        this.target = Assert.notNull(target);
    }
    
    public Order getOrder() {
        return order;
    }
    
    public boolean isAscending(){
        return order == Order.ASC;        
    }
    
    public Expr<A> getTarget() {
        return target;
    }

    public String toString(){
        return target + " " + order;
    }
    
}
