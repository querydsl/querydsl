/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

public class OrderSpecifier<A extends Comparable<A>> {
    public Order order;
    public Types.Expr<A> target;
}