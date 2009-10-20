/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.util.Map;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EMapBase;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.util.NotEmpty;

/**
 * PEntityMap represents entity map paths
 * 
 * @author tiwe
 * 
 * @param <K> key type
 * @param <V> value type
 */
@SuppressWarnings("serial")
public class PEntityMap<K, V> extends EMapBase<K, V> implements PMap<K, V> {
    
    private volatile EBoolean isnull, isnotnull;
    
    private final Class<K> keyType;
    
    private final PathMetadata<?> metadata;
    
    private final Class<V> valueType;
    
    @NotEmpty 
    private final String entityName;
    
    private final Path<?> root;

    @SuppressWarnings("unchecked")
    public PEntityMap(Class<? super K> keyType, Class<? super V> valueType, @NotEmpty String entityName,
            PathMetadata<?> metadata) {
        super((Class)Map.class);
        this.keyType = (Class<K>) keyType;
        this.valueType = (Class<V>) valueType;
        this.entityName = entityName;
        this.metadata = metadata;
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
    }

    public PEntityMap(Class<? super K> keyType, Class<? super V> valueType, @NotEmpty String entityName, @NotEmpty String var) {
        this(keyType, valueType, entityName, PathMetadata.forVariable(var));
    }
    
    public PEntityMap(Class<? super K> keyType, Class<? super V> valueType, @NotEmpty String entityName, Path<?> parent, @NotEmpty String var) {
        this(keyType, valueType, entityName, PathMetadata.forProperty(parent, var));
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        return o instanceof Path ? ((Path<?>) o).getMetadata().equals(metadata)
                : false;
    }

    @Override
    public PEntity<V> get(Expr<K> key) {
        return new PEntity<V>(valueType, entityName, PathMetadata.forMapAccess(
                this, key));
    }

    @Override
    public PEntity<V> get(K key) {
        return new PEntity<V>(valueType, entityName, PathMetadata.forMapAccess(
                this, key));
    }

    @Override
    public Class<K> getKeyType() {
        return keyType;
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
    public Class<V> getValueType() {
        return valueType;
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
    public EMapBase<K,V> asExpr() {
        return this;
    }
    
}