/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.util.Map;

import com.mysema.query.types.Grammar;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 * 
 * @param <K>
 * @param <V>
 */
public class PEntityMap<K, V> extends Expr<Map<K, V>> implements PMap<K, V> {
    private EBoolean isnull, isnotnull;
    private final Class<K> keyType;
    private final PathMetadata<?> metadata;
    private final Class<V> valueType;
    private final String entityName;
    private final Path<?> root;

    public PEntityMap(Class<K> keyType, Class<V> valueType, String entityName,
            PathMetadata<?> metadata) {
        super(null);
        this.keyType = keyType;
        this.valueType = valueType;
        this.entityName = entityName;
        this.metadata = metadata;
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
    }

    public PEntityMap(Class<K> keyType, Class<V> valueType, String entityName,
            String var) {
        this(keyType, valueType, entityName, PathMetadata.forVariable(var));
    }

    public PEntity<V> get(Expr<K> key) {
        return new PEntity<V>(valueType, entityName, PathMetadata.forMapAccess(
                this, key));
    }

    public PEntity<V> get(K key) {
        return new PEntity<V>(valueType, entityName, PathMetadata.forMapAccess(
                this, key));
    }

    public Class<K> getKeyType() {
        return keyType;
    }

    public PathMetadata<?> getMetadata() {
        return metadata;
    }

    public Class<V> getValueType() {
        return valueType;
    }

    public EBoolean isnotnull() {
        if (isnotnull == null) {
            isnotnull = Grammar.isnotnull(this);
        }
        return isnotnull;
    }

    public EBoolean isnull() {
        if (isnull == null) {
            isnull = Grammar.isnull(this);
        }
        return isnull;
    }

    public Path<?> getRoot() {
        return root;
    }

    public int hashCode() {
        return metadata.hashCode();
    }

    public boolean equals(Object o) {
        return o instanceof Path ? ((Path<?>) o).getMetadata().equals(metadata)
                : false;
    }
    
    @Override
    public EBoolean containsKey(Expr<K> key) {
        return Grammar.containsKey(this, key);
    }

    @Override
    public EBoolean containsKey(K key) {
        return Grammar.containsKey(this, key);
    }

    @Override
    public EBoolean containsValue(Expr<V> value) {
        return Grammar.containsValue(this, value);
    }

    @Override
    public EBoolean containsValue(V value) {
        return Grammar.containsValue(this, value);
    }
}