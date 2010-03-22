/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Iterator;
import java.util.List;

import net.jcip.annotations.Immutable;

import org.apache.commons.collections15.IteratorUtils;

import com.mysema.query.types.EBoolean;
import com.mysema.query.types.Expr;

/**
 * IteratorFactory provides Iterator utilities
 * 
 * @author tiwe
 * @version $Id$
 */
@Immutable
public class IteratorFactory {
    
    private final EvaluatorFactory evaluatorFactory;
    
    public IteratorFactory(EvaluatorFactory evaluatorFactory){
        this.evaluatorFactory = evaluatorFactory;
    }
    
    public <S> Iterator<S> multiArgFilter(Iterator<S> source, List<Expr<?>> sources, EBoolean condition) {
        Evaluator<Boolean> ev = evaluatorFactory.create( sources, condition);
        return multiArgFilter(source, ev);
    }

    private <S> Iterator<S> multiArgFilter(Iterator<S> source, final Evaluator<Boolean> ev) {
        return IteratorUtils.filteredIterator(source, new EvaluatorPredicate<S>(ev));
    }

    public <S, T> Iterator<T> transform(Iterator<S> source, List<Expr<?>> sources, Expr<T> projection) {
        Evaluator<T> ev = evaluatorFactory.create(sources, projection);
        return transform(source, ev);
    }

    private <S, T> Iterator<T> transform(Iterator<S> source, final Evaluator<T> ev) {
        return IteratorUtils.transformedIterator(source, new EvaluatorTransformer<S, T>(ev));
    }

    public <S> Iterator<S> singleArgFilter(Iterator<S> source, final Evaluator<Boolean> ev) {
        return IteratorUtils.filteredIterator(source, new SingleArgEvaluatorPredicate<S>(ev));
    }

    public <S> Iterator<S[]> toArrayIterator(Iterator<S> source) {
        return IteratorUtils.transformedIterator(source, new ArrayTransformer<S>());
    }

}
