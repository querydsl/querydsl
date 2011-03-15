/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.collections15.IteratorUtils;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.NonUniqueResultException;
import com.mysema.query.Projectable;
import com.mysema.query.SearchResults;
import com.mysema.query.types.Expression;

/**
 * ProjectableQuery extends the QueryBase class to provide default
 * implementations of the methods of the Projectable interface
 *
 * @author tiwe
 */
public abstract class ProjectableQuery<Q extends ProjectableQuery<Q>>
        extends QueryBase<Q> implements Projectable {

    public ProjectableQuery(QueryMixin<Q> queryMixin) {
        super(queryMixin);
    }

    @Override
    public final long countDistinct() {
        queryMixin.setDistinct(true);
        return count();
    }



    @Override
    public final CloseableIterator<Object[]> iterate(Expression<?> first, Expression<?> second, Expression<?>... rest) {
        return iterate(merge(first, second, rest));
    }

    @Override
    public final CloseableIterator<Object[]> iterateDistinct(Expression<?> first, Expression<?> second, Expression<?>... rest) {
        queryMixin.setDistinct(true);
        return iterate(first, second, rest);
    }

    @Override
    public final CloseableIterator<Object[]> iterateDistinct(Expression<?>[] args) {
        queryMixin.setDistinct(true);
        return iterate(args);
    }

    @Override
    public final <RT> CloseableIterator<RT> iterateDistinct(Expression<RT> projection) {
        queryMixin.setDistinct(true);
        return iterate(projection);
    }

    @Override
    public final List<Object[]> list(Expression<?> first, Expression<?> second, Expression<?>... rest) {
        return list(merge(first, second, rest));
    }

    @Override
    public List<Object[]> list(Expression<?>[] args) {
        return IteratorUtils.toList(iterate(args));
    }

    @Override
    public <RT> List<RT> list(Expression<RT> projection) {
        return IteratorUtils.toList(iterate(projection));
    }

    @Override
    public final List<Object[]> listDistinct(Expression<?> first, Expression<?> second, Expression<?>... rest) {
        queryMixin.setDistinct(true);
        return list(first, second, rest);
    }

    public final List<Object[]> listDistinct(Expression<?>[] args) {
        queryMixin.setDistinct(true);
        return list(args);
    }

    @Override
    public final <RT> List<RT> listDistinct(Expression<RT> projection) {
        queryMixin.setDistinct(true);
        return list(projection);
    }

    @Override
    public final <RT> SearchResults<RT> listDistinctResults(Expression<RT> projection){
        queryMixin.setDistinct(true);
        return listResults(projection);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final <K, V> Map<K, V> map(Expression<K> key, Expression<V> value) {
        List<Object[]> list = list(key, value);
        Map<K, V> results = new LinkedHashMap<K, V>(list.size());
        for (Object[] row : list){
            results.put((K)row[0], (V)row[1]);
        }
        return results;
    }

    private Expression<?>[] merge(Expression<?> first, Expression<?> second, Expression<?>... rest){
        Expression<?>[] args = new Expression<?>[rest.length + 2];
        args[0] = first;
        args[1] = second;
        System.arraycopy(rest, 0, args, 2, rest.length);
        return args;
    }

    @Override
    public final boolean notExists(){
        return !exists();
    }

    @Override
    public final Object[] singleResult(Expression<?> first, Expression<?> second, Expression<?>... rest) {
        return singleResult(merge(first, second, rest));
    }

    @Override
    public final Object[] singleResult(Expression<?>[] args) {
        return limit(1).uniqueResult(args);
    }

    @Override
    public final <RT> RT singleResult(Expression<RT> expr) {
        return limit(1).uniqueResult(expr);
    }

    @Override
    public final Object[] uniqueResult(Expression<?> first, Expression<?> second, Expression<?>... rest) {
        return uniqueResult(merge(first, second, rest));
    }
    
    @Nullable
    protected <T> T uniqueResult(CloseableIterator<T> it) {
        try{
            if (it.hasNext()){
                T rv = it.next();
                if (it.hasNext()){
                    throw new NonUniqueResultException();
                }
                return rv;
            }else{
                return null;
            }
        }finally{
            it.close();
        }
    }


}
