/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.lang.reflect.Array;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;

/**
 * PArray represents an array typed path
 * 
 * @author tiwe
 * 
 * @param <D> component type
 */
public abstract class PArray<D> extends Expr<D[]> implements Path<D[]>{
    protected final Class<D[]> arrayType;
    protected final Class<D> componentType;
    private EBoolean isnull, isnotnull;
    private final PathMetadata<?> metadata;
    private EComparable<Integer> size;
    private final Path<?> root;

    @SuppressWarnings("unchecked")
    public PArray(Class<D> type, PathMetadata<?> metadata) {
        super(null);
        this.arrayType = (Class<D[]>) Array.newInstance(type, 0).getClass();
        this.componentType = type;
        this.metadata = metadata;
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
    }

    public PArray(Class<D> type, String var) {
        this(type, PathMetadata.forVariable(var));
    }

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
    public abstract Expr<D> get(int index);

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
            isnotnull = new OBoolean(Ops.ISNOTNULL, this);
        }
        return isnotnull;
    }
    
    @Override
    public EBoolean isNull() {
        if (isnull == null) {
            isnull = new OBoolean(Ops.ISNULL, this);
        }
        return isnull;
    }

    /**
     * Create an expression for the array size
     * 
     * @return
     */
    public EComparable<Integer> size() {
        if (size == null) {
            size = ONumber.create(Integer.class, Ops.COL_SIZE, this);
        }
        return size;
    }
}