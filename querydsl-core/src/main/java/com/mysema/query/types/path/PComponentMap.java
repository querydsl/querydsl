/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.util.Map;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;

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

    @SuppressWarnings("unchecked")
    public PComponentMap(Class<K> keyType, Class<V> valueType,
            PathMetadata<?> metadata) {
        super((Class)Map.class);
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
        return new OBoolean(Ops.CONTAINS_KEY, this, key);
    }

    @Override
    public EBoolean containsKey(K key) {
        return new OBoolean(Ops.CONTAINS_KEY, this, EConstant.create(key));
    }

    @Override
    public EBoolean containsValue(Expr<V> value) {
        return new OBoolean(Ops.CONTAINS_VALUE, this, value);
    }

    @Override
    public EBoolean containsValue(V value) {
        return new OBoolean(Ops.CONTAINS_VALUE, this, EConstant.create(value));
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
            empty = new OBoolean(Ops.MAP_ISEMPTY, this);
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
            isnotnull = new OBoolean(Ops.ISNOTNULL);
        }
        return isnotnull;
    }
    
    @Override
    public EBoolean isNull() {
        if (isnull == null) {
            isnull = new OBoolean(Ops.ISNULL);
        }
        return isnull;
    }
    
    @Override
    public ENumber<Integer> size() {
        if (size == null) {
            size = ONumber.create(Integer.class,Ops.MAP_SIZE, this);
        }
        return size;
    }
}