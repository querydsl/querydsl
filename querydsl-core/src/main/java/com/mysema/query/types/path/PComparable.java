/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;

/**
 * PComparable represents Comparable paths
 * 
 * @author tiwe
 * 
 * @param <D>
 * @see java.util.Comparable
 */
@SuppressWarnings({"unchecked","serial"})
public class PComparable<D extends Comparable> extends EComparable<D> implements Path<D> {

    private final Path<D> pathMixin;
    
    public PComparable(Class<? extends D> type, Path<?> parent, String property) {
        this(type, PathMetadataFactory.forProperty(parent, property));
    }

    public PComparable(Class<? extends D> type, PathMetadata<?> metadata) {
        super(type);
        this.pathMixin = new PathMixin<D>(this, metadata);
    }
    
    public PComparable(Class<? extends D> type, String var) {
        this(type, PathMetadataFactory.forVariable(var));
    }
    
    public PComparable(Path<?> parent, String property) {
        this((Class<? extends D>) Comparable.class, PathMetadataFactory.forProperty(parent, property));
    }
    
    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }

    @Override
    public EComparable<D> asExpr() {
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