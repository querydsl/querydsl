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
package com.mysema.query.jdo.sql;

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

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.NonUniqueResultException;
import com.mysema.query.QueryException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.jdo.JDOTuple;
import com.mysema.query.sql.SQLCommonQuery;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Expression;
import com.mysema.query.types.QTuple;

/**
 * JDOSQLQuery is an SQLQuery implementation that uses JDO's SQL query functionality
 * to execute queries
 *
 * @author tiwe
 *
 */
public final class JDOSQLQuery extends AbstractSQLQuery<JDOSQLQuery> implements SQLCommonQuery<JDOSQLQuery> {
    
    private static final Logger logger = LoggerFactory.getLogger(JDOSQLQuery.class);
    
    private final Closeable closeable = new Closeable() {
        @Override
        public void close() throws IOException {
            JDOSQLQuery.this.close();            
        }        
    };

    private final boolean detach;

    private List<Object> orderedConstants = new ArrayList<Object>();

    @Nullable
    private final PersistenceManager persistenceManager;

    private List<Query> queries = new ArrayList<Query>(2);

    public JDOSQLQuery(@Nullable PersistenceManager persistenceManager, SQLTemplates templates) {
        this(persistenceManager, templates, new DefaultQueryMetadata(), false);
    }

    public JDOSQLQuery(
            @Nullable PersistenceManager persistenceManager,
            SQLTemplates templates,
            QueryMetadata metadata, boolean detach) {
        super(metadata, templates);
        this.persistenceManager = persistenceManager;
        this.detach = detach;
    }

    public void close() {
        for (Query query : queries) {
            query.closeAll();
        }
    }

    public long count() {
        Query query = createQuery(true);
        query.setUnique(true);
        reset();
        Long rv = (Long) execute(query);
        if (rv != null) {
            return rv.longValue();
        } else {
            throw new QueryException("Query returned null");
        }
    }

    private Query createQuery(boolean forCount) {
        SQLSerializer serializer = new SQLSerializer(templates);
        if (union != null) {
            serializer.serializeUnion(union, queryMixin.getMetadata(), unionAll);
        } else {
            serializer.serialize(queryMixin.getMetadata(), forCount);    
        }
        

        // create Query
        if (logger.isDebugEnabled()) {
            logger.debug(serializer.toString());
        }
        Query query = persistenceManager.newQuery("javax.jdo.query.SQL",serializer.toString());
        orderedConstants = serializer.getConstants();
        queries.add(query);

        if (!forCount) {
            List<? extends Expression<?>> projection = queryMixin.getMetadata().getProjection();
            Class<?> exprType = projection.get(0).getClass();
            if (exprType.equals(QTuple.class)) {
                query.setResultClass(JDOTuple.class);
            } else if (ConstructorExpression.class.isAssignableFrom(exprType)) {
                query.setResultClass(projection.get(0).getType());
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

    private Object execute(Query query) {
        Object rv;
        if (!orderedConstants.isEmpty()) {
            rv = query.executeWithArray(orderedConstants.toArray());
        } else {
            rv = query.execute();
        }
        if (isDetach()) {
            rv = detach(rv);
        }
        return rv;
    }

    public QueryMetadata getMetadata() {
        return queryMixin.getMetadata();
    }

    public boolean isDetach() {
        return detach;
    }

    public CloseableIterator<Object[]> iterate(Expression<?>[] args) {
        return new IteratorAdapter<Object[]>(list(args).iterator(), closeable);
    }

    public <RT> CloseableIterator<RT> iterate(Expression<RT> projection) {
        return new IteratorAdapter<RT>(list(projection).iterator(), closeable);
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> list(Expression<?>[] args) {
        queryMixin.addToProjection(args);
        Object rv = execute(createQuery(false));
        reset();
        return (rv instanceof List) ? ((List<Object[]>)rv) : Collections.singletonList((Object[])rv);
    }

    @SuppressWarnings("unchecked")
    public <RT> List<RT> list(Expression<RT> expr) {
        queryMixin.addToProjection(expr);
        Object rv = execute(createQuery(false));
        reset();
        return rv instanceof List ? (List<RT>)rv : Collections.singletonList((RT)rv);
    }

    @SuppressWarnings("unchecked")
    public <RT> SearchResults<RT> listResults(Expression<RT> expr) {
        queryMixin.addToProjection(expr);
        Query countQuery = createQuery(true);
        countQuery.setUnique(true);
        long total = (Long) execute(countQuery);
        if (total > 0) {
            QueryModifiers modifiers = queryMixin.getMetadata().getModifiers();
            Query query = createQuery(false);
            reset();
            return new SearchResults<RT>((List<RT>) execute(query), modifiers, total);
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
            SQLSerializer serializer = new SQLSerializer(templates);
            serializer.serialize(queryMixin.getMetadata(), false);
            return serializer.toString().trim();
        } else {
            return super.toString();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @Nullable
    public <RT> RT uniqueResult(Expression<RT> expr) {
        queryMixin.addToProjection(expr);
        return (RT)uniqueResult();
    }
    
    @Override
    @Nullable
    public Object[] uniqueResult(Expression<?>[] args) {
        queryMixin.addToProjection(args);
        Object obj = uniqueResult();
        if (obj != null) {
            return obj.getClass().isArray() ? (Object[])obj : new Object[]{obj};    
        } else {
            return null;
        }     
    }

    @Nullable
    private Object uniqueResult() {
        if (getMetadata().getModifiers().getLimit() == null) {
            limit(2);
        }
        Query query = createQuery(false);
        reset();
        Object rv = execute(query);
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
