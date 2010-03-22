/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.lang.reflect.AnnotatedElement;

import com.mysema.query.types.EBoolean;
import com.mysema.query.types.EComparable;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.Visitor;

/**
 * PComparable represents Comparable paths
 * 
 * @author tiwe
 * 
 * @param <D>
 * @see java.util.ComparableType
 */
@SuppressWarnings({"unchecked"})
public class PComparable<D extends Comparable> extends EComparable<D> implements Path<D> {

    private static final long serialVersionUID = -7434767743611671666L;

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
    
    @Override
    public AnnotatedElement getAnnotatedElement(){
        return pathMixin.getAnnotatedElement();
    }
}