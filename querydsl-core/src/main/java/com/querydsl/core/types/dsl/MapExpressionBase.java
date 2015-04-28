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
package com.querydsl.core.types.dsl;

import java.util.Map;

import javax.annotation.Nullable;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.MapExpression;
import com.querydsl.core.types.Ops;

/**
 * {@code MapExpressionBase} is an abstract base class for {@link MapExpression} implementations
 *
 * @author tiwe
 *
 * @param <K> key type
 * @param <V> value type
 */
public abstract class MapExpressionBase<K, V, Q extends SimpleExpression<? super V>> extends DslExpression<Map<K,V>> implements MapExpression<K,V> {

    private static final long serialVersionUID = 2856001983312366841L;

    @Nullable
    private volatile NumberExpression<Integer> size;

    @Nullable
    private volatile BooleanExpression empty;

    public MapExpressionBase(Expression<Map<K, V>> mixin) {
        super(mixin);
    }

    /**
     * Create a {@code (key, value) in this} expression
     *
     * @param key key of entry
     * @param value value of entry
     * @return expression
     */
    public final BooleanExpression contains(K key, V value) {
        return get(key).eq(value);
    }

    /**
     * Create a {@code (key, value) in this} expression
     *
     * @param key key of entry
     * @param value value of entry
     * @return expression
     */
    @SuppressWarnings("unchecked")
    public final BooleanExpression contains(Expression<K> key, Expression<V> value) {
        return get(key).eq((Expression)value);
    }

    /**
     * Create a {@code key in keys(this)} expression
     *
     * @param key key
     * @return expression
     */
    public final BooleanExpression containsKey(Expression<K> key) {
        return Expressions.booleanOperation(Ops.CONTAINS_KEY, mixin, key);
    }

    /**
     * Create a {@code key in keys(this)} expression
     *
     * @param key key
     * @return expression
     */
    public final BooleanExpression containsKey(K key) {
        return Expressions.booleanOperation(Ops.CONTAINS_KEY, mixin, ConstantImpl.create(key));
    }

    /**
     * Create a {@code value in values(this)} expression
     *
     * @param value value
     * @return expression
     */
    public final BooleanExpression containsValue(Expression<V> value) {
        return Expressions.booleanOperation(Ops.CONTAINS_VALUE, mixin, value);
    }

    /**
     * Create a {@code value in values(this)} expression
     *
     * @param value value
     * @return expression
     */
    public final BooleanExpression containsValue(V value) {
        return Expressions.booleanOperation(Ops.CONTAINS_VALUE, mixin, ConstantImpl.create(value));
    }

    /**
     * Create a {@code this.get(key)} expression
     *
     * @param key key
     * @return this.get(key)
     */
    public abstract Q get(Expression<K> key);

    /**
     * Create a {@code this.get(key)} expression
     *
     * @param key key
     * @return this.get(key)
     */
    public abstract Q get(K key);

    /**
     * Create a {@code this.isEmpty()} expression
     *
     * @return this.isEmpty()
     */
    public final BooleanExpression isEmpty() {
        if (empty == null) {
            empty = Expressions.booleanOperation(Ops.MAP_IS_EMPTY, mixin);
        }
        return empty;
    }

    /**
     * Create a {@code !this,isEmpty()} expression
     *
     * @return !this.isEmpty()
     */
    public final BooleanExpression isNotEmpty() {
        return isEmpty().not();
    }

    /**
     * Create a {@code this.size()} expression
     *
     * @return this.size()
     */
    public final NumberExpression<Integer> size() {
        if (size == null) {
            size = Expressions.numberOperation(Integer.class, Ops.MAP_SIZE, mixin);
        }
        return size;
    }

}
