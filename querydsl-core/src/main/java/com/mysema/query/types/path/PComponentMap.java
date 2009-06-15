/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.Grammar;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;

/**
 * PComponentMap represents component map paths
 * 
 * @author tiwe
 * 
 * @param <K> key type
 * @param <V> value type
 */
public class PComponentMap<K, V> extends Expr<java.util.Map<K, V>> implements PMap<K, V> {    
    private final Class<K> keyType;
    private final PathMetadata<?> metadata;
    private final Class<V> valueType;
    private final Path<?> root;
    
    private EBoolean isnull, isnotnull;
    private ENumber<Integer> size;    
    private EBoolean empty;
    private EBoolean notEmpty;

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

    @Override
    public boolean equals(Object o) {
        return o instanceof Path ? ((Path<?>) o).getMetadata().equals(metadata)
                : false;
    }

    @Override
    public Expr<V> get(Expr<K> key) {
        return new PSimple<V>(valueType, PathMetadata.forMapAccess(this, key));
    }

    @Override
    public Expr<V> get(K key) {
        return new PSimple<V>(valueType, PathMetadata.forMapAccess(this, key));
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