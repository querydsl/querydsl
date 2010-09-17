/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import java.util.Map;

import javax.annotation.Nullable;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;

/**
 * MapExpressionBase is an abstract base class for EMap implementations
 *
 * @author tiwe
 *
 * @param <K>
 * @param <V>
 */
public abstract class MapExpressionBase<K,V> extends SimpleExpression<Map<K,V>> implements MapExpression<K,V> {

    private static final long serialVersionUID = 2856001983312366841L;

    @Nullable
    private volatile NumberExpression<Integer> size;

    @Nullable
    private volatile BooleanExpression empty;

    public MapExpressionBase(Class<? extends Map<K, V>> type) {
        super(type);
    }

    @Override
    public final BooleanExpression contains(Expression<K> key, Expression<V> value) {
        return get(key).eq(value);
    }

    @Override
    public final BooleanExpression containsKey(Expression<K> key) {
        return BooleanOperation.create(Ops.CONTAINS_KEY, this, key);
    }

    @Override
    public final BooleanExpression containsKey(K key) {
        return BooleanOperation.create(Ops.CONTAINS_KEY, this, new ConstantImpl<K>(key));
    }

    @Override
    public final BooleanExpression containsValue(Expression<V> value) {
        return BooleanOperation.create(Ops.CONTAINS_VALUE, this, value);
    }

    @Override
    public final BooleanExpression containsValue(V value) {
        return BooleanOperation.create(Ops.CONTAINS_VALUE, this, new ConstantImpl<V>(value));
    }

    @Override
    public final BooleanExpression isEmpty() {
        if (empty == null){
            empty = BooleanOperation.create(Ops.MAP_ISEMPTY, this);
        }
        return empty;
    }

    @Override
    public final BooleanExpression isNotEmpty() {
        return isEmpty().not();
    }

    @Override
    public final NumberExpression<Integer> size() {
        if (size == null) {
            size = NumberOperation.create(Integer.class, Ops.MAP_SIZE, this);
        }
        return size;
    }

}
