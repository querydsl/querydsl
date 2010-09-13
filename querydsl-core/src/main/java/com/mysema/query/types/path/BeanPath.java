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

import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionException;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.expr.SimpleConstant;
import com.mysema.query.types.expr.BooleanOperation;

/**
 * BeanPath represents bean paths
 *
 * @author tiwe
 *
 * @param <D>
 *            Java type
 */
public class BeanPath<D> extends SimpleExpression<D> implements Path<D> {

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
            throw new ExpressionException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new ExpressionException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new ExpressionException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new ExpressionException(e.getMessage(), e);
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
    protected <A> ArrayPath<A> createArray(String property, Class<? super A[]> type) {
        return add(new ArrayPath<A>(type, forProperty(property)));
    }

    /**
     * Create a new Boolean path
     *
     * @param property
     * @return
     */
    protected BooleanPath createBoolean(String property) {
        return add(new BooleanPath(forProperty(property)));
    }

    /**
     * Create a new Collection typed path
     *
     * @param <A>
     * @param property
     * @param type
     * @return
     */
    protected <A> CollectionPath<A> createCollection(String property, Class<? super A> type) {
        return add(new CollectionPath<A>(type, type.getSimpleName(), forProperty(property)));
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
    protected <A extends Comparable> ComparablePath<A> createComparable(String property, Class<? super A> type) {
        return add(new ComparablePath<A>((Class) type, forProperty(property)));
    }
    
    /**
     * @param <A>
     * @param property
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <A extends Enum<A>> EnumPath<A> createEnum(String property, Class<A> type) {
        return add(new EnumPath<A>((Class) type, forProperty(property)));
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
    protected <A extends Comparable> DatePath<A> createDate(String property, Class<? super A> type) {
        return add(new DatePath<A>((Class) type, forProperty(property)));
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
    protected <A extends Comparable> DateTimePath<A> createDateTime(String property, Class<? super A> type) {
        return add(new DateTimePath<A>((Class) type, forProperty(property)));
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
    protected <A, E extends SimpleExpression<A>> ListPath<A, E> createList(String property, Class<? super A> type, Class<? super E> queryType) {
        return add(new ListPath<A, E>(type, (Class) queryType, forProperty(property)));
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
    protected <K, V, E extends SimpleExpression<V>> MapPath<K, V, E> createMap(String property, Class<? super K> key, Class<? super V> value, Class<? super E> queryType) {
        return add(new MapPath<K, V, E>(key, value, (Class) queryType, forProperty(property)));
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
    protected <A extends Number & Comparable<?>> NumberPath<A> createNumber(String property, Class<? super A> type) {
        return add(new NumberPath<A>((Class) type, forProperty(property)));
    }

    /**
     * Create a new Set typed path
     *
     * @param <A>
     * @param property
     * @param type
     * @return
     */
    protected <A> SetPath<A> createSet(String property, Class<? super A> type) {
        return add(new SetPath<A>(type, type.getSimpleName(), forProperty(property)));
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
    protected <A> SimplePath<A> createSimple(String property, Class<? super A> type) {
        return add(new SimplePath<A>((Class<A>) type, forProperty(property)));
    }

    /**
     * Create a new String path
     *
     * @param property
     * @return
     */
    protected StringPath createString(String property) {
        return add(new StringPath(forProperty(property)));
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
    protected <A extends Comparable> TimePath<A> createTime(String property, Class<? super A> type) {
        return add(new TimePath<A>((Class) type, forProperty(property)));
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
    public <B extends D> BooleanExpression instanceOf(Class<B> type) {
        return BooleanOperation.create(Ops.INSTANCE_OF, this, SimpleConstant.create(type));
    }

    @Override
    public AnnotatedElement getAnnotatedElement(){
        return pathMixin.getAnnotatedElement();
    }

}
