/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.support;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.IteratorUtils;

import com.mysema.query.Projectable;
import com.mysema.query.QueryBase;
import com.mysema.query.QueryMixin;
import com.mysema.query.SearchResults;
import com.mysema.query.types.expr.Expr;

/**
 * ProjectableQuery extends the QueryBase class to provide default
 * implementations of the methods of the Projectable interface
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class ProjectableQuery<SubType extends ProjectableQuery<SubType>>
        extends QueryBase<SubType> implements Projectable {

    public ProjectableQuery(QueryMixin<SubType> queryMixin) {
        super(queryMixin);
    }

    protected <A> A[] asArray(A[] target, A first, A second, A... rest) {
        target[0] = first;
        target[1] = second;
        System.arraycopy(rest, 0, target, 2, rest.length);
        return target;
    }

    @Override
    public long countDistinct() {
        queryMixin.setDistinct(true);
        return count();
    }

    @Override
    public final Iterator<Object[]> iterateDistinct(Expr<?> first,
            Expr<?> second, Expr<?>... rest) {
        queryMixin.setDistinct(true);
        return iterate(first, second, rest);
    }

    @Override
    public final <RT> Iterator<RT> iterateDistinct(Expr<RT> projection) {
        queryMixin.setDistinct(true);
        return iterate(projection);
    }

    @Override
    public List<Object[]> list(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return IteratorUtils.toList(iterate(first, second, rest));
    }

    @Override
    public <RT> List<RT> list(Expr<RT> projection) {
        return IteratorUtils.toList(iterate(projection));
    }

    @Override
    public final List<Object[]> listDistinct(Expr<?> first, Expr<?> second,
            Expr<?>... rest) {
        queryMixin.setDistinct(true);
        return list(first, second, rest);
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
    
    @Override
    public Object[] uniqueResult(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        queryMixin.setUnique(true);
        Iterator<Object[]> it = iterate(first, second, rest);
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
