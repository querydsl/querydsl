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
package com.querydsl.core.types.expr;

import java.util.Collection;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathImpl;

/**
 * SimpleExpression is the base class for Expression implementations.
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public abstract class SimpleExpression<T> extends DslExpression<T> {

    private static final long serialVersionUID = -4405387187738167105L;

    @Nullable
    private volatile NumberExpression<Long> count;

    @Nullable
    private volatile NumberExpression<Long> countDistinct;

    @Nullable
    private volatile BooleanExpression isnull, isnotnull;

    public SimpleExpression(Expression<T> mixin) {
        super(mixin);
    }


    /**
     * Create an alias for the expression
     *
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public SimpleExpression<T> as(Path<T> alias) {
        return SimpleOperation.create((Class<T>)getType(),Ops.ALIAS, mixin, alias);
    }

    /**
     * Create an alias for the expression
     *
     * @return
     */
    @Override
    public SimpleExpression<T> as(String alias) {
        return as(new PathImpl<T>(getType(), alias));
    }

    /**
     * Create a {@code this is not null} expression
     *
     * @return
     */
    public BooleanExpression isNotNull() {
        if (isnotnull == null) {
            isnotnull = BooleanOperation.create(Ops.IS_NOT_NULL, mixin);
        }
        return isnotnull;
    }

    /**
     * Create a {@code this is null} expression
     *
     * @return
     */
    public BooleanExpression isNull() {
        if (isnull == null) {
            isnull = BooleanOperation.create(Ops.IS_NULL, mixin);
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
            count = NumberOperation.create(Long.class, Ops.AggOps.COUNT_AGG, mixin);
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
            countDistinct = NumberOperation.create(Long.class, Ops.AggOps.COUNT_DISTINCT_AGG, mixin);
        }
        return countDistinct;
    }

    /**
     * Get a {@code this == right} expression
     *
     * <p>Use expr.isNull() instead of expr.eq(null)</p>
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression eq(T right) {
        if (right == null) {
            throw new IllegalArgumentException("eq(null) is not allowed. Use isNull() instead");
        } else {
            return eq(ConstantImpl.create(right));
        }
    }

    /**
     * Get a {@code this == right} expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression eq(Expression<? super T> right) {
        return BooleanOperation.create(Ops.EQ, mixin, right);
    }

    /**
     * @param right
     * @return
     */
    public BooleanExpression eqAll(CollectionExpression<?, ? super T> right) {
        return eq(ExpressionUtils.all(right));
    }

    /**
     * @param right
     * @return
     */
    public BooleanExpression eqAny(CollectionExpression<?, ? super T> right) {
        return eq(ExpressionUtils.any(right));
    }

    /**
     * Get a {@code this in right} expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression in(Collection<? extends T> right) {
        if (right.size() == 1) {
            return eq(right.iterator().next());
        } else {
            return BooleanOperation.create(Ops.IN, mixin, ConstantImpl.create(right));
        }
    }

    /**
     * Get a {@code this in right} expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression in(T... right) {
        if (right.length == 1) {
            return eq(right[0]);
        } else {
            return BooleanOperation.create(Ops.IN, mixin, ConstantImpl.create(ImmutableList.copyOf(right)));
        }
    }


    /**
     * Get a {@code this in right} expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression in(CollectionExpression<?,? extends T> right) {
        return BooleanOperation.create(Ops.IN, mixin, right);
    }

    /**
     * Get a {@code this <> right} expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression ne(T right) {
        if (right == null) {
            throw new IllegalArgumentException("ne(null) is not allowed. Use isNotNull() instead");
        } else {
            return ne(ConstantImpl.create(right));
        }
    }

    /**
     * Get a {@code this <> right} expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression ne(Expression<? super T> right) {
        return BooleanOperation.create(Ops.NE, mixin, right);
    }

    /**
     * @param right
     * @return
     */
    public BooleanExpression neAll(CollectionExpression<?, ? super T> right) {
        return ne(ExpressionUtils.all(right));
    }

    /**
     * @param right
     * @return
     */
    public BooleanExpression neAny(CollectionExpression<?, ? super T> right) {
        return ne(ExpressionUtils.any(right));
    }

    /**
     * Get a {@code this not in right} expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression notIn(Collection<? extends T> right) {
        if (right.size() == 1) {
            return ne(right.iterator().next());
        } else {
            return BooleanOperation.create(Ops.NOT_IN, mixin, ConstantImpl.create(right));
        }
    }

    /**
     * Get a {@code this not in right} expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression notIn(T... right) {
        if (right.length == 1) {
            return ne(right[0]);
        } else {
            return BooleanOperation.create(Ops.NOT_IN, mixin, ConstantImpl.create(ImmutableList.copyOf(right)));
        }
    }

    /**
     * Get a {@code this not in right} expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public final BooleanExpression notIn(CollectionExpression<?,? extends T> right) {
        return BooleanOperation.create(Ops.NOT_IN, mixin, right);
    }


    /**
     * Get a {@code nullif(this, other)} expression
     *
     * @param other
     * @return
     */
    @SuppressWarnings("unchecked")
    public SimpleExpression<T> nullif(Expression<T> other) {
        return SimpleOperation.create((Class<T>)this.getType(), Ops.NULLIF, this, other);
    }

    /**
     * Get a {@code nullif(this, other)} expression
     *
     * @param other
     * @return
     */
    public SimpleExpression<T> nullif(T other) {
        return nullif(ConstantImpl.create(other));
    }

    /**
     * Get a case expression builder
     *
     * @param other
     * @return
     */
    public CaseForEqBuilder<T> when(T other) {
        return new CaseForEqBuilder<T>(mixin, ConstantImpl.create(other));
    }

    /**
     * Get a case expression builder
     *
     * @param other
     * @return
     */
    public CaseForEqBuilder<T> when(Expression<? extends T> other) {
        return new CaseForEqBuilder<T>(mixin, other);
    }

}
