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
import com.mysema.query.types.expr.EEntity;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.expr.ExprConst;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.util.NotEmpty;

/**
 * PEntityCollection represents an entity collection path
 * 
 * @author tiwe
 * 
 * @param <D> component type
 */
@SuppressWarnings("serial")
public class PEntityCollection<D> extends EEntity<java.util.Collection<D>> implements PCollection<D> {
    
    private final PathMetadata<?> metadata;
    
    protected final Class<D> elementType;
    
    protected final String entityName;
    
    private volatile EBoolean isnull, isnotnull, empty;    
    
    private volatile ENumber<Integer> size;    
    
    private final Path<?> root;
    
    @SuppressWarnings("unchecked")
    public PEntityCollection(Class<? super D> type, @NotEmpty String entityName, PathMetadata<?> metadata) {
        super((Class)Collection.class);
        this.elementType = (Class<D>) Assert.notNull(type,"type is null");
        this.metadata = Assert.notNull(metadata,"metadata is null");
        this.entityName = Assert.notNull(entityName,"entityName is null");
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
    }

    public PEntityCollection(Class<? super D> type, @NotEmpty String entityName, @NotEmpty String var) {
        this(type, entityName, PathMetadata.forVariable(var));
    }
    
    public PEntityCollection(Class<? super D> type, @NotEmpty String entityName, Path<?> parent, @NotEmpty String property) {
        this(type, entityName, PathMetadata.forProperty(parent, property));
    }

    @Override
    public EBoolean contains(D child) {
        return OBoolean.create(Ops.IN, ExprConst.__create(child), this);
    }

    @Override
    public EBoolean contains(Expr<D> child) {
        return OBoolean.create(Ops.IN, child, this);
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
    public Class<D> getElementType() {
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
    public EBoolean isEmpty() {
        if (empty == null){
            empty = OBoolean.create(Ops.COL_IS_EMPTY, this);
        }
        return empty;
    }
    
    @Override
    public EBoolean isNotEmpty() {
        return isEmpty().not();
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
    public ENumber<Integer> size() {
        if (size == null) {
            size = ONumber.create(Integer.class, Ops.COL_SIZE, this);
        }
        return size;
    }
    
    @Override
    public Expr<Collection<D>> asExpr() {
        return this;
    }

}