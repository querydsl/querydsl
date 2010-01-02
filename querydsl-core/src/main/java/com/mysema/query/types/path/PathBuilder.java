package com.mysema.query.types.path;

import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings("serial")
public class PathBuilder<D> extends PEntity<D>{

    /**
     * @param type
     * @param pathMetadata
     */
    public PathBuilder(Class<? extends D> type, PathMetadata<?> pathMetadata) {
        super(type, pathMetadata);
    }
    
    /**
     * @param type
     * @param variable
     */
    public PathBuilder(Class<? extends D> type, String variable) {
        super(type, PathMetadataFactory.forVariable(variable));
    }
    
    public PathBuilder<Object> get(String property) {
        return get(property, Object.class);
    }

    public <A> PathBuilder<A> get(String property, Class<A> type) {
        return new PathBuilder<A>(type, forProperty(property));
    }

    public <A> PArray<A> getArray(String property, Class<A> type) {
        return super.createArray(property, type);
    }

    public PBoolean getBoolean(String propertyName) {
        return super.createBoolean(propertyName);
    }

    public <A> PCollection<A> getCollection(String property, Class<A> type) {
        return super.createCollection(property, type);
    }

    public <A extends Comparable<?>> PComparable<A> getComparable(String property, Class<A> type) {
        return super.createComparable(property, type);
    }

    public <A extends Comparable<?>> PDate<A> getDate(String property, Class<A> type) {
        return super.createDate(property, type);
    }
    
    public <A extends Comparable<?>> PDateTime<A> getDateTime(String property, Class<A> type) {
        return super.createDateTime(property, type);
    }

    public <A> PList<A, PathBuilder<A>> getList(String property, Class<A> type) {
        return super.createList(property, type, PathBuilder.class);
    }
    
    public <A, E extends Expr<A>> PList<A, E> getList(String property, Class<A> type, Class<E> queryType) {
        return super.createList(property, type, queryType);
    }

    public <K, V> PMap<K, V, PathBuilder<V>> getMap(String property, Class<K> key, Class<V> value) {
        return super.<K,V,PathBuilder<V>>createMap(property, key, value, PathBuilder.class);
    }

    public <K, V, E extends Expr<V>> PMap<K, V, E> getMap(String property, Class<K> key, Class<V> value, Class<E> queryType) {
        return super.<K,V,E>createMap(property, key, value, queryType);
    }
    
    public <A extends Number & Comparable<?>> PNumber<A> getNumber(String property, Class<A> type) {
        return super.createNumber(property, type);
    }

    public <A> PSet<A> getSet(String property, Class<A> type) {
        return super.createSet(property, type);
    }
    
    public <A> PSimple<A> getSimple(String property, Class<A> type) {
        return super.createSimple(property, type);
    }

    public PString getString(String property) {
        return super.createString(property);
    }

    public <A extends Comparable<?>> PTime<A> getTime(String property, Class<A> type) {
        return super.createTime(property, type);
    }

}
