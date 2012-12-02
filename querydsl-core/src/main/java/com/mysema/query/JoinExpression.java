/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import com.google.common.base.Objects;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;

/**
 * JoinExpression is a join element in a {@link Query} instance.
 *
 * @author tiwe
 */
public final class JoinExpression implements Serializable{

    private static final long serialVersionUID = -1131755765747174886L;

    private final BooleanBuilder condition = new BooleanBuilder();

    private final Set<JoinFlag> flags = new LinkedHashSet<JoinFlag>();

    private final Expression<?> target;

    private final JoinType type;

    /**
     * Create a new JoinExpression instance
     * 
     * @param type
     * @param target
     */
    public JoinExpression(JoinType type, Expression<?> target) {
        this.type = type;
        this.target = target;
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
        return Objects.hashCode(condition, target, type);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof JoinExpression) {
            JoinExpression j = (JoinExpression) o;
            return Objects.equal(condition, j.condition) &&
                   Objects.equal(target, j.target) &&
                   Objects.equal(type, j.type);
        } else {
            return false;
        }
    }

}
