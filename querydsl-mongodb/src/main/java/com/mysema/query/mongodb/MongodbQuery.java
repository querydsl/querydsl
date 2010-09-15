package com.mysema.query.mongodb;

import java.util.List;

import com.google.code.morphia.Datastore;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mysema.commons.lang.Assert;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.SimpleProjectable;
import com.mysema.query.SimpleQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Param;
import com.mysema.query.types.expr.EBoolean;

/**
 * MongoDb query
 * 
 * @author laimw
 * 
 * @param <K>
 */
public class MongodbQuery<K> implements SimpleQuery<MongodbQuery<K>>,
        SimpleProjectable<K> {

    private final QueryMixin<MongodbQuery<K>> queryMixin;
    private final EntityPath<K> ePath;
    private final Datastore ds;
    private final DBCollection coll;
    private final MongodbSerializer serializer = new MongodbSerializer();

    public MongodbQuery(Datastore datastore, EntityPath<K> entityPath) {
        queryMixin = new QueryMixin<MongodbQuery<K>>(this);
        ePath = entityPath;
        ds = datastore;
        coll = ds.getCollection(ePath.getType());
    }

    @Override
    public MongodbQuery<K> where(EBoolean... e) {
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> MongodbQuery<K> set(Param<T> param, T value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<K> list() {
        // TODO Auto-generated method stub
        return null;
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
        // return createMorphiaQuery().countAll();
        return coll.count(createQuery());
    }

    @Override
    public long countDistinct() {
        // TODO Auto-generated method stub
        return 0;
    }

    // private Query<? extends K> createMorphiaQuery() {
    // QueryMetadata metadata = queryMixin.getMetadata();
    // Assert.notNull(metadata.getWhere(), "where needs to be set");
    //          
    //            
    // return ds.createQuery(ePath.getType()).filter("firstName", "Juuso");
    // //where(" firstName : \"Juuso\" ");
    // }

    private DBObject createQuery() {
        QueryMetadata metadata = queryMixin.getMetadata();
        Assert.notNull(metadata.getWhere(), "where needs to be set");

        return (DBObject) serializer.handle(metadata.getWhere());

        // org.apache.lucene.search.Query query =
        // serializer.toQuery(metadata.getWhere(), metadata);
        //
        // FullTextQuery fullTextQuery = session.createFullTextQuery(query,
        // path.getType());
        //
        // // order
        // if (!metadata.getOrderBy().isEmpty() && !forCount){
        // fullTextQuery.setSort(serializer.toSort(metadata.getOrderBy()));
        // }
        //
        // // paging
        // QueryModifiers modifiers = metadata.getModifiers();
        // if (modifiers != null && modifiers.isRestricting() && !forCount){
        // if (modifiers.getLimit() != null){
        // fullTextQuery.setMaxResults(modifiers.getLimit().intValue());
        // }
        // if (modifiers.getOffset() != null){
        // fullTextQuery.setFirstResult(modifiers.getOffset().intValue());
        // }
        // }
        // return fullTextQuery;
    }

}