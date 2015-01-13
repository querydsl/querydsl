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
package com.querydsl.jdo.sql;

import javax.annotation.Nullable;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.querydsl.core.*;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.ProjectableSQLQuery;
import com.querydsl.sql.SQLSerializer;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for JDO based SQLQuery implementations
 *
 * @author tiwe
 *
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractSQLQuery<T extends AbstractSQLQuery<T>> extends ProjectableSQLQuery<T> {


    private static final Logger logger = LoggerFactory.getLogger(JDOSQLQuery.class);

    private final Closeable closeable = new Closeable() {
        @Override
        public void close() throws IOException {
            AbstractSQLQuery.this.close();
        }
    };

    protected final boolean detach;

    private List<Object> orderedConstants = new ArrayList<Object>();

    @Nullable
    protected final PersistenceManager persistenceManager;

    protected List<Query> queries = new ArrayList<Query>(2);

    @Nullable
    protected FactoryExpression<?> projection;

    protected final QueryMixin<T> queryMixin;

    @SuppressWarnings("unchecked")
    public AbstractSQLQuery(QueryMetadata metadata, Configuration conf, PersistenceManager persistenceManager,
                            boolean detach) {
        super(new QueryMixin<T>(metadata, false), conf);
        this.queryMixin = super.queryMixin;
        this.queryMixin.setSelf((T)this);
        this.persistenceManager = persistenceManager;
        this.detach = detach;
    }

    public void close() {
        for (Query query : queries) {
            query.closeAll();
        }
    }

    @Override
    public long count() {
        Query query = createQuery(true);
        query.setUnique(true);
        reset();
        Long rv = (Long) execute(query, true);
        if (rv != null) {
            return rv.longValue();
        } else {
            throw new QueryException("Query returned null");
        }
    }

    private Query createQuery(boolean forCount) {
        SQLSerializer serializer = new SQLSerializer(configuration);
        if (union != null) {
            serializer.serializeUnion(union, queryMixin.getMetadata(), unionAll);
        } else {
            serializer.serialize(queryMixin.getMetadata(), forCount);
        }


        // create Query
        if (logger.isDebugEnabled()) {
            logger.debug(serializer.toString());
        }
        Query query = persistenceManager.newQuery("javax.jdo.querydsl.SQL",serializer.toString());
        orderedConstants = serializer.getConstants();
        queries.add(query);

        if (!forCount) {
            List<? extends Expression<?>> projection = queryMixin.getMetadata().getProjection();
            if (projection.get(0) instanceof FactoryExpression) {
                this.projection = (FactoryExpression<?>)projection.get(0);
            }
        } else {
            query.setResultClass(Long.class);
        }

        return query;
    }

    @SuppressWarnings("unchecked")
    private <T> T detach(T results) {
        if (results instanceof Collection) {
            return (T) persistenceManager.detachCopyAll(results);
        } else {
            return persistenceManager.detachCopy(results);
        }
    }

    private Object project(FactoryExpression<?> expr, Object row) {
        if (row == null) {
            return null;
        } else if (row.getClass().isArray()) {
            return expr.newInstance((Object[])row);
        } else {
            return expr.newInstance(new Object[]{row});
        }
    }

    private Object execute(Query query, boolean forCount) {
        Object rv;
        if (!orderedConstants.isEmpty()) {
            rv = query.executeWithArray(orderedConstants.toArray());
        } else {
            rv = query.execute();
        }
        if (isDetach()) {
            rv = detach(rv);
        }
        if (projection != null && !forCount) {
            if (rv instanceof List) {
                List<?> original = (List<?>)rv;
                rv = Lists.newArrayList();
                for (Object o : original) {
                    ((List)rv).add(project(projection, o));
                }
            } else {
                rv = project(projection, rv);
            }
        }
        return rv;
    }

    @Override
    public QueryMetadata getMetadata() {
        return queryMixin.getMetadata();
    }

    public boolean isDetach() {
        return detach;
    }

    @Override
    public CloseableIterator<Tuple> iterate(Expression<?>... args) {
        return iterate(queryMixin.createProjection(args));
    }

    @Override
    public <RT> CloseableIterator<RT> iterate(Expression<RT> projection) {
        return new IteratorAdapter<RT>(list(projection).iterator(), closeable);
    }

    @Override
    public List<Tuple> list(Expression<?>... args) {
        return list(queryMixin.createProjection(args));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <RT> List<RT> list(Expression<RT> expr) {
        queryMixin.addProjection(expr);
        Object rv = execute(createQuery(false), false);
        reset();
        return rv instanceof List ? (List<RT>)rv : Collections.singletonList((RT) rv);
    }

    @Override
    public SearchResults<Tuple> listResults(Expression<?>... args) {
        return listResults(queryMixin.createProjection(args));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <RT> SearchResults<RT> listResults(Expression<RT> expr) {
        queryMixin.addProjection(expr);
        Query countQuery = createQuery(true);
        countQuery.setUnique(true);
        long total = (Long) execute(countQuery, true);
        if (total > 0) {
            QueryModifiers modifiers = queryMixin.getMetadata().getModifiers();
            Query query = createQuery(false);
            reset();
            return new SearchResults<RT>((List<RT>) execute(query, false), modifiers, total);
        } else {
            reset();
            return SearchResults.emptyResults();
        }
    }

    private void reset() {
        queryMixin.getMetadata().reset();
    }

    @Override
    public String toString() {
        if (!queryMixin.getMetadata().getJoins().isEmpty()) {
            SQLSerializer serializer = new SQLSerializer(configuration);
            serializer.serialize(queryMixin.getMetadata(), false);
            return serializer.toString().trim();
        } else {
            return super.toString();
        }
    }


    @Override
    @Nullable
    public Tuple uniqueResult(Expression<?>... args) {
        return uniqueResult(queryMixin.createProjection(args));
    }

    @Override
    @SuppressWarnings("unchecked")
    @Nullable
    public <RT> RT uniqueResult(Expression<RT> expr) {
        queryMixin.addProjection(expr);
        return (RT)uniqueResult();
    }

    @Nullable
    private Object uniqueResult() {
        if (getMetadata().getModifiers().getLimit() == null) {
            limit(2);
        }
        Query query = createQuery(false);
        reset();
        Object rv = execute(query, false);
        if (rv instanceof List) {
            List<?> list = (List<?>)rv;
            if (!list.isEmpty()) {
                if (list.size() > 1) {
                    throw new NonUniqueResultException();
                }
                return list.get(0);
            } else {
                return null;
            }
        } else {
            return rv;
        }
    }
}
