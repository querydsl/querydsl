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
public class PEntityMap<K, V, E extends PEntity<V>> extends EMapBase<K, V> implements PMap<K, V> {
    
    private volatile EBoolean isnull, isnotnull;
    
    private final Class<K> keyType;
    
    private final Class<V> valueType;
    
    private final Class<E> queryType;
    
    private final PathMetadata<?> metadata;
    
    @NotEmpty 
    private final String entityName;
    
    private final Path<?> root;

    @SuppressWarnings("unchecked")
    public PEntityMap(Class<? super K> keyType, Class<? super V> valueType, Class<E> queryType, PathMetadata<?> metadata) {
        super((Class)Map.class);
        this.keyType = (Class<K>) keyType;
        this.valueType = (Class<V>) valueType;
        this.queryType = queryType;
        this.entityName = valueType.getSimpleName();
        this.metadata = metadata;
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
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
    public E get(Expr<K> key) {
        PathMetadata<K> md =  PathMetadata.forMapAccess(this, key);
        try {
            return queryType.getConstructor(PathMetadata.class).newInstance(md);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public E get(K key) {
        PathMetadata<K> md =  PathMetadata.forMapAccess(this, key);
        try {
            return queryType.getConstructor(PathMetadata.class).newInstance(md);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
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