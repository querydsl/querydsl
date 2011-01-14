/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.annotation.Nullable;

import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionException;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.MapExpressionBase;
import com.mysema.query.types.expr.SimpleExpression;

/**
 * MapPath represents map paths
 *
 * @author tiwe
 *
 * @param <K> key type
 * @param <V> value type
 */
public class MapPath<K, V, E extends SimpleExpression<? super V>> extends MapExpressionBase<K, V, E> implements Path<Map<K, V>> {

    private static final long serialVersionUID = -9113333728412016832L;

    private final Class<K> keyType;

    private final Path<Map<K,V>> pathMixin;

    private final Class<E> queryType;

    @Nullable
    private transient Constructor<E> constructor;

    private final Class<V> valueType;

    public MapPath(Class<? super K> keyType, Class<? super V> valueType, Class<E> queryType, String variable) {
        this(keyType, valueType, queryType, PathMetadataFactory.forVariable(variable));
    }
    
    public MapPath(Class<? super K> keyType, Class<? super V> valueType, Class<E> queryType, Path<?> parent, String property) {
        this(keyType, valueType, queryType, PathMetadataFactory.forProperty(parent, property));   
    }
    
    @SuppressWarnings("unchecked")
    public MapPath(Class<? super K> keyType, Class<? super V> valueType, Class<E> queryType, PathMetadata<?> metadata) {
        super((Class)Map.class);
        this.keyType = (Class<K>) keyType;
        this.valueType = (Class<V>) valueType;
        this.queryType = queryType;
        this.pathMixin = new PathImpl<Map<K,V>>((Class)Map.class, metadata);
    }

    protected PathMetadata<K> forMapAccess(K key){
        return PathMetadataFactory.forMapAccess(this, key);
    }

    protected PathMetadata<K> forMapAccess(Expression<K> key){
        return PathMetadataFactory.forMapAccess(this, key);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public boolean equals(Object o) {
        return pathMixin.equals(o);
    }

    @Override
    public E get(Expression<K> key) {
        try {
            PathMetadata<K> md =  forMapAccess(key);
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
            PathMetadata<K> md =  forMapAccess(key);
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

    public Class<K> getKeyType() {
        return keyType;
    }

    @Override
    public PathMetadata<?> getMetadata() {
        return pathMixin.getMetadata();
    }

    @Override
    public Path<?> getRoot() {
        return pathMixin.getRoot();
    }

    public Class<V> getValueType() {
        return valueType;
    }

    @Override
    public int hashCode() {
        return pathMixin.hashCode();
    }

    @Override
    public AnnotatedElement getAnnotatedElement(){
        return pathMixin.getAnnotatedElement();
    }

    private E newInstance(PathMetadata<?> pm) throws NoSuchMethodException,
        InstantiationException, IllegalAccessException,
        InvocationTargetException {
        if (constructor == null){
            if (Constants.isTyped(queryType)){
                constructor = queryType.getConstructor(Class.class, PathMetadata.class);
            }else{
                constructor = queryType.getConstructor(PathMetadata.class);
            }
        }
        if (Constants.isTyped(queryType)){
            return constructor.newInstance(getValueType(), pm);
        }else{
            return constructor.newInstance(pm);
        }
    }
    
    @Override
    public Class<?> getParameter(int index) {
        if (index == 0){
            return keyType;
        }else if (index == 1){    
            return valueType;
        }else{
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

}
