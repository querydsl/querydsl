/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import org.apache.commons.collections15.Predicate;

import com.mysema.codegen.Evaluator;

/**
 * @author tiwe
 *
 * @param <S>
 */
public final class SingleArgEvaluatorPredicate<S> implements Predicate<S> {
    
    private final Evaluator<Boolean> ev;

    SingleArgEvaluatorPredicate(Evaluator<Boolean> ev) {
        this.ev = ev;
    }

    public boolean evaluate(S object) {
        return ev.evaluate(object);
    }
}