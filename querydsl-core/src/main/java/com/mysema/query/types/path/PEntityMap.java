/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.util.Map;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;

/**
 * PEntityMap represents entity map paths
 * 
 * @author tiwe
 * 
 * @param <K> key type
 * @param <V> value type
 */
public class PEntityMap<K, V> extends Expr<Map<K, V>> implements PMap<K, V> {
    private EBoolean isnull, isnotnull;
    private final Class<K> keyType;
    private final PathMetadata<?> metadata;
    private final Class<V> valueType;
    private final String entityName;
    private final Path<?> root;
    private ENumber<Integer> size;    

    private EBoolean empty;

    private EBoolean notEmpty;

    public PEntityMap(Class<K> keyType, Class<V> valueType, String entityName,
            PathMetadata<?> metadata) {
        super((Class)Map.class);
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
            isnotnull = new OBoolean(Ops.ISNOTNULL, this);
        }
        return isnotnull;
    }
    
    @Override
    public EBoolean isNull() {
        if (isnull == null) {
            isnull = new OBoolean(Ops.ISNULL, this);
        }
        return isnull;
    }
    
    @Override
    public ENumber<Integer> size() {
        if (size == null) {
            size = ONumber.create(Integer.class, Ops.MAP_SIZE, this);
        }
        return size;
    }
}