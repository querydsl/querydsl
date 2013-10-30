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
package com.mysema.query.types.path;

import java.util.Map;

import com.google.common.collect.Maps;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.expr.SimpleExpression;

/**
 * PathBuilder is an extension to EntityPathBase for dynamic path construction
 *
 * <p>Usage example:</p>
 * <pre>
 * PathBuilder&lt;User&gt; user = new PathBuilder&lt;User&gt;(User.class, "user");
 * Predicate filter = user.getString("firstName").eq("Bob");
 * List&lt;User&gt; users = query.from(user).where(filter).list(user);
 * </pre>
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public final class PathBuilder<T> extends EntityPathBase<T> {

    private static final long serialVersionUID = -1666357914232685088L;

    private final Map<String, PathBuilder<?>> properties = Maps.newHashMap();

    private final Map<Path<?>, Object> propertyMetadata = Maps.newHashMap();

    /**
     * Creates a new PathBuilder instance
     *
     * @param type
     * @param pathMetadata
     */
    public PathBuilder(Class<? extends T> type, PathMetadata<?> pathMetadata) {
        super(type, pathMetadata);
    }

    /**
     * Creates a new PathBuilder instance
     *
     * @param type
     * @param variable
     */
    public PathBuilder(Class<? extends T> type, String variable) {
        super(type, PathMetadataFactory.forVariable(variable));
    }

    /**
     * @param newPath
     * @param path
     * @return
     */
    private <P extends Path<?>> P  addMetadataOf(P newPath, Path<?> path) {
        if (path.getMetadata().getParent() instanceof EntityPath) {
            EntityPath<?> parent = (EntityPath)path.getMetadata().getParent();
            propertyMetadata.put(newPath, parent.getMetadata(path));
        }
        return newPath;
    }

    /**
     * Override this method to do some validation of the properties created
     *
     * @param property
     */
    protected void validate(String property) {
        // do nothing
    }

    @Override
    public Object getMetadata(Path<?> property) {
        return propertyMetadata.get(property);
    }

    /**
     * Get a PathBuilder instance for the given property
     *
     * @param property property name
     * @return
     */
    @SuppressWarnings("unchecked")
    public PathBuilder<Object> get(String property) {
        PathBuilder<Object> path = (PathBuilder) properties.get(property);
        if (path == null) {
            validate(property);
            path = new PathBuilder<Object>(Object.class, forProperty(property));
            properties.put(property, path);
        }
        return path;
    }

    /**
     * Get a PathBuilder for the given property with the given type
     *
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    public <A> PathBuilder<A> get(String property, Class<A> type) {
        PathBuilder<A> path = (PathBuilder<A>) properties.get(property);
        if (path == null || !type.isAssignableFrom(path.getType())) {
            validate(property);
            path = new PathBuilder<A>(type, forProperty(property));
            properties.put(property, path);
        }
        return path;
    }

    /**
     * Get a PArray instance for the given property and the given array type
     *
     * @param <A>
     * @param <E>
     * @param property property name
     * @param type
     * @return
     */
    public <A, E> ArrayPath<A, E> getArray(String property, Class<A> type) {
        validate(property);
        return super.createArray(property, type);
    }

    /**
     * @param path
     * @return
     */
    public BooleanPath get(BooleanPath path) {
        BooleanPath newPath = getBoolean(toString(path));
        return addMetadataOf(newPath, path);
    }

    /**
     * Get a new Boolean typed path
     *
     * @param propertyName property name
     * @return
     */
    public BooleanPath getBoolean(String propertyName) {
        validate(propertyName);
        return super.createBoolean(propertyName);
    }

    /**
     * Get a new Collection typed path
     *
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A> CollectionPath<A, PathBuilder<A>> getCollection(String property, Class<A> type) {
        validate(property);
        return super.<A, PathBuilder<A>>createCollection(property, type, PathBuilder.class, PathInits.DIRECT);
    }

    /**
     * Get a new Collection typed path
     *
     * @param <A>
     * @param <E>
     * @param property property name
     * @param type
     * @param queryType
     * @return
     */
    public <A, E extends SimpleExpression<A>> CollectionPath<A, E> getCollection(String property, Class<A> type, Class<E> queryType) {
        validate(property);
        return super.<A, E>createCollection(property, type, queryType, PathInits.DIRECT);
    }

    /**
     * @param <A>
     * @param path
     * @return
     */
    @SuppressWarnings("unchecked")
    public <A extends Comparable<?>> ComparablePath<A> get(ComparablePath<A> path) {
        ComparablePath<A> newPath = getComparable(toString(path), (Class<A>)path.getType());
        return addMetadataOf(newPath, path);
    }

    /**
     * Get a new Comparable typed path
     *
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A extends Comparable<?>> ComparablePath<A> getComparable(String property, Class<A> type) {
        validate(property);
        return super.createComparable(property, type);
    }

    /**
     * @param <A>
     * @param path
     * @return
     */
    @SuppressWarnings("unchecked")
    public <A extends Comparable<?>> DatePath<A> get(DatePath<A> path) {
        DatePath<A> newPath = getDate(toString(path), (Class<A>)path.getType());
        return addMetadataOf(newPath, path);
    }

    /**
     * Get a new Date path
     *
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A extends Comparable<?>> DatePath<A> getDate(String property, Class<A> type) {
        validate(property);
        return super.createDate(property, type);
    }

    /**
     * @param <A>
     * @param path
     * @return
     */
    @SuppressWarnings("unchecked")
    public <A extends Comparable<?>> DateTimePath<A> get(DateTimePath<A> path) {
        DateTimePath<A> newPath = getDateTime(toString(path), (Class<A>)path.getType());
        return addMetadataOf(newPath, path);
    }

    /**
     * Get a new DateTime path
     *
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A extends Comparable<?>> DateTimePath<A> getDateTime(String property, Class<A> type) {
        validate(property);
        return super.createDateTime(property, type);
    }

    /**
     * Get a new Enum path
     *
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A extends Enum<A>> EnumPath<A> getEnum(String property, Class<A> type) {
        validate(property);
        return super.createEnum(property, type);
    }

    /**
     * @param <A>
     * @param path
     * @return
     */
    @SuppressWarnings("unchecked")
    public <A extends Enum<A>> EnumPath<A> get(EnumPath<A> path) {
        EnumPath<A> newPath = getEnum(toString(path), (Class<A>)path.getType());
        return addMetadataOf(newPath, path);
    }

    /**
     * Get a new List typed path
     *
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A> ListPath<A, PathBuilder<A>> getList(String property, Class<A> type) {
        validate(property);
        return super.<A, PathBuilder<A>>createList(property, type, PathBuilder.class, PathInits.DIRECT);
    }

    /**
     * Get a new List typed path
     *
     * @param <A>
     * @param <E>
     * @param property property name
     * @param type
     * @param queryType
     * @return
     */
    public <A, E extends SimpleExpression<A>> ListPath<A, E> getList(String property, Class<A> type, Class<E> queryType) {
        validate(property);
        return super.<A, E>createList(property, type, queryType, PathInits.DIRECT);
    }

    /**
     * Get a new Map typed path
     *
     * @param <K>
     * @param <V>
     * @param property property name
     * @param key
     * @param value
     * @return
     */
    public <K, V> MapPath<K, V, PathBuilder<V>> getMap(String property, Class<K> key, Class<V> value) {
        validate(property);
        return super.<K,V,PathBuilder<V>>createMap(property, key, value, PathBuilder.class);
    }

    /**
     * Get a new Map typed path
     *
     * @param <K>
     * @param <V>
     * @param <E>
     * @param property property name
     * @param key
     * @param value
     * @param queryType
     * @return
     */
    public <K, V, E extends SimpleExpression<V>> MapPath<K, V, E> getMap(String property, Class<K> key, Class<V> value, Class<E> queryType) {
        validate(property);
        return super.<K,V,E>createMap(property, key, value, queryType);
    }

    /**
     * @param <A>
     * @param path
     * @return
     */
    @SuppressWarnings("unchecked")
    public <A extends Number & Comparable<?>> NumberPath<A> get(NumberPath<A> path) {
        NumberPath<A> newPath = getNumber(toString(path), (Class<A>)path.getType());
        return addMetadataOf(newPath, path);
    }

    /**
     * Get a new Number typed path
     *
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A extends Number & Comparable<?>> NumberPath<A> getNumber(String property, Class<A> type) {
        validate(property);
        return super.createNumber(property, type);
    }

    /**
     * Get a new Set typed path
     *
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A> SetPath<A, PathBuilder<A>> getSet(String property, Class<A> type) {
        validate(property);
        return super.<A, PathBuilder<A>>createSet(property, type, PathBuilder.class, PathInits.DIRECT);
    }

    /**
     * Get a new Set typed path
     *
     * @param <A>
     * @param <E>
     * @param property property name
     * @param type
     * @param queryType
     * @return
     */
    public <A, E extends SimpleExpression<A>> SetPath<A, E> getSet(String property, Class<A> type, Class<E> queryType) {
        validate(property);
        return super.<A, E>createSet(property, type, queryType, PathInits.DIRECT);
    }

    /**
     * @param <A>
     * @param path
     * @return
     */
    @SuppressWarnings("unchecked")
    public <A> SimplePath<A> get(Path<A> path) {
        SimplePath<A> newPath = getSimple(toString(path), (Class<A>)path.getType());
        return addMetadataOf(newPath, path);
    }

    /**
     * Get a new Simple path
     *
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A> SimplePath<A> getSimple(String property, Class<A> type) {
        validate(property);
        return super.createSimple(property, type);
    }

    /**
     * @param path
     * @return
     */
    public StringPath get(StringPath path) {
        StringPath newPath = getString(toString(path));
        return addMetadataOf(newPath, path);
    }

    /**
     * Get a new String typed path
     *
     * @param property property name
     * @return
     */
    public StringPath getString(String property) {
        validate(property);
        return super.createString(property);
    }

    /**
     * @param <A>
     * @param path
     */
    @SuppressWarnings("unchecked")
    public <A extends Comparable<?>> TimePath<A> get(TimePath<A> path) {
        TimePath<A> newPath = getTime(toString(path), (Class<A>)path.getType());
        return addMetadataOf(newPath, path);
    }

    /**
     * Get a new Time typed path
     *
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A extends Comparable<?>> TimePath<A> getTime(String property, Class<A> type) {
        validate(property);
        return super.createTime(property, type);
    }

    /**
     * @param path
     * @return
     */
    private String toString(Path<?> path) {
        return path.getMetadata().getElement().toString();
    }

}
