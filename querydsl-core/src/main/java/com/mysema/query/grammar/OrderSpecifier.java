/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import com.mysema.query.grammar.types.Expr;


/**
 * OrderSpecifier represents an order by element in a Query instance
 * 
 * @param <A>  * 
 * @author tiwe
 * @version $Id$
 */
public class OrderSpecifier<A extends Comparable<? super A>> {
    public Order order;
    public Expr<A> target;
    
    public String toString(){
        return target + " " + order;
    }
}