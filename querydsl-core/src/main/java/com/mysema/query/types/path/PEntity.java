/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.util.HashMap;
import java.util.Map;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EEntity;
import com.mysema.query.types.expr.ExprConst;
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
    
    private final Map<Class<?>,Object> casts = new HashMap<Class<?>,Object>();
    
    private final String entityName;
    
    private volatile EBoolean isnull, isnotnull;
    
    private final PathMetadata<?> metadata;

    private final Path<?> root;

    public PEntity(Class<? extends D> type, @NotEmpty String entityName, PathMetadata<?> metadata) {
        super(type);
        this.entityName = entityName;
        this.metadata = metadata;
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }

    /**
     * Cast the path to a subtype querytype
     * 
     * @param <T>
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends PEntity<? extends D>> T as(Class<T> clazz){
        try {
            if (!casts.containsKey(clazz)){
                T rv = (T)clazz.getConstructor(PathMetadata.class).newInstance(this.getMetadata());
                casts.put(clazz, rv);
                return rv;
            }else{
                return (T)casts.get(clazz);    
            }
             
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public EEntity<D> asExpr() {
        return this;
    }

    protected PBoolean createBoolean(@NotEmpty String propertyName) {
        return new PBoolean(this, propertyName);
    }

    protected <A extends Comparable<?>> PComparable<A> createComparable(@NotEmpty String property, Class<A> type) {
        return new PComparable<A>(type, this, property);
    }

    protected <A extends Comparable<?>> PDate<A> createDate(@NotEmpty String property, Class<A> type) {
        return new PDate<A>(type, PathMetadata.forProperty(this, property));
    }

    protected <A extends Comparable<?>> PDateTime<A> createDateTime(@NotEmpty String property, Class<A> type) {
        return new PDateTime<A>(type, this, property);
    }

    protected <A> PEntityCollection<A> createEntityCollection(@NotEmpty String property, Class<? super A> type) {
        return new PEntityCollection<A>(type, type.getSimpleName(), this, property);
    }

    protected <A, E extends PEntity<A>> PEntityList<A, E> createEntityList(@NotEmpty String property, Class<? super A> type, Class<E> queryType) {
        return new PEntityList<A, E>(type, queryType, PathMetadata.forProperty(this, property));
    }

    protected <K, V, E extends PEntity<V>> PEntityMap<K, V, E> createEntityMap(@NotEmpty String property, Class<? super K> key, Class<? super V> value, Class<E> queryType) {
        return new PEntityMap<K, V, E>(key, value, queryType, PathMetadata.forProperty(this, property));
    }

    protected <A extends Number & Comparable<?>> PNumber<A> createNumber(@NotEmpty String property, Class<A> type) {
        return new PNumber<A>(type, this, property);
    }

    @SuppressWarnings("unchecked")
    protected <A> PSimple<A> createSimple(@NotEmpty String path, Class<? super A> type) {
        return new PSimple<A>((Class<A>)type, this, path);
    }

    protected <A> PComponentCollection<A> createSimpleCollection(@NotEmpty String property, Class<A> type) {
        return new PComponentCollection<A>(type, this, property);
    }

    protected <A> PComponentList<A> createSimpleList(@NotEmpty String property, Class<A> type) {
        return new PComponentList<A>(type, PathMetadata.forProperty(this, property));
    }

    protected <K, V> PComponentMap<K, V> createSimpleMap(@NotEmpty String property, Class<? super K> key, Class<? super V> value) {
        return new PComponentMap<K, V>(key, value, this, property);
    }

    protected PString createString(@NotEmpty String property) {
        return new PString(this, property);
    }

    protected <A extends Comparable<?>> PTime<A> createTime(@NotEmpty String property, Class<A> type) {
        return new PTime<A>(type, this, property);
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