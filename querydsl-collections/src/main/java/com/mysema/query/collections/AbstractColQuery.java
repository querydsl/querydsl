/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.IteratorUtils;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.JoinType;
import com.mysema.query.QueryException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.EArrayConstructor;
import com.mysema.query.types.expr.OSimple;

/**
 * AbstractColQuery provides a base class for Collection query implementations.
 * Extend it like this
 * 
 * <pre>
 * public class MyType extends AbstractColQuery&lt;MyType&gt;{
 *   ...
 * }
 * </pre>
 * 
 * @see ColQuery
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class AbstractColQuery<Q extends AbstractColQuery<Q>>  extends ProjectableQuery<Q> {
    
    private final ExprEvaluatorFactory evaluatorFactory;
    
    private final Map<Expr<?>, Iterable<?>> iterables = new HashMap<Expr<?>, Iterable<?>>();

    private final IteratorFactory iteratorFactory;

    @SuppressWarnings("unchecked")
    public AbstractColQuery(QueryMetadata metadata, ExprEvaluatorFactory evaluatorFactory) {
        super(new QueryMixin<Q>(metadata));
        this.queryMixin.setSelf((Q) this);
        this.evaluatorFactory = evaluatorFactory;
        this.iteratorFactory = new IteratorFactory(evaluatorFactory);
    }
    
    @Override
    public long count() {
        try {
            Iterator<?> iterator = createIterator(true);
            long count = 0l;
            while (iterator.hasNext()) {
                iterator.next();
                count++;
            }
            return count;
        } catch (Exception e) {
            throw new QueryException(e.getMessage(), e);
        }finally{
            reset();
        }
    }
    
    private <RT> Iterator<RT> createIterator(boolean forCount){
        QueryMetadata metadata = queryMixin.getMetadata();
        if (metadata.getJoins().size() == 1) {
            return new SingleSourceIterable<Object,RT>(metadata, 
                    evaluatorFactory, iteratorFactory, 
                    iterables, forCount).iterator();
        } else {
            return new MultiSourceIterable<RT>(metadata, 
                    evaluatorFactory, iteratorFactory, 
                    iterables, forCount).iterator();
        }
    }

    private <RT> Iterator<RT> createPagedIterator() throws Exception {
        Iterator<RT> iterator = createIterator(false);
        return LimitingIterator.create(iterator, queryMixin.getMetadata().getModifiers());
    }
    
    @SuppressWarnings("unchecked")
    public <A> Q from(Path<A> entity, Iterable<? extends A> col) {
        iterables.put(entity.asExpr(), col);
        queryMixin.getMetadata().addJoin(JoinType.DEFAULT, entity.asExpr());
        return (Q)this;
    }
    
    @SuppressWarnings("unchecked")
    public <P> Q innerJoin(Path<? extends Collection<P>> target, Path<P> alias) {
        queryMixin.getMetadata().addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return (Q)this;
    }

    @SuppressWarnings("unchecked")
    private <D> Expr<D> createAlias(Path<? extends Collection<D>> target, Path<D> alias){
        return OSimple.create((Class<D>)alias.getType(), Ops.ALIAS, target.asExpr(), alias.asExpr());
    }

    protected ExprEvaluatorFactory getEvaluatorFactory() {
        return evaluatorFactory;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public CloseableIterator<Object[]> iterate(Expr<?>[] args) {
        return iterate(new EArrayConstructor(Object[].class, args));
    }
    
    @Override
    public <RT> CloseableIterator<RT> iterate(Expr<RT> projection) {        
        try {
            queryMixin.addToProjection(projection);
            return new IteratorAdapter<RT>(this.<RT>createPagedIterator());
        } catch (Exception e) {
            throw new QueryException(e.getMessage(), e);
        }finally{
            reset();
        }        
    }

    @Override
    public <RT> SearchResults<RT> listResults(Expr<RT> projection) {
        queryMixin.addToProjection(projection);
        QueryModifiers modifiers = queryMixin.getMetadata().getModifiers();
        List<RT> list;
        try {
            list = IteratorUtils.toList(this.<RT>createIterator(false));
        } catch (Exception e) {
            throw new QueryException(e.getMessage(), e);
        }
        // empty results
        if (list.isEmpty()) {
            reset();
            return SearchResults.emptyResults();
            
        // no restrictions    
        } else if (!modifiers.isRestricting()) {
            reset();
            return new SearchResults<RT>(list, modifiers, list.size());
            
        } else {
            int start = 0;
            int end = list.size();
            if (modifiers.getOffset() != null) {
                if (modifiers.getOffset() < list.size()) {
                    start = modifiers.getOffset().intValue();
                } else {
                    reset();
                    return new SearchResults<RT>(Collections.<RT> emptyList(), modifiers, list.size());
                }
            }
            if (modifiers.getLimit() != null) {
                end = (int) Math.min(start + modifiers.getLimit(), list.size());
            }
            reset();
            return new SearchResults<RT>(list.subList(start, end), modifiers, list.size());
        }
    }

    private void reset(){
        queryMixin.getMetadata().reset();
    }

}
