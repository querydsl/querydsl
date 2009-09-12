/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EMapBase;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.util.NotEmpty;

/**
 * PString represents String typed paths
 * 
 * @author tiwe
 * 
 */
@SuppressWarnings("serial")
public class PString extends EString implements Path<String> {
    
    private EBoolean isnull, isnotnull;
    
    private final PathMetadata<?> metadata;

    public PString(PathMetadata<?> metadata) {
        this.metadata = metadata;
    }

    public PString(@NotEmpty String var) {
        this(PathMetadata.forVariable(var));
    }
    
    public PString(Path<?> parent, @NotEmpty String property) {
        this(PathMetadata.forProperty(parent, property));
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
        return metadata.getRoot() != null ? metadata.getRoot() : this;
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
    public EString asExpr() {
        return this;
    }
}