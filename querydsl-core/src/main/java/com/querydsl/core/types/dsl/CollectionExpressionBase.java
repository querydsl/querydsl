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

import java.util.Collection;

import org.jetbrains.annotations.Nullable;

import com.querydsl.core.types.*;

/**
 * {@code CollectionExpressionBase} is an abstract base class for {@link CollectionExpression} implementations
 *
 * @author tiwe
 *
 * @param <T> expression type
 * @param <E> collection element type
 */
public abstract class CollectionExpressionBase<T extends Collection<E>, E> extends DslExpression<T> implements CollectionExpression<T, E> {

    private static final long serialVersionUID = 691230660037162054L;

    @Nullable
    private transient volatile BooleanExpression empty;

    @Nullable
    private transient volatile NumberExpression<Integer> size;

    public CollectionExpressionBase(Expression<T> mixin) {
        super(mixin);
    }

    public DslExpression<E> as(EntityPath<E> alias) {
        return Expressions.dslOperation(getElementType(), Ops.ALIAS, mixin, alias);
    }

    /**
     * Create a {@code this.contains(child)} expression
     *
     * <p>Evaluates to true, if child is contained in this</p>
     *
     * @param child element to check
     * @return this.contains(child)
     */
    public final BooleanExpression contains(E child) {
        return contains(ConstantImpl.create(child));
    }

    /**
     * Create a {@code this.contains(child)} expression
     *
     * <p>Evaluates to true, if child is contained in this</p>
     *
     * @param child element to check
     * @return this.contains(child)
     */
    public final BooleanExpression contains(Expression<E> child) {
        return Expressions.booleanOperation(Ops.IN, child, mixin);
    }

    /**
     * Get the element type
     *
     * @return element type
     */
    public abstract Class<E> getElementType();

    /**
     * Create a {@code this.isEmpty()} expression
     *
     * <p>Evaluates to true, if this has no elements.</p>
     *
     * @return this.isEmpty()
     */
    public final BooleanExpression isEmpty() {
        if (empty == null) {
            empty = Expressions.booleanOperation(Ops.COL_IS_EMPTY, mixin);
        }
        return empty;
    }

    /**
     * Create a {@code !this.isEmpty()} expression
     *
     * <p>Evaluates to true, if this has elements</p>
     *
     * @return !this.isEmpty()
     */
    public final BooleanExpression isNotEmpty() {
        return isEmpty().not();
    }

    /**
     * Create a {@code this.size()} expression
     *
     * <p>Gets the number of elements in this collection</p>
     *
     * @return this.size()
     */
    public final NumberExpression<Integer> size() {
        if (size == null) {
            size = Expressions.numberOperation(Integer.class, Ops.COL_SIZE, mixin);
        }
        return size;
    }

}
