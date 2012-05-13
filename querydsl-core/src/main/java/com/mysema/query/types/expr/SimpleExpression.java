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
package com.mysema.query.types.expr;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;

import com.mysema.query.types.CollectionExpression;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;

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

    protected final boolean primitive;
    
    public SimpleExpression(Class<? extends T> type) {
        super(type);
        this.primitive = type.isPrimitive()
            || Number.class.isAssignableFrom(type)
            || Boolean.class.equals(type)
            || Character.class.equals(type);
    }


    /**
     * Create an alias for the expression
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public SimpleExpression<T> as(Path<T> alias) {
        return SimpleOperation.create((Class<T>)getType(),Ops.ALIAS, this, alias);
    }

    /**
     * Create an alias for the expression
     *
     * @return
     */
    public SimpleExpression<T> as(String alias) {
        return as(new PathImpl<T>(getType(), alias));
    }
    
    /**
     * Create a <code>this is not null</code> expression
     *
     * @return
     */
    public BooleanExpression isNotNull() {
        if (isnotnull == null) {
            isnotnull = BooleanOperation.create(Ops.IS_NOT_NULL, this);
        }
        return isnotnull;
    }

    /**
     * Create a <code>this is null</code> expression
     *
     * @return
     */
    public BooleanExpression isNull() {
        if (isnull == null) {
            isnull = BooleanOperation.create(Ops.IS_NULL, this);
        }
        return isnull;
    }

    /**
     * Get the <code>count(this)</code> expression
     *
     * @return count(this)
     */
    public NumberExpression<Long> count(){
        if (count == null) {
            count = NumberOperation.create(Long.class, Ops.AggOps.COUNT_AGG, this);
        }
        return count;
    }


    /**
     * Get the <code>count(distinct this)</code> expression
     *
     * @return count(distinct this)
     */
    public NumberExpression<Long> countDistinct(){
        if (countDistinct == null) {
          countDistinct = NumberOperation.create(Long.class, Ops.AggOps.COUNT_DISTINCT_AGG, this);
        }
        return countDistinct;
    }

    /**
     * Get a <code>this == right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression eq(T right) {
        if (right == null) {
            throw new IllegalArgumentException("eq(null) is not allowed. Use isNull() instead");
        } else {
            return eq(new ConstantImpl<T>(right));
        }
    }

    /**
     * Get a <code>this == right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression eq(Expression<? super T> right) {
        if (primitive) {
            return BooleanOperation.create(Ops.EQ_PRIMITIVE, this, right);
        } else {
            return BooleanOperation.create(Ops.EQ_OBJECT, this, right);
        }
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public int hashCode() {
        return getType().hashCode();
    }

    /**
     * Get a <code>this in right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression in(Collection<? extends T> right) {
        if (right.size() == 1) {
            return eq(right.iterator().next());
        } else {
            return BooleanOperation.create(Ops.IN, this, new ConstantImpl<Collection<? extends T>>(right));
        }
    }

    /**
     * Get a <code>this in right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression in(T... right) {
        if (right.length == 1) {
            return eq(right[0]);
        } else {
            return BooleanOperation.create(Ops.IN, this, new ConstantImpl<List<T>>(Arrays.asList(right)));
        }
    }


    /**
     * Get a <code>this in right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression in(CollectionExpression<?,? extends T> right) {
        return BooleanOperation.create(Ops.IN, this, right);
    }

    /**
     * Get a <code>this &lt;&gt; right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression ne(T right) {
        return ne(new ConstantImpl<T>(right));
    }

    /**
     * Get a <code>this &lt;&gt; right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression ne(Expression<? super T> right) {
        if (primitive) {
            return BooleanOperation.create(Ops.NE_PRIMITIVE, this, right);
        } else {
            return BooleanOperation.create(Ops.NE_OBJECT, this, right);
        }
    }

    /**
     * Get a <code>this not in right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression notIn(Collection<? extends T> right) {
        if (right.size() == 1){
            return ne(right.iterator().next());
        }else{
            return in(right).not();
        }
    }

    /**
     * Get a <code>this not in right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression notIn(T... right) {
        if (right.length == 1) {
            return ne(right[0]);
        } else {
            return in(right).not();
        }
    }



    /**
     * Get a <code>this not in right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public final BooleanExpression notIn(CollectionExpression<?,? extends T> right) {
        return in(right).not();
    }


    /**
     * Get a case expression builder
     *
     * @param other
     * @return
     */
    public CaseForEqBuilder<T> when(T other){
        return new CaseForEqBuilder<T>(this, new ConstantImpl<T>(other));
    }

    /**
     * Get a case expression builder
     *
     * @param other
     * @return
     */
    public CaseForEqBuilder<T> when(Expression<? extends T> other){
        return new CaseForEqBuilder<T>(this, other);
    }

}
