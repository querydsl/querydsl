/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.Grammar;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EEntity;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;

/**
 * PEntityCollection represents an entity collection path
 * 
 * @author tiwe
 * 
 * @param <D> component type
 */
public class PEntityCollection<D> extends EEntity<java.util.Collection<D>> implements PCollection<D> {
    private final PathMetadata<?> metadata;
    protected final Class<D> type;
    protected final String entityName;
    
    private EBoolean isnull, isnotnull;    
    private ENumber<Integer> size;    
    private final Path<?> root;
    private EBoolean empty;
    private EBoolean notEmpty;

    public PEntityCollection(Class<D> type, String entityName,
            PathMetadata<?> metadata) {
        super(null);
        this.type = type;
        this.metadata = metadata;
        this.entityName = entityName;
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
    }

    public PEntityCollection(Class<D> type, String entityName, String var) {
        this(type, entityName, PathMetadata.forVariable(var));
    }

    @Override
    public EBoolean contains(D child) {
        return Grammar.in(child, this);
    }

    @Override
    public EBoolean contains(Expr<D> child) {
        return Grammar.in(child, this);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Path ? ((Path<?>) o).getMetadata().equals(metadata) : false;
    }

    @Override
    public Class<D> getElementType() {
        return type;
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
            empty = Grammar.isEmpty(this);
        }
        return empty;
    }
    
    @Override
    public EBoolean isNotEmpty() {
        if (notEmpty == null){
            notEmpty = Grammar.isEmpty(this).not(); 
        }
        return notEmpty;
    }

    @Override
    public EBoolean isNotNull() {
        if (isnotnull == null) {
            isnotnull = Grammar.isNotNull(this);
        }
        return isnotnull;
    }

    @Override
    public EBoolean isNull() {
        if (isnull == null) {
            isnull = Grammar.isNull(this);
        }
        return isnull;
    }

    @Override
    public ENumber<Integer> size() {
        if (size == null) {
            size = Grammar.size(this);
        }
        return size;
    }

}