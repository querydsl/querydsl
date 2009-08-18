/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.Expr;
import com.mysema.query.util.NotEmpty;

/**
 * PComponentList represents component list paths
 * 
 * @author tiwe
 * 
 * @param <D> component type
 */
public class PComponentList<D> extends PComponentCollection<D> implements PList<D> {
    
    public PComponentList(Class<D> type, PathMetadata<?> metadata) {
        super(type, metadata);
    }

    public PComponentList(Class<D> type, @NotEmpty String var) {
        super(type, PathMetadata.forVariable(var));
    }
    
    public PComponentList(Class<D> type, Path<?> path, @NotEmpty String property) {
        super(type, PathMetadata.forProperty(path, property));
    }

    @Override
    public Expr<D> get(Expr<Integer> index) {
        return new PSimple<D>(type, PathMetadata.forListAccess(this, index));
    }

    @Override
    public Expr<D> get(int index) {
        return new PSimple<D>(type, PathMetadata.forListAccess(this, index));
    }
}