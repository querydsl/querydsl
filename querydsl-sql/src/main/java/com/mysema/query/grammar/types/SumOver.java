/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.ENumber;

/**
 * SumOver is a fluent type for Oracle specific sum over / partition by / order by constructs
 *
 * @author tiwe
 * @version $Id$
 */
public class SumOver<A extends Number & Comparable<? super A>> extends ENumber<A>{
    private Expr<A> target;        
    private Expr<?> partitionBy;
    private List<Expr<?>> orderBy = new ArrayList<Expr<?>>();
    public SumOver(Expr<A> expr) {
        super(expr.getType());
        target = expr;
    }
    public SumOver<A> partition(Expr<?> partitionBy){
        this.partitionBy = partitionBy;
        return this;
    }    
    public SumOver<A> order(Expr<?>... orderBy){
        this.orderBy.addAll(Arrays.asList(orderBy));
        return this;
    }    
    public Expr<A> getTarget() {
        return target;
    }
    public Expr<?> getPartitionBy() {
        return partitionBy;
    }
    public List<Expr<?>> getOrderBy() {
        return orderBy;
    }        
}