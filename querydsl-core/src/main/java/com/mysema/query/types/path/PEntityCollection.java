/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.util.Collection;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.EEntity;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;

/**
 * PEntityCollection represents an entity collection path
 * 
 * @author tiwe
 * 
 * @param <D> component type
 */
public class PEntityCollection<D> extends EEntity<java.util.Collection<D>> implements PCollection<D> {
    private final PathMetadata<?> metadata;
    protected final Class<D> elementType;
    protected final String entityName;
    
    private EBoolean isnull, isnotnull;    
    private ENumber<Integer> size;    
    private final Path<?> root;
    private EBoolean empty;
    private EBoolean notEmpty;

    @SuppressWarnings("unchecked")
    public PEntityCollection(Class<D> type, String entityName,
            PathMetadata<?> metadata) {
        super((Class)Collection.class);
        this.elementType = Assert.notNull(type,"type is null");
        this.metadata = Assert.notNull(metadata,"metadata is null");
        this.entityName = Assert.notNull(entityName,"entityName is null");
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
    }

    public PEntityCollection(Class<D> type, String entityName, String var) {
        this(type, entityName, PathMetadata.forVariable(var));
    }

    @Override
    public EBoolean contains(D child) {
        return new OBoolean(Ops.IN, EConstant.create(child), this);
    }

    @Override
    public EBoolean contains(Expr<D> child) {
        return new OBoolean(Ops.IN, child, this);
    }

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
            empty = new OBoolean(Ops.COL_ISEMPTY, this);
        }
        return empty;
    }
    
    @Override
    public EBoolean isNotEmpty() {
        if (notEmpty == null){
            notEmpty = isEmpty().not(); 
        }
        return notEmpty;
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

    @Override
    public ENumber<Integer> size() {
        if (size == null) {
            size = ONumber.create(Integer.class, Ops.COL_SIZE, this);
        }
        return size;
    }

}