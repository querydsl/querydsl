/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.Expr;
import com.mysema.query.util.NotEmpty;

/**
 * PEntityList represents entity list paths
 * 
 * @author tiwe
 * 
 * @param <D> component type
 */
@SuppressWarnings("serial")
public class PEntityList<D> extends PEntityCollection<D> implements PList<D> {
    
    public PEntityList(Class<D> elementType, @NotEmpty String entityName, PathMetadata<?> metadata) {
        super(elementType, entityName, metadata);
    }

    public PEntityList(Class<D> elementType, @NotEmpty String entityName, @NotEmpty String var) {
        super(elementType, entityName, PathMetadata.forVariable(var));
    }

    public PEntityList(Class<D> elementType, @NotEmpty String entityName, Path<?> parent, @NotEmpty String property) {
        super(elementType, entityName, PathMetadata.forProperty(parent, property));
    }
    
    @Override
    public PEntity<D> get(Expr<Integer> index) {
        return new PEntity<D>(elementType, entityName, PathMetadata.forListAccess(this, index));
    }

    @Override
    public PEntity<D> get(int index) {
        // TODO : cache
        return new PEntity<D>(elementType, entityName, PathMetadata.forListAccess(this, index));
    }

}