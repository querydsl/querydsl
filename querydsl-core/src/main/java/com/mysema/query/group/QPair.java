/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import java.util.Arrays;
import java.util.List;

import com.mysema.commons.lang.Assert;
import com.mysema.commons.lang.Pair;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionBase;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Visitor;

/**
 * A pair of (Map) key and value 
 * 
 * @author sasa
 * @param <K> Map key type
 * @param <V> Map value type
 */
 final class QPair<K, V> extends ExpressionBase<Pair<K, V>> implements FactoryExpression<Pair<K, V>> {

    private static final long serialVersionUID = -1943990903548916056L;

    private final Expression<K> key;
    
    private final Expression<V> value;
    
    public static <K, V> QPair<K, V> create(Expression<K> key, Expression<V> value) {
        return new QPair<K, V>(key, value);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public QPair(Expression<K> key, Expression<V> value) {
        super((Class) Pair.class);

        Assert.notNull(key, "key");
        Assert.notNull(value, "value");
        
        this.key = key;
        this.value = value;
    }
    
    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public List<Expression<?>> getArgs() {
        return Arrays.<Expression<?>>asList(key, value);
    }
    
    public boolean equals(Expression<?> keyExpr, Expression<?> valueExpr) {
        return key.equals(keyExpr) && value.equals(valueExpr);
    }
    
    public boolean equals(Expression<?> keyExpr, Class<?> valueType) {
        return key.equals(keyExpr) && valueType.isAssignableFrom(value.getType());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Pair<K, V> newInstance(Object... args) {
        return new Pair<K, V>((K) args[0], (V) args[1]);
    }
    
    @Override
    public int hashCode() {
        return 31*key.hashCode() + value.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof QPair<?,?>) {
            QPair<?,?> other = (QPair<?,?>) o;
            return this.key.equals(other.key) && this.value.equals(other.value);
        } else {
            return false;
        }
    }
}