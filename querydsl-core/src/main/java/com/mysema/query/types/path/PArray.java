/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.lang.reflect.Array;

import javax.annotation.Nonnegative;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EArray;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;

/**
 * PArray represents an array typed path
 * 
 * @author tiwe
 * 
 * @param <E> component type
 */
@SuppressWarnings("serial")
public class PArray<E> extends Expr<E[]> implements Path<E[]>, EArray<E>{
    
    private final Class<E[]> arrayType;
    
    private final Class<E> componentType;
    
    private volatile EBoolean isnull, isnotnull;
    
    private final PathMetadata<?> metadata;
    
    private volatile ENumber<Integer> size;
    
    private final Path<?> root;

    @SuppressWarnings("unchecked")
    public PArray(Class<? super E> type, PathMetadata<?> metadata) {
        super((Class)Object[].class);
        this.arrayType = (Class<E[]>) Array.newInstance(type, 0).getClass();
        this.componentType = (Class<E>) type;
        this.metadata = metadata;
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
    }

//    public PArray(Class<E> type, @NotEmpty String var) {
//        this(type, PathMetadata.forVariable(var));
//    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        return o instanceof Path ? ((Path<?>) o).getMetadata().equals(metadata)
                : false;
    }

    /**
     * Create a expression for indexed access
     * 
     * @param index
     * @return
     */
    public PSimple<E> get(Expr<Integer> index){
        PathMetadata<Integer> md = PathMetadata.forArrayAccess(this, index);        
        return new PSimple<E>(componentType, md);
    }

    /**
     * Create a expression for indexed access
     * 
     * @param index
     * @return
     */
    public PSimple<E> get(@Nonnegative int index){
        PathMetadata<Integer> md = PathMetadata.forArrayAccess(this, index);        
        return new PSimple<E>(componentType, md);   
    }

//    @Override
    public Class<E> getElementType() {
        return componentType;
    }

    @Override
    public PathMetadata<?> getMetadata() {
        return metadata;
    }

    @Override
    public Path<?> getRoot() {
        return root;
    }

    @Override
    public Class<E[]> getType() {
        return arrayType;
    }

    @Override
    public int hashCode() {
        return metadata.hashCode();
    }

    @Override
    public EBoolean isNotNull() {
        if (isnotnull == null) {
            isnotnull = OBoolean.create(Ops.IS_NOT_NULL, this);
        }
        return isnotnull;
    }
    
    @Override
    public EBoolean isNull() {
        if (isnull == null) {
            isnull = OBoolean.create(Ops.IS_NULL, this);
        }
        return isnull;
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

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }

    @Override
    public Expr<E[]> asExpr() {
        return this;
    }
}