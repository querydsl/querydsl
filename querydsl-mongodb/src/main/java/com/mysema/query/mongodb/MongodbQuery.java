package com.mysema.query.mongodb;

import java.util.ArrayList;
import java.util.List;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.mapping.cache.DefaultEntityCache;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mysema.commons.lang.Assert;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.SimpleProjectable;
import com.mysema.query.SimpleQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.Predicate;

/**
 * MongoDb query
 * 
 * @author laimw
 *
 * @param <K>
 */
public class MongodbQuery<K> implements SimpleQuery<MongodbQuery<K>>, SimpleProjectable<K> {
        
        private final QueryMixin<MongodbQuery<K>> queryMixin;
        private final EntityPath<K> ePath;
        private final Morphia morphia;
        private final Datastore ds;
        private final DBCollection coll;
        private final MongodbSerializer serializer = new MongodbSerializer();
        private final DefaultEntityCache cache = new DefaultEntityCache();

        public MongodbQuery(Morphia morphiaParam, Datastore datastore, EntityPath<K> entityPath) {
            queryMixin = new QueryMixin<MongodbQuery<K>>(this);
            ePath = entityPath;
            ds = datastore;
            morphia = morphiaParam;
            coll = ds.getCollection(ePath.getType());
        }
        
        @Override
        public MongodbQuery<K> where(Predicate... e) {
           return queryMixin.where(e);
        }

        @Override
        public MongodbQuery<K> limit(long limit) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public MongodbQuery<K> offset(long offset) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public MongodbQuery<K> restrict(QueryModifiers modifiers) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public MongodbQuery<K> orderBy(OrderSpecifier<?>... o) {
            return queryMixin.orderBy(o);
        }

        @Override
        public <T> MongodbQuery<K> set(ParamExpression<T> param, T value) {
            // TODO Auto-generated method stub
            return null;
        }

        public CloseableIterator<K> iterate(){
            // TODO : optimize
            return new IteratorAdapter<K>(list().iterator());
        }
        
        @Override
        public List<K> list() {
            QueryMetadata metadata = queryMixin.getMetadata();
            //Long queryLimit = metadata.getModifiers().getLimit();
            //Long queryOffset = metadata.getModifiers().getOffset();
            DBCursor cursor = coll.find(createQuery());
            if(metadata.getOrderBy().size() > 0) {
                cursor.sort(serializer.toSort(metadata.getOrderBy()));
            }
            List<K> results = new ArrayList<K>(cursor.size());
            for (DBObject dbObject : cursor) {
                results.add(morphia.fromDBObject(ePath.getType(), dbObject, cache ));
            }
            
            return results;
            
//            int limit;
//            int offset = queryOffset != null ? queryOffset.intValue() : 0;
//            try {
//                limit = searcher.maxDoc();
//            } catch (IOException e) {
//                throw new QueryException(e);
//            }
//            if (queryLimit != null && queryLimit.intValue() < limit) {
//                limit = queryLimit.intValue();
//            }
//            if (!orderBys.isEmpty()) {
//                return listSorted(orderBys, limit, offset);
//            }
//
//            List<Document> documents = null;
//            try {
//                ScoreDoc[] scoreDocs = searcher.search(createQuery(), limit + offset).scoreDocs;
//                documents = new ArrayList<Document>(scoreDocs.length - offset);
//                for (int i = offset; i < scoreDocs.length; ++i) {
//                    documents.add(searcher.doc(scoreDocs[i].doc));
//                }
//            } catch (IOException e) {
//                throw new QueryException(e);
//            }
//            return documents;
        }

        @Override
        public List<K> listDistinct() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public K uniqueResult() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public SearchResults<K> listResults() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public SearchResults<K> listDistinctResults() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long count() {
            return coll.count(createQuery());
        }

        @Override
        public long countDistinct() {
            // TODO Auto-generated method stub
            return 0;
        }
        
        
        private DBObject createQuery(){
            QueryMetadata metadata = queryMixin.getMetadata();
            Assert.notNull(metadata.getWhere(), "where needs to be set");
            
            return (DBObject) serializer.handle(metadata.getWhere());
            
        // org.apache.lucene.search.Query query =
        // serializer.toQuery(metadata.getWhere(), metadata);
//
        // FullTextQuery fullTextQuery = session.createFullTextQuery(query,
        // path.getType());
//
//            // order
//            if (!metadata.getOrderBy().isEmpty() && !forCount){
//                fullTextQuery.setSort(serializer.toSort(metadata.getOrderBy()));
//            }
//
//            // paging
//            QueryModifiers modifiers = metadata.getModifiers();
//            if (modifiers != null && modifiers.isRestricting() && !forCount){
//                if (modifiers.getLimit() != null){
//                    fullTextQuery.setMaxResults(modifiers.getLimit().intValue());
//                }
//                if (modifiers.getOffset() != null){
//                    fullTextQuery.setFirstResult(modifiers.getOffset().intValue());
//                }
//            }
//            return fullTextQuery;
        }
        
    }