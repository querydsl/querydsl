/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.mysema.query.types.Expr;
import com.mysema.query.types.ExprException;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ESimple;
import com.mysema.query.types.expr.ExprConst;
import com.mysema.query.types.expr.OBoolean;

/**
 * BeanPath represents bean paths
 *
 * @author tiwe
 *
 * @param <D>
 *            Java type
 */
public class BeanPath<D> extends ESimple<D> implements Path<D> {

    private static final long serialVersionUID = -1845524024957822731L;

    private final Map<Class<?>, Object> casts = new HashMap<Class<?>, Object>();

    @Nullable
    private final PathInits inits;

    private final Path<D> pathMixin;

    public BeanPath(Class<? extends D> type, String variable) {
        this(type, PathMetadataFactory.forVariable(variable), null);
    }

    public BeanPath(Class<? extends D> type, PathMetadata<?> metadata) {
        this(type, metadata, null);
    }

    public BeanPath(Class<? extends D> type, PathMetadata<?> metadata, @Nullable PathInits inits) {
        super(type);
        this.pathMixin = new PathMixin<D>(this, metadata);
        this.inits = inits;
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    /**
     * Cast the path to a subtype querytype
     *
     * @param <T>
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends BeanPath<? extends D>> T as(Class<T> clazz) {
        try {
            if (!casts.containsKey(clazz)) {
                T rv;
                if (inits != null) {
                    rv = clazz.getConstructor(PathMetadata.class,
                            PathInits.class).newInstance(this.getMetadata(), inits);
                } else {
                    rv = (T) clazz.getConstructor(PathMetadata.class)
                            .newInstance(this.getMetadata());
                }
                casts.put(clazz, rv);
                return rv;
            } else {
                return (T) casts.get(clazz);
            }

        } catch (InstantiationException e) {
            throw new ExprException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new ExprException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new ExprException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new ExprException(e.getMessage(), e);
        }
    }

    /**
     * Template method for tracking child path creation
     * 
     * @param <P>
     * @param path
     * @return
     */
    protected <P extends Path<?>> P add(P path){
        return path;
    }
    
    /**
     * Create a new array path
     *
     * @param <A>
     * @param property
     * @param type
     * @return
     */
    protected <A> PArray<A> createArray(String property, Class<? super A[]> type) {
        return add(new PArray<A>(type, forProperty(property)));
    }

    /**
     * Create a new Boolean path
     *
     * @param property
     * @return
     */
    protected PBoolean createBoolean(String property) {
        return add(new PBoolean(forProperty(property)));
    }

    /**
     * Create a new Collection typed path
     *
     * @param <A>
     * @param property
     * @param type
     * @return
     */
    protected <A> PCollection<A> createCollection(String property, Class<? super A> type) {
        return add(new PCollection<A>(type, type.getSimpleName(), forProperty(property)));
    }

    /**
     * Create a new Comparable typed path
     *
     * @param <A>
     * @param property
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <A extends Comparable> PComparable<A> createComparable(String property, Class<? super A> type) {
        return add(new PComparable<A>((Class) type, forProperty(property)));
    }
    
    /**
     * @param <A>
     * @param property
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <A extends Enum<A>> PEnum<A> createEnum(String property, Class<A> type) {
        return add(new PEnum<A>((Class) type, forProperty(property)));
    }


    /**
     * Create a new Date path
     *
     * @param <A>
     * @param property
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <A extends Comparable> PDate<A> createDate(String property, Class<? super A> type) {
        return add(new PDate<A>((Class) type, forProperty(property)));
    }

    /**
     * Create a new DateTime path
     *
     * @param <A>
     * @param property
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <A extends Comparable> PDateTime<A> createDateTime(String property, Class<? super A> type) {
        return add(new PDateTime<A>((Class) type, forProperty(property)));
    }

    /**
     * Create a new List typed path
     *
     * @param <A>
     * @param <E>
     * @param property
     * @param type
     * @param queryType
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <A, E extends Expr<A>> PList<A, E> createList(String property, Class<? super A> type, Class<? super E> queryType) {
        return add(new PList<A, E>(type, (Class) queryType, forProperty(property)));
    }

    /**
     * Create a new Map typed path
     *
     * @param <K>
     * @param <V>
     * @param <E>
     * @param property
     * @param key
     * @param value
     * @param queryType
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <K, V, E extends Expr<V>> PMap<K, V, E> createMap(String property, Class<? super K> key, Class<? super V> value, Class<? super E> queryType) {
        return add(new PMap<K, V, E>(key, value, (Class) queryType, forProperty(property)));
    }

    /**
     * Create a new Number path
     *
     * @param <A>
     * @param property
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <A extends Number & Comparable<?>> PNumber<A> createNumber(String property, Class<? super A> type) {
        return add(new PNumber<A>((Class) type, forProperty(property)));
    }

    /**
     * Create a new Set typed path
     *
     * @param <A>
     * @param property
     * @param type
     * @return
     */
    protected <A> PSet<A> createSet(String property, Class<? super A> type) {
        return add(new PSet<A>(type, type.getSimpleName(), forProperty(property)));
    }

    /**
     * Create a new Simpe path
     *
     * @param <A>
     * @param property
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <A> PSimple<A> createSimple(String property, Class<? super A> type) {
        return add(new PSimple<A>((Class<A>) type, forProperty(property)));
    }

    /**
     * Create a new String path
     *
     * @param property
     * @return
     */
    protected PString createString(String property) {
        return add(new PString(forProperty(property)));
    }

    /**
     * Create a new Time path
     *
     * @param <A>
     * @param property
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <A extends Comparable> PTime<A> createTime(String property, Class<? super A> type) {
        return add(new PTime<A>((Class) type, forProperty(property)));
    }

    @Override
    public boolean equals(Object o) {
        return pathMixin.equals(o);
    }

    protected PathMetadata<?> forProperty(String property) {
        return PathMetadataFactory.forProperty(this, property);
    }

    @Override
    public PathMetadata<?> getMetadata() {
        return pathMixin.getMetadata();
    }

    @Override
    public Path<?> getRoot() {
        return pathMixin.getRoot();
    }

    @Override
    public int hashCode() {
        return pathMixin.hashCode();
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
        return pathMixin.isNotNull();
    }

    @Override
    public EBoolean isNull() {
        return pathMixin.isNull();
    }

    @Override
    public AnnotatedElement getAnnotatedElement(){
        return pathMixin.getAnnotatedElement();
    }

}
