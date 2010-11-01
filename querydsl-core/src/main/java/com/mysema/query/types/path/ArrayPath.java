/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

import java.lang.reflect.AnnotatedElement;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.PathImpl;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.ArrayExpression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.NumberOperation;
import com.mysema.query.types.expr.SimpleExpression;

/**
 * ArrayPath represents an array typed path
 *
 * @author tiwe
 *
 * @param <E> component type
 */
public class ArrayPath<E> extends SimpleExpression<E[]> implements Path<E[]>, ArrayExpression<E>{

    private static final long serialVersionUID = 7795049264874048226L;

    private final Class<E> componentType;

    private final Path<E[]> pathMixin;

    @Nullable
    private volatile NumberExpression<Integer> size;

    public ArrayPath(Class<? super E[]> type, String variable) {
        this(type, PathMetadataFactory.forVariable(variable));
    }
    
    public ArrayPath(Class<? super E[]> type, Path<?> parent, String property) {
        this(type, PathMetadataFactory.forProperty(parent, property));
    }
    
    @SuppressWarnings("unchecked")
    public ArrayPath(Class<? super E[]> type, PathMetadata<?> metadata) {
        super((Class)type);
        this.pathMixin = new PathImpl<E[]>((Class)type, metadata);
        this.componentType = (Class<E>) Assert.notNull(type.getComponentType(),"componentType");
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public boolean equals(Object o) {
        return pathMixin.equals(o);
    }

    /**
     * Create a expression for indexed access
     *
     * @param index
     * @return
     */
    public SimplePath<E> get(Expression<Integer> index){
        PathMetadata<Integer> md = PathMetadataFactory.forArrayAccess(this, index);
        return new SimplePath<E>(componentType, md);
    }

    /**
     * Create a expression for indexed access
     *
     * @param index
     * @return
     */
    public SimplePath<E> get(@Nonnegative int index){
        PathMetadata<Integer> md = PathMetadataFactory.forArrayAccess(this, index);
        return new SimplePath<E>(componentType, md);
    }

    public Class<E> getElementType() {
        return componentType;
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

    /**
     * Create an expression for the array size
     *
     * @return
     */
    public NumberExpression<Integer> size() {
        if (size == null) {
            size = NumberOperation.create(Integer.class, Ops.ARRAY_SIZE, this);
        }
        return size;
    }

}
