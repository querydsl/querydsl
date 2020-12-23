/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.core;

import java.io.Serializable;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.util.CollectionUtils;

/**
 * {@code JoinExpression} is a join element in a {@link Query} instance.
 *
 * @author tiwe
 */
@Immutable
public final class JoinExpression implements Serializable {

    private static final long serialVersionUID = -1131755765747174886L;

    @Nullable
    private final Predicate condition;

    private final Set<JoinFlag> flags;

    private final Expression<?> target;

    private final JoinType type;

    /**
     * Create a new JoinExpression instance
     *
     * @param type type of join
     * @param target target of join
     */
    public JoinExpression(JoinType type, Expression<?> target) {
        this(type, target, null, Collections.emptySet());
    }


    /**
     * Create a new JoinExpression instance
     *
     * @param type type of join
     * @param target target of join
     * @param condition join condition or null, if none is used
     * @param flags join flags
     */
    public JoinExpression(JoinType type, Expression<?> target, @Nullable Predicate condition,
            Set<JoinFlag> flags) {
        this.type = type;
        this.target = target;
        this.condition = condition;
        this.flags = CollectionUtils.unmodifiableSet(flags);
    }

    @Nullable
    public Predicate getCondition() {
        return condition;
    }

    public Expression<?> getTarget() {
        return target;
    }

    public JoinType getType() {
        return type;
    }

    public boolean hasFlag(JoinFlag flag) {
        return flags.contains(flag);
    }

    public Set<JoinFlag> getFlags() {
        return flags;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(type).append(" ").append(target);
        if (condition != null) {
            builder.append(" on ").append(condition);
        }
        return builder.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(condition, target, type);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof JoinExpression) {
            JoinExpression j = (JoinExpression) o;
            return Objects.equals(condition, j.condition) &&
                   Objects.equals(target, j.target) &&
                   Objects.equals(type, j.type);
        } else {
            return false;
        }
    }

}
