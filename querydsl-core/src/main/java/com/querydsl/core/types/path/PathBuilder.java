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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.expr.SimpleExpression;

/**
 * PathBuilder is an extension to EntityPathBase for dynamic path construction
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * PathBuilder<User> user = new PathBuilder<User>(User.class, "user");
 * Predicate filter = user.getString("firstName").eq("Bob");
 * List<User> users = querydsl.from(user).where(filter).list(user);
 * }</pre>
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public class PathBuilder<T> extends EntityPathBase<T> {

    private static final long serialVersionUID = -1666357914232685088L;

    private final Map<String, PathBuilder<?>> properties = Maps.newHashMap();

    private final Map<Path<?>, Object> propertyMetadata = Maps.newHashMap();

    private final PathBuilderValidator validator;

    /**
     * Creates a new PathBuilder instance
     *
     * @param type
     * @param pathMetadata
     * @param validator
     */
    public PathBuilder(Class<? extends T> type, PathMetadata<?> pathMetadata, PathBuilderValidator validator) {
        super(type, pathMetadata);
        this.validator = validator;
    }

    /**
     * Creates a new PathBuilder instance
     *
     * @param type
     * @param pathMetadata
     */
    public PathBuilder(Class<? extends T> type, PathMetadata<?> pathMetadata) {
        this(type, pathMetadata, PathBuilderValidator.DEFAULT);
    }


    /**
     * Creates a new PathBuilder instance
     *
     * @param type
     * @param variable
     * @param validator
     */
    public PathBuilder(Class<? extends T> type, String variable, PathBuilderValidator validator) {
        this(type, PathMetadataFactory.forVariable(variable), validator);
    }

    /**
     * Creates a new PathBuilder instance
     *
     * @param type
     * @param variable
     */
    public PathBuilder(Class<? extends T> type, String variable) {
        this(type, PathMetadataFactory.forVariable(variable), PathBuilderValidator.DEFAULT);
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

    protected <T> Class<? extends T> validate(String property, Class<T> propertyType) {
        Class<T> validatedType = (Class)validator.validate(getType(), property, propertyType);
        if (validatedType != null) {
            return validatedType;
        } else {
            throw new IllegalArgumentException("Illegal property " + property);
        }
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
            Class<?> vtype = validate(property, Object.class);
            path = new PathBuilder<Object>(vtype, forProperty(property), validator);
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
            Class<? extends A> vtype = validate(property, type);
            path = new PathBuilder<A>(vtype, forProperty(property), validator);
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
        validate(property, Array.newInstance(type, 0).getClass());
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
        validate(propertyName, Boolean.class);
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
        validate(property, Collection.class);
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
        validate(property, Collection.class);
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
        Class<? extends A> vtype = validate(property, type);
        return super.createComparable(property, (Class)vtype);
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
        Class<? extends A> vtype = validate(property, type);
        return super.createDate(property, (Class)vtype);
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
        Class<? extends A> vtype = validate(property, type);
        return super.createDateTime(property, (Class)vtype);
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
        validate(property, type);
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
        validate(property, List.class);
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
        validate(property, List.class);
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
        validate(property, Map.class);
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
        validate(property, Map.class);
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
        Class<? extends A> vtype = validate(property, type);
        return super.createNumber(property, (Class)vtype);
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
        validate(property, Set.class);
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
        validate(property, Set.class);
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
        Class<? extends A> vtype = validate(property, type);
        return super.createSimple(property, (Class)vtype);
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
        validate(property, String.class);
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
        Class<? extends A> vtype = validate(property, type);
        return super.createTime(property, (Class)vtype);
    }

    /**
     * @param path
     * @return
     */
    private String toString(Path<?> path) {
        return path.getMetadata().getElement().toString();
    }

}
