/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.IteratorUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;

import com.mysema.query.collections.ColQueryTemplates;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * IteratorFactory provides Iterator utilities
 * 
 * @author tiwe
 * @version $Id$
 */
public class IteratorFactory {
    
    public <S> Iterator<S> multiArgFilter(ColQueryTemplates patterns, Iterator<S> source, List<Expr<?>> sources, EBoolean condition) {
        Evaluator ev = Evaluator.create(patterns, sources, condition);
        return multiArgFilter(source, ev);
    }

    private <S> Iterator<S> multiArgFilter(Iterator<S> source, final Evaluator ev) {
        return IteratorUtils.filteredIterator(source, new Predicate<S>() {
            public boolean evaluate(S object) {
                return ev.<Boolean> evaluate((Object[]) object).booleanValue();
            }
        });
    }

    public <S, T> Iterator<T> transform(ColQueryTemplates patterns, Iterator<S> source, List<Expr<?>> sources, Expr<?> projection) {
        Evaluator ev = Evaluator.create(patterns, sources, projection);
        return transform(source, ev);
    }

    private <S, T> Iterator<T> transform(Iterator<S> source, final Evaluator ev) {
        return IteratorUtils.transformedIterator(source,
                new Transformer<S, T>() {
                    public T transform(S input) {
                        return ev.<T> evaluate((Object[]) input);
                    }
                });
    }

    @SuppressWarnings("unchecked")
    public <S, T> Map<S, ? extends Iterable<T>> projectToMap(Iterator<T> source, Evaluator ev) {
        int size = 300;
        if (source instanceof Collection) {
            size = (int) Math.ceil(((Collection<?>) source).size() * 0.7);
        }
        Map<S, Collection<T>> map = new HashMap<S, Collection<T>>(size);
        while (source.hasNext()) {
            T value = source.next();
            S key = ev.<S> evaluate(value);
            Collection<T> col = map.get(key);
            if (col == null) {
                col = new ArrayList<T>();
                map.put(key, col);
            }
            col.add(value);
        }
        return map;
    }

    public <S> Iterator<S> singleArgFilter(Iterator<S> source, final Evaluator ev) {
        return IteratorUtils.filteredIterator(source, new Predicate<S>() {
            public boolean evaluate(S object) {
                return ev.<Boolean> evaluate(object);
            }
        });
    }

    public <S> Iterator<S[]> toArrayIterator(Iterator<S> source) {
        return IteratorUtils.transformedIterator(source,
                new Transformer<S, S[]>() {
                    @SuppressWarnings("unchecked")
                    public S[] transform(S input) {
                        return (S[])new Object[]{input};
                    }
                });
    }

}
