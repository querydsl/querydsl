/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.Ops;

/**
 * PathMixin defines a mixin version of the Path interface which can be used 
 * as a component and target in actual Path implementations
 * 
 * @author tiwe
 *
 * @param <T>
 */
class PathMixin<T> implements Path<T> {
    
    private volatile EBoolean isnull, isnotnull;
    
    private final PathMetadata<?> metadata;
    
    private final Path<?> root;
    
    private final Expr<T> self;
    
    @SuppressWarnings("unchecked")
    public PathMixin(Expr<T> self, PathMetadata<?> metadata){
        this.self = self;
        this.metadata = metadata;
        this.root = metadata.getRoot() != null ? metadata.getRoot() : (Path<T>)self;
    }

    @Override
    public Expr<T> asExpr() {
        return self;
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        return o instanceof Path ? ((Path<?>) o).getMetadata().equals(metadata) : false;
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
    public Class<? extends T> getType() {
        return self.getType();
    }

    @Override
    public int hashCode() {
        return metadata.hashCode();
    }
    
    @Override
    public EBoolean isNotNull() {
        if (isnotnull == null) {
            isnotnull = OBoolean.create(Ops.IS_NOT_NULL, self);
        }
        return isnotnull;
    }
    
    @Override
    public EBoolean isNull() {
        if (isnull == null) {
            isnull = OBoolean.create(Ops.IS_NULL, self);
        }
        return isnull;
    }

}
