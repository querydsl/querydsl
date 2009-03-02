/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.types.Expr;

/**
 * JoinExpression represents a join element in a Query instance
 * 
 * @author tiwe
 * @version $Id$
 */
public class JoinExpression<T> {
    private Expr.EBoolean condition;
    private final Expr<?> target;
    private final JoinType type;
    private final T metadata;
    
    public JoinExpression(JoinType type, Expr<?> target) {
        this.type = type;
        this.target = target;
        this.metadata = null;
    }
    
    public JoinExpression(JoinType type, Expr<?> target, T metadata) {
        this.type = type;
        this.target = target;
        this.metadata = metadata;
    }
    
    public Expr.EBoolean getCondition() {
        return condition;
    }
    
    public void setCondition(Expr.EBoolean condition) {
        this.condition = condition;
    }
    
    public Expr<?> getTarget() {
        return target;
    }
    
    public JoinType getType() {
        return type;
    }

    public T getMetadata() {
        return metadata;
    }
    
    public String toString(){
        return type + " " + target;
    }
    
}