package com.mysema.query.hazelcast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.hazelcast.query.Predicate;
import com.hazelcast.query.PredicateBuilder;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.NonUniqueResultException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.SimpleProjectable;
import com.mysema.query.SimpleQuery;
import com.mysema.query.hazelcast.impl.HazelcastSerializer;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.Path;

public abstract class AbstractIMapQuery<Q> implements SimpleQuery<AbstractIMapQuery<Q>>, SimpleProjectable<Q>{

    protected final HazelcastSerializer serializer;

    protected final QueryMixin<AbstractIMapQuery<Q>> queryMixin;

    public AbstractIMapQuery() {
        super();
        this.queryMixin = new QueryMixin<AbstractIMapQuery<Q>>(this,
                new DefaultQueryMetadata().noValidate());
        this.serializer = HazelcastSerializer.DEFAULT;
    }

    @Override
    public AbstractIMapQuery<Q> where(com.mysema.query.types.Predicate... e) {
        return queryMixin.where(e);
    }

    @Override
    public boolean exists() {
        return iterate().hasNext();
    }

    @Override
    public boolean notExists() {
        return !exists();
    }

    @Override
    public CloseableIterator<Q> iterate() {
        final Iterator<? extends Q> iterator = query().iterator();
        return new CloseableIterator<Q>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
    
            @Override
            public Q next() {
                return iterator.next();
            }
    
            @Override
            public void remove() {
                iterator.remove();
            }
    
            @Override
            public void close() {
            }
        };
    }

    private Collection<Q> query() {
        Predicate<?, Q> query = createQuery(queryMixin.getMetadata());
        return query(query);
    }

    protected abstract Collection<Q> query(Predicate<?, Q> query) ;

    public List<Q> list(Path<?>... paths) {
        queryMixin.addProjection(paths);
        return list();
    }

    @Override
    public List<Q> list() {
        return new ArrayList<Q>(query());
    }

    @SuppressWarnings("unchecked")
    protected Predicate<?, Q> createQuery(@Nullable QueryMetadata queryMetadata) {
        if (queryMetadata.getWhere() != null) {
            return serializer.handle(queryMetadata.getWhere());
        } else {
            return new PredicateBuilder();
        }
    }

    @Override
    public Q singleResult() {
        return limit(1).uniqueResult();
    }

    @Override
    public Q uniqueResult() {
        List<Q> result = list();
        if (result.size() == 0) {
            return null;
        }
    
        if (result.size() != 1) {
            throw new NonUniqueResultException();
        }
    
        return result.get(0);
    }

    @Override
    public SearchResults<Q> listResults() {
        long total = count();
        if (total > 0l) {
            return new SearchResults<Q>(list(), queryMixin.getMetadata().getModifiers(), total);
        } else {
            return SearchResults.emptyResults();
        }
    }

    @Override
    public long count() {
        return query().size();
    }

    @Override
    public AbstractIMapQuery<Q> limit(long limit) {
        return queryMixin.limit(limit);
    }

    @Override
    public AbstractIMapQuery<Q> offset(long offset) {
        return queryMixin.offset(offset);
    }

    @Override
    public AbstractIMapQuery<Q> restrict(QueryModifiers modifiers) {
        return queryMixin.restrict(modifiers);
    }

    @Override
    public AbstractIMapQuery<Q> orderBy(OrderSpecifier<?>... o) {
        return queryMixin.orderBy(o);
    }

    @Override
    public <T> AbstractIMapQuery<Q> set(ParamExpression<T> param, T value) {
        return queryMixin.set(param, value);
    }

    @Override
    public AbstractIMapQuery<Q> distinct() {
        return queryMixin.distinct();
    }

}