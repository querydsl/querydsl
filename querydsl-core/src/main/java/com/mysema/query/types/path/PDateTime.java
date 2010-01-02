/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EDateTime;

/**
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings({"unchecked","serial"})
public class PDateTime<D extends Comparable> extends EDateTime<D> implements Path<D> {

    private final Path<D> pathMixin;
    
    public PDateTime(Class<? extends D> type, Path<?> parent, String property) {
        this(type, PathMetadataFactory.forProperty(parent, property));
    }

    public PDateTime(Class<? extends D> type, PathMetadata<?> metadata) {
        super(type);
        this.pathMixin = new PathMixin<D>(this, metadata);
    }

    public PDateTime(Class<? extends D> type, String var) {
        this(type, PathMetadataFactory.forVariable(var));
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }

    @Override
    public EDateTime<D> asExpr() {
        return this;
    }
    
    @Override
    public boolean equals(Object o) {
        return pathMixin.equals(o);
    }
    
    @Override
    public PathMetadata<?> getMetadata() {
        return pathMixin.getMetadata();
    }

    @Override
    public Path<?> getRoot() {
        return pathMixin.getRoot();
    }

    @Override
    public int hashCode() {
        return pathMixin.hashCode();
    }

    @Override
    public EBoolean isNotNull() {
        return pathMixin.isNotNull();
    }
    
    @Override
    public EBoolean isNull() {
        return pathMixin.isNull();
    }
}
