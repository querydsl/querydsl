package com.mysema.query.dynamodb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.NonUniqueResultException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.SimpleProjectable;
import com.mysema.query.SimpleQuery;
import com.mysema.query.dynamodb.impl.DynamodbSerializer;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;

/**
 * DynamoDBQuery is the implementation of the {@link SimpleQuery} for DynamoDB
 *
 * @author velo
 */
public class DynamoDBQuery<Q> implements SimpleQuery<DynamoDBQuery<Q>>, SimpleProjectable<Q> {

    private AmazonDynamoDB client;
    private DynamoDBMapper mapper;
    private final DynamodbSerializer serializer;
    private final QueryMixin<DynamoDBQuery<Q>> queryMixin;
    private EntityPath<Q> entityPath;

    public DynamoDBQuery(AmazonDynamoDB client, EntityPath<Q> entityPath) {
        this.queryMixin = new QueryMixin<DynamoDBQuery<Q>>(this,
                new DefaultQueryMetadata().noValidate());
        this.client = client;
        this.mapper = new DynamoDBMapper(this.client);
        this.serializer = DynamodbSerializer.DEFAULT;
        this.entityPath = entityPath;
    }

    @Override
    public DynamoDBQuery<Q> where(Predicate... e) {
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

    public List<Q> list(Path<?>... paths) {
        queryMixin.addProjection(paths);
        return list();
    }

    @Override
    public List<Q> list() {
        PaginatedScanList<? extends Q> result = query();
        return cast(result);
    }

    private PaginatedScanList<? extends Q> query() {
        DynamoDBScanExpression query = createQuery(queryMixin.getMetadata());
        PaginatedScanList<? extends Q> result = mapper.scan(entityPath.getType(), query);
        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<Q> cast(PaginatedScanList result) {
        return new ArrayList<Q>(result);
    }

    private DynamoDBScanExpression createQuery(@Nullable QueryMetadata queryMetadata) {
        if (queryMetadata.getWhere() != null) {
            return serializer.handle(queryMetadata.getWhere());
        } else {
            return new DynamoDBScanExpression();
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
        DynamoDBScanExpression query = createQuery(queryMixin.getMetadata());
        return mapper.count(entityPath.getType(), query);
    }

    @Override
    public DynamoDBQuery<Q> limit(long limit) {
        return queryMixin.limit(limit);
    }

    @Override
    public DynamoDBQuery<Q> offset(long offset) {
        return queryMixin.offset(offset);
    }

    @Override
    public DynamoDBQuery<Q> restrict(QueryModifiers modifiers) {
        return queryMixin.restrict(modifiers);
    }

    @Override
    public DynamoDBQuery<Q> orderBy(OrderSpecifier<?>... o) {
        return queryMixin.orderBy(o);
    }

    @Override
    public <T> DynamoDBQuery<Q> set(ParamExpression<T> param, T value) {
        return queryMixin.set(param, value);
    }

    @Override
    public DynamoDBQuery<Q> distinct() {
        return queryMixin.distinct();
    }

}
