/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import javax.annotation.Nullable;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.ExpressionException;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.SimpleExpression;

/**
 * SetPath represents set paths
 *
 * @author tiwe
 *
 * @param <E> component type
 */
public class SetPath<E, Q extends SimpleExpression<E>> extends CollectionPathBase<Set<E>,E> {

    private static final long serialVersionUID = 4145848445507037373L;

    private final Class<E> elementType;

    private final Path<Set<E>> pathMixin;
    
    @Nullable
    private transient Q any;
    
    private final Class<Q> queryType;

    public SetPath(Class<? super E> type, Class<Q> queryType, String variable) {
        this(type, queryType, PathMetadataFactory.forVariable(variable));
    }
    
    @SuppressWarnings("unchecked")
    public SetPath(Class<? super E> type, Class<Q> queryType, PathMetadata<?> metadata) {
        super((Class)Set.class);
        this.elementType = (Class<E>) Assert.notNull(type,"type");
        this.queryType = queryType;
        this.pathMixin = new PathImpl<Set<E>>((Class)Set.class, metadata);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }
    
    @Override
    public Q any(){
        if (any == null){
            try {
                any = newInstance(queryType, PathMetadataFactory.forCollectionAny(this));
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

    @Override
    public boolean equals(Object o) {
        return pathMixin.equals(o);
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
