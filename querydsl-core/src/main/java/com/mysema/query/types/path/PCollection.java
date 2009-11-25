/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.util.Collection;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ECollectionBase;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.util.NotEmpty;

/**
 * PCollection represents collection paths
 * 
 * @author tiwe
 * 
 * @param <E> component type
 */
@SuppressWarnings("serial")
public class PCollection<E> extends ECollectionBase<Collection<E>,E> implements Path<Collection<E>> {
    
    private final PathMetadata<?> metadata;
    
    private final Class<E> elementType;
    
    private final String entityName;
    
    private volatile EBoolean isnull, isnotnull;    
    
    private final Path<?> root;
    
    @SuppressWarnings("unchecked")
    public PCollection(Class<? super E> type, @NotEmpty String entityName, PathMetadata<?> metadata) {
        super((Class)Collection.class);
        this.elementType = (Class<E>) Assert.notNull(type,"type is null");
        this.metadata = Assert.notNull(metadata,"metadata is null");
        this.entityName = Assert.notNull(entityName,"entityName is null");
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        return o instanceof Path ? ((Path<?>) o).getMetadata().equals(metadata) : false;
    }

    @Override
    public Class<E> getElementType() {
        return elementType;
    }

    /**
     * Get the entity name for this Entity collection path
     * 
     * @return
     */
    public String getEntityName() {
        return entityName;
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
    public Expr<Collection<E>> asExpr() {
        return this;
    }

}