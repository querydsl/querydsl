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

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.annotation.Nullable;

import com.querydsl.core.types.*;

/**
 * {@code MapPath} represents map paths
 *
 * @author tiwe
 *
 * @param <K> key type
 * @param <V> value type
 */
public class MapPath<K, V, E extends SimpleExpression<? super V>> extends MapExpressionBase<K, V, E> implements Path<Map<K, V>> {

    private static final long serialVersionUID = -9113333728412016832L;

    private final Class<K> keyType;

    private final PathImpl<Map<K,V>> pathMixin;

    private final Class<E> queryType;

    @Nullable
    private transient Constructor<E> constructor;

    private final Class<V> valueType;

    protected MapPath(Class<? super K> keyType, Class<? super V> valueType, Class<E> queryType, String variable) {
        this(keyType, valueType, queryType, PathMetadataFactory.forVariable(variable));
    }

    protected MapPath(Class<? super K> keyType, Class<? super V> valueType, Class<E> queryType, Path<?> parent, String property) {
        this(keyType, valueType, queryType, PathMetadataFactory.forProperty(parent, property));
    }

    @SuppressWarnings("unchecked")
    protected MapPath(Class<? super K> keyType, Class<? super V> valueType, Class<E> queryType, PathMetadata metadata) {
        super(new ParameterizedPathImpl<Map<K,V>>((Class)Map.class, metadata, keyType, valueType));
        this.keyType = (Class<K>) keyType;
        this.valueType = (Class<V>) valueType;
        this.queryType = queryType;
        this.pathMixin = (PathImpl<Map<K,V>>)mixin;
    }

    @Override
    public final <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(pathMixin, context);
    }

    protected PathMetadata forMapAccess(K key) {
        return PathMetadataFactory.forMapAccess(this, key);
    }

    protected PathMetadata forMapAccess(Expression<K> key) {
        return PathMetadataFactory.forMapAccess(this, key);
    }

    @Override
    public E get(Expression<K> key) {
        try {
            PathMetadata md =  forMapAccess(key);
            return newInstance(md);
        } catch (NoSuchMethodException e) {
            throw new ExpressionException(e);
        } catch (InstantiationException e) {
            throw new ExpressionException(e);
        } catch (IllegalAccessException e) {
            throw new ExpressionException(e);
        } catch (InvocationTargetException e) {
            throw new ExpressionException(e);
        }
    }

    @Override
    public E get(K key) {
        try {
            PathMetadata md =  forMapAccess(key);
            return newInstance(md);
        } catch (NoSuchMethodException e) {
            throw new ExpressionException(e);
        } catch (InstantiationException e) {
            throw new ExpressionException(e);
        } catch (IllegalAccessException e) {
            throw new ExpressionException(e);
        } catch (InvocationTargetException e) {
            throw new ExpressionException(e);
        }
    }

    /**
     * Get the key type
     *
     * @return key type
     */
    public Class<K> getKeyType() {
        return keyType;
    }

    @Override
    public PathMetadata getMetadata() {
        return pathMixin.getMetadata();
    }

    @Override
    public Path<?> getRoot() {
        return pathMixin.getRoot();
    }

    /**
     * Get the value type
     *
     * @return value type
     */
    public Class<V> getValueType() {
        return valueType;
    }

    @Override
    public AnnotatedElement getAnnotatedElement() {
        return pathMixin.getAnnotatedElement();
    }

    private E newInstance(PathMetadata pm) throws NoSuchMethodException,
        InstantiationException, IllegalAccessException,
        InvocationTargetException {
        if (constructor == null) {
            if (Constants.isTyped(queryType)) {
                constructor = queryType.getDeclaredConstructor(Class.class, PathMetadata.class);
            } else {
                constructor = queryType.getDeclaredConstructor(PathMetadata.class);
            }
            constructor.setAccessible(true);
        }
        if (Constants.isTyped(queryType)) {
            return constructor.newInstance(getValueType(), pm);
        } else {
            return constructor.newInstance(pm);
        }
    }

    @Override
    public Class<?> getParameter(int index) {
        if (index == 0) {
            return keyType;
        } else if (index == 1) {
            return valueType;
        } else {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

}
