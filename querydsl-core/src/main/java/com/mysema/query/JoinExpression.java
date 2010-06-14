/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Expr;
import com.mysema.query.types.expr.EBoolean;

/**
 * JoinExpression is a join element in a {@link Query} instance. 
 * 
 * @author tiwe
 */
public final class JoinExpression {
    
    // mutable
    private EBoolean condition;

    private final Set<Object> flags = new HashSet<Object>();

    private final Expr<?> target;
    
    private final JoinType type;

    public JoinExpression(JoinType type, Expr<?> target) {
        this.type = Assert.notNull(type,"type");
        this.target = Assert.notNull(target,"target");
    }

    public EBoolean getCondition() {
        return condition;
    }

    public void setCondition(EBoolean condition) {
        this.condition = condition;
    }

    public Expr<?> getTarget() {
        return target;
    }

    public JoinType getType() {
        return type;
    }

    public void setFlag(Object flag){
        flags.add(flag);
    }
    
    public void removeFlag(Object flag){
        flags.remove(flag);
    }
    
    public boolean hasFlag(Object flag){
        return flags.contains(flag);
    }
    
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(type).append(" ").append(target);
        if (condition != null) {
            builder.append(" ON ").append(condition);
        }
        return builder.toString();
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }else if (o instanceof JoinExpression) {
            JoinExpression j = (JoinExpression) o;
            return new EqualsBuilder()
                .append(condition, j.condition)
                .append(target, j.target)
                .append(type, j.type).isEquals();
        } else {
            return false;
        }
    }

}