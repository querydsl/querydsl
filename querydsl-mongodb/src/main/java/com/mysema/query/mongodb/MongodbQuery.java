/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.NonUniqueResultException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.SimpleProjectable;
import com.mysema.query.SimpleQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.Predicate;

/**
 * MongodbQuery provides a general Querydsl query implementation with a pluugable DBObject to Bean transformation
 *
 * @author laimw
 *
 * @param <K>
 */
public class MongodbQuery<K> implements SimpleQuery<MongodbQuery<K>>, SimpleProjectable<K> {

    private final MongodbSerializer serializer;

    private final QueryMixin<MongodbQuery<K>> queryMixin;

    private final DBCollection collection;

    private final Transformer<DBObject, K> transformer;

    public MongodbQuery(DBCollection collection, Transformer<DBObject, K> transformer, MongodbSerializer serializer) {
        this.queryMixin = new QueryMixin<MongodbQuery<K>>(this, new DefaultQueryMetadata(false));
        this.transformer = transformer;
        this.collection = collection;
        this.serializer = serializer;
    }

    @Override
    public boolean exists() {
        return collection.findOne(createQuery()) != null;
    }

    @Override
    public boolean notExists() {
        return collection.findOne(createQuery()) == null;
    }

    @Override
    public MongodbQuery<K> distinct(){
        return queryMixin.distinct();
    }

    @Override
    public MongodbQuery<K> where(Predicate... e) {
        return queryMixin.where(e);
    }

    @Override
    public MongodbQuery<K> limit(long limit) {
        return queryMixin.limit(limit);
    }

    @Override
    public MongodbQuery<K> offset(long offset) {
        return queryMixin.offset(offset);
    }

    @Override
    public MongodbQuery<K> restrict(QueryModifiers modifiers) {
        return queryMixin.restrict(modifiers);
    }

    @Override
    public MongodbQuery<K> orderBy(OrderSpecifier<?>... o) {
        return queryMixin.orderBy(o);
    }

    @Override
    public <T> MongodbQuery<K> set(ParamExpression<T> param, T value) {
        return queryMixin.set(param, value);
    }

    @Override
    public CloseableIterator<K> iterate() {
        final DBCursor cursor = createCursor();
        return new CloseableIterator<K>() {
            @Override
            public boolean hasNext() {
                return cursor.hasNext();
            }

            @Override
            public K next() {
                return transformer.transform(cursor.next());
            }

            @Override
            public void remove() {
            }

            @Override
            public void close() {
            }
        };
    }

    @Override
    public CloseableIterator<K> iterateDistinct() {
        return iterate();
    }

    @Override
    public List<K> list() {
        DBCursor cursor = createCursor();
        List<K> results = new ArrayList<K>(cursor.size());
        for (DBObject dbObject : cursor) {
            results.add(transformer.transform(dbObject));
        }
        return results;
    }

    protected DBCursor createCursor() {
        QueryMetadata metadata = queryMixin.getMetadata();
        QueryModifiers modifiers = metadata.getModifiers();

        DBCursor cursor = collection.find(createQuery());
        if (modifiers.getLimit() != null){
            cursor.limit(modifiers.getLimit().intValue());
        }
        if (modifiers.getOffset() != null){
            cursor.skip(modifiers.getOffset().intValue());
        }
        if (metadata.getOrderBy().size() > 0) {
            cursor.sort(serializer.toSort(metadata.getOrderBy()));
        }
        return cursor;
    }

    @Override
    public List<K> listDistinct() {
        return list();
    }

    @Override
    public K singleResult() {
        DBCursor c = createCursor().limit(1);
        if (c.hasNext()){
            return transformer.transform(c.next());
        }else{
            return null;
        }
    }

    @Override
    public K uniqueResult() {
        Long limit = queryMixin.getMetadata().getModifiers().getLimit();
        if (limit == null){
            limit = 2l;
        }
        DBCursor c = createCursor().limit(limit.intValue());
        if (c.hasNext()){
            K rv = transformer.transform(c.next());
            if (c.hasNext()){
                throw new NonUniqueResultException();
            }
            return rv;
        }else{
            return null;
        }
    }

    @Override
    public SearchResults<K> listResults() {
        long total = count();
        if (total > 0l){
            return new SearchResults<K>(list(), queryMixin.getMetadata().getModifiers(), total);
        }else{
            return SearchResults.emptyResults();
        }
    }

    @Override
    public SearchResults<K> listDistinctResults() {
        return listResults();
    }

    @Override
    public long count() {
        return collection.count(createQuery());
    }

    @Override
    public long countDistinct() {
        return count();
    }

    private DBObject createQuery() {
        QueryMetadata metadata = queryMixin.getMetadata();
        if (metadata.getWhere() != null){
            return (DBObject) serializer.handle(metadata.getWhere());
        }else{
            return new BasicDBObject();
        }

    }

    @Override
    public String toString() {
        return createQuery().toString();
    }

}