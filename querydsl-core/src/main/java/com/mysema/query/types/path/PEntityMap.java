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

    private EBoolean empty;

    private EBoolean notEmpty;

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

    public boolean equals(Object o) {
        return o instanceof Path ? ((Path<?>) o).getMetadata().equals(metadata)
                : false;
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

    public Path<?> getRoot() {
        return root;
    }

    public Class<V> getValueType() {
        return valueType;
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
}