/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.support;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.IteratorUtils;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.Projectable;
import com.mysema.query.SearchResults;
import com.mysema.query.types.expr.Expr;

/**
 * ProjectableQuery extends the QueryBase class to provide default
 * implementations of the methods of the Projectable interface
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class ProjectableQuery<Q extends ProjectableQuery<Q>>
        extends QueryBase<Q> implements Projectable {

    public ProjectableQuery(QueryMixin<Q> queryMixin) {
        super(queryMixin);
    }

    @Override
    public long countDistinct() {
        queryMixin.setDistinct(true);
        return count();
    }
    
    @Override
    public final CloseableIterator<Object[]> iterate(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return iterate(merge(first, second, rest));
    }

    @Override
    public final CloseableIterator<Object[]> iterateDistinct(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        queryMixin.setDistinct(true);
        return iterate(first, second, rest);
    }

    @Override
    public final CloseableIterator<Object[]> iterateDistinct(Expr<?>[] args) {
        queryMixin.setDistinct(true);
        return iterate(args);
    }
    
    @Override
    public final <RT> CloseableIterator<RT> iterateDistinct(Expr<RT> projection) {
        queryMixin.setDistinct(true);
        return iterate(projection);
    }
    
    @Override
    public final List<Object[]> list(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return list(merge(first, second, rest));
    }

    @Override
    public List<Object[]> list(Expr<?>[] args) {
        return IteratorUtils.toList(iterate(args));
    }

    @Override
    public <RT> List<RT> list(Expr<RT> projection) {
        return IteratorUtils.toList(iterate(projection));
    }
    
    @Override
    public final List<Object[]> listDistinct(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        queryMixin.setDistinct(true);
        return list(first, second, rest);
    }

    public final List<Object[]> listDistinct(Expr<?>[] args) {
        queryMixin.setDistinct(true);
        return list(args);
    }
    
    @Override
    public final <RT> List<RT> listDistinct(Expr<RT> projection) {
        queryMixin.setDistinct(true);
        return list(projection);
    }

    @Override
    public final <RT> SearchResults<RT> listDistinctResults(Expr<RT> projection){
        queryMixin.setDistinct(true);
        return listResults(projection);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <K, V> Map<K, V> map(Expr<K> key, Expr<V> value) {
        List<Object[]> list = list(key, value);
        Map<K, V> results = new LinkedHashMap<K, V>(list.size());
        for (Object[] row : list){
            results.put((K)row[0], (V)row[1]);
        }
        return results;
    }
    
    private Expr<?>[] merge(Expr<?> first, Expr<?> second, Expr<?>... rest){
        Expr<?>[] args = new Expr<?>[rest.length + 2];
        args[0] = first;
        args[1] = second;
        System.arraycopy(rest, 0, args, 2, rest.length);
        return args;
    }
    
    @Override
    public final Object[] uniqueResult(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return uniqueResult(merge(first, second, rest));
    }
    
    public Object[] uniqueResult(Expr<?>[] args) {
        queryMixin.setUnique(true);
        Iterator<Object[]> it = iterate(args);
        return it.hasNext() ? it.next() : null;
    }
    
    @Override
    public <RT> RT uniqueResult(Expr<RT> expr) {
        queryMixin.setUnique(true);
        limit(1l);
        Iterator<RT> it = iterate(expr);
        return it.hasNext() ? it.next() : null;
    }
}
