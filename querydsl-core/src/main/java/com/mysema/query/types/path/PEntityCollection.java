/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.Grammar;
import com.mysema.query.types.alias.AEntityCollection;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EEntity;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 * 
 * @param <D>
 */
public class PEntityCollection<D> extends EEntity<java.util.Collection<D>>
        implements PCollection<D> {
    private EBoolean isnull, isnotnull;
    private final PathMetadata<?> metadata;
    private ENumber<Integer> size;
    protected final Class<D> type;
    protected final String entityName;
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

    public AEntityCollection<D> as(PEntity<D> to) {
        return Grammar.as(this, to);
    }

    public EBoolean contains(D child) {
        return Grammar.in(child, this);
    }

    public EBoolean contains(Expr<D> child) {
        return Grammar.in(child, this);
    }

    public boolean equals(Object o) {
        return o instanceof Path ? ((Path<?>) o).getMetadata().equals(metadata) : false;
    }

    public Class<D> getElementType() {
        return type;
    }

    public String getEntityName() {
        return entityName;
    }

    public PathMetadata<?> getMetadata() {
        return metadata;
    }

    public Path<?> getRoot() {
        return root;
    }
    
    public int hashCode() {
        return metadata.hashCode();
    }

    public EBoolean isEmpty() {
        if (empty == null){
            empty = Grammar.isEmpty(this);
        }
        return empty;
    }
    
    public EBoolean isNotEmpty() {
        if (notEmpty == null){
            notEmpty = Grammar.isNotEmpty(this); 
        }
        return notEmpty;
    }

    public EBoolean isNotNull() {
        if (isnotnull == null) {
            isnotnull = Grammar.isNotNull(this);
        }
        return isnotnull;
    }

    public EBoolean isNull() {
        if (isnull == null) {
            isnull = Grammar.isNull(this);
        }
        return isnull;
    }

    public ENumber<Integer> size() {
        if (size == null) {
            size = Grammar.size(this);
        }
        return size;
    }

}