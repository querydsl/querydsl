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
package com.querydsl.core.types.dsl;

import javax.annotation.Nullable;

import com.querydsl.core.types.*;

/**
 * {@code BooleanExpression} represents {@link java.lang.Boolean} expressions
 *
 * @author tiwe
 * @see java.lang.Boolean
 *
 */
public abstract class BooleanExpression extends LiteralExpression<Boolean> implements Predicate {

    private static final long serialVersionUID = 3797956062512074164L;

    @Nullable
    private transient volatile BooleanExpression eqTrue, eqFalse;

    @Nullable
    private transient volatile BooleanExpression not;

    public BooleanExpression(Expression<Boolean> mixin) {
        super(mixin);
    }

    @Override
    public BooleanExpression as(Path<Boolean> alias) {
        return Expressions.booleanOperation(Ops.ALIAS, mixin, alias);
    }

    @Override
    public BooleanExpression as(String alias) {
        return as(ExpressionUtils.path(Boolean.class, alias));
    }

    /**
     * Create a {@code this && right} expression
     *
     * <p>Returns an intersection of this and the given expression</p>
     *
     * @param right right hand side of the union
     * @return {@code this &amp;&amp; right}
     */
    public BooleanExpression and(@Nullable Predicate right) {
        right = (Predicate) ExpressionUtils.extract(right);
        if (right != null) {
            return Expressions.booleanOperation(Ops.AND, mixin, right);
        } else {
            return this;
        }
    }

    /**
     * Create a {@code this && any(predicates)} expression
     *
     * <p>Returns an intersection of this and the union of the given predicates</p>
     *
     * @param predicates union of predicates
     * @return this &amp;&amp; any(predicates)
     */
    public BooleanExpression andAnyOf(Predicate... predicates) {
        return and(ExpressionUtils.anyOf(predicates));
    }

    /**
     * Create a {@code !this} expression
     *
     * <p>Returns a negation of this boolean expression</p>
     *
     * @return !this
     */
    @Override
    public BooleanExpression not() {
        if (not == null) {
            // uses this, because it makes unwrapping easier
            not = Expressions.booleanOperation(Ops.NOT, this);
        }
        return not;
    }

    /**
     * Create a {@code this || right} expression
     *
     * <p>Returns a union of this and the given expression</p>
     *
     * @param right right hand side of the union
     * @return this || right
     */
    public BooleanExpression or(@Nullable Predicate right) {
        right = (Predicate) ExpressionUtils.extract(right);
        if (right != null) {
            return Expressions.booleanOperation(Ops.OR, mixin, right);
        } else {
            return this;
        }
    }

    /**
     * Create a {@code this or all(predicates)} expression
     *
     * <p>Return a union of this and the intersection of the given predicates</p>
     *
     * @param predicates intersection of predicates
     * @return this or all(predicates)
     */
    public BooleanExpression orAllOf(Predicate... predicates) {
        return or(ExpressionUtils.allOf(predicates));
    }

    /**
     * Create a {@code this == true} expression
     *
     * @return this == true
     */
    public BooleanExpression isTrue() {
        return eq(true);
    }

    /**
     * Create a {@code this == false} expression
     *
     * @return this == false
     */
    public BooleanExpression isFalse() {
        return eq(false);
    }

    @Override
    public BooleanExpression eq(Boolean right) {
        if (right) {
            if (eqTrue == null) {
                eqTrue = super.eq(true);
            }
            return eqTrue;
        } else {
            if (eqFalse == null) {
                eqFalse = super.eq(false);
            }
            return eqFalse;
        }
    }
}
