/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.EEntity;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.Ops;

/**
 * PEntity represents entity paths
 * 
 * @author tiwe
 *
 * @param <D> Java type
 */
public class PEntity<D> extends EEntity<D> implements Path<D> {
    private final String entityName;
    private EBoolean isnull, isnotnull;
    private final PathMetadata<?> metadata;
    private final Path<?> root;

    /**
     * Create a new PEntity instance with the given Java type, entity name and Path metadata
     * 
     * @param type
     * @param entityName
     * @param metadata
     */
    public PEntity(Class<? extends D> type, String entityName,
            PathMetadata<?> metadata) {
        super(type);
        this.entityName = entityName;
        this.metadata = metadata;
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
    }

    /**
     * Create a new PEntity instance with the given Java type, entity name and variable name
     * 
     * @param type
     * @param entityName
     * @param var
     */
    public PEntity(Class<? extends D> type, String entityName, String var) {
        this(type, entityName, PathMetadata.forVariable(var));
    }
    
    /**
     * Create Boolean typed subpath for the given property
     * 
     * @param propertyName
     * @return
     */
    protected PBoolean _boolean(String propertyName) {
        return new PBoolean(PathMetadata.forProperty(this, propertyName));
    }

    /**
     * Create a Comparable typed subpath for the given property
     * 
     * @param <A>
     * @param propertyName
     * @param type
     * @return
     */
    protected <A extends Comparable<?>> PComparable<A> _comparable(String propertyName,
            Class<A> type) {
        return new PComparable<A>(type, PathMetadata.forProperty(this, propertyName));
    }
    
    /**
     * @param <A>
     * @param propertyName
     * @param type
     * @return
     */
    protected <A extends Comparable<?>> PDate<A> _date(String propertyName,
            Class<A> type) {
        return new PDate<A>(type, PathMetadata.forProperty(this, propertyName));
    }
    
    /**
     * @param <A>
     * @param propertyName
     * @param type
     * @return
     */
    protected <A extends Comparable<?>> PDateTime<A> _dateTime(String propertyName,
            Class<A> type) {
        return new PDateTime<A>(type, PathMetadata.forProperty(this, propertyName));
    }
    
    /**
     * Create an Entity subpath for the given property
     * 
     * @param <A>
     * @param property
     * @param entityName
     * @param type
     * @return
     */
    protected <A> PEntity<A> _entity(String property, String entityName,
            Class<A> type) {
        return new PEntity<A>(type, entityName, PathMetadata.forProperty(this,
                property));
    }

    /**
     * Create a new Entity collection subpath for the given property
     * 
     * @param <A>
     * @param property
     * @param type
     * @param entityName
     * @return
     */
    protected <A> PEntityCollection<A> _entitycol(String property, Class<A> type,
            String entityName) {
        return new PEntityCollection<A>(type, entityName, PathMetadata
                .forProperty(this, property));
    }

    /**
     * Create a new Entity list subpath for the given property
     * 
     * @param <A>
     * @param property
     * @param type
     * @param entityName
     * @return
     */
    protected <A> PEntityList<A> _entitylist(String property, Class<A> type,
            String entityName) {
        return new PEntityList<A>(type, entityName, PathMetadata.forProperty(
                this, property));
    }

    /**
     * Create a new Entity map subpath for the given property
     * 
     * @param <K>
     * @param <V>
     * @param path
     * @param key
     * @param value
     * @param entityName
     * @return
     */
    protected <K, V> PEntityMap<K, V> _entitymap(String property, Class<K> key,
            Class<V> value, String entityName) {
        return new PEntityMap<K, V>(key, value, entityName, PathMetadata
                .forProperty(this, property));
    }

    /**
     * Create a new numeric subpath for the given property
     * 
     * @param <A>
     * @param property
     * @param type
     * @return
     */
    protected <A extends Number & Comparable<?>> PNumber<A> _number(
            String property, Class<A> type) {
        return new PNumber<A>(type, PathMetadata.forProperty(this, property));
    }

    /**
     * Create a new simple subpath for the given property
     * 
     * @param <A>
     * @param path
     * @param type
     * @return
     */
    protected <A> PSimple<A> _simple(String path, Class<A> type) {
        return new PSimple<A>(type, PathMetadata.forProperty(this, path));
    }

    /**
     * Create a new Collection typed subpath for the given property
     * 
     * @param <A>
     * @param path
     * @param type
     * @return
     */
    protected <A> PComponentCollection<A> _simplecol(String path, Class<A> type) {
        return new PComponentCollection<A>(type, PathMetadata.forProperty(this,
                path));
    }

    /**
     * Create a new List typed subpath for the given property
     * 
     * @param <A>
     * @param path
     * @param type
     * @return
     */
    protected <A> PComponentList<A> _simplelist(String path, Class<A> type) {
        return new PComponentList<A>(type, PathMetadata.forProperty(this, path));
    }

    /**
     * Create a new Map typed subpath for the given property
     * 
     * @param <K>
     * @param <V>
     * @param path
     * @param key
     * @param value
     * @return
     */
    protected <K, V> PComponentMap<K, V> _simplemap(String path, Class<K> key,
            Class<V> value) {
        return new PComponentMap<K, V>(key, value, PathMetadata.forProperty(
                this, path));
    }

    /**
     * Create a new String typed subpath for the given property
     * 
     * @param property
     * @return
     */
    protected PString _string(String property) {
        return new PString(PathMetadata.forProperty(this, property));
    }

    /**
     * @param <A>
     * @param propertyName
     * @param type
     * @return
     */
    protected <A extends Comparable<?>> PTime<A> _time(String propertyName,
            Class<A> type) {
        return new PTime<A>(type, PathMetadata.forProperty(this, propertyName));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        return o instanceof Path ? ((Path<?>) o).getMetadata().equals(metadata)
                : false;
    }

    /**
     * Get the entity name for this Entity path
     * 
     * @return
     */
    public String getEntityName() {
        return entityName;
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

    /**
     * Create an <code>this instanceOf type</code> expression
     * 
     * @param <B>
     * @param type
     * @return
     */
    public <B extends D> EBoolean instanceOf(Class<B> type) {
        return new OBoolean(Ops.INSTANCEOF, this, EConstant.create(type));
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
}