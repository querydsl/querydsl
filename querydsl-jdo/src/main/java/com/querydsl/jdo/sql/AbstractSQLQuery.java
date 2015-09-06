/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.querydsl.core.*;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.ProjectableSQLQuery;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLSerializer;

/**
 * Base class for JDO-based {@link SQLQuery} implementations
 *
 * @author tiwe
 *
 * @param <T> result type
 * @param <Q> concrete subclass
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractSQLQuery<T, Q extends AbstractSQLQuery<T, Q>> extends ProjectableSQLQuery<T, Q> {

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

    protected final QueryMixin<Q> queryMixin;

    @SuppressWarnings("unchecked")
    public AbstractSQLQuery(QueryMetadata metadata, Configuration conf, PersistenceManager persistenceManager,
                            boolean detach) {
        super(new QueryMixin<Q>(metadata, false), conf);
        this.queryMixin = super.queryMixin;
        this.queryMixin.setSelf((Q) this);
        this.persistenceManager = persistenceManager;
        this.detach = detach;
    }

    /**
     * Close the query and related resources
     */
    public void close() {
        for (Query query : queries) {
            query.closeAll();
        }
    }

    @Override
    public long fetchCount() {
        Query query = createQuery(true);
        query.setUnique(true);
        Long rv = (Long) execute(query, true);
        if (rv != null) {
            return rv;
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
        Query query = persistenceManager.newQuery("javax.jdo.query.SQL", serializer.toString());
        orderedConstants = serializer.getConstants();
        queries.add(query);

        if (!forCount) {
            Expression<?> projection = queryMixin.getMetadata().getProjection();
            if (projection instanceof FactoryExpression) {
                this.projection = (FactoryExpression<?>) projection;
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
            return expr.newInstance((Object[]) row);
        } else {
            return expr.newInstance(row);
        }
    }

    @SuppressWarnings("unchecked")
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
                List<?> original = (List<?>) rv;
                rv = Lists.newArrayList();
                for (Object o : original) {
                    ((List) rv).add(project(projection, o));
                }
            } else {
                rv = project(projection, rv);
            }
        }
        return rv;
    }

    public boolean isDetach() {
        return detach;
    }

    @Override
    public CloseableIterator<T> iterate() {
        return new IteratorAdapter<T>(fetch().iterator(), closeable);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> fetch() {
        Object rv = execute(createQuery(false), false);
        return rv instanceof List ? (List<T>) rv : Collections.singletonList((T) rv);
    }

    @Override
    @SuppressWarnings("unchecked")
    public QueryResults<T> fetchResults() {
        Query countQuery = createQuery(true);
        countQuery.setUnique(true);
        long total = (Long) execute(countQuery, true);
        if (total > 0) {
            QueryModifiers modifiers = queryMixin.getMetadata().getModifiers();
            Query query = createQuery(false);
            return new QueryResults<T>((List<T>) execute(query, false), modifiers, total);
        } else {
            return QueryResults.emptyResults();
        }
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

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public T fetchOne() {
        if (getMetadata().getModifiers().getLimit() == null) {
            limit(2);
        }
        Query query = createQuery(false);
        Object rv = execute(query, false);
        if (rv instanceof List) {
            List<?> list = (List<?>) rv;
            if (!list.isEmpty()) {
                if (list.size() > 1) {
                    throw new NonUniqueResultException();
                }
                return (T) list.get(0);
            } else {
                return null;
            }
        } else {
            return (T) rv;
        }
    }
}
