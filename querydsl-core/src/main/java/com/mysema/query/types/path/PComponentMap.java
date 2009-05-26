/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.Grammar;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ESimple;
import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 * 
 * @param <K>
 * @param <V>
 */
public class PComponentMap<K, V> extends ESimple<java.util.Map<K, V>> implements
        PMap<K, V> {
    private EBoolean isnull, isnotnull;
    private final Class<K> keyType;
    private final PathMetadata<?> metadata;
    private final Class<V> valueType;
    private final Path<?> root;

    public PComponentMap(Class<K> keyType, Class<V> valueType,
            PathMetadata<?> metadata) {
        super(null);
        this.keyType = keyType;
        this.valueType = valueType;
        this.metadata = metadata;
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
    }

    public PComponentMap(Class<K> keyType, Class<V> valueType, String var) {
        this(keyType, valueType, PathMetadata.forVariable(var));
    }

    public ESimple<V> get(Expr<K> key) {
        return new PSimple<V>(valueType, PathMetadata.forMapAccess(this, key));
    }

    public ESimple<V> get(K key) {
        return new PSimple<V>(valueType, PathMetadata.forMapAccess(this, key));
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
    
    public EBoolean empty() {
        return Grammar.empty(this);
    }

    public EBoolean notEmpty() {
        return Grammar.notEmpty(this);
    }
}