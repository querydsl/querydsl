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
import com.mysema.query.types.EArray;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.operation.ONumber;

/**
 * PArray represents an array typed path
 * 
 * @author tiwe
 * 
 * @param <E> component type
 */
public class PArray<E> extends Expr<E[]> implements Path<E[]>, EArray<E>{
    
    private static final long serialVersionUID = 7795049264874048226L;

    private final Class<E> componentType;
    
    private final Path<E[]> pathMixin;
    
    @Nullable
    private volatile ENumber<Integer> size;

    @SuppressWarnings("unchecked")
    public PArray(Class<? super E[]> type, PathMetadata<?> metadata) {
        super((Class)type);
        this.pathMixin = new PathMixin<E[]>(this, metadata);
        this.componentType = (Class<E>) Assert.notNull(type.getComponentType(),"componentType");
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
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
    public PSimple<E> get(Expr<Integer> index){
        PathMetadata<Integer> md = PathMetadataFactory.forArrayAccess(this, index);        
        return new PSimple<E>(componentType, md);
    }


    /**
     * Create a expression for indexed access
     * 
     * @param index
     * @return
     */
    public PSimple<E> get(@Nonnegative int index){
        PathMetadata<Integer> md = PathMetadataFactory.forArrayAccess(this, index);        
        return new PSimple<E>(componentType, md);   
    }

    //    @Override
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
    public EBoolean isNotNull() {
        return pathMixin.isNotNull();
    }

    @Override
    public EBoolean isNull() {
        return pathMixin.isNull();
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
    public ENumber<Integer> size() {
        if (size == null) {
            size = ONumber.create(Integer.class, Ops.ARRAY_SIZE, this);
        }
        return size;
    }
    
}