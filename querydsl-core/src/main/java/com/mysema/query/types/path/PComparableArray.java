/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.util.NotEmpty;

/**
 * PComparableArray represents comparable array paths
 * @author tiwe
 * 
 * @param <D> component type
 */
@SuppressWarnings("unchecked")
public class PComparableArray<D extends Comparable> extends PArray<D> {
    
    public PComparableArray(Class<D> type, PathMetadata<?> metadata) {
        super(type, metadata);
    }

    public PComparableArray(Class<D> type, @NotEmpty String var) {
        super(type, PathMetadata.forVariable(var));
    }

    @Override
    public EComparable<D> get(Expr<Integer> index) {
        return new PComparable<D>(componentType, PathMetadata.forArrayAccess(this, index));
    }

    @Override
    public EComparable<D> get(int index) {
        return new PComparable<D>(componentType, PathMetadata.forArrayAccess(this, index));
    }
    
    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }
}
