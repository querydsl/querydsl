/*
 * Copyright 2011, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.types.path;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.ExpressionException;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathImpl;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.PathType;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.types.expr.BooleanOperation;
import com.querydsl.core.types.expr.SimpleExpression;

/**
 * BeanPath represents bean paths
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public class BeanPath<T> extends SimpleExpression<T> implements Path<T> {

    private static final long serialVersionUID = -1845524024957822731L;

    private final Map<Class<?>, Object> casts = new HashMap<Class<?>, Object>();

    @Nullable
    private final PathInits inits;

    private final PathImpl<T> pathMixin;

    public BeanPath(Class<? extends T> type, String variable) {
        this(type, PathMetadataFactory.forVariable(variable), null);
    }

    public BeanPath(Class<? extends T> type, Path<?> parent,  String property) {
        this(type, PathMetadataFactory.forProperty(parent, property), null);
    }

    public BeanPath(Class<? extends T> type, PathMetadata<?> metadata) {
        this(type, metadata, null);
    }

    public BeanPath(Class<? extends T> type, PathMetadata<?> metadata, @Nullable PathInits inits) {
        super(new PathImpl<T>(type, metadata));
        this.pathMixin = (PathImpl<T>)mixin;
        this.inits = inits;
    }

    @Override
    public final <R,C> R accept(Visitor<R,C> v, C context) {
        // mixin is not used here, because subtype instances may have data that needs to be made available
        return v.visit(this, context);
    }

    /**
     * Cast the path to a subtype querytype
     *
     * @param <U>
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public <U extends BeanPath<? extends T>> U as(Class<U> clazz) {
        try {
            if (!casts.containsKey(clazz)) {
                PathMetadata<T> metadata;
                if (pathMixin.getMetadata().getPathType() != PathType.COLLECTION_ANY) {
                    metadata = PathMetadataFactory.forDelegate(pathMixin);
                } else {
                    metadata = (PathMetadata)pathMixin.getMetadata();
                }
                U rv;
                // the inits for the subtype will be wider, if it's a variable path
                if (inits != null && pathMixin.getMetadata().getPathType() != PathType.VARIABLE) {
                    rv = clazz.getConstructor(PathMetadata.class, PathInits.class).newInstance(metadata, inits);
                } else {
                    rv = clazz.getConstructor(PathMetadata.class).newInstance(metadata);
                }
                casts.put(clazz, rv);
                return rv;
            } else {
                return (U) casts.get(clazz);
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
    protected <P extends Path<?>> P add(P path) {
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
    protected <A, E> ArrayPath<A, E> createArray(String property, Class<? super A> type) {
        return add(new ArrayPath<A, E>(type, forProperty(property)));
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
    @SuppressWarnings("unchecked")
    protected <A, Q extends SimpleExpression<? super A>> CollectionPath<A, Q> createCollection(String property, Class<? super A> type, Class<? super Q> queryType, PathInits inits) {
        return add(new CollectionPath<A, Q>(type, (Class) queryType, forProperty(property), inits));
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
    protected <A extends Enum<A>> EnumPath<A> createEnum(String property, Class<A> type) {
        return add(new EnumPath<A>(type, forProperty(property)));
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
    protected <A, E extends SimpleExpression<? super A>> ListPath<A, E> createList(String property, Class<? super A> type, Class<? super E> queryType, PathInits inits) {
        return add(new ListPath<A, E>(type, (Class) queryType, forProperty(property), inits));
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
    protected <K, V, E extends SimpleExpression<? super V>> MapPath<K, V, E> createMap(String property, Class<? super K> key, Class<? super V> value, Class<? super E> queryType) {
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
    @SuppressWarnings("unchecked")
    protected <A, E extends SimpleExpression<? super A>> SetPath<A, E> createSet(String property, Class<? super A> type, Class<? super E> queryType, PathInits inits) {
        return add(new SetPath<A, E>(type, (Class) queryType, forProperty(property), inits));
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

    /**
     * Create an {@code this instanceOf type} expression
     *
     * @param <B>
     * @param type
     * @return
     */
    public <B extends T> BooleanExpression instanceOf(Class<B> type) {
        return BooleanOperation.create(Ops.INSTANCE_OF, pathMixin, ConstantImpl.create(type));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public BooleanExpression instanceOfAny(Class... types) {
        BooleanExpression[] exprs = new BooleanExpression[types.length];
        for (int i = 0; i < types.length; i++) {
            exprs[i] = this.instanceOf(types[i]);
        }
        return BooleanExpression.anyOf(exprs);
    }

    @Override
    public AnnotatedElement getAnnotatedElement() {
        return pathMixin.getAnnotatedElement();
    }

}
