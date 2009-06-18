/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ECollectionBase;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.Ops;

/**
 * PComponentCollection represents component collection paths
 * 
 * @author tiwe
 * 
 * @param <D> component type
 */
public class PComponentCollection<D> extends ECollectionBase<D> implements PCollection<D> {
    protected final Class<D> type;
    private final Path<?> root;
    private final PathMetadata<?> metadata;
    
    private EBoolean isnull, isnotnull;    
    
    public PComponentCollection(Class<D> type, PathMetadata<?> metadata) {
        super(null);
        this.type = type;
        this.metadata = metadata;
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
    }

    public PComponentCollection(Class<D> type, String var) {
        this(type, PathMetadata.forVariable(var));
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Path ? ((Path<?>) o).getMetadata().equals(metadata)
                : false;
    }

    @Override
    public Class<D> getElementType() {
        return type;
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

}