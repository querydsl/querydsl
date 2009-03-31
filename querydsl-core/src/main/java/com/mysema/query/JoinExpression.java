/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.mysema.query.grammar.types.Expr;
import com.mysema.query.util.Assert;

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
    
    public JoinExpression(Expr<?> target){
        this(JoinType.DEFAULT, target, null);
    }
    
    public JoinExpression(JoinType type, Expr<?> target) {
        this(type, target, null);
    }
    
    public JoinExpression(JoinType type, Expr<?> target, T metadata) {
        this.type = type;
        this.target = Assert.notNull(target);
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
        StringBuilder builder = new StringBuilder();
        builder.append(type).append(" ").append(target);
        if (condition != null){
            builder.append(" ON ").append(condition);
        }
        return builder.toString();
    }
    
    @Override
    public int hashCode(){
        return target.hashCode();
    }
    
    @Override
    public boolean equals(Object o){
        if (o instanceof JoinExpression){
            JoinExpression<?> j = (JoinExpression<?>)o;
            return new EqualsBuilder()
                .append(condition, j.condition)
                .append(metadata, j.metadata)
                .append(target, j.target)
                .append(type, j.type).isEquals();
        }else{
            return false;
        }
    }
    
}