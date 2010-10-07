/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdo;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.QTuple;

/**
 * Abstract base class for custom implementations of the JDOQLQuery interface.
 *
 * @author tiwe
 *
 * @param <Q>
 */
public abstract class AbstractJDOQLQuery<Q extends AbstractJDOQLQuery<Q>> extends ProjectableQuery<Q>{

    private static final Logger logger = LoggerFactory.getLogger(JDOQLQueryImpl.class);
    
    private final Closeable closeable = new Closeable(){
        @Override
        public void close() throws IOException {
            AbstractJDOQLQuery.this.close();            
        }        
    };
    
    private final boolean detach;

    private List<Object> orderedConstants = new ArrayList<Object>();

    @Nullable
    private final PersistenceManager persistenceManager;

    private List<Query> queries = new ArrayList<Query>(2);

    private final JDOQLTemplates templates;
    
    private Set<String> fetchGroups = new HashSet<String>();
    
    @Nullable
    private Integer maxFetchDepth;

    public AbstractJDOQLQuery(@Nullable PersistenceManager persistenceManager) {
        this(persistenceManager, JDOQLTemplates.DEFAULT, new DefaultQueryMetadata(), false);
    }

    @SuppressWarnings("unchecked")
    public AbstractJDOQLQuery(
            @Nullable PersistenceManager persistenceManager,
            JDOQLTemplates templates,
            QueryMetadata metadata, boolean detach) {
        super(new QueryMixin<Q>(metadata));
        this.queryMixin.setSelf((Q) this);
        this.templates = templates;
        this.persistenceManager = persistenceManager;
        this.detach = detach;
    }

    @SuppressWarnings("unchecked")
    public Q addFetchGroup(String fetchGroupName) {
        fetchGroups.add(fetchGroupName);
        return (Q)this;
    }

    public void close() {
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
        Expression<?> source = queryMixin.getMetadata().getJoins().get(0).getTarget();

        // serialize
        JDOQLSerializer serializer = new JDOQLSerializer(getTemplates(), source);
        serializer.serialize(queryMixin.getMetadata(), forCount, false);

        logQuery(serializer.toString());
        
        // create Query
        Query query = persistenceManager.newQuery(serializer.toString());
        orderedConstants = serializer.getConstants();
        queries.add(query);

        if (!forCount){
            List<? extends Expression<?>> projection = queryMixin.getMetadata().getProjection();
            Class<?> exprType = projection.get(0).getClass();
            if (exprType.equals(QTuple.class)){
                query.setResultClass(JDOTuple.class);
            } else if (FactoryExpression.class.isAssignableFrom(exprType)){
                query.setResultClass(projection.get(0).getType());
            }
            
            if (!fetchGroups.isEmpty()){
                query.getFetchPlan().setGroups(fetchGroups);
            }
            if (maxFetchDepth != null){
                query.getFetchPlan().setMaxFetchDepth(maxFetchDepth);
            }
        }

        return query;
    }
    
    protected void logQuery(String queryString){
        if (logger.isDebugEnabled()){
            logger.debug(queryString.replace('\n', ' '));
        }
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

    public Q from(EntityPath<?>... args) {
        return queryMixin.from(args);
    }

    public QueryMetadata getMetadata(){
        return queryMixin.getMetadata();
    }

    public JDOQLTemplates getTemplates() {
        return templates;
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
        countQuery.setResult("count(this)");
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

    @SuppressWarnings("unchecked")
    public Q setMaxFetchDepth(int depth) {
        maxFetchDepth = depth;
        return (Q)this;
    }
    

    @Override
    public String toString(){
        if (!queryMixin.getMetadata().getJoins().isEmpty()){
            Expression<?> source = queryMixin.getMetadata().getJoins().get(0).getTarget();
            JDOQLSerializer serializer = new JDOQLSerializer(getTemplates(), source);
            serializer.serialize(queryMixin.getMetadata(), false, false);
            return serializer.toString().trim();
        }else{
            return super.toString();
        }
    }

    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expression<RT> expr) {
        queryMixin.addToProjection(expr);
        Query query = createQuery(false);
        query.setUnique(true);
        reset();
        return (RT) execute(query);
    }
}
