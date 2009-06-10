/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.jdoql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.mysema.query.Projectable;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.support.QueryBaseWithProjectionAndDetach;
import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;

/**
 * Abstract base class for custom implementations of the JDOQLQuery interface.
 * 
 * @author tiwe
 *
 * @param <SubType>
 */
public abstract class AbstractJDOQLQuery<SubType extends AbstractJDOQLQuery<SubType>> extends QueryBaseWithProjectionAndDetach<Object, SubType> implements Projectable {
    
    private List<Object> orderedConstants = new ArrayList<Object>();

    private List<Query> queries = new ArrayList<Query>(2);
    
    private final JDOQLPatterns patterns;

    private final PersistenceManager pm;
    
    public AbstractJDOQLQuery(PersistenceManager pm) {
        this(pm, JDOQLPatterns.DEFAULT);
    }

    public AbstractJDOQLQuery(PersistenceManager pm, JDOQLPatterns patterns) {
        this.patterns = patterns;
        this.pm = pm;
    }

    @Override
    protected SubType addToProjection(Expr<?>... o) {
        for (Expr<?> expr : o) {
            if (expr instanceof EConstructor) {
                EConstructor<?> constructor = (EConstructor<?>) expr;
                for (Expr<?> arg : constructor.getArgs()) {
                    super.addToProjection(arg);
                }
            } else {
                super.addToProjection(expr);
            }
        }
        return _this;
    }

    public SubType from(PEntity<?>... o) {
        super.from(o);
        return _this;
    }

    @Override
    protected void clear() {
        super.clear();
    }

    public long count() {
        Query query = createQuery(null, true);
        query.setUnique(true);
        return (Long) execute(query);
    }

    private Query createQuery(QueryModifiers modifiers, boolean forCount) {        
        Expr<?> source = this.getMetadata().getJoins().get(0).getTarget();
        
        // serialize
        JDOQLSerializer serializer = new JDOQLSerializer(patterns, source);
        serializer.serialize(getMetadata(), forCount, false);
        
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
        addToProjection(expr1, expr2);
        addToProjection(rest);
        return (List<Object[]>) execute(createQuery(getMetadata().getModifiers(), false));
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
        addToProjection(expr);
        return (List<RT>) execute(createQuery(getMetadata().getModifiers(), false));
    }

    @SuppressWarnings("unchecked")
    public <RT> SearchResults<RT> listResults(Expr<RT> expr) {
        addToProjection(expr);
        Query countQuery = createQuery(null, true);
        countQuery.setUnique(true);
        countQuery.setResult("count(this)");
        long total = (Long) execute(countQuery);
        if (total > 0) {
            QueryModifiers modifiers = getMetadata().getModifiers();
            Query query = createQuery(modifiers, false);
            return new SearchResults<RT>((List<RT>) execute(query), modifiers,
                    total);
        } else {
            return SearchResults.emptyResults();
        }
    }

    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expr<RT> expr) {
        addToProjection(expr);
        Query query = createQuery(QueryModifiers.limit(1), false);
        query.setUnique(true);
        return (RT) execute(query);
    }

    public void close() throws IOException {
        for (Query query : queries){
            query.closeAll();
        }        
    }
}
