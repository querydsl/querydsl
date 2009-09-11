/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.lang.reflect.Array;

import javax.annotation.Nonnegative;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.util.NotEmpty;

/**
 * PArray represents an array typed path
 * 
 * @author tiwe
 * 
 * @param <D> component type
 */
@SuppressWarnings("serial")
public abstract class PArray<D> extends Expr<D[]> implements Path<D[]>{
    
    protected final Class<D[]> arrayType;
    
    protected final Class<D> componentType;
    
    private EBoolean isnull, isnotnull;
    
    private final PathMetadata<?> metadata;
    
    private ENumber<Integer> size;
    
    private final Path<?> root;

    @SuppressWarnings("unchecked")
    public PArray(Class<D> type, PathMetadata<?> metadata) {
        super((Class)Object[].class);
        this.arrayType = (Class<D[]>) Array.newInstance(type, 0).getClass();
        this.componentType = type;
        this.metadata = metadata;
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
    }

    public PArray(Class<D> type, @NotEmpty String var) {
        this(type, PathMetadata.forVariable(var));
    }

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
    public abstract Expr<D> get(Expr<Integer> index);

    /**
     * Create a expression for indexed access
     * 
     * @param index
     * @return
     */
    public abstract Expr<D> get(@Nonnegative int index);

//    @Override
    public Class<D> getElementType() {
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
    public Class<D[]> getType() {
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
            size = ONumber.create(Integer.class, Ops.COL_SIZE, this);
        }
        return size;
    }
}