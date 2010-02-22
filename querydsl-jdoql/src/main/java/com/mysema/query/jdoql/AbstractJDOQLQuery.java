/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.mysema.query.Projectable;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;

/**
 * Abstract base class for custom implementations of the JDOQLQuery interface.
 * 
 * @author tiwe
 *
 * @param <SubType>
 */
public abstract class AbstractJDOQLQuery<SubType extends AbstractJDOQLQuery<SubType>> extends ProjectableQuery<SubType> implements Projectable {
    
    private final boolean detach;

    private List<Object> orderedConstants = new ArrayList<Object>();
    
    @Nullable
    private final PersistenceManager persistenceManager;

    private List<Query> queries = new ArrayList<Query>(2);
    
    private final JDOQLTemplates templates;

    @SuppressWarnings("unchecked")
    public AbstractJDOQLQuery(@Nullable PersistenceManager persistenceManager, JDOQLTemplates templates, QueryMetadata metadata, boolean detach) {
        super(new JDOQLQueryMixin<SubType>(metadata));
        this.queryMixin.setSelf((SubType) this);
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
        return (Long) execute(query);
    }

    private Query createQuery(boolean forCount) {        
        Expr<?> source = queryMixin.getMetadata().getJoins().get(0).getTarget();
        
        // serialize
        JDOQLSerializer serializer = new JDOQLSerializer(getTemplates(), source);
        serializer.serialize(queryMixin.getMetadata(), forCount, false);
        
        // create Query 
        Query query = persistenceManager.newQuery(serializer.toString());
        orderedConstants = serializer.getConstants();
        queries.add(query);               
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

    public SubType from(PEntity<?>... args) {
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
    
    public Iterator<Object[]> iterate(Expr<?>[] args) {
        return list(args).iterator();
    }

    public <RT> Iterator<RT> iterate(Expr<RT> projection) {
        return list(projection).iterator();
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
    
    @Override
    public String toString(){
        if (!queryMixin.getMetadata().getJoins().isEmpty()){
            Expr<?> source = queryMixin.getMetadata().getJoins().get(0).getTarget();
            JDOQLSerializer serializer = new JDOQLSerializer(getTemplates(), source);
            serializer.serialize(queryMixin.getMetadata(), false, false);
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
