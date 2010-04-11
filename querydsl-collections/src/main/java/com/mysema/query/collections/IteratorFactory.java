/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.jcip.annotations.Immutable;

import org.apache.commons.collections15.IteratorUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;

import com.mysema.codegen.Evaluator;
import com.mysema.query.types.Expr;
import com.mysema.query.types.expr.EBoolean;

/**
 * IteratorFactory provides Iterator utilities
 * 
 * @author tiwe
 * @version $Id$
 */
@Immutable
public class IteratorFactory {
    
    static class EvaluatorTransformer<S, T> implements Transformer<S, T> {
        
        private final Evaluator<T> ev;

        EvaluatorTransformer(Evaluator<T> ev) {
            this.ev = ev;
        }
        @Override
        public T transform(S input) {         
            if (input.getClass().isArray()){
                return ev.evaluate((Object[]) input);    
            }else{
                return ev.evaluate(new Object[]{input});
            }            
        }
    }
    
    static class MultiArgEvaluatorPredicate<S> implements Predicate<S> {
        
        private final Evaluator<Boolean> ev;

        MultiArgEvaluatorPredicate(Evaluator<Boolean> ev) {
            this.ev = ev;
        }
        @Override
        public boolean evaluate(S object) {
            return ev.evaluate((Object[]) object).booleanValue();
        }
    }
    
    static class SingleArgEvaluatorPredicate<S> implements Predicate<S> {
        
        private final Evaluator<Boolean> ev;

        SingleArgEvaluatorPredicate(Evaluator<Boolean> ev) {
            this.ev = ev;
        }
        @Override
        public boolean evaluate(S object) {
            return ev.evaluate(object).booleanValue();
        }
    }
    
    private final ExprEvaluatorFactory evaluatorFactory;
    
    public IteratorFactory(ExprEvaluatorFactory evaluatorFactory){
        this.evaluatorFactory = evaluatorFactory;
    }
    
    private <S> Iterator<S> multiArgFilter(Iterator<S> iterator, Evaluator<Boolean> ev) {
        return IteratorUtils.filteredIterator(iterator, new MultiArgEvaluatorPredicate<S>(ev));
    }

    public <S> Iterator<S> multiArgFilter(Iterator<S> iterator, List<Expr<?>> sources, EBoolean condition) {
        return multiArgFilter(iterator, evaluatorFactory.create(sources, condition));
    }

    public <S> Iterator<S> singleArgFilter(Iterator<S> iterator, Evaluator<Boolean> ev) {
        return IteratorUtils.filteredIterator(iterator, new SingleArgEvaluatorPredicate<S>(ev));
    }

    public <S> Iterator<S> singleArgFilter(Iterator<S> iterator, Expr<S> source, EBoolean condition){
        Evaluator<Boolean> ev = evaluatorFactory.create(Collections.singletonList(source), condition);
        return singleArgFilter(iterator, ev);
    }
    
    private <S, T> Iterator<T> transform(Iterator<S> source, Evaluator<T> ev) {
        return IteratorUtils.transformedIterator(source, new EvaluatorTransformer<S, T>(ev));
    }

    public <S, T> Iterator<T> transform(Iterator<S> source, List<Expr<?>> sources, Expr<T> projection) {
        return transform(source, evaluatorFactory.create(sources, projection));
    }

}
