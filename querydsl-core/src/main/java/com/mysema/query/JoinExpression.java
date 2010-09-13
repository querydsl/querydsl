/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;

/**
 * JoinExpression is a join element in a {@link Query} instance.
 *
 * @author tiwe
 */
public final class JoinExpression implements Serializable{

    private static final long serialVersionUID = -1131755765747174886L;

    private BooleanBuilder condition = new BooleanBuilder();

    private final Set<JoinFlag> flags = new LinkedHashSet<JoinFlag>();

    private final Expression<?> target;

    private final JoinType type;

    public JoinExpression(JoinType type, Expression<?> target) {
        this.type = Assert.notNull(type,"type");
        this.target = Assert.notNull(target,"target");
    }

    public Predicate getCondition() {
        return condition.getValue();
    }

    public void addCondition(Predicate c) {
        condition.and(c);
    }

    public Expression<?> getTarget() {
        return target;
    }

    public JoinType getType() {
        return type;
    }

    public void addFlag(JoinFlag flag){
        flags.add(flag);
    }

    public boolean hasFlag(JoinFlag flag){
        return flags.contains(flag);
    }
    
    public Set<JoinFlag> getFlags(){
        return flags;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(type).append(" ").append(target);
        if (condition.getValue() != null) {
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
