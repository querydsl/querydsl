/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EMapBase;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.util.NotEmpty;

/**
 * PSimple represents simple paths
 * 
 * @author tiwe
 * 
 * @param <D> Java type
 */
@SuppressWarnings("serial")
public class PSimple<D> extends Expr<D> implements Path<D> {
    
    private EBoolean isnull, isnotnull;
    
    private final PathMetadata<?> metadata;
    
    private final Path<?> root;

    public PSimple(Class<? extends D> type, PathMetadata<?> metadata) {
        super(type);
        this.metadata = metadata;
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
    }

    public PSimple(Class<? extends D> type, @NotEmpty String var) {
        this(type, PathMetadata.forVariable(var));
    }
    
    public PSimple(Class<? extends D> type, @NotEmpty Path<?> parent, String property) {
        this(type, PathMetadata.forProperty(parent, property));
    }
    
    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        return o instanceof Path ? ((Path<?>) o).getMetadata().equals(metadata)
                : false;
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
    
    @Override
    public Expr<D> asExpr() {
        return this;
    }
    
}