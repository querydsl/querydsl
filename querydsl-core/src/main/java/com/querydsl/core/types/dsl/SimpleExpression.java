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

import java.util.Arrays;
import java.util.Collection;

import org.jetbrains.annotations.Nullable;

import com.querydsl.core.types.*;
import com.querydsl.core.util.CollectionUtils;

/**
 * {@code SimpleExpression} is the base class for {@link Expression} implementations.
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public abstract class SimpleExpression<T> extends DslExpression<T> {

    private static final long serialVersionUID = -4405387187738167105L;

    @Nullable
    private transient volatile NumberExpression<Long> count;

    @Nullable
    private transient volatile NumberExpression<Long> countDistinct;

    @Nullable
    private transient volatile BooleanExpression isnull, isnotnull;

    public SimpleExpression(Expression<T> mixin) {
        super(mixin);
    }


    /**
     * Create an alias for the expression
     *
     * @return alias expression
     */
    @Override
    public SimpleExpression<T> as(Path<T> alias) {
        return Expressions.operation(getType(),Ops.ALIAS, mixin, alias);
    }

    /**
     * Create an alias for the expression
     *
     * @return alias expression
     */
    @Override
    public SimpleExpression<T> as(String alias) {
        return as(ExpressionUtils.path(getType(), alias));
    }

    /**
     * Create a {@code this is not null} expression
     *
     * @return this is not null
     */
    public BooleanExpression isNotNull() {
        if (isnotnull == null) {
            isnotnull = Expressions.booleanOperation(Ops.IS_NOT_NULL, mixin);
        }
        return isnotnull;
    }

    /**
     * Create a {@code this is null} expression
     *
     * @return this is null
     */
    public BooleanExpression isNull() {
        if (isnull == null) {
            isnull = Expressions.booleanOperation(Ops.IS_NULL, mixin);
        }
        return isnull;
    }

    /**
     * Get the {@code count(this)} expression
     *
     * @return count(this)
     */
    public NumberExpression<Long> count() {
        if (count == null) {
            count = Expressions.numberOperation(Long.class, Ops.AggOps.COUNT_AGG, mixin);
        }
        return count;
    }


    /**
     * Get the {@code count(distinct this)} expression
     *
     * @return count(distinct this)
     */
    public NumberExpression<Long> countDistinct() {
        if (countDistinct == null) {
            countDistinct = Expressions.numberOperation(Long.class, Ops.AggOps.COUNT_DISTINCT_AGG, mixin);
        }
        return countDistinct;
    }

    /**
     * Create a {@code this == right} expression
     *
     * <p>Use expr.isNull() instead of expr.eq(null)</p>
     *
     * @param right rhs of the comparison
     * @return this == right
     */
    public BooleanExpression eq(T right) {
        if (right == null) {
            throw new IllegalArgumentException("eq(null) is not allowed. Use isNull() instead");
        } else {
            return eq(ConstantImpl.create(right));
        }
    }

    /**
     * Create a {@code this == right} expression
     *
     * @param right rhs of the comparison
     * @return this == right
     */
    public BooleanExpression eq(Expression<? super T> right) {
        return Expressions.booleanOperation(Ops.EQ, mixin, right);
    }

    /**
     * Create a {@code this == all right} expression
     *
     * @param right
     * @return this == all right
     */
    public BooleanExpression eqAll(CollectionExpression<?, ? super T> right) {
        return eq(ExpressionUtils.all(right));
    }

    /**
     * Create a {@code this == < right} expression
     *
     * @param right
     * @return this == any right
     */
    public BooleanExpression eqAny(CollectionExpression<?, ? super T> right) {
        return eq(ExpressionUtils.any(right));
    }

    /**
     * Create a {@code this == all right} expression
     *
     * @param right
     * @return this == all right
     */
    public BooleanExpression eqAll(SubQueryExpression<? extends T> right) {
        return eq(ExpressionUtils.all(right));
    }

    /**
     * Create a {@code this == any right} expression
     *
     * @param right
     * @return this == any right
     */
    public BooleanExpression eqAny(SubQueryExpression<? extends T> right) {
        return eq(ExpressionUtils.any(right));
    }


    /**
     * Create a {@code this in right} expression
     *
     * @param right rhs of the comparison
     * @return this in right
     */
    public BooleanExpression in(Collection<? extends T> right) {
        if (right.size() == 1) {
            return eq(right.iterator().next());
        } else {
            return Expressions.booleanOperation(Ops.IN, mixin, ConstantImpl.create(right));
        }
    }

    /**
     * Create a {@code this in right} expression
     *
     * @param right rhs of the comparison
     * @return this in right
     */
    public BooleanExpression in(T... right) {
        if (right.length == 1) {
            return eq(right[0]);
        } else {
            return Expressions.booleanOperation(Ops.IN, mixin, ConstantImpl.create(CollectionUtils.unmodifiableList(Arrays.asList(right))));
        }
    }

    /**
     * Create a {@code this in right} expression
     *
     * @param right rhs of the comparison
     * @return this in right
     */
    public BooleanExpression in(CollectionExpression<?,? extends T> right) {
        return Expressions.booleanOperation(Ops.IN, mixin, right);
    }

    /**
     * Create a {@code this in right} expression
     *
     * @param right rhs of the comparison
     * @return this in right
     */
    public BooleanExpression in(SubQueryExpression<? extends T> right) {
        return Expressions.booleanOperation(Ops.IN, mixin, right);
    }

    /**
     * Create a {@code this in right} expression
     *
     * @param right rhs of the comparison
     * @return this in right
     */
    public BooleanExpression in(Expression<? extends T>... right) {
        return Expressions.booleanOperation(Ops.IN, mixin, Expressions.set(right));
    }

    /**
     * Create a {@code this <> right} expression
     *
     * @param right rhs of the comparison
     * @return this != right
     */
    public BooleanExpression ne(T right) {
        if (right == null) {
            throw new IllegalArgumentException("ne(null) is not allowed. Use isNotNull() instead");
        } else {
            return ne(ConstantImpl.create(right));
        }
    }

    /**
     * Create a {@code this <> right} expression
     *
     * @param right rhs of the comparison
     * @return this != right
     */
    public BooleanExpression ne(Expression<? super T> right) {
        return Expressions.booleanOperation(Ops.NE, mixin, right);
    }

    /**
     * Create a {@code this != all right} expression
     *
     * @param right
     * @return this != all right
     */
    public BooleanExpression neAll(CollectionExpression<?, ? super T> right) {
        return ne(ExpressionUtils.all(right));
    }

    /**
     * Create a {@code this != any right} expression
     *
     * @param right
     * @return this != any right
     */
    public BooleanExpression neAny(CollectionExpression<?, ? super T> right) {
        return ne(ExpressionUtils.any(right));
    }

    /**
     * Create a {@code this not in right} expression
     *
     * @param right rhs of the comparison
     * @return this not in right
     */
    public BooleanExpression notIn(Collection<? extends T> right) {
        if (right.size() == 1) {
            return ne(right.iterator().next());
        } else {
            return Expressions.booleanOperation(Ops.NOT_IN, mixin, ConstantImpl.create(right));
        }
    }

    /**
     * Create a {@code this not in right} expression
     *
     * @param right rhs of the comparison
     * @return this not in right
     */
    public BooleanExpression notIn(T... right) {
        if (right.length == 1) {
            return ne(right[0]);
        } else {
            return Expressions.booleanOperation(Ops.NOT_IN, mixin, ConstantImpl.create(Arrays.asList(right)));
        }
    }

    /**
     * Create a {@code this not in right} expression
     *
     * @param right rhs of the comparison
     * @return this not in right
     */
    public BooleanExpression notIn(CollectionExpression<?,? extends T> right) {
        return Expressions.booleanOperation(Ops.NOT_IN, mixin, right);
    }

    /**
     * Create a {@code this not in right} expression
     *
     * @param right rhs of the comparison
     * @return this not in right
     */
    public BooleanExpression notIn(SubQueryExpression<? extends T> right) {
        return Expressions.booleanOperation(Ops.NOT_IN, mixin, right);
    }

    /**
     * Create a {@code this not in right} expression
     *
     * @param right rhs of the comparison
     * @return this not in right
     */
    public BooleanExpression notIn(Expression<? extends T>... right) {
        return Expressions.booleanOperation(Ops.NOT_IN, mixin, Expressions.list(right));
    }

    /**
     * Create a {@code nullif(this, other)} expression
     *
     * @param other
     * @return nullif(this, other)
     */
    public SimpleExpression<T> nullif(Expression<T> other) {
        return Expressions.operation(this.getType(), Ops.NULLIF, this, other);
    }

    /**
     * Create a {@code nullif(this, other)} expression
     *
     * @param other
     * @return nullif(this, other)
     */
    public SimpleExpression<T> nullif(T other) {
        return nullif(ConstantImpl.create(other));
    }

    /**
     * Create a case expression builder
     *
     * @param other
     * @return case expression builder
     */
    public CaseForEqBuilder<T> when(T other) {
        return new CaseForEqBuilder<T>(mixin, ConstantImpl.create(other));
    }

    /**
     * Create a case expression builder
     *
     * @param other
     * @return case expression builder
     */
    public CaseForEqBuilder<T> when(Expression<? extends T> other) {
        return new CaseForEqBuilder<T>(mixin, other);
    }

    /**
     * Create a {@code PRIOR this} expression
     *
     * @return PRIOR this
     */
    public SimpleExpression<T> prior() {
        return Expressions.operation(this.getType(), Ops.PRIOR, this);
    }

}
