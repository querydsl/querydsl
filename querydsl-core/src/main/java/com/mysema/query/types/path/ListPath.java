/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionException;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.ListExpression;
import com.mysema.query.types.expr.SimpleExpression;

/**
 * ListPath represents list paths
 *
 * @author tiwe
 *
 * @param <E> component type
 */
public class ListPath<E, Q extends SimpleExpression<E>> extends CollectionPathBase<List<E>,E> implements ListExpression<E> {

    private static final long serialVersionUID = 3302301599074388860L;

    private final Map<Integer,Q> cache = new HashMap<Integer,Q>();

    private final Class<E> elementType;

    private final Path<List<E>> pathMixin;

    private final Class<Q> queryType;

    @Nullable
    private transient Q any;    

    public ListPath(Class<? super E> elementType, Class<Q> queryType, String variable) {
        this(elementType, queryType, PathMetadataFactory.forVariable(variable));
    }
    
    @SuppressWarnings("unchecked")
    public ListPath(Class<? super E> elementType, Class<Q> queryType, PathMetadata<?> metadata) {
        super((Class)List.class);
        this.elementType = (Class<E>) Assert.notNull(elementType,"type");
        this.queryType = queryType;
        this.pathMixin = new PathImpl<List<E>>((Class)List.class, metadata);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }
    
    @Override
    public Q any(){
        if (any == null){
            try {
                any = newInstance(queryType, pathMixin.getMetadata());
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
        return any;
    }

    protected PathMetadata<Integer> forListAccess(int index){
        return PathMetadataFactory.forListAccess(this, index);
    }

    protected PathMetadata<Integer> forListAccess(Expression<Integer> index){
        return PathMetadataFactory.forListAccess(this, index);
    }

    private Q create(int index){
        try {
            PathMetadata<Integer> md = forListAccess(index);
            return newInstance(queryType, md);
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
    public boolean equals(Object o) {
        return pathMixin.equals(o);
    }

    @Override
    public Q get(Expression<Integer> index) {
        try {
            PathMetadata<Integer> md = forListAccess(index);
            return newInstance(queryType, md);
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
    public Q get(int index) {
        if (cache.containsKey(index)){
            return cache.get(index);
        }else{
            Q rv = create(index);
            cache.put(index, rv);
            return rv;
        }
    }

    public Class<E> getElementType() {
        return elementType;
    }

    @Override
    public PathMetadata<?> getMetadata() {
        return pathMixin.getMetadata();
    }

    @Override
    public Path<?> getRoot() {
        return pathMixin.getRoot();
    }

    @Override
    public int hashCode() {
        return pathMixin.hashCode();
    }

    @Override
    public AnnotatedElement getAnnotatedElement(){
        return pathMixin.getAnnotatedElement();
    }

    @Override
    public Class<?> getParameter(int index) {
        if (index == 0){
            return elementType;
        }else{
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

}
