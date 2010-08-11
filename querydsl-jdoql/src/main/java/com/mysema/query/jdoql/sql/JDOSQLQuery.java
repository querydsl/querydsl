/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdoql.sql;

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
import com.mysema.query.QueryException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.jdoql.JDOTuple;
import com.mysema.query.sql.SQLCommonQuery;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.EConstructor;
import com.mysema.query.types.Expr;
import com.mysema.query.types.expr.QTuple;

/**
 * JDOSQLQuery is an SQLQuery implementation that uses JDO's SQL query functionality
 * to execute queries
 *
 * @author tiwe
 *
 */
public final class JDOSQLQuery extends AbstractSQLQuery<JDOSQLQuery> implements SQLCommonQuery<JDOSQLQuery>, Closeable{
    
    private static final Logger logger = LoggerFactory.getLogger(JDOSQLQuery.class);

    private final boolean detach;

    private List<Object> orderedConstants = new ArrayList<Object>();

    @Nullable
    private final PersistenceManager persistenceManager;

    private List<Query> queries = new ArrayList<Query>(2);

    private final SQLTemplates templates;

    public JDOSQLQuery(@Nullable PersistenceManager persistenceManager, SQLTemplates templates) {
        this(persistenceManager, templates, new DefaultQueryMetadata(), false);
    }

    public JDOSQLQuery(
            @Nullable PersistenceManager persistenceManager,
            SQLTemplates templates,
            QueryMetadata metadata, boolean detach) {
        super(metadata);
        this.templates = templates;
        this.persistenceManager = persistenceManager;
        this.detach = detach;
    }

    public void close() throws IOException {
        for (Query query : queries){
            query.closeAll();
        }
    }

    public long count() {
        Query query = createQuery(true);
        query.setUnique(true);
        reset();
        Long rv = (Long) execute(query);
        if (rv != null){
            return rv.longValue();
        }else{
            throw new QueryException("Query returned null");
        }
    }

    private Query createQuery(boolean forCount) {
        SQLSerializer serializer = new SQLSerializer(templates);
        serializer.serialize(queryMixin.getMetadata(), forCount);

        // create Query
        if (logger.isDebugEnabled()){
            logger.debug(serializer.toString());
        }
        Query query = persistenceManager.newQuery("javax.jdo.query.SQL",serializer.toString());
        orderedConstants = serializer.getConstants();
        queries.add(query);

        if (!forCount){
            List<? extends Expr<?>> projection = queryMixin.getMetadata().getProjection();
            Class<?> exprType = projection.get(0).getClass();
            if (exprType.equals(QTuple.class)){
                query.setResultClass(JDOTuple.class);
            } else if (exprType.equals(EConstructor.class)){
                query.setResultClass(projection.get(0).getType());
            }
        }else{
            query.setResultClass(Long.class);
        }

        return query;
    }

    @SuppressWarnings("unchecked")
    private <T> T detach(T results){
        if (results instanceof Collection){
            return (T) persistenceManager.detachCopyAll(results);
        }else{
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
        if (isDetach()){
            rv = detach(rv);
        }
        return rv;
    }

    public QueryMetadata getMetadata(){
        return queryMixin.getMetadata();
    }

    public boolean isDetach() {
        return detach;
    }

    public CloseableIterator<Object[]> iterate(Expr<?>[] args) {
        return new IteratorAdapter<Object[]>(list(args).iterator(), this);
    }

    public <RT> CloseableIterator<RT> iterate(Expr<RT> projection) {
        return new IteratorAdapter<RT>(list(projection).iterator(), this);
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> list(Expr<?>[] args) {
        queryMixin.addToProjection(args);
        Object rv = execute(createQuery(false));
        reset();
        return (rv instanceof List) ? ((List<Object[]>)rv) : Collections.singletonList((Object[])rv);
    }

    @SuppressWarnings("unchecked")
    public <RT> List<RT> list(Expr<RT> expr) {
        queryMixin.addToProjection(expr);
        Object rv = execute(createQuery(false));
        reset();
        return rv instanceof List ? (List<RT>)rv : Collections.singletonList((RT)rv);
    }

    @SuppressWarnings("unchecked")
    public <RT> SearchResults<RT> listResults(Expr<RT> expr) {
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

    private void reset(){
        queryMixin.getMetadata().reset();
    }

    @Override
    public String toString(){
        if (!queryMixin.getMetadata().getJoins().isEmpty()){
            SQLSerializer serializer = new SQLSerializer(templates);
            serializer.serialize(queryMixin.getMetadata(), false);
            return serializer.toString().trim();
        }else{
            return super.toString();
        }
    }

    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expr<RT> expr) {
        queryMixin.addToProjection(expr);
        Query query = createQuery(false);
        query.setUnique(true);
        reset();
        return (RT) execute(query);
    }


}
