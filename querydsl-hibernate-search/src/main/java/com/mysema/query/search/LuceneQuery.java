package com.mysema.query.search;

import java.util.List;

import org.hibernate.Session;

import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.SimpleProjectable;
import com.mysema.query.SimpleQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.Path;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class LuceneQuery<T> implements SimpleQuery<LuceneQuery<T>>, SimpleProjectable<T>{
    
    private final Session session;
    
    private final Path<?> entityPath;
    
    private final QueryMixin<LuceneQuery<T>> queryMixin;
    
    public LuceneQuery(Session session, Path<?> entityPath) {
        this.session = session;
        this.entityPath = entityPath;
        this.queryMixin = new QueryMixin<LuceneQuery<T>>(this);
    }

    @Override
    public LuceneQuery<T> limit(long limit) {
        return queryMixin.limit(limit);
    }

    @Override
    public LuceneQuery<T> offset(long offset) {
        return queryMixin.offset(offset);
    }

    @Override
    public LuceneQuery<T> restrict(QueryModifiers modifiers) {
        return queryMixin.restrict(modifiers);
    }

    @Override
    public LuceneQuery<T> where(EBoolean... e) {
        return queryMixin.where(e);
    }

    @Override
    public long count() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long countDistinct() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<T> list() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<T> listDistinct() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SearchResults<T> listDistinctResults() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SearchResults<T> listResults() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T uniqueResult() {
        // TODO Auto-generated method stub
        return null;
    }

}
