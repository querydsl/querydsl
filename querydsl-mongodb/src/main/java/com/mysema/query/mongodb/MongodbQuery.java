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
package com.mysema.query.mongodb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.collections15.Transformer;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinExpression;
import com.mysema.query.NonUniqueResultException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.SimpleProjectable;
import com.mysema.query.SimpleQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Operation;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;
import com.mysema.query.types.Predicate;

/**
 * MongodbQuery provides a general Querydsl query implementation with a pluugable DBObject to Bean transformation
 *
 * @author laimw
 *
 * @param <K>
 */
public abstract class MongodbQuery<K> implements SimpleQuery<MongodbQuery<K>>, SimpleProjectable<K> {
    
    @SuppressWarnings("serial")
    private static class NoResults extends RuntimeException {}
    
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
    
    public <T> JoinBuilder<K,T> join(Path<T> ref, Path<T> target) {        
        return new JoinBuilder<K,T>(queryMixin, ref, target);
    }
    
    public <T> AnyEmbeddedBuilder<K> anyEmbedded(Path<? extends Collection<T>> collection, Path<T> target) {
        return new AnyEmbeddedBuilder<K>(queryMixin, collection);
    }
    
    protected abstract DBCollection getCollection(Class<?> type);
    
    @Override
    public boolean exists() {        
        try {
            QueryMetadata metadata = queryMixin.getMetadata();
            Predicate filter = createFilter(metadata);
            return collection.findOne(createQuery(filter)) != null;
        } catch (NoResults ex) {
            return false;
        }        
    }
    
    @Nullable
    protected Predicate createFilter(QueryMetadata metadata) {        
        Predicate filter;
        if (!metadata.getJoins().isEmpty()) {
            filter = ExpressionUtils.allOf(metadata.getWhere(), createJoinFilter(metadata));
        } else {
            filter = metadata.getWhere();
        }   
        return filter;
    }
    
    @Nullable
    protected Predicate createJoinFilter(QueryMetadata metadata) {
        Predicate extraFilter = null;
        List<JoinExpression> joins = metadata.getJoins();
        for (int i = joins.size() - 1; i >= 0; i--) {
            JoinExpression join = joins.get(i);
            Expression<?> source = ((Operation<?>)join.getTarget()).getArg(0);
            Class<?> target = ((Operation<?>)join.getTarget()).getArg(1).getType();
            Predicate filter = ExpressionUtils.allOf(join.getCondition(), extraFilter);
            List<Object> ids = getIds(target, filter);
            if (ids.isEmpty()) {
                throw new NoResults();
            }
            Path path = new PathImpl<String>(String.class, (Path)source, "$id");
            extraFilter = ExpressionUtils.in(path, ids);
        }
        return extraFilter;
    }
    
    protected List<Object> getIds(Class<?> target, Predicate condition) {
        DBCollection collection = getCollection(target);
        // TODO : fetch only ids
        DBCursor cursor = createCursor(collection, condition, QueryModifiers.EMPTY, Collections.<OrderSpecifier<?>>emptyList());
        if (cursor.hasNext()) {
            List<Object> ids = new ArrayList<Object>(cursor.count());
            for (DBObject obj : cursor) {
                ids.add(obj.containsField("id") ? obj.get("id") : obj.get("_id"));
            }
            return ids;
        } else {
            return Collections.emptyList();
        }
    }
    
    @Override
    public boolean notExists() {
        return !exists();
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
        try {
            DBCursor cursor = createCursor();
            List<K> results = new ArrayList<K>(cursor.size());
            for (DBObject dbObject : cursor) {
                results.add(transformer.transform(dbObject));
            }
            return results;    
        } catch (NoResults ex) {
            return Collections.emptyList();
        }        
    }
    
    protected DBCursor createCursor() {
        QueryMetadata metadata = queryMixin.getMetadata();
        Predicate filter = createFilter(metadata);
        return createCursor(collection, filter, metadata.getModifiers(), metadata.getOrderBy());     
    }

    protected DBCursor createCursor(DBCollection collection, @Nullable Predicate where, QueryModifiers modifiers,
            List<OrderSpecifier<?>> orderBy) {
        DBCursor cursor = collection.find(createQuery(where));
        if (modifiers.getLimit() != null){
            cursor.limit(modifiers.getLimit().intValue());
        }
        if (modifiers.getOffset() != null){
            cursor.skip(modifiers.getOffset().intValue());
        }
        if (orderBy.size() > 0) {
            cursor.sort(serializer.toSort(orderBy));
        }
        return cursor;
    }

    @Override
    public List<K> listDistinct() {
        return list();
    }

    @Override
    public K singleResult() {
        try {
            DBCursor c = createCursor().limit(1);
            if (c.hasNext()){
                return transformer.transform(c.next());
            } else {
                return null;
            }    
        } catch (NoResults ex) {
            return null;
        }        
    }

    @Override
    public K uniqueResult() {
        try {
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
            } else {
                return null;
            }    
        } catch (NoResults ex) {
            return null;
        }        
    }

    @Override
    public SearchResults<K> listResults() {
        try {
            long total = count();
            if (total > 0l){
                return new SearchResults<K>(list(), queryMixin.getMetadata().getModifiers(), total);
            } else {
                return SearchResults.emptyResults();
            }    
        } catch (NoResults ex) {
            return SearchResults.emptyResults();
        }        
    }

    @Override
    public SearchResults<K> listDistinctResults() {
        return listResults();
    }

    @Override
    public long count() {
        try {
            Predicate filter = createFilter(queryMixin.getMetadata());            
            return collection.count(createQuery(filter));    
        } catch (NoResults ex) {
            return 0l;
        }        
    }

    @Override
    public long countDistinct() {
        return count();
    }

    private DBObject createQuery(@Nullable Predicate predicate) {
        if (predicate != null){
            return (DBObject) serializer.handle(predicate);
        } else {
            return new BasicDBObject();
        }
    }

    @Override
    public String toString() {
        return createQuery(queryMixin.getMetadata().getWhere()).toString();
    }

}