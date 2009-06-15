/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * JoinExpression is a join element in a Query instance. 
 * 
 * @author tiwe
 * @version $Id$
 */
public class JoinExpression {
    private EBoolean condition;
    private boolean fetch;
    private final Expr<?> target;
    private final JoinType type;

    public JoinExpression(JoinType type, Expr<?> target) {
        this.type = type;
        this.target = Assert.notNull(target);
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

    public boolean isFetch() {
        return fetch;
    }

    public void setFetch(boolean fetch) {
        this.fetch = fetch;
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
        if (o instanceof JoinExpression) {
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