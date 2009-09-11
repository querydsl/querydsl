/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ExprConst;
import com.mysema.query.types.expr.EEntity;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.util.NotEmpty;

/**
 * PEntity represents entity paths
 * 
 * @author tiwe
 *
 * @param <D> Java type
 */
@SuppressWarnings("serial")
public class PEntity<D> extends EEntity<D> implements Path<D> {
    
    private final String entityName;
    
    private EBoolean isnull, isnotnull;
    
    private final PathMetadata<?> metadata;
    
    private final Path<?> root;

    public PEntity(Class<? extends D> type, @NotEmpty String entityName,
            PathMetadata<?> metadata) {
        super(type);
        this.entityName = entityName;
        this.metadata = metadata;
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
    }

    public PEntity(Class<? extends D> type, @NotEmpty String entityName, @NotEmpty String var) {
        this(type, Assert.hasLength(entityName), PathMetadata.forVariable(var));
    }

    public PEntity(Class<? extends D> type, @NotEmpty String entityName, Path<?> parent, @NotEmpty String property) {
        this(type, Assert.hasLength(entityName), PathMetadata.forProperty(parent, property));
    }

    protected PBoolean createBoolean(@NotEmpty String propertyName) {
        return new PBoolean(this, propertyName);
    }

    protected <A extends Comparable<?>> PComparable<A> createComparable(@NotEmpty String propertyName, Class<A> type) {
        return new PComparable<A>(type, this, propertyName);
    }

    protected <A extends Comparable<?>> PDate<A> createDate(@NotEmpty String propertyName, Class<A> type) {
        return new PDate<A>(type, PathMetadata.forProperty(this, propertyName));
    }

    protected <A extends Comparable<?>> PDateTime<A> createDateTime(@NotEmpty String propertyName, Class<A> type) {
        return new PDateTime<A>(type, this, propertyName);
    }

    protected <A> PEntity<A> createEntity(@NotEmpty String property, @NotEmpty String entityName, Class<A> type) {
        return new PEntity<A>(type, entityName, this,property);
    }

    protected <A> PEntityCollection<A> createEntityCollection(@NotEmpty String property, Class<A> type, @NotEmpty String entityName) {
        return new PEntityCollection<A>(type, entityName, this, property);
    }

    protected <A> PEntityList<A> createEntityList(@NotEmpty String property, Class<A> type, @NotEmpty String entityName) {
        return new PEntityList<A>(type, entityName, this, property);
    }

    protected <K, V> PEntityMap<K, V> createEntityMap(@NotEmpty String property, Class<K> key, Class<V> value, @NotEmpty String entityName) {
        return new PEntityMap<K, V>(key, value, entityName, this, property);
    }

    protected <A extends Number & Comparable<?>> PNumber<A> createNumber(@NotEmpty String property, Class<A> type) {
        return new PNumber<A>(type, this, property);
    }

    protected <A> PSimple<A> createSimple(@NotEmpty String path, Class<A> type) {
        return new PSimple<A>(type, this, path);
    }

    protected <A> PComponentCollection<A> createSimpleCollection(@NotEmpty String path, Class<A> type) {
        return new PComponentCollection<A>(type, this,path);
    }

    protected <A> PComponentList<A> createSimpleList(@NotEmpty String path, Class<A> type) {
        return new PComponentList<A>(type, this, path);
    }

    protected <K, V> PComponentMap<K, V> createSimpleMap(@NotEmpty String path, Class<K> key, Class<V> value) {
        return new PComponentMap<K, V>(key, value, this, path);
    }

    protected PString createString(@NotEmpty String property) {
        return new PString(this, property);
    }

    protected <A extends Comparable<?>> PTime<A> createTime(@NotEmpty String propertyName, Class<A> type) {
        return new PTime<A>(type, this, propertyName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        return o instanceof Path ? ((Path<?>) o).getMetadata().equals(metadata) : false;
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

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }
    
    /**
     * Create an <code>this instanceOf type</code> expression
     * 
     * @param <B>
     * @param type
     * @return
     */
    public <B extends D> EBoolean instanceOf(Class<B> type) {
        return OBoolean.create(Ops.INSTANCE_OF, this, ExprConst.create(type));
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
}