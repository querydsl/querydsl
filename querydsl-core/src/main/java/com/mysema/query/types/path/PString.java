/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.lang.reflect.AnnotatedElement;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EString;

/**
 * PString represents String typed paths
 * 
 * @author tiwe
 * 
 */
public class PString extends EString implements Path<String> {

    private static final long serialVersionUID = 7983490925756833429L;

    private final Path<String> pathMixin;
    
    public PString(Path<?> parent, String property) {
        this(PathMetadataFactory.forProperty(parent, property));
    }

    public PString(PathMetadata<?> metadata) {
        this.pathMixin = new PathMixin<String>(this, metadata);
    }
    
    public PString(String var) {
        this(PathMetadataFactory.forVariable(var));
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