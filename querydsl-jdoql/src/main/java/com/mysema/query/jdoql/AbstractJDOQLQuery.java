/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.mysema.query.DefaultQueryMetadata;
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
    
    private List<Object> orderedConstants = new ArrayList<Object>();

    private List<Query> queries = new ArrayList<Query>(2);
    
    private final JDOQLTemplates templates;

    private final PersistenceManager pm;
    
    @SuppressWarnings("unchecked")
    public AbstractJDOQLQuery(PersistenceManager pm, JDOQLTemplates templates) {
        super(new JDOQLQueryMixin<SubType>());
        this.queryMixin.setSelf((SubType) this);
        this.templates = templates;
        this.pm = pm;
    }

    public SubType from(PEntity<?>... args) {
        return queryMixin.from(args);
    }

    public long count() {
        Query query = createQuery(true);
        query.setUnique(true);
        return (Long) execute(query);
    }

    private Query createQuery(boolean forCount) {        
        Expr<?> source = queryMixin.getMetadata().getJoins().get(0).getTarget();
        
        // serialize
        JDOQLSerializer serializer = new JDOQLSerializer(templates, source);
        serializer.serialize(queryMixin.getMetadata(), forCount, false);
        
        // create Query 
        Query query = pm.newQuery(serializer.toString());
        orderedConstants = serializer.getConstants();
        queries.add(query);               
        return query;
    }

    public Iterator<Object[]> iterate(Expr<?> e1, Expr<?> e2, Expr<?>... rest) {
        // TODO : optimize
        return list(e1, e2, rest).iterator();
    }

    public <RT> Iterator<RT> iterate(Expr<RT> projection) {
        // TODO : optimize
        return list(projection).iterator();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> list(Expr<?> expr1, Expr<?> expr2, Expr<?>... rest) {
        queryMixin.addToProjection(expr1, expr2);
        queryMixin.addToProjection(rest);
        Object rv = execute(createQuery(false));
        return (rv instanceof List) ? ((List<Object[]>)rv) : Collections.singletonList((Object[])rv);
    }

    private Object execute(Query query) {
        Object rv;
        if (!orderedConstants.isEmpty()) {
            rv = query.executeWithArray(orderedConstants.toArray());
        } else {
            rv = query.execute();
        }
        // query.closeAll();
        return rv;
    }

    @SuppressWarnings("unchecked")
    public <RT> List<RT> list(Expr<RT> expr) {
        queryMixin.addToProjection(expr);
        Object rv = execute(createQuery(false));
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
            return new SearchResults<RT>((List<RT>) execute(query), modifiers, total);
        } else {
            return SearchResults.emptyResults();
        }
    }

    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expr<RT> expr) {
        queryMixin.addToProjection(expr);
        Query query = createQuery(false);
        query.setUnique(true);
        return (RT) execute(query);
    }

    public void close() throws IOException {
        for (Query query : queries){
            query.closeAll();
        }        
    }
    
    @Override
    public String toString(){
        if (!queryMixin.getMetadata().getJoins().isEmpty()){
            Expr<?> source = queryMixin.getMetadata().getJoins().get(0).getTarget();
            JDOQLSerializer serializer = new JDOQLSerializer(templates, source);
            serializer.serialize(queryMixin.getMetadata(), false, false);
            return serializer.toString().trim();
        }else{
            return super.toString();
        }        
    }
}
