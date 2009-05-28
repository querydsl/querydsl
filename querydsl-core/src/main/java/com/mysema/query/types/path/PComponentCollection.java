/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.Grammar;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.Expr;

/**
 * PComponentCollection represents component collection paths
 * 
 * @author tiwe
 * 
 * @param <D> component type
 */
public class PComponentCollection<D> extends Expr<java.util.Collection<D>>
        implements PCollection<D> {
    private EBoolean isNull, notNull;
    private final PathMetadata<?> metadata;
    private EComparable<Integer> size;
    protected final Class<D> type;
    private final Path<?> root;

    private EBoolean empty;

    private EBoolean notEmpty;

    public PComponentCollection(Class<D> type, PathMetadata<?> metadata) {
        super(null);
        this.type = type;
        this.metadata = metadata;
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
    }

    public PComponentCollection(Class<D> type, String var) {
        this(type, PathMetadata.forVariable(var));
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
        return o instanceof Path ? ((Path<?>) o).getMetadata().equals(metadata)
                : false;
    }

    @Override
    public Class<D> getElementType() {
        return type;
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
            notEmpty = Grammar.isNotEmpty(this); 
        }
        return notEmpty; 
    }

    @Override
    public EBoolean isNotNull() {
        if (notNull == null) {
            notNull = Grammar.isNotNull(this);
        }
        return notNull;
    }

    @Override
    public EBoolean isNull() {
        if (isNull == null) {
            isNull = Grammar.isNull(this);
        }
        return isNull;
    }

    @Override
    public EComparable<Integer> size() {
        if (size == null) {
            size = Grammar.size(this);
        }
        return size;
    }
}