/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.IteratorUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.iterators.FilterIterator;
import org.apache.commons.collections15.iterators.UniqueFilterIterator;

import com.mysema.query.JoinExpression;
import com.mysema.query.QueryException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryMixin;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.collections.ColQuery;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EArrayConstructor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.Path;
import com.mysema.util.MultiIterator;

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
public abstract class AbstractColQuery<SubType extends AbstractColQuery<SubType>> 
    extends ProjectableQuery<SubType> {
    
    private boolean arrayProjection = false;
    
    private final EvaluatorFactory evaluatorFactory;
    
    private final Map<Expr<?>, Iterable<?>> exprToIt = new HashMap<Expr<?>, Iterable<?>>();

    private final IteratorFactory iteratorFactory;

    @SuppressWarnings("unchecked")
    public AbstractColQuery(QueryMetadata metadata, EvaluatorFactory evaluatorFactory) {
        super(new QueryMixin<SubType>(metadata));
        this.queryMixin.setSelf((SubType) this);
        this.evaluatorFactory = evaluatorFactory;
        this.iteratorFactory = new IteratorFactory(evaluatorFactory);
    }
    
    @SuppressWarnings("unchecked")
    private <RT> Iterator<RT> asDistinctIterator(Iterator<RT> rv) {
        if (!arrayProjection) {
            return new UniqueFilterIterator<RT>(rv);
        } else {
            return new FilterIterator<RT>(rv, new Predicate() {
                private Set<List<Object>> set = new HashSet<List<Object>>();

                public boolean evaluate(Object object) {
                    return set.add(Arrays.asList((Object[]) object));
                }
            });
        }
    }
    
    public long count() {
        try {
            List<Expr<?>> sources = new ArrayList<Expr<?>>();
            Iterator<?> it;
            if (queryMixin.getMetadata().getJoins().size() == 1) {
                it = handleFromWhereSingleSource(sources);
            } else {
                it = handleFromWhereMultiSource(sources);
            }
            if (queryMixin.isDistinct()) {
                arrayProjection = true;
                it = asDistinctIterator(it);
            }
            long count = 0l;
            while (it.hasNext()) {
                it.next();
                count++;
            }
            return count;
        } catch (Exception e) {
            throw new QueryException(e.getMessage(), e);
        }finally{
            reset();
        }
    }
    
    private <RT> Iterator<RT> createIterator(Expr<RT> projection) throws Exception {
        QueryMetadata md = queryMixin.getMetadata();
        List<Expr<?>> sources = new ArrayList<Expr<?>>();
        // from / where
        Iterator<?> it;
        if (md.getJoins().size() == 1) {
            it = handleFromWhereSingleSource(sources);
        } else {
            it = handleFromWhereMultiSource(sources);
        }

        // group by
        if (!md.getGroupBy().isEmpty()){
            // TODO
            
            // having
            if (md.getHaving() != null){
                it = iteratorFactory.multiArgFilter(it, sources, md.getHaving());
            }
        }
        
        if (it.hasNext()) {
            // order
            if (!md.getOrderBy().isEmpty()) {
                it = handleOrderBy(sources, it);
            }

            // select
            return handleSelect(it, sources, projection);

        } else {
            return Collections.<RT> emptyList().iterator();
        }

    }

    private <RT> Iterator<RT> createPagedIterator(Expr<RT> projection) throws Exception {
        Iterator<RT> iterator = createIterator(projection);
        return LimitingIterator.create(iterator, queryMixin.getMetadata().getModifiers());
    }
    
    @SuppressWarnings("unchecked")
    public <A> SubType from(Path<A> entity, Iterable<? extends A> col) {
        exprToIt.put(entity.asExpr(), col);
        queryMixin.getMetadata().addFrom(entity.asExpr());
        return (SubType)this;
    }

    protected EvaluatorFactory getEvaluatorFactory() {
        return evaluatorFactory;
    }

    @SuppressWarnings("unchecked")
    protected Iterator<?> handleFromWhereMultiSource(List<Expr<?>> sources) throws Exception {
        EBoolean condition = queryMixin.getMetadata().getWhere();
        List<JoinExpression> joins = new ArrayList<JoinExpression>(queryMixin.getMetadata().getJoins());
        for (JoinExpression join : joins) {
            sources.add(join.getTarget());
        }                
        List<Iterable<?>> iterables = new ArrayList<Iterable<?>>(sources.size());
        for (Expr<?> expr : sources){
            iterables.add(exprToIt.get(expr));
        }
        Iterator it = new MultiIterator(iterables);
        if (condition != null) {
            it = iteratorFactory.multiArgFilter(it, sources, condition);
        }
        return it;
    }

    protected Iterator<?> handleFromWhereSingleSource(List<Expr<?>> sources) throws Exception {
        EBoolean condition = queryMixin.getMetadata().getWhere();
        JoinExpression join = queryMixin.getMetadata().getJoins().get(0);
        sources.add(join.getTarget());
        
        // project source to array
        Iterator<?> it = iteratorFactory.toArrayIterator(exprToIt.get(join.getTarget()).iterator());
        if (condition != null) {
            // where
            it = iteratorFactory.multiArgFilter(it, sources, condition);
        }
        
        return it;

    }

    @SuppressWarnings("unchecked")
    protected Iterator<?> handleOrderBy(List<Expr<?>> sources, Iterator<?> it) throws Exception {
        // create a projection for the order
        List<OrderSpecifier<?>> orderBy = queryMixin.getMetadata().getOrderBy();
        Expr<Object>[] orderByExpr = new Expr[orderBy.size()];
        boolean[] directions = new boolean[orderBy.size()];
        for (int i = 0; i < orderBy.size(); i++) {
            orderByExpr[i] = (Expr) orderBy.get(i).getTarget();
            directions[i] = orderBy.get(i).getOrder() == Order.ASC;
        }
        Expr<?> expr = new EArrayConstructor<Object>(Object[].class, orderByExpr);
        Evaluator ev = evaluatorFactory.create(sources, expr);

        // transform the iterator to list
        List<Object[]> itAsList = IteratorUtils.toList((Iterator<Object[]>) it);
        Collections.sort(itAsList, new MultiComparator(ev, directions));
        it = itAsList.iterator();
        return it;
    }

    protected <RT> Iterator<RT> handleSelect(Iterator<?> it,
            List<Expr<?>> sources, Expr<RT> projection) throws Exception {
        Iterator<RT> rv = iteratorFactory.transform(it, sources, projection);
        if (queryMixin.isDistinct()) {
            rv = asDistinctIterator(rv);
        }
        return rv;
    }
    
    @SuppressWarnings("unchecked")
    public Iterator<Object[]> iterate(Expr<?>[] args) {
        arrayProjection = true;
        Expr<Object[]> projection = new EArrayConstructor(Object[].class, args);
        return iterate(projection);
    }
    
    public <RT> Iterator<RT> iterate(Expr<RT> projection) {        
        try {
            queryMixin.addToProjection(projection);
            return createPagedIterator(projection);
        } catch (Exception e) {
            throw new QueryException(e.getMessage(), e);
        }finally{
            reset();
        }
        
    }

    public <RT> SearchResults<RT> listResults(Expr<RT> projection) {
        QueryModifiers modifiers = queryMixin.getMetadata().getModifiers();
        List<RT> list;
        try {
            list = IteratorUtils.toList(createIterator(projection));
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
